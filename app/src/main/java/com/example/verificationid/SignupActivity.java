package com.example.verificationid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        EditText name = findViewById(R.id.firstName);
        EditText last_name = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.email);
        EditText pass = findViewById(R.id.password);
        EditText con_pass = findViewById(R.id.conPass);

        Button create = findViewById(R.id.signUpAccButton);


        TextView logInView = findViewById(R.id.loginView);
        logInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passWord = pass.getText().toString();
                String con_password = con_pass.getText().toString();

                if(name.getText().toString().isEmpty()) {
                    name.setError("First Name Required");
                    name.requestFocus();
                    return;
                }
                if(last_name.getText().toString().isEmpty()) {
                    last_name.setError("Last Name Required");
                    last_name.requestFocus();
                    return;
                }
                if(email.getText().toString().isEmpty()) {
                    email.setError("Email Required");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Valid Email Required");
                    email.requestFocus();
                    return;
                }
                if(pass.getText().toString().isEmpty()) {
                    pass.setError("Password Required");
                    pass.requestFocus();
                    return;
                }
                if(pass.getText().toString().length()<6) {
                    pass.setError("Min 6 char required");
                    pass.requestFocus();
                    return;
                }
                if(con_pass.getText().toString().isEmpty() || !con_pass.getText().toString().equals(pass.getText().toString())) {
                    con_pass.setError("Password does not matches !!");
                    con_pass.requestFocus();
                    return;
                }


                registerUser(emailText,passWord);
            }
        });

    }

    public void registerUser(String email, String password) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignupActivity.this, "Please verify email to continue", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("failure" , "Email not sent" + e.getMessage());
                            }
                        });

//                        //SignUp success
//                        Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
//                        intent.putExtra("Email",email);
//                        startActivity(intent);
//                        finish();
                    }
                    else {
                        Toast.makeText(SignupActivity.this, "Email ID already exists..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}