package com.kimuli.julius.droidnote.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.kimuli.julius.droidnote.R;
import com.kimuli.julius.droidnote.model.Note;

public class NoteViewHolder extends RecyclerView.ViewHolder{

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

        public void bind(final Note note){

            mTitleText.setText(note.getTitle());
            mContentText.setText(note.getContent());

        }

}

