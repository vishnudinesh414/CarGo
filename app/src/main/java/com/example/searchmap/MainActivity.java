package com.example.searchmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


   // FirebaseDatabase database;

    String email, password, mobile;

    EditText mEmail, mPassword, mFullname, mMobile;
    Button signup, button;
    TextView login;
    ProgressBar progressBar;

    DatabaseReference dbref;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mFullname = findViewById(R.id.fullname);
        mMobile = findViewById(R.id.mobilenum);

//        button = findViewById(R.id.button);
        signup = findViewById(R.id.submit);
        login = findViewById(R.id.here);
        progressBar = findViewById(R.id.progressBar);

        //setting progressbar to gone
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                register();
                //to end the task
//                finish();

            }
        });

    }

    public void register() {

        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        mobile = mMobile.getText().toString();


        if (TextUtils.isEmpty(email)){
            mEmail.setError("Enter valid email");
            progressBar.setVisibility(View.GONE);
            return;
        }
        else if (TextUtils.isEmpty(password)){
            mPassword.setError("Enter Password");
            progressBar.setVisibility(View.GONE);
            return;
        }
        else if(!isvalidEmail(email)){
            mEmail.setError("Enter a vaild email id");
            progressBar.setVisibility(View.GONE);
            return;
        }


        //Creating user with email and password.
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User created successfully", Toast.LENGTH_SHORT).show();
                            uploadData();
//                            onMain();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                        }

                        else{
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    public void onGotoLogin(View view){
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    public void onMain(){
        Intent intent = new Intent(MainActivity.this, Home.class);
        startActivity(intent);
    }

    //To validate email
    private boolean isvalidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());

    }
    //to validate mobile
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    // upload data to Firebase DB
    public void uploadData() {
        //to store user full_name and Mobile
        String fullname = mFullname.getText().toString();


        dbref = reference.child("users");
//        String key = dbref.push().getKey();

        HashMap<String, String> user = new HashMap<>();
//      user.put("key", key);
        user.put("Full name", fullname);
        user.put("Mobile no", mobile);
        user.put("email",email);
        user.put("password", password);

        //to get user id
        String n = firebaseAuth.getUid();


        dbref.child(n).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "user data stored", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}