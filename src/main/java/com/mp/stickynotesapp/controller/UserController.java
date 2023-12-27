package com.mp.stickynotesapp.controller;

import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.repository.UserRepository;
import com.mp.stickynotesapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all/team/{teamName}/manager/{id}")
    public ResponseEntity<List<User>> findAllByTeamName(@PathVariable String teamName, @PathVariable Long id) {
        List<User> users = userService.findAllByTeamNameAndManagerId(teamName, id);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> findAllBy() {
        List<User> users = userService.findAllBy();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/workCountry/{workCountry}")
    public ResponseEntity<List<User>> findAllByWorkCountry(@PathVariable String workCountry) {
        List<User> users = userService.findAllByWorkCountry(workCountry);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createManager/admin/{id}")
    public ResponseEntity<User> createManager(@RequestBody User user, @PathVariable Long id) {
        User createdManager = userService.createManager(user, id);
        return new ResponseEntity<>(createdManager, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/createEmployee/manager/{id}")
    public ResponseEntity<User> createEmployee(@RequestBody User user, @PathVariable Long id) {
        User createdEmployee = userService.createEmployee(user, id);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        String msg = userService.deleteUserById(userId);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @PatchMapping("/{id}/update/password")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        User updatedUser = userService.updateUser(id, fields);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

}
