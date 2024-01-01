package com.mp.stickynotesapp.controller;

import com.mp.stickynotesapp.dto.UserCreationDTO;
import com.mp.stickynotesapp.dto.UserDTO;
import com.mp.stickynotesapp.dto.UserForNoteDTO;
import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.service.UserService;
import jakarta.validation.Valid;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> findAllBy() {
        List<UserDTO> users = userService.findAllBy();
        return ResponseEntity.ok(users);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/work-country/{workCountry}")
    public ResponseEntity<List<UserDTO>> findAllByWorkCountry(@PathVariable String workCountry) {
        List<UserDTO> users = userService.findAllByWorkCountry(workCountry);
        return ResponseEntity.ok(users);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-manager/admin/{id}")
    public ResponseEntity<UserCreationDTO> createManager(@Valid @RequestBody User user, @PathVariable Long id) {
        UserCreationDTO createdManager = userService.createManager(user, id);
        return new ResponseEntity<>(createdManager, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all/team/{teamName}/manager/{id}")
    public ResponseEntity<List<UserForNoteDTO>> findAllByTeamNameAndManagerId(@PathVariable String teamName, @PathVariable Long id) {
        List<UserForNoteDTO> users = userService.findAllByTeamNameAndManagerId(teamName, id);
        return ResponseEntity.ok(users);
    }
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/create-employee/manager/{id}")
    public ResponseEntity<UserCreationDTO> createEmployee(@Valid @RequestBody User user, @PathVariable Long id) {
        UserCreationDTO createdEmployee = userService.createEmployee(user, id);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Long> deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id) ?
                new ResponseEntity<>(id, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PatchMapping("/{id}/update-password")
    public ResponseEntity<UserDTO> updateUserPassword(@PathVariable Long id, @RequestBody Map<String, Object> fields) {

        return new ResponseEntity<>(userService.updateUserPassword(id, fields), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}/admin-update")
    public ResponseEntity<UserDTO> updateUserByAdministrator(@PathVariable Long id, @RequestBody Map<String, Object> fields) {

        return new ResponseEntity<>(userService.updateUserByAdministrator(id, fields), HttpStatus.OK);
    }

}
