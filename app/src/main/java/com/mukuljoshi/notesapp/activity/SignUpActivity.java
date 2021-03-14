package com.mukuljoshi.notesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mukuljoshi.notesapp.R;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputLayout text_name, text_email, text_password, text_number;
    ProgressBar progressBar;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        text_name = findViewById(R.id.textName);
        text_email = findViewById(R.id.textEmail);
        text_password = findViewById(R.id.password);
        text_number = findViewById(R.id.phone);
        progressBar = findViewById(R.id.progress);
        btnSignUp = findViewById(R.id.btnSingUp);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = Objects.requireNonNull(text_name.getEditText()).getText().toString();

                final String email = Objects.requireNonNull(text_email.getEditText()).getText().toString();

                final String password = Objects.requireNonNull(text_password.getEditText()).getText().toString();

                final String phone = Objects.requireNonNull(text_number.getEditText()).getText().toString();

                if (name.isEmpty()) {
                    text_name.setError("Name is required");
                    text_name.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    text_email.setError("Email is required");
                    text_email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    text_email.setError("please provide valid email");
                    text_email.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    text_password.setError("password is required");
                    text_password.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    text_password.setError("Min password should be 6 character!");
                    text_password.requestFocus();
                    return;
                }
                if (phone.isEmpty()) {
                    text_number.setError("number is required");
                    text_number.requestFocus();
                    return;
                }
                if (phone.length() < 10) {
                    text_number.setError("please provide valid number");
                    text_number.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    UserHelperClass userHelperClass = new UserHelperClass(name, email, password, phone);

                                    FirebaseDatabase.getInstance().getReference("user")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                            .setValue(userHelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                Toast.makeText(SignUpActivity.this, "Registration is successfully done", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);

                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(SignUpActivity.this, "ERROR!!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);

                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(SignUpActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }
}