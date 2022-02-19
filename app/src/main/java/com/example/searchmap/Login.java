package com.example.searchmap;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button  login;
    EditText mEmail, mPassword;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login = findViewById(R.id.login);
        mEmail = findViewById(R.id.loginemail);
        mPassword = findViewById(R.id.loginpassword);
        progressBar = findViewById(R.id.loginbar);

        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                onLoginUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });


    }

    public void onLoginUser(){


        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            mEmail.setError("Enter valid email");
            progressBar.setVisibility(View.GONE);
            return;
        }
        else if (TextUtils.isEmpty(password)){
            progressBar.setVisibility(View.GONE);
            mPassword.setError("Enter Password");
            return;
        }
        else if(!isvalidEmail(email)){
            mEmail.setError("Enter a vaild email id");
            progressBar.setVisibility(View.GONE);
            return;
        }


        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed."
                                            +task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
        progressBar.setVisibility(View.GONE);
    }

    //Email validation
    private boolean isvalidEmail(CharSequence target){
        progressBar.setVisibility(View.GONE);
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public void updateUI(FirebaseUser user) {
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
//            reload();
        }
    }

    // to reload the login page
//    private void reload() {
//        finish();
//        startActivity(getIntent());
//    }

    public void onSignUp(View view)
    {
        startActivity(new Intent(Login.this, MainActivity.class));
    }
}