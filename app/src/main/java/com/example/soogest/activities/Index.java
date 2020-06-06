package com.example.soogest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soogest.R;
import com.example.soogest.http_requests.HttpCall;
import com.example.soogest.http_requests.HttpRequest;

import com.example.soogest.http_requests.OkHttpRequest;
import com.example.soogest.http_requests.UserAuthentication;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.Request;
import okhttp3.Response;
import responses.AccessToken;
import responses.ResponseAPI;


public class Index extends AppCompatActivity {
    Button btnLogout;
    TextView textIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_projeto);



        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(
                        getApplicationContext(),
                        MainActivity.class
                );
                startActivity(main);
                finish();
            }
        });
    }
}