package in.bm.AuthGateway.CONFIG;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConf {

    @Bean
    @LoadBalanced
    public WebClient.Builder WebclientBuilder(){
        return WebClient.builder();
    }
}
