package com.mp.stickynotesapp.dto;

import com.mp.stickynotesapp.model.Note;
import com.mp.stickynotesapp.model.Role;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String jobID;
    private Set<Role> roles;
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
    private List<Note> assignedNotes;
    private List<Note> createdNotes;
}
