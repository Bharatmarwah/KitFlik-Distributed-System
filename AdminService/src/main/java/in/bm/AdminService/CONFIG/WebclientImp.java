package in.bm.AdminService.CONFIG;

import in.bm.AdminService.DTO.BookingDTO;
import in.bm.AdminService.DTO.UserResponseDTO;
import in.bm.AdminService.EXCEPTION.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class WebclientImp {

    @Autowired
    private WebClient.Builder webClient;

    @CircuitBreaker(name = "global", fallbackMethod = "usersDetailsFallback")
    @Retry(name = "global")
    @RateLimiter(name = "global")
    public List<UserResponseDTO> usersDetails() {
        return webClient.build()
                .get()
                .uri("lb://kitflikapplication/user/userdetails")
                .retrieve()
                .bodyToFlux(UserResponseDTO.class)
                .collectList()
                .block();
    }

    public List<UserResponseDTO> usersDetailsFallback(Throwable exception) {
        log.error("Fallback: User Service DOWN - Cannot fetch user list");
        return Collections.emptyList();
    }

    @CircuitBreaker(name = "global", fallbackMethod = "removeUserFallback")
    @Retry(name = "global")
    @RateLimiter(name = "global")
    public String removeUser(int id) {
        return webClient.build()
                .delete()
                .uri("lb://kitflikapplication/user/userRemove/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String removeUserFallback(int id, Throwable exception) {
        log.error("Fallback: User Service DOWN - Cannot remove user {}", id);
        return "Service unavailable: Could not remove user " + id;
    }

    @CircuitBreaker(name = "global", fallbackMethod = "userDetailFallback")
    @Retry(name = "global")
    @RateLimiter(name = "global")
    public UserResponseDTO userDetail(int id) {

        return webClient.build()
                .get()
                .uri("lb://kitflikapplication/user/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        Mono.error(new CustomException("User not found with id: " + id))
                )
                .bodyToMono(UserResponseDTO.class)
                .block();
    }

    public UserResponseDTO userDetailFallback(int id, Throwable exception) {
        log.error("Fallback: User Service DOWN - Cannot fetch details for user {}", id);
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUsername("SERVICE_UNAVAILABLE");
        dto.setEmail("SERVICE_UNAVAILABLE");
        dto.setRole("UNKNOWN");
        return dto;
    }

    @CircuitBreaker(name = "global", fallbackMethod = "moviesBookingsdetailsFallback")
    @Retry(name = "global")
    @RateLimiter(name = "global")
    public List<BookingDTO> moviesBookingsdetails() {

        return webClient.build()
                .get()
                .uri("lb://kitflikbookingservice/bookings/info")
                .retrieve()
                .bodyToFlux(BookingDTO.class)
                .collectList()
                .block();
    }

    public List<BookingDTO> moviesBookingsdetailsFallback(Throwable exception) {
        log.error("Fallback: Booking Service DOWN - Cannot fetch bookings list");
        return Collections.emptyList();
    }

    @CircuitBreaker(name = "global", fallbackMethod = "movieBookingDetailsPerUserFallback")
    @Retry(name = "global")
    @RateLimiter(name = "global")
    public BookingDTO movieBookingDetailsPerUser(long id) {
        return webClient.build()
                .get()
                .uri("lb://kitflikbookingservice/bookings/{id}", id)
                .retrieve()
                .bodyToMono(BookingDTO.class)
                .block();
    }

    public BookingDTO movieBookingDetailsPerUserFallback(long id, Throwable exception) {
        log.error("Fallback: Booking Service DOWN - Cannot fetch booking for user {}", id);
        return null;
    }
}
