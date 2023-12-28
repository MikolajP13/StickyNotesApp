package com.mp.stickynotesapp.service;

import com.mp.stickynotesapp.dto.UserCreationDTO;
import com.mp.stickynotesapp.dto.UserDTO;
import com.mp.stickynotesapp.dto.UserForNoteDTO;
import com.mp.stickynotesapp.exception.UserException;
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

    public Boolean updateUser(Long id, Map<String, Object> fields) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isEmpty())
                throw new UserException("User does not exist!");

            String oldPassword = (String) fields.get("oldPassword");
            fields.remove("oldPassword");

            User userToUpdate = optionalUser.get();

            if (!passwordEncoder.matches(oldPassword, userToUpdate.getPassword()))
                throw new UserException("Old password does not match!");

            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(User.class, k);
                field.setAccessible(true);
                if (k.equals("password")) {

                    if (passwordEncoder.matches(field.toString(), userToUpdate.getPassword()))
                        throw new UserException("Cannot set the same password!");

                    ReflectionUtils.setField(field, userToUpdate, passwordEncoder.encode(v.toString()));

                    if (userToUpdate.getIsFirstLogin())
                        userToUpdate.setIsFirstLogin(false);

                } else {
                    ReflectionUtils.setField(field, userToUpdate, v);
                }
            });

            userRepository.save(userToUpdate);
            return true;

        } catch (UserException e) {
            return false;
        }
    }

    public List<UserForNoteDTO> findAllByTeamNameAndManagerId(String teamName, Long managerId) {
        Optional<List<User>> optionalUserList = userRepository.findAllByTeamNameAndCreatorId(teamName, managerId);
        List<User> users = optionalUserList.orElse(Collections.emptyList());
        return users.stream()
                .map(userMapper::convertToUserForNoteDTO)
                .collect(Collectors.toList());
    }

    public UserCreationDTO createManager(User user, Long adminId) {
        User newUser = new User();

        if (userRepository.findById(adminId).isEmpty())
            throw new UserException("Administrator with id=" + adminId + " does not exist!");

        Optional<User> optionalUser = userRepository.findByFirstNameAndLastNameAndJobID(user.getFirstName(), user.getLastName(), user.getJobID());

        if (optionalUser.isPresent())
            throw new UserException(String.format("Manager with the following details already exists: Job ID='%s', First Name='%s', Last Name='%s'",
                    user.getFirstName(), user.getLastName(), user.getJobID()));

        newUser.setJobID(user.getJobID());
        newUser.setCreatorId(adminId);

        if (!this.countryNames.contains(user.getWorkCountry()))
            throw new UserException("Country name not valid!");
        newUser.setUsername(createUsername(user));

        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDateOfBirth(user.getDateOfBirth());
        newUser.setWorkCountry(user.getWorkCountry());
        newUser.setTeamName(user.getTeamName());
        newUser.setJobTitle(user.getJobTitle());
        newUser.setPassword(passwordEncoder.encode(user.getTeamName()));
        newUser.setIsFirstLogin(true);
        newUser.setAssignedNotes(user.getAssignedNotes());
        newUser.setCreatedNotes(user.getCreatedNotes());

        userRepository.save(newUser);

        Role roleManager = new Role();
        roleManager.setRole("ROLE_MANAGER");
        roleManager.setUser(newUser);
        roleRepository.save(roleManager);

        return userMapper.convertToUserCreationDTO(newUser);
    }

    public UserCreationDTO createEmployee(User user, Long managerId) {
        User newUser = new User();

        Optional<User> optionalManager = userRepository.findById(managerId);

        if (optionalManager.isEmpty())
            throw new UserException("Manager with id=" + managerId + " does not exist!");

        User manager = optionalManager.get();
        Optional<User> optionalUser = userRepository.findByFirstNameAndLastNameAndJobID(user.getFirstName(), user.getLastName(), user.getJobID());

        if(optionalUser.isPresent())
            throw new UserException(String.format("Employee with the following details already exists: Job ID='%s', First Name='%s', Last Name='%s'",
                    user.getFirstName(), user.getLastName(), user.getJobID()));

        newUser.setJobID(user.getJobID());
        newUser.setCreatorId(managerId);
        if (!this.countryNames.contains(user.getWorkCountry()))
            throw new UserException("Country name not valid!");
        newUser.setUsername(createUsername(user));

        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDateOfBirth(user.getDateOfBirth());
        newUser.setWorkCountry(user.getWorkCountry());
        newUser.setTeamName(manager.getTeamName());
        newUser.setJobTitle(user.getJobTitle());
        newUser.setPassword(passwordEncoder.encode(manager.getTeamName())); // after first login employee will have to change password
        newUser.setIsFirstLogin(true);
        newUser.setAssignedNotes(user.getAssignedNotes());
        newUser.setCreatedNotes(user.getCreatedNotes());

        userRepository.save(newUser);

        Role roleEmployee = new Role();
        roleEmployee.setRole("ROLE_EMPLOYEE");
        roleEmployee.setUser(newUser);
        roleRepository.save(roleEmployee);

        return userMapper.convertToUserCreationDTO(newUser);
    }

    public Boolean deleteUserById(Long userId) {
        //TODO: deleted user cannot have uncompleted assigned notes!
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            return false;
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

}
