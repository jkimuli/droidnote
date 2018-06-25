package com.kimuli.julius.droidnote.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kimuli.julius.droidnote.R;
import com.kimuli.julius.droidnote.model.Note;

import java.util.List;

public class NoteAdapter  extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private Context mContext;
    private List<Note> mNoteList;
    private NoteAdapterClickHandler mClickHandler;

    public interface NoteAdapterClickHandler{
        public void onClick(int id);
    }

    public NoteAdapter(Context context,List<Note> data,NoteAdapterClickHandler handler){
        this.mContext = context;
        mClickHandler = handler;
        mNoteList = data;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.note_item,parent,false);


        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        holder.bind(mNoteList.get(position));
    }

    @Override
    public int getItemCount() {

        if(mNoteList == null){
            return 0;
        }

        else{
            return mNoteList.size();
        }
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleText;
        private TextView mContentText;

        NoteViewHolder(View view){
            super(view);

            mTitleText = view.findViewById(R.id.txtTitle);
            mContentText = view.findViewById(R.id.txtContent);
        }

        /**
         * Method bind attaches the attributes of a given Note to the
         * respective text fields in the UI
         * @param note
         */

        private void bind(final Note note){

            mTitleText.setText(note.getTitle());
            mContentText.setText(note.getContent());

        }

        public void onClick(View v){

        }


    }
}
