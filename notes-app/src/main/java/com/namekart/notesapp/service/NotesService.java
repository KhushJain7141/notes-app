package com.namekart.notesapp.service;

import com.namekart.notesapp.DTO.NoteDTO;
import com.namekart.notesapp.DTO.NoteRequestDTO;
import com.namekart.notesapp.model.Notes;
import com.namekart.notesapp.model.User;
import com.namekart.notesapp.repository.NotesRepository;
import com.namekart.notesapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotesService {

    private final NotesRepository notesRepository;
    private final UserRepository userRepository;

    public NotesService(NotesRepository notesRepository, UserRepository userRepository) {
        this.notesRepository = notesRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private NoteDTO convertToDTO(Notes note) {
        NoteDTO DTO = new NoteDTO();
        DTO.setId(note.getId());
        DTO.setTitle(note.getTitle());
        DTO.setContent(note.getContent());
        DTO.setCreatedAt(note.getCreatedAt());
        DTO.setUpdatedAt(note.getUpdatedAt());
        return DTO;
    }

    // CREATE
    public NoteDTO createNote(NoteRequestDTO request) {
        User user = getCurrentUser();
        Notes note = new Notes();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setUser(user);
        Notes savedNote = notesRepository.save(note);
        return convertToDTO(savedNote);
    }

    // GET ALL
    public List<NoteDTO> getAllNotes() {
        User user = getCurrentUser();
        return notesRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public NoteDTO getNoteById(Long noteId) {
        User user = getCurrentUser();
        Notes note = notesRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to access this note");
        }
        return convertToDTO(note);
    }

    // UPDATE
    public NoteDTO updateNote(Long noteId, NoteRequestDTO request) {
        User user = getCurrentUser();
        Notes note = notesRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this note");
        }
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        Notes updatedNote = notesRepository.save(note);
        return convertToDTO(updatedNote);
    }

    // DELETE
    public void deleteNote(Long noteId) {
        User user = getCurrentUser();
        Notes note = notesRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this note");
        }
        notesRepository.delete(note);
    }
}
