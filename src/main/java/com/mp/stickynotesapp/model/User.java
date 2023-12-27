package com.mp.stickynotesapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String jobID;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
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

    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore
    private List<Note> assignedNotes;

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private List<Note> createdNotes;
}
