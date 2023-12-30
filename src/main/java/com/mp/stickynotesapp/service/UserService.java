package com.mp.stickynotesapp.service;

import com.mp.stickynotesapp.dto.UserCreationDTO;
import com.mp.stickynotesapp.dto.UserDTO;
import com.mp.stickynotesapp.dto.UserForNoteDTO;
import com.mp.stickynotesapp.exception.InvalidPasswordException;
import com.mp.stickynotesapp.exception.InvalidUserDataException;
import com.mp.stickynotesapp.exception.UserNotFoundException;
import com.mp.stickynotesapp.model.Role;
import com.mp.stickynotesapp.model.User;
import com.mp.stickynotesapp.repository.RoleRepository;
import com.mp.stickynotesapp.repository.UserRepository;
import com.mp.stickynotesapp.util.UserMapper;
import com.neovisionaries.i18n.CountryCode;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final List<String> countryNames = Arrays.stream(CountryCode.values()).map(CountryCode::getName).toList();

    public UserDTO updateUser(Long id, Map<String, Object> fields) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User with id=" + id + " does not exist!");

        String oldPassword = (String) fields.get("oldPassword");
        fields.remove("oldPassword");

        User userToUpdate = optionalUser.get();

        if (!passwordEncoder.matches(oldPassword, userToUpdate.getPassword()))
            throw new InvalidPasswordException("Old password does not match!");

        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(User.class, k);
            field.setAccessible(true);
            if (k.equals("password")) {

                if (passwordEncoder.matches(field.toString(), userToUpdate.getPassword()))
                    throw new InvalidPasswordException("Cannot set the same password!");

                ReflectionUtils.setField(field, userToUpdate, passwordEncoder.encode(v.toString()));

                if (userToUpdate.getIsFirstLogin())
                    userToUpdate.setIsFirstLogin(false);

            } else {
                ReflectionUtils.setField(field, userToUpdate, v);
            }
        });

        userRepository.save(userToUpdate);

        return this.userMapper.convertToUserDTO(userToUpdate);
    }

    public UserDTO updateUserPassword(Long id, Map<String, Object> fields) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User with id=" + id + " does not exist!");

        User userToUpdate = optionalUser.get();

        Field passwordField = ReflectionUtils.findField(User.class, "password");
        String newPassword = (String) fields.get("password");
        passwordField.setAccessible(true);
        ReflectionUtils.setField(passwordField, userToUpdate, passwordEncoder.encode(newPassword));

        userRepository.save(userToUpdate);

        return this.userMapper.convertToUserDTO(userToUpdate);
    }

    public List<UserForNoteDTO> findAllByTeamNameAndManagerId(String teamName, Long managerId) {
        Optional<List<User>> optionalUserList = userRepository.findAllByTeamNameAndCreatorId(teamName, managerId);
        List<User> users = optionalUserList.orElse(Collections.emptyList());

        return users.stream()
                .map(userMapper::convertToUserForNoteDTO)
                .collect(Collectors.toList());
    }

    public UserCreationDTO createManager(User user, Long adminId) {

        if (userRepository.findById(adminId).isEmpty())
            throw new UserNotFoundException("Administrator with id=" + adminId + " does not exist!");

        Optional<User> optionalUser = userRepository.findByFirstNameAndLastNameAndJobID(user.getFirstName(), user.getLastName(), user.getJobID());

        if (optionalUser.isPresent())
            throw new UserNotFoundException(String.format("Manager with the following details already exists: Job ID='%s', First Name='%s', Last Name='%s'",
                    user.getFirstName(), user.getLastName(), user.getJobID()));

        User newUser = this.setUserData(user, adminId);
        newUser.setTeamName(user.getTeamName());
        newUser.setPassword(passwordEncoder.encode(user.getTeamName())); // after first login employee will have to change password
        userRepository.save(newUser);

        Role roleManager = new Role();
        roleManager.setRole("ROLE_MANAGER");
        roleManager.setUser(newUser);
        roleRepository.save(roleManager);

        return this.userMapper.convertToUserCreationDTO(newUser);
    }

    public UserCreationDTO createEmployee(User user, Long managerId) {
        Optional<User> optionalManager = userRepository.findById(managerId);

        if (optionalManager.isEmpty())
            throw new UserNotFoundException("Manager with id=" + managerId + " does not exist!");

        User manager = optionalManager.get();
        Optional<User> optionalUser = userRepository.findByFirstNameAndLastNameAndJobID(user.getFirstName(), user.getLastName(), user.getJobID());

        if (optionalUser.isPresent())
            throw new UserNotFoundException(String.format("Employee with the following details already exists: Job ID='%s', First Name='%s', Last Name='%s'",
                    user.getFirstName(), user.getLastName(), user.getJobID()));

        User newUser = this.setUserData(user, managerId);
        newUser.setTeamName(manager.getTeamName());
        newUser.setPassword(passwordEncoder.encode(manager.getTeamName())); // after first login employee will have to change password
        userRepository.save(newUser);

        Role roleEmployee = new Role();
        roleEmployee.setRole("ROLE_EMPLOYEE");
        roleEmployee.setUser(newUser);
        roleRepository.save(roleEmployee);

        return this.userMapper.convertToUserCreationDTO(newUser);
    }

    public Boolean deleteUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with id=" + id + " does not exist!");
        } else {
            User userToDelete = optionalUser.get();
            userRepository.delete(userToDelete);
            return true;
        }
    }

    public List<UserDTO> findAllBy() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> findAllByWorkCountry(String workCountry) {
        String countryName;

        if (workCountry.length() == 2)
            countryName = CountryCode.getByCode(workCountry).getName();
        else
            countryName = workCountry;

        if (countryName == null)
            throw new InvalidUserDataException("Work country not valid!");

        Optional<List<User>> optionalList = userRepository.findAllByWorkCountry(countryName);
        List<User> users = optionalList.orElse(Collections.emptyList());

        return users.stream()
                .map(userMapper::convertToUserDTO)
                .collect(Collectors.toList());
    }

    private String createUsername(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getJobID());
        sb.append("_");
        sb.append(user.getFirstName().toLowerCase(), 0, 2);
        sb.append(user.getLastName().toLowerCase(), 0, 2);
        sb.append(CountryCode.findByName(user.getWorkCountry()).get(0).name().toLowerCase());

        return sb.toString();
    }

    private User setUserData(User user, Long creatorId) {
        User newUser = new User();

        newUser.setJobID(user.getJobID());
        newUser.setCreatorId(creatorId);

        if (!this.countryNames.contains(user.getWorkCountry()))
            throw new InvalidUserDataException("Work country not valid!");

        newUser.setUsername(createUsername(user));
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDateOfBirth(user.getDateOfBirth());
        newUser.setWorkCountry(user.getWorkCountry());
        newUser.setJobTitle(user.getJobTitle());
        newUser.setIsFirstLogin(true);
        newUser.setAssignedNotes(user.getAssignedNotes());
        newUser.setCreatedNotes(user.getCreatedNotes());

        return newUser;
    }
}
