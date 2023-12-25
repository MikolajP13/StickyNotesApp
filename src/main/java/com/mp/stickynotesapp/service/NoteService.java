package com.mp.stickynotesapp.service;

import com.mp.stickynotesapp.repository.NoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NoteService {
    
    private NoteRepository noteRepository;


}
