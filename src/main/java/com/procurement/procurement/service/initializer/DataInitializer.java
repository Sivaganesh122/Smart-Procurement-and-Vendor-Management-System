package com.procurement.procurement.service.initializer;

import com.procurement.procurement.entity.user.Role;
import com.procurement.procurement.entity.user.User;
import com.procurement.procurement.repository.user.RoleRepository;
import com.procurement.procurement.repository.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // ================= ROLES =================

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(
                        new Role("ROLE_ADMIN", "System Administrator")
                ));

        Role managerRole = roleRepository.findByName("ROLE_PROCUREMENT_MANAGER")
                .orElseGet(() -> roleRepository.save(
                        new Role("ROLE_PROCUREMENT_MANAGER", "Procurement Manager")
                ));

        Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseGet(() -> roleRepository.save(
                        new Role("ROLE_EMPLOYEE", "Normal Employee")
                ));

        System.out.println("Roles Initialized");

        // ================= ADMIN =================

        if (userRepository.findByUsername("admin").isEmpty()) {
            createUser("admin", "admin123", "admin@procurement.com", adminRole);
        }

        // ================= MANAGER =================

        if (userRepository.findByUsername("manager").isEmpty()) {
            createUser("manager", "manager123", "manager@procurement.com", managerRole);
        }

        // ================= EMPLOYEE =================

        if (userRepository.findByUsername("employee").isEmpty()) {
            createUser("employee", "employee123", "employee@procurement.com", employeeRole);
        }

        System.out.println("===================================");
        System.out.println("DEFAULT LOGIN CREDENTIALS:");
        System.out.println("ADMIN     → admin / admin123");
        System.out.println("MANAGER   → manager / manager123");
        System.out.println("EMPLOYEE  → employee / employee123");
        System.out.println("===================================");
    }

    private void createUser(String username, String password, String email, Role role) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFullName(username.toUpperCase());

        user.setEnabled(true);
        user.setActive(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);
    }
}