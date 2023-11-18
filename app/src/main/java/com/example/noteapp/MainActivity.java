package com.example.noteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "NotePrefs";
    private static final String KEY_NOTE_COUNT = "NoteCount";
    private LinearLayout notesContainer;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesContainer = findViewById(R.id.notesContainer);
        Button saveButton = findViewById(R.id.saveButton);
        Button changePasswordButton = findViewById(R.id.changePassword);

        noteList = new ArrayList<>();

        saveButton.setOnClickListener(view -> saveNote());

        changePasswordButton.setOnClickListener(view -> showChangePassword());

        loadNotesFromPreferences();
        displayNotes();
    }

    private void displayNotes() {
        for (Note note : noteList){
            createNoteView(note);
        }
    }

    private void loadNotesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        int noteCount =  sharedPreferences.getInt(KEY_NOTE_COUNT, 0);

        for (int i = 0; i < noteCount; i++){
            String title  = sharedPreferences.getString("note_title_" +i, "");
            String content = sharedPreferences.getString("note_content_" +i, "");

            Note note =  new Note();
            note.setTitle(title);
            note.setContent(content);

            noteList.add(note);
        }
    }

    private void saveNote() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        if (!title.isEmpty() && !content.isEmpty()){
            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);

            noteList.add(note);
            saveNotesToPreferences();

            createNoteView(note);
            clearInputFields();

        }
    }

    private void clearInputFields() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText =  findViewById(R.id.contentEditText);

        titleEditText.getText().clear();
        contentEditText.getText().clear();
    }

    private void createNoteView(final Note note) {
        View noteView = getLayoutInflater().inflate(R.layout.note_item, null);
        TextView titleTextView = noteView.findViewById(R.id.titleTextView);
        TextView contentTextView = noteView.findViewById(R.id.contentTextView);

        titleTextView.setText(note.getTitle());
        contentTextView.setText(note.getContent());

        noteView.setOnLongClickListener(view -> {
            showDeleteDialog(note);
            return true;
        });

        notesContainer.addView(noteView);

    }

    private void showDeleteDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit note?");
        builder.setMessage("Choose what you want.");
        builder.setPositiveButton("Rewrite", (dialogInterface, i) -> rewriteNote(note));
        builder.setNegativeButton("Delete", (dialogInterface, i) -> deleteNoteAndRefresh(note));
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    private void rewriteNote(Note note) {

        String title = note.getTitle();
        String content = note.getContent();
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        Button saveRewriteButton = findViewById(R.id.saveButton);

        titleEditText.setText(title);
        contentEditText.setText(content);

        saveRewriteButton.setOnClickListener(view -> {
            saveRewrittenNote(note);

            saveRewriteButton.setOnClickListener(view1 -> saveNote());
        });

    }

    private void showChangePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change password?");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Change", (dialogInterface, i) -> {
            Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteNoteAndRefresh(Note note) {
        noteList.remove(note);
        saveNotesToPreferences();
        refreshNotesView();
    }

    private void refreshNotesView() {
        notesContainer.removeAllViews();
        displayNotes();
    }

    private void saveNotesToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_NOTE_COUNT, noteList.size());
        for (int i = 0; i < noteList.size(); i++){
            Note note = noteList.get(i);
            editor.putString("note_title_" + i, note.getTitle());
            editor.putString("note_content_" + i, note.getContent());
        }
        editor.apply();
    }

    private void saveRewrittenNote(Note note) {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        if (!title.isEmpty() && !content.isEmpty()) {
            note.setTitle(title);
            note.setContent(content);

            saveNotesToPreferences();
            refreshNotesView();
            clearInputFields();
        }
    }

}