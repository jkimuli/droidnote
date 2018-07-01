package com.kimuli.julius.droidnote;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.kimuli.julius.droidnote.model.Note;

class NoteViewHolder extends RecyclerView.ViewHolder{

    private final TextView mTitleText;
    private final TextView mContentText;
    private final View divider;
    private final TextView mDateText;

    private final View mView;

    NoteViewHolder(View itemView){
        super(itemView);
        this.mView = itemView;

        mTitleText = itemView.findViewById(R.id.txtTitle);
        mContentText = itemView.findViewById(R.id.txtContent);
        divider = itemView.findViewById(R.id.view);
        mDateText = itemView.findViewById(R.id.textDate);

    }

    View getView(){
        return this.mView;
    }

    /**
     * Method bind attaches the attributes of a given Note to the
     * respective text fields in the UI
     */
    public void bind(final Note note){

        mTitleText.setText(note.getTitle());
        mContentText.setText(note.getContent());
        mDateText.setText(note.getCreatedAt());

    }

}


