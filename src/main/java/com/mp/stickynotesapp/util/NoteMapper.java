package com.mp.stickynotesapp.util;

import com.mp.stickynotesapp.dto.NoteDTO;
import com.mp.stickynotesapp.model.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
    public NoteDTO convertToNoteDTO(Note note) {
        NoteDTO dto = new NoteDTO();

        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setProjectName(note.getProjectName());
        dto.setContent(note.getContent());
        dto.setAssignedEmployeeId(note.getAssignedTo().getId());
        dto.setAssignedEmployeeJobTitle(note.getAssignedTo().getJobTitle());
        dto.setAssignedEmployeeFirstname(note.getAssignedTo().getFirstName());
        dto.setAssignedEmployeeLastname(note.getAssignedTo().getLastName());
        dto.setAssignedEmployeeWorkCountry(note.getAssignedTo().getWorkCountry());
        dto.setManagerId(note.getCreatedBy().getId());
        dto.setManagerJobTitle(note.getCreatedBy().getJobTitle());
        dto.setManagerFirstname(note.getCreatedBy().getFirstName());
        dto.setManagerLastname(note.getCreatedBy().getLastName());
        dto.setManagerWorkCountry(note.getCreatedBy().getWorkCountry());
        dto.setCreationDate(note.getCreationDate());
        dto.setDateFrom(note.getDateFrom());
        dto.setDateTo(note.getDateTo());
        dto.setStatus(note.getStatus());
        dto.setPriority(note.getPriority());

        return dto;
    }

}
