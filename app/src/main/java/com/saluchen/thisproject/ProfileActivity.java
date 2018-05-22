package com.saluchen.thisproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    private ImageView editPhone;
    private ImageView editName;
    private ImageView editEmail;
    private ImageView editPassword;
    private EditText editPhoneText;
    private EditText editNameText;
    private EditText editEmailText;
    private EditText editPasswordText;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(Config.sharedPrefsCreds,
                Context.MODE_PRIVATE);

        editPhone = findViewById(R.id.profile_phone_edit_image);
        editName = findViewById(R.id.profile_username_edit_image);
        editEmail = findViewById(R.id.profile_email_edit_image);
        editPassword = findViewById(R.id.profile_password_edit_image);
        editPhoneText = findViewById(R.id.profile_phone_text);
        editNameText = findViewById(R.id.profile_username_text);
        editEmailText = findViewById(R.id.profile_email_text);
        editPasswordText = findViewById(R.id.profile_password_text);

        editPhoneText.setText(sharedPreferences.getString(Config.CURRENT_PHONE, ""));
        editNameText.setText(sharedPreferences.getString(Config.CURRENT_NAME, ""));
        editEmailText.setText(sharedPreferences.getString(Config.CURRENT_EMAIL, ""));

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPhoneText.setFocusable(true);
                editPhoneText.setFocusableInTouchMode(true);
                editPhoneText.requestFocus();
            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNameText.setFocusable(true);
                editNameText.setFocusableInTouchMode(true);
                editNameText.requestFocus();
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                editEmailText.setFocusable(true);
//                editEmailText.setFocusableInTouchMode(true);
//                editEmailText.requestFocus();
            }
        });

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
            }
        });
    }

}
