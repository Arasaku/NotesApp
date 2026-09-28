package com.example.main.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main.R;
import com.example.main.db.NoteDao;
import com.example.main.model.Note;
import com.example.main.util.AppExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class NoteEditActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etContent;
    private NoteDao noteDao;
    private AppExecutors executors;

    private long userId;
    private long noteId = -1;
    private boolean isEditMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        noteDao = new NoteDao(this);
        executors = AppExecutors.getInstance();

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        MaterialButton btnSave = findViewById(R.id.btnSave);
        MaterialButton btnDelete = findViewById(R.id.btnDelete);



        userId = getIntent().getLongExtra("user_id", -1);
        noteId = getIntent().getLongExtra("note_id", -1);

        if (noteId != -1) {
            isEditMode = true;
            etTitle.setText(getIntent().getStringExtra("note_title"));
            etContent.setText(getIntent().getStringExtra("note_content"));
            btnDelete.setVisibility(android.view.View.VISIBLE);
        }


        btnSave.setOnClickListener(v -> saveNote());
        btnDelete.setOnClickListener(v -> deleteNote());


    }

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Введите заголовок", Toast.LENGTH_SHORT).show();
            return;
        }

        executors.diskIO().execute(() -> {
            if (isEditMode) {
                Note note = new Note(noteId, userId, title, content, 0);
                noteDao.updateNote(note);
            } else {
                Note note = new Note(userId, title, content);
                noteDao.insertNote(note);
            }
            executors.mainThread().execute(() -> {
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void deleteNote() {
        executors.diskIO().execute(() -> {
            noteDao.deleteNote(noteId);
            executors.mainThread().execute(() -> {
                Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
