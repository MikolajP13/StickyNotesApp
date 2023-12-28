package com.mp.stickynotesapp.util;

import com.mp.stickynotesapp.dto.UserCreationDTO;
import com.mp.stickynotesapp.dto.UserDTO;
import com.mp.stickynotesapp.dto.UserForNoteDTO;
import com.mp.stickynotesapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserCreationDTO convertToUserCreationDTO(User user) {
        UserCreationDTO dto = new UserCreationDTO();

        dto.setJobID(user.getJobID());
        dto.setJobTitle(user.getJobTitle());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setWorkCountry(user.getWorkCountry());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());

        return dto;
    }

    public UserForNoteDTO convertToUserForNoteDTO(User user) {
        UserForNoteDTO dto = new UserForNoteDTO();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setWorkCountry(user.getWorkCountry());
        dto.setJobTitle(user.getJobTitle());

        return dto;
    }

    public UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setJobID(user.getJobID());
        dto.setRoles(user.getRoles());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setWorkCountry(user.getWorkCountry());
        dto.setTeamName(user.getTeamName());
        dto.setJobTitle(user.getJobTitle());
        dto.setPassword(user.getPassword());
        dto.setCreatorId(user.getCreatorId());
        dto.setAssignedNotes(user.getAssignedNotes());
        dto.setCreatedNotes(user.getCreatedNotes());

        return dto;
    }
}
