package com.example.soogest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.soogest.R;

public class MainActivity extends AppCompatActivity {
    TextView textCreateUser, textResetPassword;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);

        textCreateUser = findViewById(R.id.textCreateUser);
        textResetPassword = findViewById(R.id.textResetPassword);

        textResetPassword.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentResetPassword = new Intent(getApplicationContext(), ResetPassword.class);
                        startActivity(intentResetPassword);
                        finish();
                    }
                }
        );

        textCreateUser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentCreateUser = new Intent(getApplicationContext(), CreateUser.class);
                        startActivity(intentCreateUser);
                        finish();
                    }
                }
        );

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentIndex = new Intent(getApplicationContext(), Index.class);
                startActivity(intentIndex);
                finish();
            }
        });
    }
}
