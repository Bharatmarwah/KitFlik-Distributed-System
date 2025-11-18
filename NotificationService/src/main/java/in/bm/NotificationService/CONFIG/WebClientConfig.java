package in.bm.NotificationService.CONFIG;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webclient(){
        return WebClient.builder();
    }


}
