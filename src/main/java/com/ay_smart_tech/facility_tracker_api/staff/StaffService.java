package com.ay_smart_tech.facility_tracker_api.staff;

import com.ay_smart_tech.facility_tracker_api.auth.dto.CreateStaffRequestDto;
import com.ay_smart_tech.facility_tracker_api.auth.dto.CreateStaffResponseDto;
import com.ay_smart_tech.facility_tracker_api.common.exceptions.DuplicateResourceException;
import com.ay_smart_tech.facility_tracker_api.user.User;
import com.ay_smart_tech.facility_tracker_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String TEMP_PASSWORD_CHARS =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public CreateStaffResponseDto createStaff(CreateStaffRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already in use: " + request.email());
        }

        String tempPassword = generateTempPassword();

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(tempPassword));
        user.setRole(request.role());
        user.setMustChangePassword(true);
        User savedUser = userRepository.save(user);

        Staff staff = new Staff();
        staff.setUserId(savedUser.getId());
        staff.setFullName(request.fullName());
        staff.setEmail(request.email());
        Staff savedStaff = staffRepository.save(staff);

        return new CreateStaffResponseDto(savedStaff.getId(), savedStaff.getEmail(), tempPassword);
    }

    private String generateTempPassword() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(TEMP_PASSWORD_CHARS.charAt(RANDOM.nextInt(TEMP_PASSWORD_CHARS.length())));
        }
        return sb.toString();
    }
}
