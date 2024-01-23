package com.mp.stickynotesapp.service;

import com.mp.stickynotesapp.dto.UserDTO;
import com.mp.stickynotesapp.exception.UserNotFoundException;
import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.repository.RoleRepository;
import com.mp.stickynotesapp.repository.UserRepository;
import com.mp.stickynotesapp.util.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private UserService userService;
    private User user;

    @BeforeEach
    void init() {
        user = new User();
        user.setId(1L);

        roleRepository = Mockito.mock(RoleRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userMapper = Mockito.mock(UserMapper.class);

        userService = new UserService(userRepository, roleRepository, passwordEncoder, userMapper);
    }

    @Test
    void shouldDeleteUserById() {
        Long userId = 1L;

        when(userRepository.findById(this.user.getId())).thenReturn(Optional.of(this.user));
        doNothing().when(userRepository).delete(this.user);

        Boolean result = userService.deleteUserById(userId);

        assertTrue(result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(this.user);
    }

    @Test
    void shouldNotDeleteUserWhenUserNotExists() {
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(ArgumentMatchers.any(User.class));
    }

    @Test
    void shouldFindAllUsers() {
        User u1 = new User();
        u1.setId(1L);
        u1.setFirstName("John");
        u1.setLastName("Doe");

        User u2 = new User();
        u1.setId(2L);
        u1.setFirstName("Jack");
        u1.setLastName("Smith");

        List<User> users = new ArrayList<>();
        users.add(u1);
        users.add(u2);

        List<UserDTO> expectedUserDTOs = users.stream().map(userMapper::convertToUserDTO).collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.convertToUserDTO(users.get(0))).thenReturn(expectedUserDTOs.get(0));
        when(userMapper.convertToUserDTO(users.get(1))).thenReturn(expectedUserDTOs.get(1));

        List<UserDTO> result = userService.findAll();

        assertEquals(expectedUserDTOs, result);
    }

}