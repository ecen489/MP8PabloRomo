package com.example.mp8_pablo_romo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase fbdb;
    DatabaseReference dbrf;

    FirebaseAuth mAuth;
    FirebaseUser user = null;

    private static MainActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbdb = FirebaseDatabase.getInstance();
        dbrf = fbdb.getReference();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
    }

    public void login(View view) {
        // Login the existing user if both fields are inputted. If not then give toast to create
        // account instead.

        // EditTexts used
        EditText username = findViewById(R.id.userbox);
        EditText password = findViewById(R.id.passbox);

        if(username.getText().toString().equals("")) {
            Toast.makeText(this,"Please enter in a username", Toast.LENGTH_SHORT).show();
        }
        if (password.getText().toString().equals("")) {
            Toast.makeText(this,"Please enter in a password", Toast.LENGTH_SHORT).show();
        } else {
            // Login the user
            mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Login Successful!",Toast.LENGTH_SHORT).show();
                                user = mAuth.getCurrentUser(); //The user is signed in

                                // Start Pull Activity
                                Intent pull = new Intent(MainActivity.this,PullActivity.class);
                                startActivity(pull);
                            } else {
                                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }



    }

    public void acc_creation(View view) {
        // Allows for the creation of an account for my database.

        // EditTexts used
        EditText username = findViewById(R.id.userbox);
        EditText password = findViewById(R.id.passbox);

        if(username.getText().toString().equals("")) {
            Toast.makeText(this,"Please enter in a username", Toast.LENGTH_SHORT).show();
        }
        if (password.getText().toString().equals("")) {
            Toast.makeText(this,"Please enter in a password", Toast.LENGTH_SHORT).show();
        } else {
            // Create account
            mAuth.createUserWithEmailAndPassword(username.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(getApplicationContext(),"Created Account",Toast.LENGTH_SHORT).show();
                                user = mAuth.getCurrentUser(); //The newly created user is already signed in

                                // Start Pull Activity
                                Intent pull = new Intent(MainActivity.this,PullActivity.class);
                                startActivity(pull);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
