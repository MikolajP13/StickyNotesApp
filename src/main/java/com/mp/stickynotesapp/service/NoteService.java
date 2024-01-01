package com.mp.stickynotesapp.service;

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
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public NoteDTO updateNoteStatus(Long id, Map<String, Object> fields) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        if (noteOptional.isEmpty())
            throw new NoteNotFoundException("Note with id=" + id + " does not exist!");

        Note noteToUpdate = noteOptional.get();

        fields.forEach((k, v) -> {
            Field statusField = ReflectionUtils.findField(Note.class, k);

            if (statusField == null || !k.equals("status"))
                throw new InvalidNoteDataException("Only status field is available!");

            statusField.setAccessible(true);
            Enum<?> enumValue = Enum.valueOf((Class<Enum>) statusField.getType(), (String) fields.get("status"));
            ReflectionUtils.setField(statusField, noteToUpdate, enumValue);

        });

        noteRepository.save(noteToUpdate);

        return this.noteMapper.convertToNoteDTO(noteToUpdate);
    }

    public NoteDTO updateNoteDetails(Long id, Map<String, Object> fields) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Optional<Note> noteOptional = noteRepository.findById(id);

        if (noteOptional.isEmpty())
            throw new NoteNotFoundException("Note with id=" + id + " does not exist!");

        Note noteToUpdate = noteOptional.get();

        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Note.class, k);

            if (k == null)
                throw new InvalidNoteDataException("Available fields are: assignedTo, dateFrom" +
                        ", dateTo or priority");

            field.setAccessible(true);
            if (field.getType().isEnum()) {
                Enum<?> enumValue = Enum.valueOf((Class<Enum>) field.getType(), (String) fields.get(k));
                ReflectionUtils.setField(field, noteToUpdate, enumValue);

            } else if (k.equals("dateFrom") || k.equals("dateTo")) {
                String newDate = v.toString();
                try {
                    Date from = k.equals("dateFrom") ? sdf.parse(newDate) : noteToUpdate.getDateFrom();
                    Date to = k.equals("dateTo") ? sdf.parse(newDate) : noteToUpdate.getDateTo();

                    if (from.after(to) || from.equals(to))
                        throw new InvalidNoteDataException("Date to must be after date from!");

                    ReflectionUtils.setField(field, noteToUpdate, sdf.parse(newDate));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } else if (k.equals("assignedTo")) {
                Long assignedUserId = Long.parseLong(v.toString());
                Optional<User> assignedUserOptional = userRepository.findById(assignedUserId);

                if (assignedUserOptional.isEmpty())
                    throw new UserNotFoundException("User with id=" + assignedUserId + " does not exist!");

                ReflectionUtils.setField(field, noteToUpdate, assignedUserOptional.get());
            }
        });

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
