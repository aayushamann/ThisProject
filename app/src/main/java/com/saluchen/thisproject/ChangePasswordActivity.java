package com.saluchen.thisproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText currentPasswordText;
    private EditText newPasswordText;
    private EditText confirmPasswordText;
    private SharedPreferences sharedPreferences;
    String TAG = "ChangePasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        this.setTitle("Change Password");
        this.setTitleColor(R.color.colorGreen);

        sharedPreferences = getSharedPreferences(Config.sharedPrefs,
                Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        currentPasswordText = findViewById(R.id.current_password);
        newPasswordText = findViewById(R.id.new_password);
        confirmPasswordText = findViewById(R.id.confirm_password);
    }

    public void onSaveNewPassword(View view) {
        String currentPassword = currentPasswordText.getText().toString();
        String newPassword = newPasswordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(sharedPreferences.getString(Config.CURRENT_EMAIL, ""),
                        sharedPreferences.getString(Config.CURRENT_PASSWORD, ""));

        if (currentPassword.equals(sharedPreferences.getString(Config.CURRENT_PASSWORD, ""))) {
            currentUser.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "User re-authenticated.");
                        }
                    });
            if (newPassword.equals(confirmPassword)) {
                currentUser.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                    Toast.makeText(ChangePasswordActivity.this,
                                            "Passwords updated.",
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
            } else {
                Toast.makeText(ChangePasswordActivity.this, "Passwords do not match.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChangePasswordActivity.this, "Wrong current password.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
