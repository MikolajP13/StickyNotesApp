package com.mp.stickynotesapp.repository;

import com.mp.stickynotesapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void initUser() throws ParseException {
        userRepository = Mockito.mock(UserRepository.class);

        String dateOfBirthString = "1979-10-01";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = dateFormat.parse(dateOfBirthString);

        testUser = new User();
        testUser.setJobID("TT001");
        testUser.setUsername("TT001_jodoes");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setDateOfBirth(dateOfBirth);
        testUser.setWorkCountry("Spain");
        testUser.setTeamName("Test Team");
        testUser.setJobTitle("Test");

        Mockito.when(userRepository.findByFirstName("John")).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.findByUsername("TT001_jodoes")).thenReturn(Optional.of(testUser));
    }

    @Test
    void shouldFindUserByFirstName() {
        Optional<User> optionalUser = userRepository.findByFirstName("John");

        assertTrue(optionalUser.isPresent());
        assertEquals(testUser.getId(), optionalUser.get().getId());
        assertEquals(testUser.getFirstName(), optionalUser.get().getFirstName());
    }

    @Test
    void shouldNotFindUserByFirstNameWhenNotFound() {
        Optional<User> foundUser = userRepository.findByFirstName("not_existing_firstname");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void shouldFindUserByUsername() {
        Optional<User> optionalUser = userRepository.findByUsername("TT001_jodoes");

        assertTrue(optionalUser.isPresent());
        assertEquals(testUser.getId(), optionalUser.get().getId());
        assertEquals(testUser.getUsername(), optionalUser.get().getUsername());
    }

    @Test
    void shouldNotFindUserByUsernameWhenNotFound() {
        Optional<User> optionalUser = userRepository.findByUsername("not_existing_username");

        assertFalse(optionalUser.isPresent());
    }

}