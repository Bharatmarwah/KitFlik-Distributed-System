package in.bm.AuthGateway.AuthController;

import in.bm.AuthGateway.CONFIG.WebClientConf;
import in.bm.AuthGateway.Dto.userForgetPasswordDto;
import in.bm.AuthGateway.Dto.userLogin;
import in.bm.AuthGateway.Dto.userRegisterDto;
import in.bm.AuthGateway.JwtToken.BlacklistService;
import in.bm.AuthGateway.JwtToken.UserClient;
import in.bm.AuthGateway.JwtToken.jwtToken;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private WebClientConf webClientConf;

    @Autowired
    private jwtToken jwtToken;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private UserClient userClient;

    // ----------------------------------------------------------
    // LOGIN
    // ----------------------------------------------------------
    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String,String>>> login(
            @RequestBody userLogin credentials,
            ServerWebExchange exchange
    ) {

        if (credentials.getUsername() == null || credentials.getPassword() == null) {
            return Mono.just(
                    ResponseEntity.badRequest()
                            .body(Map.of("error", "Username or password missing"))
            );
        }

        WebClient webClient = webClientConf.WebclientBuilder().build();
        log.info("User Service called");

        return webClient.post()
                .uri("lb://kitflikapplication/user/validate")
                .bodyValue(credentials)
                .retrieve()

                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                        res -> Mono.error(new WebClientResponseException(
                                401,
                                "BAD_CREDENTIALS",
                                null, null, null
                        ))
                )

                .onStatus(HttpStatusCode::is4xxClientError,
                        res -> Mono.error(new WebClientResponseException(
                                400,
                                "INVALID_REQUEST",
                                null, null, null
                        ))
                )

                .onStatus(HttpStatusCode::is5xxServerError,
                        res -> Mono.error(new WebClientResponseException(
                                503,
                                "SERVICE_DOWN",
                                null, null, null
                        ))
                )
                .bodyToMono(Map.class)

                .map(userResponse -> {

                    String role = (String) userResponse.get("Role");
                    String name = (String) userResponse.get("Username");

                    String accessToken = jwtToken.generateAccessToken(name, role);
                    String refreshToken = jwtToken.generateRefreshToken(name, role);

                    ResponseCookie cookie = ResponseCookie.from("refresh-token", refreshToken)
                            .httpOnly(true)
                            .secure(true)
                            .path("/")
                            .maxAge(7 * 24 * 60 * 60)
                            .build();

                    exchange.getResponse().addCookie(cookie);

                    return ResponseEntity.ok(Map.of("accessToken", accessToken));
                })

                .onErrorResume(WebClientResponseException.class, e -> {
                    int code = e.getRawStatusCode();

                    if (code == 401) {
                        return Mono.just(ResponseEntity
                                .status(401)
                                .body(Map.of("error", "Invalid username or password")));
                    }

                    if (code == 400) {
                        return Mono.just(ResponseEntity
                                .badRequest()
                                .body(Map.of("error", "Invalid request")));
                    }

                    return Mono.just(ResponseEntity
                            .status(503)
                            .body(Map.of("error", "User service unavailable")));
                });
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(
            @RequestBody @Valid userRegisterDto registerDto) {

        WebClient webClient = webClientConf.WebclientBuilder().build();

        log.info("User Service called");

        return webClient.post()
                .uri("lb://kitflikapplication/user/register")
                .bodyValue(registerDto)
                .retrieve()
                .toEntity(String.class)
                .onErrorResume(WebClientResponseException.class,
                        e -> Mono.just(
                                ResponseEntity.status(e.getStatusCode())
                                        .body(e.getResponseBodyAsString())
                        )
                );
    }

    @PostMapping("/forget-password")
    public Mono<ResponseEntity<String>> forgetPassword(@RequestParam String username) {

        WebClient webClient = webClientConf.WebclientBuilder().build();

        return webClient.post()
                .uri("lb://kitflikapplication/user/forgot-password?username=" + username)
                .retrieve()
                .toEntity(String.class)
                .onErrorResume(WebClientResponseException.class,
                        e -> Mono.just(
                                ResponseEntity.status(e.getStatusCode())
                                        .body(e.getResponseBodyAsString())
                        )
                );
    }

    @PostMapping("/reset-password")
    public Mono<ResponseEntity<String>> resetPassword(
            @RequestBody userForgetPasswordDto forgetPasswordDto) {

        WebClient webClient = webClientConf.WebclientBuilder().build();

        return webClient.post()
                .uri("lb://kitflikapplication/user/reset-password")
                .bodyValue(forgetPasswordDto)
                .retrieve()
                .toEntity(String.class)
                .onErrorResume(WebClientResponseException.class,
                        e -> Mono.just(
                                ResponseEntity.status(e.getStatusCode())
                                        .body(e.getResponseBodyAsString())
                        )
                );
    }

    @GetMapping("/logout")
    public Mono<ResponseEntity<String>> logout(
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            ServerWebExchange exchange) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.just(ResponseEntity.badRequest().body("Invalid Authorization header"));
        }

        String accessToken = authHeader.substring(7).trim();

        ResponseCookie expiredCookie = ResponseCookie.from("refresh-token", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .build();

        exchange.getResponse().addCookie(expiredCookie);

        return blacklistService.blacklistToken(accessToken)
                .then(Mono.just(ResponseEntity.ok("Logged out successfully")))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Logout failed"))
                );
    }
    @PostMapping("/refresh-token")
    public Mono<ResponseEntity<Map<String, String>>> refreshToken(ServerWebExchange exchange) {

        HttpCookie refreshCookie = exchange.getRequest()
                .getCookies()
                .getFirst("refresh-token");

        if (refreshCookie == null) {
            return Mono.just(
                    ResponseEntity.badRequest()
                            .body(Map.of("error", "Refresh token missing"))
            );
        }

        String refreshToken = refreshCookie.getValue();
        String username = jwtToken.extractUsername(refreshToken);

        return userClient.getUserdetails(username)
                .flatMap(userDetails -> {

                    if (!jwtToken.validateToken(refreshToken)) {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(Map.of("error", "Invalid refresh token"))
                        );
                    }

                    String role = (String) userDetails.get("role");
                    String newAccessToken = jwtToken.generateAccessToken(username, role);

                    return Mono.just(ResponseEntity.ok(Map.of("New-Access-Token", newAccessToken)));
                })
                .onErrorResume(e ->
                        Mono.just(
                                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(Map.of("error", "Failed to refresh token"))
                        )
                );
    }
}
