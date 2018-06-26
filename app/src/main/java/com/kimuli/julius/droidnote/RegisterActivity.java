package com.kimuli.julius.droidnote;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mConfirmPasswordField;
    private Button mRegisterBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailField = findViewById(R.id.emailField);
        mPasswordField = findViewById(R.id.passwordField);
        mConfirmPasswordField = findViewById(R.id.confirmPasswordField);
        mRegisterBtn = findViewById(R.id.register_btn);

        mAuth = FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // call Firebase Authentication module for creating users with email and password

                registerUser();
            }
        });

    }

    /**
     * Method handles creating users with the Firebase backend
     */

    private void registerUser() {

        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        String confirm_password = mConfirmPasswordField.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Registering User");

        if(TextUtils.isEmpty(email)){
            mEmailField.setError("Field Required");
        }

        if(TextUtils.isEmpty(password)){
            mPasswordField.setError("Field Required");
        }

        if(TextUtils.isEmpty(confirm_password)){
            mConfirmPasswordField.setError("Field Required");
        }

        if(password.equals(confirm_password)){

            // only register user if the passwords entered match
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,
                                         "User Registered",Toast.LENGTH_LONG).show();
                        }
                        else{

                            String message = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this,
                                           message,Toast.LENGTH_LONG).show();
                        }

                     }
                 })

                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(RegisterActivity.this,
                                      "Unable to register",
                                       Toast.LENGTH_LONG).show();
                     }
                 });

            finish();  // finish activity and return control to calling activity;
        }



    }
}
