package com.mp.stickynotesapp.security;

import com.mp.stickynotesapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username) //TODO: concatenation of 3 fields <?>
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

}
