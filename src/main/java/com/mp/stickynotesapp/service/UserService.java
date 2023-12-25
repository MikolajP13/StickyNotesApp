package com.mp.stickynotesapp.service;

import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> findAllByTeamName(String teamName) {
        Optional<List<User>> optionalUserList = userRepository.findAllByTeamName(teamName);
        List<User> users = optionalUserList.orElse(Collections.emptyList());
        return users;
    }
}
