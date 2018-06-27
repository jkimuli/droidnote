package com.kimuli.julius.droidnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private static final int REQUEST_SIGN_IN = 1;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        startSignInScreen();

    }

    /**
     * This method opens up a sign in screen based on Firebase Authentication UI
     */
    private void startSignInScreen() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );   // we only provide google signin for firebase ui

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                      .setAvailableProviders(providers)
                      .build(),
                REQUEST_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String message = String.format("User %s successfully login!", user.getDisplayName());
                Toast.makeText(this,message,Toast.LENGTH_LONG).show();
                finish();

            } else {

                Toast.makeText(this,
                            response.getError().getMessage(),
                            Toast.LENGTH_LONG).show();
            }
        }
    }
}
