package com.mp.stickynotesapp.controller;

import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @GetMapping("/all/team/{teamName}")
    public ResponseEntity<List<User>> findAllByTeamName(@PathVariable String teamName) {
        List<User> users = userService.findAllByTeamName(teamName);
        return ResponseEntity.ok(users);
    }

}
