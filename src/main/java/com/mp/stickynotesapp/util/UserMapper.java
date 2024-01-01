package com.mp.stickynotesapp.util;

import com.mp.stickynotesapp.dto.UserCreationDTO;
import com.mp.stickynotesapp.dto.UserDTO;
import com.mp.stickynotesapp.dto.UserForNoteDTO;
import com.mp.stickynotesapp.model.Role;
import com.mp.stickynotesapp.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserMapper {

    private final NoteMapper noteMapper;

    public UserCreationDTO convertToUserCreationDTO(User user) {
        UserCreationDTO dto = new UserCreationDTO();

        dto.setJobID(user.getJobID());
        dto.setJobTitle(user.getJobTitle());
        dto.setTeamName(user.getTeamName());
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
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setWorkCountry(user.getWorkCountry());
        dto.setTeamName(user.getTeamName());
        dto.setJobTitle(user.getJobTitle());
        dto.setPassword(user.getPassword());
        dto.setCreatorId(user.getCreatorId());
        dto.setIsFirstLogin(user.getIsFirstLogin());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet())
        );
        dto.setAssignedNotes(user.getAssignedNotes().stream()
                .map(noteMapper::convertToNoteDTO)
                .collect(Collectors.toList()));
        dto.setCreatedNotes(user.getCreatedNotes().stream()
                .map(noteMapper::convertToNoteDTO)
                .collect(Collectors.toList()));

        return dto;
    }
}
