package com.procurement.procurement.controller.auth;

import com.procurement.procurement.dto.auth.LoginRequestDTO;
import com.procurement.procurement.dto.auth.AuthResponseDTO;
import com.procurement.procurement.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Get UserDetails
        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        // Generate token
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponseDTO(token);
    }
}