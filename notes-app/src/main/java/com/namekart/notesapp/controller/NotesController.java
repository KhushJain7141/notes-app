package com.namekart.notesapp.controller;

import com.namekart.notesapp.DTO.NoteDTO;
import com.namekart.notesapp.DTO.NoteRequestDTO;
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

    // CREATE
    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteRequestDTO request) {
        return ResponseEntity.ok(notesService.createNote(request));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<NoteDTO>> getAllNotes() {
        return ResponseEntity.ok(notesService.getAllNotes());
    }

    // GET BY ID
    @GetMapping("/{noteId}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable Long noteId) {
        return ResponseEntity.ok(notesService.getNoteById(noteId));
    }

    // UPDATE
    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long noteId,
                                              @RequestBody NoteRequestDTO request) {
        return ResponseEntity.ok(notesService.updateNote(noteId, request));
    }

    // DELETE
    @DeleteMapping("/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable Long noteId) {
        notesService.deleteNote(noteId);
        return ResponseEntity.ok("Note deleted successfully");
    }
}
