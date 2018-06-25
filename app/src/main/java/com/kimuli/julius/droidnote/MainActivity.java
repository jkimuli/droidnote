package com.kimuli.julius.droidnote;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kimuli.julius.droidnote.adapter.NoteAdapter;
import com.kimuli.julius.droidnote.model.Note;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteAdapterClickHandler {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private NoteAdapter mNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Note note = new Note();
        note.setTitle("First Note");
        note.setUserId("jkimuli");
        note.setTimestamp(new Date());
        note.setContent("My First Note");

        Note note1 = new Note();
        note1.setTitle("First Note");
        note1.setUserId("jkimuli");
        note1.setTimestamp(new Date());
        note1.setContent("My First Note");

        Note note2 = new Note();
        note2.setTitle("First Note");
        note2.setUserId("jkimuli");
        note2.setTimestamp(new Date());
        note2.setContent("My First Note");

        ArrayList<Note> mNoteList = new ArrayList<Note>();
        mNoteList.add(note);
        mNoteList.add(note1);
        mNoteList.add(note2);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteAdapter = new NoteAdapter(this,mNoteList,this);
        mNoteAdapter.notifyDataSetChanged();

        mRecyclerView.setAdapter(mNoteAdapter);

        mFloatingActionButton = findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create an intent to start the PostNote Activity

                Intent startIntent = new Intent(MainActivity.this,
                                                PostNoteActivity.class);

                startActivity(startIntent);
            }
        });

    }

    @Override
    public void onClick(int id) {

    }
}
