package com.mp.stickynotesapp.dto;

import lombok.Data;

@Data
public class UserCreationDTO {
    private String jobID;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String workCountry;
    private String jobTitle;
}
