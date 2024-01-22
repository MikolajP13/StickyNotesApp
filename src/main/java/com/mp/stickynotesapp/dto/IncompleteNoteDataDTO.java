package com.mp.stickynotesapp.dto;

import com.mp.stickynotesapp.model.Note;
import lombok.Data;

import java.util.Date;

@Data
public class IncompleteNoteDataDTO {
    private Note.Status status;
    private Note.Priority priority;
    private Long assignedTo;
    private Date dateFrom;
    private Date dateTo;
}
