package com.procurement.procurement.security;

import com.procurement.procurement.entity.user.User;
import com.procurement.procurement.repository.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() ->
                        userRepository.findByEmail(usernameOrEmail)
                                .orElseThrow(() ->
                                        new UsernameNotFoundException(
                                                "User not found with username or email: "
                                                        + usernameOrEmail)));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // 🔥 Always return username
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toSet())
        );
    }
}