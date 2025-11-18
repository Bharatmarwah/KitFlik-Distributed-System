package in.bm.AdminService.FILTER;

import in.bm.AdminService.SERVICE.BlacklistService;
import in.bm.AdminService.SERVICE.JwtToken;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtToken jwtTokenUtil; // Your JwtToken service

    @Autowired
    private BlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // ✅ Proceed if JWT is present and starts with Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if(token!=null && blacklistService.isTokenBlacklisted(token)){
                filterChain.doFilter(request,response);
            }

            try {
                // ✅ Validate the token (signature + expiry)
                if (jwtTokenUtil.validateToken(token)) {
                    String username = jwtTokenUtil.extractUsername(token);
                    String role = jwtTokenUtil.extractRole(token);

                    // ✅ Create authentication object with extracted details
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    new User(username, "", Collections.emptyList()), // principal
                                    null,
                                    Collections.singletonList(() -> role) // authority
                            );

                    // ✅ Attach request info to authentication
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // ✅ Set authentication in the SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (ExpiredJwtException e) {
                System.out.println("❌ JWT expired: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Invalid JWT: " + e.getMessage());
            }
        }

        // ✅ Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
