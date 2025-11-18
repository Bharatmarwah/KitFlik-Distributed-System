package in.bm.AuthGateway.JwtToken;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Service
public class BlacklistService {
    private Set<String> blacklistedTokens = new HashSet<>();

    public Mono<Void> blacklistToken(String token) {
        blacklistedTokens.add(token);
        return Mono.empty();
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
