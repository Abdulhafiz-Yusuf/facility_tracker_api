package com.ay_smart_tech.facility_tracker_api.config;

import com.ay_smart_tech.facility_tracker_api.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MustChangePasswordFilter extends OncePerRequestFilter {

    private static final String ALLOWED_PATH = "/auth/change-password";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof User user
                && user.isMustChangePassword()
                && !request.getRequestURI().equals(ALLOWED_PATH)) {

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Password change required before accessing this resource\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
