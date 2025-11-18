package in.bm.NotificationService.CONFIG;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
@Component
public class WebClientImp {

    @Autowired
    private WebClient.Builder webClient;

    @CircuitBreaker(name="global",fallbackMethod ="getEmailFallback")
    @Retry(name="global")
    @RateLimiter(name="global")
    public List<String> getEmails() {
        try {
            return webClient
                    .build()
                    .get()
                    .uri("lb://kitflikapplication/user/emails")
                    .retrieve()
                    .bodyToFlux(String.class)
                    .collectList()
                    .block();
        }catch (Exception e){
            log.error("ERROR FETCHING EMAILS: {}",e.getMessage());
        }
        return new ArrayList<>();
    }
    public List<String> getEmailFallback(Throwable ex){
        log.error("Fallback triggered : User service DOWN - cant fetch emails");
        return Collections.emptyList();
    }
}
