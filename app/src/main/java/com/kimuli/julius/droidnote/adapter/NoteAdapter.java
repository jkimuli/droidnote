package com.kimuli.julius.droidnote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.kimuli.julius.droidnote.R;
import com.kimuli.julius.droidnote.model.Note;

public class NoteAdapter extends FirebaseRecyclerAdapter<Note,NoteViewHolder> {

    private Context mContext;

    public NoteAdapter(FirebaseRecyclerOptions<Note> sentOptions,Context context){

        super(sentOptions);
        mContext = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.note_item, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note model) {
          holder.bind(model);
    }






}
