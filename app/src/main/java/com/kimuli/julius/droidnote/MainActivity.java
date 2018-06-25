package com.kimuli.julius.droidnote;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.kimuli.julius.droidnote.adapter.NoteAdapter;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteAdapterClickHandler {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private NoteAdapter mNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(int id) {

    }
}
