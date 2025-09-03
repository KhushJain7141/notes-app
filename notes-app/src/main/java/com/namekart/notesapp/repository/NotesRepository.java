package com.namekart.notesapp.repository;

import com.namekart.notesapp.model.Notes;
import com.namekart.notesapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepository  extends JpaRepository<Notes,Long> {
    List<Notes> findByUser(User user);
}
