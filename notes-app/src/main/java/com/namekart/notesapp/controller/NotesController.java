package com.namekart.notesapp.controller;

import com.namekart.notesapp.model.Notes;
import com.namekart.notesapp.service.NotesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping
    public ResponseEntity<Notes> createNote(@RequestBody Notes note) {
        return ResponseEntity.ok(notesService.createNote(note));
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<Notes> getNoteById(@PathVariable Long noteId) {
        return ResponseEntity.ok(notesService.getNoteById(noteId));
    }

    @GetMapping
    public ResponseEntity<List<Notes>> getAllNotes() {
        return ResponseEntity.ok(notesService.getAllNotes());
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Notes> updateNote(@PathVariable Long noteId, @RequestBody Notes updatedNote) {
        return ResponseEntity.ok(notesService.updateNote(noteId, updatedNote));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable Long noteId) {
        notesService.deleteNote(noteId);
        return ResponseEntity.ok("Note deleted successfully");
    }
}
