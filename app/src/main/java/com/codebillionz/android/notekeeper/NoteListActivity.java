package com.codebillionz.android.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter mNoteRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDisplayContent();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
            }
        });
    }

    private void initDisplayContent(){
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_notes);
        final RecyclerView.LayoutManager notesLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(notesLayoutManager);
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
     //   mNoteRecyclerAdapter = new NoteRecyclerAdapter(this,notes);
        recyclerView.setAdapter(mNoteRecyclerAdapter);

    };

    @Override
    protected void onResume() {
        super.onResume();
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }
}