package in.bm.MovieManagementService.CLIENT;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Slf4j
@Component
public class UserServiceWebClient {

    @Autowired
    private WebClient.Builder webclient;

    @CircuitBreaker(name="global",fallbackMethod = "getUserEmailByUsernameFallback")
    @Retry(name="global")
    @RateLimiter(name="global")
    public String getUserEmailByUsername(String username) {

        return webclient.build().get()
                .uri("lb://kitflikapplication/user/username/email?username={username}", username)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    System.err.println("Failed to fetch email for username " + username + ": " + e.getMessage());
                    return Mono.empty();
                })
                .block();
    }
    public String getUserEmailByUsernameFallback(String username){
        log.error("Fallback triggered: User Service DOWN - Cannot fetch email of username {}",username);
        return "SERVICE_UNAVAILABLE";
    }
}
