package com.ay_smart_tech.facility_tracker_api.config;

import com.ay_smart_tech.facility_tracker_api.user.User;
import com.ay_smart_tech.facility_tracker_api.user.UserRepository;
import com.ay_smart_tech.facility_tracker_api.user.UserServiceDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceDetailsImpl userDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String email = jwtService.extractEmail(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByEmail(email)
                        .orElse(null);

                if (jwtService.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }catch(io.jsonwebtoken.JwtException | IllegalArgumentException ex) {
            // Malformed, expired, or invalid signature — treat as "not authenticated."
            // Do NOT set authentication. Let the request continue unauthenticated;
            // downstream .anyRequest().authenticated() will correctly reject it as 401.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
