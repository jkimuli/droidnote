package com.kimuli.julius.droidnote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kimuli.julius.droidnote.model.Note;
import com.kimuli.julius.droidnote.utils.DatabaseUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostNoteActivity extends AppCompatActivity{

    private EditText mTitle;
    private EditText mContent;
    private boolean mUpdateMode;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mPostRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_note);

        mTitle = findViewById(R.id.editTextTitle);
        mContent = findViewById(R.id.editTextContent);
        Button mSaveButton = findViewById(R.id.save_btn);
        mUpdateMode = false; // we are not in update mode if no database reference key

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = DatabaseUtil.getDatabase().getReference().child("Notes");

        if(getIntent().hasExtra(MainActivity.EXTRA_DATABASE_REFERENCE_KEY)){

            String mRef = getIntent().getStringExtra(MainActivity.EXTRA_DATABASE_REFERENCE_KEY);
            mPostRef = mDatabaseReference.child(mRef);

            mPostRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mTitle.setText(dataSnapshot.getValue(Note.class).getTitle());
                    mContent.setText(dataSnapshot.getValue(Note.class).getContent());
                    mUpdateMode = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(mUpdateMode){
                   updateNote(mPostRef); // start method to update an existing note

               }else{

                   postNote();  // start method to post the note to the cloud database
               }
            }
        });
    }


    /**
     * This method retrieves data from the UI and saves it to Firebase Realtime database
     */
    private void postNote() {

        final String title_val = mTitle.getText().toString().trim();
        final String content_val = mContent.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(content_val)){
            // only post notes with non empty title and content fields

            String user_id = mAuth.getCurrentUser().getUid(); // return current authenticated user
            String today = java.text.DateFormat.getDateInstance().format(new Date());
            Note note = new Note(user_id,title_val,content_val,today);

            mDatabaseReference.push().setValue(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PostNoteActivity.this,
                                           "Record added to database",
                                            Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // show the reason for fail to save to database
                            Toast.makeText(PostNoteActivity.this,
                                           e.getLocalizedMessage(),
                                           Toast.LENGTH_LONG).show();

                          }
                    });

            finish();  // return to Main Activity
        }

        else{

            Toast.makeText(this,"Field cant be empty",Toast.LENGTH_LONG).show();
        }

    }

    /*
       Method updates a single existing child node in the Firebase cloud database
     */

    private void updateNote(DatabaseReference mRef){

        final String title_val = mTitle.getText().toString().trim();
        final String content_val = mContent.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(content_val)){

            Map<String, Object> postUpdates = new HashMap<>();
            postUpdates.put("title", title_val);
            postUpdates.put("content",content_val);

            mRef.updateChildren(postUpdates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError,
                                       @NonNull DatabaseReference databaseReference){

                    if(databaseError==null){
                        Toast.makeText(PostNoteActivity.this,
                                        "Record Updated",
                                         Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(PostNoteActivity.this,
                                       databaseError.getMessage(),
                                       Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else{

            Toast.makeText(this,"Field cant be empty",Toast.LENGTH_LONG).show();
        }

        finish();

    }
}
