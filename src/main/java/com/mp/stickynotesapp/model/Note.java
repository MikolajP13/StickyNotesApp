package com.mp.stickynotesapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "notes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String projectName;
    private String content;
    @ManyToOne
    @JoinColumn(name = "assigned_to_id", nullable = false)
    private User assignedTo;
    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;
    private Date creationDate;
    private Date dateFrom;
    private Date dateTo;
    private Priority priority;
    private Status status;

    public enum Priority {
        LOW, NORMAL, HIGH
    }

    public enum Status {
        NEW, IN_PROGRESS, DONE, ACCEPTED, NOT_ACCEPTED
    }
}
