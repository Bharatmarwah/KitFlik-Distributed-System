package in.bm.AuthGateway.Filter;

import in.bm.AuthGateway.JwtToken.BlacklistService;
import in.bm.AuthGateway.JwtToken.jwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements GlobalFilter, Ordered {

    @Autowired
    private jwtToken jwtTokenUtils;

    @Autowired
    private BlacklistService blacklistService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isPublicPath(path) || path.contains("/mail/")) {
            return chain.filter(exchange);
        }


        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7).trim();

        if (blacklistService.isTokenBlacklisted(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (!jwtTokenUtils.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String username = jwtTokenUtils.extractUsername(token);
        String role = jwtTokenUtils.extractRole(token);

        exchange = exchange.mutate()
                .request(r -> r.headers(h -> {
                    h.add("Username", username);
                    h.add("Role", role);
                }))
                .build();

        return chain.filter(exchange);

    }


    private boolean isPublicPath(String path) {
        return path.startsWith("/auth/login")
                || path.startsWith("/auth/register")
                || path.startsWith("/auth/refresh-token")
                || path.startsWith("/auth/logout")
                || path.startsWith("/auth/reset-password")
                || path.startsWith("/auth/forget-password");
    }

    @Override
    public int getOrder() {
        return -1;  // run early in the chain
    }
}
