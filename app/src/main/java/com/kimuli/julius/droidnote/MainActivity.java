package com.kimuli.julius.droidnote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kimuli.julius.droidnote.adapter.NoteAdapter;
import com.kimuli.julius.droidnote.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteAdapterClickHandler {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private NoteAdapter mNoteAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private List<Note> mNoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get a reference to a node in the firebase database
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notes");
        mAuth = FirebaseAuth.getInstance();
        mNoteList = new ArrayList<Note>();

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
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        mDatabaseReference.addValueEventListener(databaseListener);

        mNoteAdapter = new NoteAdapter(this,mNoteList,this);
        mNoteAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mNoteAdapter);
    }

    private void removeFirebaseListeners(){
        mAuth.removeAuthStateListener(mAuthStateListener);
        mDatabaseReference.removeEventListener(databaseListener);
    }

    @Override
    protected void onStop() {


        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.menu_logout){
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method logouts the current Firebase User
     */
    private void logout() {

        mAuth.signOut();
    }

    private ValueEventListener databaseListener =  new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot data: dataSnapshot.getChildren()) {
                Note note = data.getValue(Note.class);
                mNoteList.add(note);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            if(mAuth.getCurrentUser() == null){

                // if no user is currently logged on start SignInActivity

                Intent newIntent = new Intent(MainActivity.this,
                                       SignInActivity.class);
                startActivity(newIntent);


            }

        }
    };

    @Override
    public void onClick(int id) {

    }
}
