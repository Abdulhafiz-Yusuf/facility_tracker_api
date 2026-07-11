package com.ay_smart_tech.facility_tracker_api.config;

import com.ay_smart_tech.facility_tracker_api.user.Role;
import com.ay_smart_tech.facility_tracker_api.user.User;
import com.ay_smart_tech.facility_tracker_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialManagerSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.manager-email}")
    private String managerEmail;

    @Value("${app.bootstrap.manager-password}")
    private String managerPassword;

    @Override
    public void run(String... args) {
        if (userRepository.existsByRole(Role.MANAGER)) {
            return; // already bootstrapped, do nothing
        }
        User manager = new User();
        manager.setEmail(managerEmail);
        manager.setPasswordHash(passwordEncoder.encode(managerPassword));
        manager.setRole(Role.MANAGER);
        manager.setMustChangePassword(true);
        userRepository.save(manager);
    }
}
