package com.mp.stickynotesapp.controller;

import com.mp.stickynotesapp.dto.NoteDTO;
import com.mp.stickynotesapp.model.Note;
import com.mp.stickynotesapp.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/create/manager/{id}")
    public ResponseEntity<Note> createNote(@RequestBody Note note, @PathVariable Long id) {
        Note newNote = noteService.createNote(note, id);
        return new ResponseEntity<>(newNote, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/allAssignedTo/{id}")
    public ResponseEntity<List<Note>> findAllByAssignedTo(@PathVariable Long id) {
        List<Note> assignedNotes = noteService.findAllByAssignedTo(id);
        return new ResponseEntity<>(assignedNotes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PatchMapping("/{id}/update")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        Note updatedNote = noteService.updateNote(id, fields);
        return new ResponseEntity<>(updatedNote, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Boolean> deleteNote(@PathVariable Long id) {
        boolean result = noteService.deleteNote(id);
        return result ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all/manager/{id}")
    public ResponseEntity<List<NoteDTO>> findAllByCreatedBy(@PathVariable Long id) {
        List<NoteDTO> notes = noteService.findAllByCreatedBy(id);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all/manager/{id}/projectName/{projectName}")
    public ResponseEntity<List<Note>> findAllByCreatedByAndProjectName(@PathVariable Long id, @PathVariable String projectName) {
        List<Note> notes = noteService.findAllByCreatedByAndProjectName(id, projectName);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all/manager/{id}/priority/{priority}")
    public ResponseEntity<List<Note>> findAllByCreatedByAndPriority(@PathVariable Long id, @PathVariable String priority) {
        List<Note> notes = noteService.findAllByCreatedByAndPriority(id, Note.Priority.valueOf(priority.toUpperCase()));
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all/manager/{id}/status/{status}")
    public ResponseEntity<List<Note>> findAllByCreatedByAndStatus(@PathVariable Long id, @PathVariable String status) {
        List<Note> notes = noteService.findAllByCreatedByAndStatus(id, Note.Status.valueOf(status.toUpperCase()));
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/all/manager/{id}/dateFrom/{from}/dateTo/{to}")
    public ResponseEntity<List<Note>> findAllByCreatedByAndCreationDateBetween(@PathVariable Long id,
                                                                               @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                                                               @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        List<Note> notes = noteService.findAllByCreatedByAndCreationDateBetween(id, from, to);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping ("/count/manager/{id}/status/{status}/dateFrom/{from}/dateTo/{to}")
    ResponseEntity<Integer> countAllByCreatedByAndStatusAndDateToBetween(@PathVariable Long id,
                                                                         @PathVariable String status,
                                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        Integer noOfNotes = noteService.countAllByCreatedByAndStatusAndDateToBetween(id, Note.Status.valueOf(status.toUpperCase()), from, to);
        return new ResponseEntity<>(noOfNotes, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping ("/count/manager/{id}/priority/{priority}/dateFrom/{from}/dateTo/{to}")
    ResponseEntity<Integer> countAllByCreatedByAndPriorityAndDateToBetween(@PathVariable Long id,
                                                                         @PathVariable String priority,
                                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        Integer noOfNotes = noteService.countAllByCreatedByAndPriorityAndDateToBetween(id, Note.Priority.valueOf(priority.toUpperCase()), from, to);
        return new ResponseEntity<>(noOfNotes, HttpStatus.OK);
    }

}
