package in.bm.AuthGateway.JwtToken;

import in.bm.AuthGateway.CONFIG.WebClientConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
@Service
public class UserClient {

    @Autowired
    private WebClientConf webClientConf;

    public Mono<Map> getUserdetails(String username){

        WebClient webClient = webClientConf.WebclientBuilder().build();
        return webClient.get()
                .uri("lb://kitflikapplication/user/details/{username}",username)
                .retrieve()
                .bodyToMono(Map.class);

    }
}
