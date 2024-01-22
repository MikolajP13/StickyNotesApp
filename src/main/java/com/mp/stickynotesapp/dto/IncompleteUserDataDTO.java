package com.mp.stickynotesapp.dto;

import lombok.Data;

@Data
public class IncompleteUserDataDTO {
    private String oldPassword;
    private String newPassword;
    private String firstName;
    private String lastName;
    private String workCountry;
}
