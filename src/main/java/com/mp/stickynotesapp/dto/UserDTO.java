package com.mp.stickynotesapp.dto;

import com.mp.stickynotesapp.model.Note;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String jobID;
    private Set<String> roles;
    private String username;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String workCountry;
    private String teamName;
    private String jobTitle;
    private String password;
    private Long creatorId;
    private Boolean isFirstLogin;
    private List<NoteDTO> assignedNotes;
    private List<NoteDTO> createdNotes;
}
