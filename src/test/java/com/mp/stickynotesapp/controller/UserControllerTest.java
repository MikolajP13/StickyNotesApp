package com.mp.stickynotesapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mp.stickynotesapp.dto.IncompleteUserDataDTO;
import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void initUser() throws ParseException {
        String dateOfBirthString = "1979-10-01";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = dateFormat.parse(dateOfBirthString);

        testUser = new User();
        testUser.setJobID("TT001");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setDateOfBirth(dateOfBirth);
        testUser.setWorkCountry("Spain");
        testUser.setTeamName("Test Team");
        testUser.setJobTitle("Test");
    }

    @AfterEach
    void tearDown() {
        Optional<User> userToDelete = userRepository.findByUsername("TT001_jodoes");
        userToDelete.ifPresent(user -> userRepository.delete(user));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void roleAdminShouldGetAccessToGetAllUsers() throws Exception {

        mockMvc.perform(get("/user/all"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(roles = {"MANAGER", "EMPLOYEE"})
    void roleManagerAndEmployeeShouldNotGetAccessToGetAllUsers() throws Exception {

        mockMvc.perform(get("/user/all"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateManager() throws Exception {

        mockMvc.perform(post("/user/create-manager/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(testUser)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.username").value("TT001_jodoes"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdateUserFirstName() throws Exception {
        testUser.setUsername("TT001_jodoes");
        this.userRepository.save(testUser);

        User user = this.userRepository.findByUsername("TT001_jodoes").orElseThrow();

        IncompleteUserDataDTO dto = new IncompleteUserDataDTO();
        dto.setFirstName("Greg");

        String updateUrl = UriComponentsBuilder.fromPath("/user/{id}/admin-update")
                .buildAndExpand(user.getId())
                .toUriString();

        mockMvc.perform(patch(updateUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("TT001_grdoes"));

        this.userRepository.deleteById(user.getId());
        assertFalse(this.userRepository.existsById(user.getId()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteUser() throws Exception {
        testUser.setUsername("TT001_jodoes");
        this.userRepository.save(testUser);

        User user = this.userRepository.findByUsername("TT001_jodoes").orElseThrow();
        String deleteUrl = UriComponentsBuilder.fromPath("/user/{id}/delete")
                .buildAndExpand(user.getId())
                .toUriString();

        mockMvc.perform(delete(deleteUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(testUser)))
                .andExpect(status().is(200));
    }
}