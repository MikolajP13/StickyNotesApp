package com.mp.stickynotesapp.dto;

import com.mp.stickynotesapp.model.Note;
import lombok.Data;

import java.util.Date;

@Data
public class NoteDTO {
    private Long id;
    private String title;
    private String projectName;
    private String content;
    private Long assignedEmployeeId;
    private String assignedEmployeeJobTitle;
    private String assignedEmployeeFirstname;
    private String assignedEmployeeLastname;
    private String assignedEmployeeWorkCountry;
    private Long managerId;
    private String managerJobTitle;
    private String managerFirstname;
    private String managerLastname;
    private String managerWorkCountry;
    private Date creationDate;
    private Date dateFrom;
    private Date dateTo;
    private Note.Status status;
    private Note.Priority priority;
}
