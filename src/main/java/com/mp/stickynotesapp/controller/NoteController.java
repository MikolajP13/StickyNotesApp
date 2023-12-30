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
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/create/manager/{id}")
    public ResponseEntity<NoteDTO> createNote(@RequestBody Note note, @PathVariable Long id) {
        NoteDTO newNote = noteService.createNote(note, id);
        return new ResponseEntity<>(newNote, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/all/assigned-to/{id}")
    public ResponseEntity<List<NoteDTO>> findAllByAssignedTo(@PathVariable Long id) {
        Optional<List<NoteDTO>> assignedNotes = noteService.findAllByAssignedTo(id);
        return assignedNotes.map(noteDTOS -> new ResponseEntity<>(noteDTOS, HttpStatus.OK)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PatchMapping("/{id}/update")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return new ResponseEntity<>(noteService.updateNote(id, fields), HttpStatus.OK);
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
    @GetMapping("/all/manager/{id}/project-name/{projectName}")
    public ResponseEntity<List<NoteDTO>> findAllByCreatedByAndProjectName(@PathVariable Long id, @PathVariable String projectName) {
        List<NoteDTO> notes = noteService.findAllByCreatedByAndProjectName(id, projectName);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all/manager/{id}/priority/{priority}")
    public ResponseEntity<List<NoteDTO>> findAllByCreatedByAndPriority(@PathVariable Long id, @PathVariable String priority) {
        List<NoteDTO> notes = noteService.findAllByCreatedByAndPriority(id, Note.Priority.valueOf(priority.toUpperCase()));
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all/manager/{id}/status/{status}")
    public ResponseEntity<List<NoteDTO>> findAllByCreatedByAndStatus(@PathVariable Long id, @PathVariable String status) {
        List<NoteDTO> notes = noteService.findAllByCreatedByAndStatus(id, Note.Status.valueOf(status.toUpperCase()));
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all/manager/{id}/date-from/{from}/date-to/{to}")
    public ResponseEntity<List<NoteDTO>> findAllByCreatedByAndCreationDateBetween(@PathVariable Long id,
                                                                               @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                                                               @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        List<NoteDTO> notes = noteService.findAllByCreatedByAndCreationDateBetween(id, from, to);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping ("/count/manager/{id}/status/{status}/date-from/{from}/date-to/{to}")
    ResponseEntity<Integer> countAllByCreatedByAndStatusAndDateToBetween(@PathVariable Long id,
                                                                         @PathVariable String status,
                                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        Integer noOfNotes = noteService.countAllByCreatedByAndStatusAndDateToBetween(id, Note.Status.valueOf(status.toUpperCase()), from, to);
        return new ResponseEntity<>(noOfNotes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping ("/count/manager/{id}/priority/{priority}/date-from/{from}/date-to/{to}")
    ResponseEntity<Integer> countAllByCreatedByAndPriorityAndDateToBetween(@PathVariable Long id,
                                                                         @PathVariable String priority,
                                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        Integer noOfNotes = noteService.countAllByCreatedByAndPriorityAndDateToBetween(id, Note.Priority.valueOf(priority.toUpperCase()), from, to);
        return new ResponseEntity<>(noOfNotes, HttpStatus.OK);
    }

}
