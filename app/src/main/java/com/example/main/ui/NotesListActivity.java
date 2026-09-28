package com.example.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.R;
import com.example.main.db.NoteDao;
import com.example.main.model.Note;
import com.example.main.util.AppExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {

    private RecyclerView rvNotes;
    private NoteAdapter adapter;
    private NoteDao noteDao;
    private AppExecutors executors;
    private long userId;
    private List<Note> notes = new ArrayList<>();

    private void createManyNotes(int count) {
        AppExecutors executors = AppExecutors.getInstance();
        for (int i = 0; i < count; i++) {
            final int index = i;
            executors.diskIO().execute(() -> {
                Note note = new Note(userId,
                        "Заметка №" + index,
                        "Содержимое заметки " + index);
                noteDao.insertNote(note);
            });
        }
        // Обновление списка после завершения всех задач
        executors.diskIO().execute(() -> {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            executors.mainThread().execute(this::loadNotes);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        userId = getIntent().getLongExtra("user_id", -1);
        noteDao = new NoteDao(this);
        executors = AppExecutors.getInstance();

        rvNotes = findViewById(R.id.rvNotes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NoteAdapter(notes, note -> {
            Intent intent = new Intent(this, NoteEditActivity.class);
            intent.putExtra("note_id", note.getId());
            intent.putExtra("note_title", note.getTitle());
            intent.putExtra("note_content", note.getContent());
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
        rvNotes.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteEditActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        executors.diskIO().execute(() -> {
            List<Note> loaded = noteDao.getNotesByUser(userId);
            executors.mainThread().execute(() -> {
                notes.clear();
                notes.addAll(loaded);
                adapter.notifyDataSetChanged();
            });
        });
    }

    // --- Adapter ---
    interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    static class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
        private final List<Note> notes;
        private final OnNoteClickListener listener;

        NoteAdapter(List<Note> notes, OnNoteClickListener listener) {
            this.notes = notes;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_note, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Note note = notes.get(position);
            holder.tvTitle.setText(note.getTitle());
            holder.tvContent.setText(note.getContent());
            holder.itemView.setOnClickListener(v -> listener.onNoteClick(note));
        }

        @Override
        public int getItemCount() { return notes.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvContent;
            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvNoteTitle);
                tvContent = itemView.findViewById(R.id.tvNoteContent);
            }
        }

    }
}
