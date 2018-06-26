package com.kimuli.julius.droidnote;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kimuli.julius.droidnote.model.Note;

import java.util.Date;

public class PostNoteActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mContent;
    private Button mSaveButton;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_note);

        mTitle = findViewById(R.id.editTextTitle);
        mContent = findViewById(R.id.editTextContent);
        mSaveButton = findViewById(R.id.save_btn);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notes");

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postNote();  // start method to post the note to the firebase database
            }
        });
    }

    /**
     * This method retrieves data from the UI and saves it to firebase database
     */
    private void postNote() {

        /**final ProgressDialog progressDialog = new ProgressDialog(PostNoteActivity.this);
        progressDialog.setMessage("Posting your Note");
        progressDialog.show();**/

        final String title_val = mTitle.getText().toString().trim();
        final String content_val = mContent.getText().toString().trim();


        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(content_val)){
            // only post notes with non empty title and content fields

            String user_id = mAuth.getCurrentUser().getUid(); // return current authenticated user

            Note note = new Note(user_id,title_val,content_val,new Date());
            mDatabaseReference.push().setValue(note);

            finish();

        }


    }
}
