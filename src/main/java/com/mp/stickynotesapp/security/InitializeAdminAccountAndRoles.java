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
public class InitializeAdminAccountAndRoles implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<User> optionalUser = this.userRepository.findByFirstName("Admin");

        if (optionalUser.isEmpty()) {

            Role roleAdmin = new Role();
            roleAdmin.setRole("ROLE_ADMIN");
            roleRepository.save(roleAdmin);

            Role roleManager = new Role();
            roleManager.setRole("ROLE_MANAGER");
            roleRepository.save(roleManager);

            Role roleEmployee = new Role();
            roleEmployee.setRole("ROLE_EMPLOYEE");
            roleRepository.save(roleEmployee);

            User admin = new User();
            admin.setJobID("sa");
            admin.setFirstName("Admin");
            admin.setUsername("sa_adm");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getRoles().add(roleAdmin);

            this.userRepository.save(admin);
        }
    }
}
