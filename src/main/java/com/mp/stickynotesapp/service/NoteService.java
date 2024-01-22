package com.mp.stickynotesapp.service;

import com.mp.stickynotesapp.dto.IncompleteNoteDataDTO;
import com.mp.stickynotesapp.dto.NoteDTO;
import com.mp.stickynotesapp.exception.InvalidNoteDataException;
import com.mp.stickynotesapp.exception.NoteNotFoundException;
import com.mp.stickynotesapp.exception.UserNotFoundException;
import com.mp.stickynotesapp.model.Note;
import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.repository.NoteRepository;
import com.mp.stickynotesapp.repository.UserRepository;
import com.mp.stickynotesapp.util.NoteMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteMapper noteMapper;

    public NoteDTO createNote(Note note, Long creatorId) {
        Note newNote = new Note();

        Optional<User> creatorOptional = userRepository.findById(creatorId);
        Optional<User> assignedUserOptional = userRepository.findById(note.getAssignedTo().getId());

        if (creatorOptional.isEmpty())
            throw new UserNotFoundException("User with id=" + creatorId + " does not exist!");
        else if (assignedUserOptional.isEmpty())
            throw new UserNotFoundException("Assigned user with id=" + note.getAssignedTo().getId() + " does not exist!");
        else if (note.getDateFrom().after(note.getDateTo()))
            throw new InvalidNoteDataException("Date to must be after date from!");

        User creator = creatorOptional.get();
        User assignedUser = assignedUserOptional.get();

        newNote.setAssignedTo(assignedUser);
        newNote.setCreatedBy(creator);
        newNote.setTitle(note.getTitle());
        newNote.setProjectName(note.getProjectName());
        newNote.setContent(note.getContent());
        newNote.setCreationDate(new Date());
        newNote.setDateFrom(note.getDateFrom());
        newNote.setDateTo(note.getDateTo());
        newNote.setPriority(note.getPriority());
        newNote.setStatus(Note.Status.NEW);

        noteRepository.save(newNote);

        return this.noteMapper.convertToNoteDTO(newNote);
    }

    public Boolean deleteNoteById(Long id) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException("Note with id=" + id + " does not exist!");
        } else {
            Note noteToDelete = noteOptional.get();
            noteRepository.delete(noteToDelete);
            return true;
        }
    }

    public NoteDTO updateNoteStatus(Long id, IncompleteNoteDataDTO incompleteNoteDataDTO) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        if (noteOptional.isEmpty())
            throw new NoteNotFoundException("Note with id=" + id + " does not exist!");

        Note noteToUpdate = noteOptional.get();
        Note.Status newStatus = incompleteNoteDataDTO.getStatus();

        if (newStatus != null) {
            if (checkIfStatusExists(newStatus)) {
                noteToUpdate.setStatus(incompleteNoteDataDTO.getStatus());
            } else {
                throw new InvalidNoteDataException("Status " + newStatus + " does not exists!");
            }
        }

        noteRepository.save(noteToUpdate);

        return this.noteMapper.convertToNoteDTO(noteToUpdate);
    }

    public NoteDTO updateNoteDetails(Long id, IncompleteNoteDataDTO incompleteNoteDataDTO) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        if (noteOptional.isEmpty())
            throw new NoteNotFoundException("Note with id=" + id + " does not exist!");

        Note noteToUpdate = noteOptional.get();
        Date newDateFrom = incompleteNoteDataDTO.getDateFrom();
        Date newDateTo = incompleteNoteDataDTO.getDateTo();
        Long assignedUserId = incompleteNoteDataDTO.getAssignedTo();
        Note.Status newStatus = incompleteNoteDataDTO.getStatus();
        Note.Priority newPriority = incompleteNoteDataDTO.getPriority();

        if (newDateFrom != null && newDateTo != null) {
            if (newDateFrom.after(newDateTo) || newDateFrom.equals(newDateTo)) {
                throw new InvalidNoteDataException("Date to must be after date from!");
            } else {
                noteToUpdate.setDateFrom(newDateFrom);
                noteToUpdate.setDateTo(newDateTo);
            }
        } else if (newDateFrom != null) {
            if (newDateFrom.after(noteToUpdate.getDateTo()) || newDateFrom.equals(noteToUpdate.getDateTo())) {
                throw new InvalidNoteDataException("Date to must be after date from!");
            } else {
                noteToUpdate.setDateFrom(newDateFrom);
            }
        } else if (newDateTo != null) {
            if (newDateTo.before(noteToUpdate.getDateFrom()) || noteToUpdate.getDateFrom().equals(newDateTo)) {
                throw new InvalidNoteDataException("Date to must be after date from!");
            } else {
                noteToUpdate.setDateTo(newDateTo);
            }
        }

        if (assignedUserId != null) {
            Optional<User> assignedUserOptional = userRepository.findById(assignedUserId);
            if (assignedUserOptional.isEmpty())
                throw new UserNotFoundException("User with id=" + assignedUserId + " does not exist!");
            else
                noteToUpdate.setAssignedTo(assignedUserOptional.get());
        }

        if (newStatus != null) {
            if (checkIfStatusExists(newStatus)) {
                noteToUpdate.setStatus(incompleteNoteDataDTO.getStatus());
            } else {
                throw new InvalidNoteDataException("Status " + newStatus + " does not exists!");
            }
        }

        if (newPriority != null) {
            if (checkIfPriorityExists(newPriority)) {
                noteToUpdate.setPriority(incompleteNoteDataDTO.getPriority());
            } else {
                throw new InvalidNoteDataException("Priority " + newPriority + " does not exists!");
            }
        }

        noteRepository.save(noteToUpdate);

        return this.noteMapper.convertToNoteDTO(noteToUpdate);
    }

    public List<NoteDTO> findAllByCreatedByAndCreationDateBetween(Long creatorId, Date startDate, Date endDate) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User with id=" + creatorId + "does not exist!");

        User createdBy = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByCreatedByAndCreationDateBetween(createdBy, startDate, endDate);
        List<Note> noteList = optionalList.orElse(Collections.emptyList());

        return noteList.stream()
                .map(noteMapper::convertToNoteDTO)
                .collect(Collectors.toList());
    }

    public List<NoteDTO> findAllByCreatedByAndPriority(Long creatorId, Note.Priority priority) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User with id=" + creatorId + "does not exist!");

        User createdBy = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByCreatedByAndPriority(createdBy, priority);
        List<Note> noteList = optionalList.orElse(Collections.emptyList());

        return noteList.stream()
                .map(noteMapper::convertToNoteDTO)
                .collect(Collectors.toList());
    }

    public List<NoteDTO> findAllByCreatedByAndStatus(Long creatorId, Note.Status status) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User with id=" + creatorId + "does not exist!");

        User createdBy = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByCreatedByAndStatus(createdBy, status);
        List<Note> noteList = optionalList.orElse(Collections.emptyList());

        return noteList.stream()
                .map(noteMapper::convertToNoteDTO)
                .collect(Collectors.toList());
    }

    public List<NoteDTO> findAllByCreatedBy(Long creatorId) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User with id=" + creatorId + "does not exist!");

        User createdBy = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByCreatedBy(createdBy);
        List<Note> notes = optionalList.orElse(Collections.emptyList());
        return notes.stream()
                .map(noteMapper::convertToNoteDTO)
                .collect(Collectors.toList());
    }

    public Optional<List<NoteDTO>> findAllByAssignedTo(Long assignedToId) {
        Optional<User> optionalUser = userRepository.findById(assignedToId);

        if (optionalUser.isEmpty())
            return Optional.empty();

        User assignedTo = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByAssignedTo(assignedTo);
        List<Note> notes = optionalList.orElse(Collections.emptyList());

        return Optional.of(
                notes.stream()
                        .map(noteMapper::convertToNoteDTO)
                        .collect(Collectors.toList())
        );
    }

    public List<NoteDTO> findAllByCreatedByAndProjectName(Long creatorId, String projectName) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User with id=" + creatorId + "does not exist!");

        User createdBy = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByCreatedByAndProjectName(createdBy, projectName);
        List<Note> notes = optionalList.orElse(Collections.emptyList());
        return notes.stream()
                .map(noteMapper::convertToNoteDTO)
                .collect(Collectors.toList());
    }

    public Integer countAllByCreatedByAndStatusAndDateToBetween(Long creatorId, Note.Status status, Date startDate, Date endDate) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User with id=" + creatorId + "does not exist!");

        User createdBy = optionalUser.get();

        return noteRepository.countAllByCreatedByAndStatusAndDateToBetween(createdBy, status, startDate, endDate);
    }

    public Integer countAllByCreatedByAndPriorityAndDateToBetween(Long creatorId, Note.Priority priority, Date startDate, Date endDate) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User with id=" + creatorId + "does not exist!");

        User createdBy = optionalUser.get();

        return noteRepository.countAllByCreatedByAndPriorityAndDateToBetween(createdBy, priority, startDate, endDate);
    }

    private boolean checkIfStatusExists(Note.Status status) {
        for (Note.Status s : Note.Status.values()) {
            if (s.name().equals(status.name()))
                return true;
        }
        return false;
    }

    private boolean checkIfPriorityExists(Note.Priority priority) {
        for (Note.Priority p : Note.Priority.values()) {
            if (p.name().equals(priority.name()))
                return true;
        }
        return false;
    }

    public List<NoteDTO> addNoteList(List<Note> notes, Long id) {
        Optional<User> optionalManager = userRepository.findById(id);

        return notes.stream().map(note -> {
            Note newNote = new Note();
            Optional<User> assignedUserOptional = userRepository.findById(note.getAssignedTo().getId());
            newNote.setAssignedTo(assignedUserOptional.get());
            newNote.setCreatedBy(optionalManager.get());
            newNote.setTitle(note.getTitle());
            newNote.setProjectName(note.getProjectName());
            newNote.setContent(note.getContent());
            newNote.setCreationDate(new Date());
            newNote.setDateFrom(note.getDateFrom());
            newNote.setDateTo(note.getDateTo());
            newNote.setPriority(note.getPriority());
            newNote.setStatus(Note.Status.NEW);
            noteRepository.save(newNote);
            return noteMapper.convertToNoteDTO(newNote);
        }).collect(Collectors.toList());
    }
}
