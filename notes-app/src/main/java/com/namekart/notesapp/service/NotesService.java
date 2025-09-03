package com.namekart.notesapp.service;

import com.namekart.notesapp.model.Notes;
import com.namekart.notesapp.model.User;
import com.namekart.notesapp.repository.NotesRepository;
import com.namekart.notesapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Notes createNote(Notes note) {
        note.setUser(getCurrentUser());
        return notesRepository.save(note);
    }

    public Notes getNoteById(Long noteId) {
        User user = getCurrentUser();
        Notes note = notesRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to access this note");
        }
        return note;
    }

    public List<Notes> getAllNotes() {
        return notesRepository.findByUser(getCurrentUser());
    }

    public Notes updateNote(Long noteId, Notes updatedNote) {
        User user = getCurrentUser();
        Notes note = getNoteById(noteId);
        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this note");
        }
        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        return notesRepository.save(note);
    }

    public void deleteNote(Long noteId) {
        User user = getCurrentUser();
        Notes note = getNoteById(noteId);
        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this note");
        }
        notesRepository.delete(note);
    }
}
