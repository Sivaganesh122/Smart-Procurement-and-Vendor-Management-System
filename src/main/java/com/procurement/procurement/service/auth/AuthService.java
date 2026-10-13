package com.procurement.procurement.service.auth;

import com.procurement.procurement.dto.auth.LoginRequestDTO;
import com.procurement.procurement.entity.user.Role;
import com.procurement.procurement.entity.user.User;
import com.procurement.procurement.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Only Register logic needed now
    public String register(LoginRequestDTO request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return "Username already exists";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getUsername() + "@procurement.com");

        userRepository.save(user);

        return "User registered successfully";
    }
}