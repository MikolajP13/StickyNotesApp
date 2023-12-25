package com.mp.stickynotesapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long jobID;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Role> roles;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String workCountry;

    private String teamName;

    private String jobTitle;

    private Boolean isFirstLogin;

    @OneToMany(mappedBy = "assignedTo")
    private List<Note> assignedNotes;

    @OneToMany(mappedBy = "createdBy")
    private List<Note> createdNotes;
}
