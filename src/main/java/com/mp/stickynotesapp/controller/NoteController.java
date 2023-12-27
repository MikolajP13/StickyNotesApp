package com.mp.stickynotesapp.controller;

import com.mp.stickynotesapp.model.Note;
import com.mp.stickynotesapp.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create/manager/{id}")
    public ResponseEntity<Note> createNote(@RequestBody Note note, @PathVariable Long id) {
        Note newNote = noteService.createNote(note, id);
        return new ResponseEntity<>(newNote, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @GetMapping("/allAssignedTo/{id}")
    public ResponseEntity<List<Note>> findAllByAssignedTo(@PathVariable Long id) {
        List<Note> assignedNotes = noteService.findAllByAssignedTo(id);
        return new ResponseEntity<>(assignedNotes, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @PatchMapping("/{id}/update")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        Note updatedNote = noteService.updateNote(id, fields);
        return new ResponseEntity<>(updatedNote, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Boolean> deleteNote(@PathVariable Long id) {
        boolean result = noteService.deleteNote(id);
        return result ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }


}
