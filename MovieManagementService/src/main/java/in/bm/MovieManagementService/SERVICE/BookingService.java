package in.bm.MovieManagementService.SERVICE;

import in.bm.MovieManagementService.ENTITY.Booking;
import in.bm.MovieManagementService.ENTITY.PriceDetails;
import in.bm.MovieManagementService.EXCEPTION.CustomException;
import in.bm.MovieManagementService.REPOSITORY.BookingDeo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class BookingService {

    private final BookingDeo bookingRepo;

    public BookingService(BookingDeo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    private static final Map<String, Double> SEAT_PRICES = Map.ofEntries(
            Map.entry("A1", 350.0), Map.entry("A2", 350.0), Map.entry("A3", 350.0),
            Map.entry("B1", 300.0), Map.entry("B2", 300.0), Map.entry("B3", 300.0),
            Map.entry("C1", 250.0), Map.entry("C2", 250.0), Map.entry("C3", 250.0),
            Map.entry("D1", 200.0), Map.entry("D2", 200.0), Map.entry("D3", 200.0),
            Map.entry("E1", 150.0), Map.entry("E2", 150.0)
    );

    public ResponseEntity<Booking> addBooking(Booking booking) {
        if (booking.getSeatsBooked() == null || booking.getSeatsBooked().isEmpty()) {
            log.warn("Booking rejected: no seats selected.");
            return ResponseEntity.badRequest().build();
        }

        PriceDetails priceDetails = calculatePrice(booking.getSeatsBooked());
        booking.setPriceDetails(priceDetails);

        booking.setStatus("PENDING");
        booking.setBookingDate(LocalDate.now());
        booking.setBookingTime(LocalTime.now());

        Booking savedBooking = bookingRepo.save(booking);
        log.info("Booking added successfully: ID {}", savedBooking.getBookingId());

        return ResponseEntity.ok(savedBooking);
    }

    public ResponseEntity<String> cancelBooking(long bookingId) {
        if (bookingRepo.existsById(bookingId)) {
            bookingRepo.deleteById(bookingId);
            log.info("Booking canceled successfully with ID {}", bookingId);
            return ResponseEntity.ok("Booking canceled successfully.");
        } else {
            log.warn("Booking not found with ID {}", bookingId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Booking not found with ID: " + bookingId);
        }
    }

    public ResponseEntity<Booking> changeBookingDetails(Booking booking) {
        Optional<Booking> optionalBooking = bookingRepo.findById(booking.getBookingId());
        if (optionalBooking.isEmpty()) {
            log.warn("Booking modification failed: ID {} not found", booking.getBookingId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Booking bookingDetails = optionalBooking.get();
        bookingDetails.setMovieId(booking.getMovieId());
        bookingDetails.setTheatreId(booking.getTheatreId());
        bookingDetails.setShowTimingId(booking.getShowTimingId());
        bookingDetails.setSeatsBooked(booking.getSeatsBooked());

        if (bookingDetails.getSeatsBooked() == null || bookingDetails.getSeatsBooked().isEmpty()) {
            log.warn("Modification skipped: No seats provided for ID {}", booking.getBookingId());
            return new ResponseEntity<>(bookingDetails, HttpStatus.NOT_MODIFIED);
        }
        bookingDetails.setPriceDetails(calculatePrice(bookingDetails.getSeatsBooked()));
        bookingDetails.setBookingTime(LocalTime.now());
        bookingDetails.setBookingDate(LocalDate.now());
        Booking updatedBooking = bookingRepo.save(bookingDetails);// Still Status is pending
        log.info("Booking updated successfully with ID {}", updatedBooking.getBookingId());
        return ResponseEntity.ok(updatedBooking);
    }

    private PriceDetails calculatePrice(List<String> seats) {
        double baseAmount = seats.stream()
                .mapToDouble(s -> SEAT_PRICES.getOrDefault(s, 0.0))
                .sum();

        double gst = baseAmount * 0.18;
        double serviceTax = baseAmount * 0.05;
        double discount = 23.4;
        double totalAmount = baseAmount + gst + serviceTax;
        double finalAmount = totalAmount - discount;

        PriceDetails priceDetails = new PriceDetails();
        priceDetails.setBaseAmount(baseAmount);
        priceDetails.setGst(gst);
        priceDetails.setServiceTax(serviceTax);
        priceDetails.setDiscount(discount);
        priceDetails.setTotalAmount(totalAmount);
        priceDetails.setFinalAmount(finalAmount);

        return priceDetails;
    }
    public List<Booking> fetchAllBooking() {
        return bookingRepo.findAllConfirmedBookings();
    }
    public Booking fetchBookingDetailsById(long bookingId) {
        Optional<Booking> bookingDetails = bookingRepo.findById(bookingId);
        if (bookingDetails.isEmpty()) {
            throw new CustomException("Booking details not found with id: " + bookingId);
        }
        return bookingDetails.get();
    }
}
