package com.kimuli.julius.droidnote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.kimuli.julius.droidnote.R;
import com.kimuli.julius.droidnote.model.Note;

public class NoteAdapter extends FirebaseRecyclerAdapter<Note,NoteAdapter.NoteViewHolder> {

    private Context mContext;
    private ItemClickHandler mItemClickHandler;

    public interface  ItemClickHandler{
        void onItemClickListener(DatabaseReference mRef);
    }

    public NoteAdapter(Context context,FirebaseRecyclerOptions<Note> sentOptions,
                       ItemClickHandler handler){

        super(sentOptions);
        mContext = context;
        mItemClickHandler=handler;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_note, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onError(@NonNull DatabaseError error) {
        super.onError(error);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note model) {
          holder.bind(model);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleText;
        private TextView mContentText;
        private View divider;
        private TextView mDateText;

        NoteViewHolder(View view){
            super(view);

            mTitleText = view.findViewById(R.id.txtTitle);
            mContentText = view.findViewById(R.id.txtContent);
            divider = view.findViewById(R.id.view);
            mDateText = view.findViewById(R.id.textDate);

            view.setOnClickListener(this);
        }

        /**
         * Method bind attaches the attributes of a given Note to the
         * respective text fields in the UI
         */
        private void bind(final Note note){

            mTitleText.setText(note.getTitle());
            mContentText.setText(note.getContent());
            mDateText.setText(note.getDate());

        }

        @Override
        public void onClick(View v) {

          DatabaseReference mItemRef = getRef(getAdapterPosition());

          mItemClickHandler.onItemClickListener(mItemRef);

        }
    }




}
