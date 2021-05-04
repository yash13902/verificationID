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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {

            if (!mAuth.getCurrentUser().isEmailVerified()) {
                Toast.makeText(this, "Please Verify your Email to continue", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();

            } else {
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                mUser.getIdToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    String idToken = task.getResult().getToken();
                                    Log.i("xx" , idToken);
                                    // Send token to your backend via HTTPS
                                    // ...
                                    String token = idToken;
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("Token", token);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Handle error -> task.getException();
                                    Log.i("Exx" , task.getException().toString());
                                }
                            }
                        });
            }
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        EditText email = findViewById(R.id.emailLogIn);
        EditText pass = findViewById(R.id.passwordLogIn);

        Button login = findViewById(R.id.login);

        TextView signUpView = findViewById(R.id.signUpView);
        signUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passWord = pass.getText().toString();

                if (email.getText().toString().isEmpty()) {
                    email.setError("Email Required");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Valid Email Required");
                    email.requestFocus();
                    return;
                }
                if (pass.getText().toString().isEmpty()) {
                    pass.setError("Password Required");
                    pass.requestFocus();
                    return;
                }
                if (pass.getText().toString().length() < 6) {
                    pass.setError("Min 6 char required");
                    pass.requestFocus();
                    return;
                }

                loginUser(emailText, passWord);
            }
        });



    }

    public void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Invalid Email / Password", Toast.LENGTH_SHORT).show();

                    } else {
                        checkIfEmailVerified(email);
                    }
                });
    }

    private void checkIfEmailVerified(String email)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                Log.i("xx" , idToken);
                                // Send token to your backend via HTTPS
                                // ...
                                String token = idToken;
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("Token", token);
                                startActivity(intent);
                                finish();
                                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle error -> task.getException();
                                Log.i("Exx" , task.getException().toString());
                            }
                        }
                    });


        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(this, "Please Verify your Email to continue", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }


}