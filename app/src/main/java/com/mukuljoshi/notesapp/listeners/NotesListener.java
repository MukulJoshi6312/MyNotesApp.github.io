package com.mukuljoshi.notesapp.listeners;

import com.mukuljoshi.notesapp.entities.Note;

public interface NotesListener {

    void onNoteClicked(Note note,int position);
}
