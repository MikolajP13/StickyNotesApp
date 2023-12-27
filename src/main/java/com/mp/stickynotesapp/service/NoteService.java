package com.mp.stickynotesapp.service;

import com.mp.stickynotesapp.exception.UserException;
import com.mp.stickynotesapp.model.Note;
import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.repository.NoteRepository;
import com.mp.stickynotesapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Service
@AllArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public Note createNote(Note note, Long creatorId) {
        Note newNote = new Note();
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserException("User does not exist!");

        User creator = optionalUser.get();
        //TODO check dates/user etc.
        newNote.setAssignedTo(note.getAssignedTo());
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

        return newNote;
    }

    public Boolean deleteNote(Long noteId) {
        Optional<Note> noteOptional = noteRepository.findById(noteId);

        if (noteOptional.isPresent()) {
            Note noteToDelete = noteOptional.get();
            noteRepository.delete(noteToDelete);
            return true;
        } else {
            return false;
        }
    }

    public Note updateNote(Long id, Map<String, Object> fields) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        if(noteOptional.isEmpty())
            throw new UserException("Note not found!");

        Note noteToUpdate = noteOptional.get();

        fields.forEach((k,v) -> {
            Field field = ReflectionUtils.findField(Note.class, k);
            field.setAccessible(true);
            if (field.getType().isEnum()) {
                Enum<?> enumValue = Enum.valueOf((Class<Enum>) field.getType(), (String) v);
                ReflectionUtils.setField(field, noteToUpdate, enumValue);
            } else {
                ReflectionUtils.setField(field, noteToUpdate, v);
            }
        });

        noteRepository.save(noteToUpdate);

        return noteToUpdate;
    }

    public Note changeAssignedTo(Long newAssignedToId) {
        Note noteToUpdate = new Note();
        Optional<User> optionalUser = userRepository.findById(newAssignedToId);

        if (optionalUser.isEmpty())
            throw new UserException("User does not exist!");

        User newAssignedTo = optionalUser.get();
        noteToUpdate.setAssignedTo(newAssignedTo);
        noteRepository.save(noteToUpdate);

        return noteToUpdate;
    }

    public List<Note> findAllByCreatedByAndCreationDateBetween(Long creatorId, Date startDate, Date endDate) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserException("");

        User createdBy = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByCreatedByAndCreationDateBetween(createdBy, startDate, endDate);

        return optionalList.orElse(Collections.emptyList());
    }

    public List<Note> findAllByCreatedByAndPriority(Long creatorId, Note.Priority priority) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserException("");

        User createdBy = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByCreatedByAndPriority(createdBy, priority);

        return optionalList.orElse(Collections.emptyList());
    }

    public List<Note> findAllByCreatedByAndStatus(Long creatorId, Note.Status status) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserException("");

        User createdBy = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByCreatedByAndStatus(createdBy, status);

        return optionalList.orElse(Collections.emptyList());
    }

    public List<Note> findAllByCreatedBy(Long creatorId) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserException("");

        User createdBy = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByCreatedBy(createdBy);

        return optionalList.orElse(Collections.emptyList());
    }

    public List<Note> findAllByAssignedTo(Long assignedToId) {
        Optional<User> optionalUser = userRepository.findById(assignedToId);

        if (optionalUser.isEmpty())
            throw new UserException("User does not exist!");

        User assignedTo = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByAssignedTo(assignedTo);

        return optionalList.orElse(Collections.emptyList());
    }

    public List<Note> findAllByCreatedByAndProjectName(Long creatorId, String projectName) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserException("");

        User createdBy = optionalUser.get();

        Optional<List<Note>> optionalList = noteRepository.findAllByCreatedByAndProjectName(createdBy, projectName);

        return optionalList.orElse(Collections.emptyList());
    }

    public Integer countAllByCreatedByAndStatusAndDateToBetween(Long creatorId, Note.Status status, Date startDate, Date endDate) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserException("");

        User createdBy = optionalUser.get();

        Integer notesCounter = noteRepository.countAllByCreatedByAndStatusAndDateToBetween(createdBy, status, startDate, endDate);

        return notesCounter;
    }

    public Integer countAllByCreatedByAndPriorityAndDateToBetween(Long creatorId, Note.Priority priority, Date startDate, Date endDate) {
        Optional<User> optionalUser = userRepository.findById(creatorId);

        if (optionalUser.isEmpty())
            throw new UserException("");

        User createdBy = optionalUser.get();

        Integer notesCounter = noteRepository.countAllByCreatedByAndPriorityAndDateToBetween(createdBy, priority, startDate, endDate);

        return notesCounter;
    }

}
