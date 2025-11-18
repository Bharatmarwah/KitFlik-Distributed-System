package in.bm.MovieManagementService.SERVICE;

import in.bm.MovieManagementService.DTO.MailRequest;
import in.bm.MovieManagementService.ENTITY.*;
import in.bm.MovieManagementService.REPOSITORY.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
@Slf4j
public class ConfirmationService {

    @Autowired private BookingDeo bookingRepo;
    @Autowired private MovieDeo movieRepo;
    @Autowired private TheatreDeo theatreRepo;
    @Autowired private ShowTimingDeo showTimingRepo;
    @Autowired private WebClient.Builder webclient;

    public ResponseEntity<String> confirmation(Long bookingId, String toEmail) {
        Optional<Booking> bookingOpt = bookingRepo.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            log.warn("Booking ID {} not found", bookingId);
            return ResponseEntity.badRequest().body("Invalid Booking ID");
        }

        Booking booking = bookingOpt.get();
        Movie movie = movieRepo.findById(booking.getMovieId()).orElse(null);
        Theatre theatre = theatreRepo.findById(booking.getTheatreId()).orElse(null);
        ShowTiming showTiming = showTimingRepo.findById(booking.getShowTimingId()).orElse(null);

        if (movie == null || theatre == null || showTiming == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Booking details incomplete (movie/theatre/show timing missing)");

        String subject = "Booking Confirmation: " + movie.getTitle();
        String body = String.format("""
                        Dear User,
                        
                        ✅ Your movie booking has been confirmed successfully!
                        
                        🎥 Movie: %s
                        🏢 Theatre: %s, %s
                        🗓️ Date: %s
                        ⏰ Time: %s
                        💺 Seats: %s
                        💰 Amount: ₹%.2f
                        
                        Thank you for booking with KitFlik!
                        Enjoy your show 🍿🎞️
                        
                        Regards,
                        KitFlik Team
                        """,
                movie.getTitle(),
                theatre.getName(),
                theatre.getLocation(),
                showTiming.getDate(),
                showTiming.getTime(),
                String.join(", ", booking.getSeatsBooked()),
                booking.getPriceDetails().getFinalAmount()
        );

        booking.setStatus("CONFIRMED");
        booking.setBookingTime(LocalTime.now());
        booking.setBookingDate(LocalDate.now());
        bookingRepo.save(booking);
        log.info("Booking status updated to CONFIRMED for ID {}", bookingId);

        sendConfirmationMail(toEmail, subject, body);
        return ResponseEntity.ok("Booking confirmed and mail sent successfully!");
    }

    private void sendConfirmationMail(String email, String subject, String body) {
        try {

            webclient.build().post()
                    .uri("lb://kitfliknotificationservice/mail/confirmation")
                    .bodyValue(new MailRequest(email, subject, body))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        log.error("❌ Failed to send mail via Notification Service: {}", e.getMessage());
                        return Mono.empty();
                    })
                    .block();
            log.info("📧 Mail request sent to Notification Service for {}", email);
        } catch (Exception e) {
            log.error("❌ Exception while calling Notification Service: {}", e.getMessage());
        }
    }
}
