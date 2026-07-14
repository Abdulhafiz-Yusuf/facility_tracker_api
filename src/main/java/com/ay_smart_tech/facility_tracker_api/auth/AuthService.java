package com.ay_smart_tech.facility_tracker_api.auth;

import com.ay_smart_tech.facility_tracker_api.auth.dto.*;
import com.ay_smart_tech.facility_tracker_api.common.exceptions.DuplicateResourceException;
import com.ay_smart_tech.facility_tracker_api.config.JwtService;
import com.ay_smart_tech.facility_tracker_api.customer.Customer;
import com.ay_smart_tech.facility_tracker_api.customer.CustomerRepository;
import com.ay_smart_tech.facility_tracker_api.customer.KycStatus;
import com.ay_smart_tech.facility_tracker_api.user.Role;
import com.ay_smart_tech.facility_tracker_api.user.User;
import com.ay_smart_tech.facility_tracker_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered: " + request.email());
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.passwordHash()));
        user.setRole(Role.CUSTOMER); // hardcoded — caller can NEVER choose their own role
        user.setMustChangePassword(false);
        User savedUser = userRepository.save(user);

        Customer customer = new Customer();
        customer.setFullName(request.fullName());
        customer.setEmail(request.email());
        customer.setKycStatus(KycStatus.PENDING);
        customer.setUserId(savedUser.getId());
        customerRepository.save(customer);

        String token = jwtService.generateToken(savedUser);
        return new AuthResponseDto(token, savedUser.getRole(), savedUser.isMustChangePassword());
    }

    public AuthResponseDto login(LoginRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        String token = jwtService.generateToken(user);
        return new AuthResponseDto(token, user.getRole(), user.isMustChangePassword());
    }

    @Transactional
    public void changePassword(String email, ChangePassowordRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);
    }
}
