package com.ay_smart_tech.facility_tracker_api.config;

import com.ay_smart_tech.facility_tracker_api.user.UserServiceDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final MustChangePasswordFilter mustChangePasswordFilter;
    private final UserServiceDetailsImpl userDetailsService;


    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Full authentication is required to access this resource\"}");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authenticationEntryPoint()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/api/health", "/actuator/health").permitAll()
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/admin/staff").hasRole("MANAGER")
                        .anyRequest().authenticated()
                )
                // JWT filter must run FIRST to set the security context
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // MustChangePassword runs AFTER JWT filter so it can inspect the populated context
                .addFilterAfter(mustChangePasswordFilter, JwtAuthFilter.class);

        return http.build();
    }
}
