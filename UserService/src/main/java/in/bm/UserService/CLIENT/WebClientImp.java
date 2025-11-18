package in.bm.UserService.CLIENT;

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
public class WebClientImp {

    @Autowired
    private WebClient.Builder webClient;

    @CircuitBreaker(name = "global",fallbackMethod ="registationMailFallback")
    @Retry(name="global")
    @RateLimiter(name="global")
    public void sendRegistationMail(String to, String username) {

        webClient
                .build()
                .get()
                .uri("lb://kitfliknotificationservice/mail/register?to={to}&username={username}",
                        to, username)
                .exchangeToMono(clientResponse -> Mono.empty())
                .subscribe(
                        success -> log.info("Registration mail request sent OK"),
                        error -> log.error("Mail request failed: {}", error.getMessage())
                );
    }

    @CircuitBreaker(name="global",fallbackMethod ="otpMailFallback")
    @Retry(name="global")
    @RateLimiter(name="global")
    public void sendOtpMail(String otp, String username, String to) {

        webClient
                .build()
                .get()
                .uri("lb://kitfliknotificationservice/mail/otp?otp={otp}&username={username}&to={to}",
                        otp, username, to)
                .exchangeToMono(clientResponse -> Mono.empty())
                .subscribe(
                        success -> log.info("OTP mail request sent OK"),
                        error -> log.error("Mail request failed: {}", error.getMessage())
                );
    }

    public void registationMailFallback(String to, String username,Throwable exception){
        log.error("Fallback triggered: Notification service DOWN. Cannot send mail to {}", to);
    }

    public void otpMailFallback(String otp , String username, String to,Throwable exception){
        log.error("Fallback triggered: Notification service DOWN. Cannot send mail to {}",to);
    }
}
