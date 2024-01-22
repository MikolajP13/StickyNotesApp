package com.mp.stickynotesapp.controller;

import com.mp.stickynotesapp.dto.NoteDTO;
import com.mp.stickynotesapp.dto.UserCreationDTO;
import com.mp.stickynotesapp.model.Note;
import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.service.NoteService;
import com.mp.stickynotesapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/test-data")
public class UploadTestDataController {

    private final NoteService noteService;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-managers/admin/{id}")
    public ResponseEntity<List<UserCreationDTO>> createManagers(@PathVariable Long id, @RequestBody List<User> managers) {
        return new ResponseEntity<>(userService.createManagers(managers, id), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-employees/manager/{id}")
    public ResponseEntity<List<UserCreationDTO>> createEmployeesOfManagerId(@PathVariable Long id, @RequestBody List<User> employees) {
        return new ResponseEntity<>(userService.createEmployeesOfManagerId(employees, id), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-list/manager/{id}")
    ResponseEntity<List<NoteDTO>> addNoteList(@PathVariable Long id, @RequestBody List<Note> notes) {
        return new ResponseEntity<>(noteService.addNoteList(notes, id), HttpStatus.CREATED);
    }
}
