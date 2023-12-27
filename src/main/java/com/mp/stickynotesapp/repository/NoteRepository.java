package com.mp.stickynotesapp.repository;

import com.mp.stickynotesapp.model.Note;
import com.mp.stickynotesapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<List<Note>> findAllByCreatedByAndCreationDateBetween(User createdBy, Date startDate, Date endDate);
    Optional<List<Note>> findAllByCreatedByAndPriority(User createdBy, Note.Priority priority);
    Optional<List<Note>> findAllByCreatedByAndStatus(User createdBy, Note.Status status);
    Optional<List<Note>> findAllByCreatedBy(User createdBy);
    Optional<List<Note>> findAllByAssignedTo(User assignedTo);
    Optional<List<Note>> findAllByCreatedByAndProjectName(User createdBy, String projectName);
    Integer countAllByCreatedByAndStatusAndDateToBetween(User createdBy, Note.Status status, Date startDate, Date endDate);
    Integer countAllByCreatedByAndPriorityAndDateToBetween(User createdBy, Note.Priority priority, Date startDate, Date endDate);

}
