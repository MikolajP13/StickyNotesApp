package com.mp.stickynotesapp.security;

import com.mp.stickynotesapp.model.Role;
import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.repository.RoleRepository;
import com.mp.stickynotesapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class InitializeAdminAccount implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<User> optionalUser = this.userRepository.findByFirstName("Admin");

        if (optionalUser.isEmpty()) {
            User admin = new User();
            admin.setJobID("sa");
            admin.setFirstName("Admin");
            admin.setUsername("sa_adm");
            admin.setPassword(passwordEncoder.encode("admin123"));

            this.userRepository.save(admin);

            Role roleAdmin = new Role();
            roleAdmin.setRole("ROLE_ADMIN");
            roleAdmin.setUser(admin);
            roleRepository.save(roleAdmin);
        }
    }
}
