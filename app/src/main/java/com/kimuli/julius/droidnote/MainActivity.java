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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kimuli.julius.droidnote.adapter.NoteAdapter;
import com.kimuli.julius.droidnote.model.Note;
import com.kimuli.julius.droidnote.utils.DatabaseUtil;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemClickHandler{

    private static final int REQUEST_SIGN_IN = 1;

    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get a reference to a node in the firebase database
        mDatabaseReference = DatabaseUtil.getDatabase().getReference().child("Notes");

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null){

                    // start the sign in flow provided by Firebase UI Authentication Library

                    startSignInScreen();
                }

                else{

                    setUpAdapter(); // attach adapter to recycler and display data for current user
                }

            }
        };

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton mFloatingActionButton = findViewById(R.id.fab);
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
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onPause() {
        if(mAuthStateListener!=null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
        mNoteAdapter.stopListening();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main,menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_SIGN_IN){

            if(resultCode == RESULT_OK){
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String message = String.format("User login for %s successful!",
                        user.getDisplayName());
                Toast.makeText(this,message,Toast.LENGTH_LONG).show();


            }else if(resultCode == RESULT_CANCELED){

                // user as cancelled sign in flow

                finish();  // close application if user decides not to login
            }
        }
    }


    @Override
    public void onItemClickListener(DatabaseReference mRef) {

        Toast.makeText(this,mRef.child("title").toString(),Toast.LENGTH_SHORT).show();

    }

    /*
     * Method is used to logout the current authenticated user
     */
    private void logout() {

        AuthUI.getInstance().signOut(this);

    }


    /*
       Method defines the adapter used to attach the firebase data to the activity
       recycler view
     */

    private void setUpAdapter(){

        /*
           Construct Firebase Query that only returns the notes that have created by
           current authenticated user
         */

        Query myQuery = mDatabaseReference.orderByChild("userId")
                            .equalTo(mAuth.getCurrentUser().getUid());

        FirebaseRecyclerOptions<Note> options =
                new FirebaseRecyclerOptions.Builder<Note>()
                        .setQuery(myQuery, Note.class)
                        .setLifecycleOwner(this)
                        .build();

        mNoteAdapter = new NoteAdapter(this,options,this);
        mRecyclerView.setAdapter(mNoteAdapter);

        mNoteAdapter.startListening();

    }


    /**
     * This method opens up a sign in screen based on Firebase Authentication UI
     */
    private void startSignInScreen() {

        // specify sign in options for Firebase UI

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(true)
                        .build(),
                REQUEST_SIGN_IN);

    }

}
