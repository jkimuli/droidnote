package com.kimuli.julius.droidnote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.kimuli.julius.droidnote.model.Note;
import com.kimuli.julius.droidnote.utils.DatabaseUtil;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final int REQUEST_SIGN_IN = 1;
    public static final String EXTRA_DATABASE_REFERENCE_KEY ="com.kimuli.julius.droidnote.key";

    private RecyclerView mRecyclerView;
    private TextView mEmptyRecycler;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseRecyclerAdapter mRecyclerAdapter;

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
        mEmptyRecycler = findViewById(R.id.empty_recycler_view);

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

    private void setUpAdapter() {

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

        mRecyclerAdapter = new FirebaseRecyclerAdapter<Note, NoteViewHolder>(options) {

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_note, parent, false);

                return new NoteViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                // show a message to invite the user to add notes if recycler view is empty
                mEmptyRecycler.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);

            }

            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder,
                                            final int position, @NonNull Note model){
                holder.bind(model);
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mRef = getRef(position).getKey();

                        startPostModify(mRef);
                    }
                });
            }

        };

        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    /**
     * Method once passed a string key for clicked item start the PostNoteActivity in update
     * mode.
     * @param mRef
     */

    private void startPostModify(String mRef) {

        Intent updateIntent = new Intent(MainActivity.this,PostNoteActivity.class);
        updateIntent.putExtra(EXTRA_DATABASE_REFERENCE_KEY,mRef);
        startActivity(updateIntent);
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
