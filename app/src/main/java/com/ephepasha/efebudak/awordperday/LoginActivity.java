package com.ephepasha.efebudak.awordperday;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class LoginActivity extends Activity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.activity_login_edit_text_email);
        editTextPassword = findViewById(R.id.activity_login_edit_text_password);
        findViewById(R.id.activity_login_button_sign_in).setOnClickListener(this);
        findViewById(R.id.activity_login_button_sign_up).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.activity_login_button_sign_up:
                createUserWithEmailAndPassword();
                break;

            case R.id.activity_login_button_sign_in:
                signInWithEmailAndPassword();
                break;

        }
    }

    private void updateUI(final FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            if (!firebaseUser.isEmailVerified()) {
                firebaseUser.sendEmailVerification()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Re-enable button
                                //findViewById(R.id.verify_email_button).setEnabled(true);

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Verification email sent to " + firebaseUser.getEmail(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                    Toast.makeText(getApplicationContext(),
                                            "Failed to send verification email.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

        Toast.makeText(
                getApplicationContext(),
                "Email is verified: "
                        + (firebaseUser == null
                        ? "NOPE"
                        : firebaseUser.isEmailVerified()),
                Toast.LENGTH_SHORT).show();

    }

    private void createUserWithEmailAndPassword() {
        firebaseAuth.createUserWithEmailAndPassword(
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signInWithEmailAndPassword() {

        firebaseAuth.signInWithEmailAndPassword(
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

}
