package in.bm.UserService.CONFIG;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @LoadBalanced
    @Bean
    public WebClient.Builder webClient() {
        return WebClient.builder();
    }
}
