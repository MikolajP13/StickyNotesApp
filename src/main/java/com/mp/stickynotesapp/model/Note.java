package com.mp.stickynotesapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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

    @NotBlank(message = "Title is mandatory")
    @Size(min = 5, message = "Title must have at least 5 characters")
    private String title;

    @NotBlank(message = "Project name is mandatory")
    @Size(min = 5, message = "Project name must have at least 5 characters")
    private String projectName;

    @NotBlank(message = "Content is mandatory")
    private String content;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id", nullable = false)
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    private Date creationDate;

    @NotNull(message = "Date from is mandatory")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFrom;

    @NotNull(message = "Date to is mandatory")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateTo;

    @NotNull(message = "Priority is mandatory")
    private Priority priority;

    private Status status;

    public enum Priority {
        LOW, NORMAL, HIGH
    }

    public enum Status {
        NEW, IN_PROGRESS, DONE, ACCEPTED, NOT_ACCEPTED
    }
}
