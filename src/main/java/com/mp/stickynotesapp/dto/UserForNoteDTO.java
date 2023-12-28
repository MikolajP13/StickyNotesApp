package com.mp.stickynotesapp.dto;

import lombok.Data;

@Data
public class UserForNoteDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String workCountry;
    private String jobTitle;
}
