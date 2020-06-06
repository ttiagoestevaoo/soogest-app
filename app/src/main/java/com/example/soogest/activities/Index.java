package com.example.soogest.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soogest.R;
import com.example.soogest.http_requests.HttpCall;

import com.example.soogest.http_requests.OkHttpRequest;
import com.google.gson.Gson;

import java.util.HashMap;

import responses.AccessTokenResponse;
import responses.ResponseAPI;
import responses.UserResponse;


public class Index extends AppCompatActivity {
    Button btnLogout, btnIndexProject, btnIndexTarefas;
    TextView textIndex;

    public String getToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        textIndex = findViewById(R.id.textIndex);

        HttpCall htppCall = new HttpCall();
        htppCall.setMethodType(HttpCall.GET);
        htppCall.setUrl("http://soogest-api.herokuapp.com/api/user");
        htppCall.setToken(getToken());
        HashMap<String,String> params = new HashMap<>();
        htppCall.setParams(params);

        new OkHttpRequest(){
            @Override
            public void onResponse(ResponseAPI response) {
                if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                    super.onResponse(response);
                    Gson gson = new Gson();
                    UserResponse userResponse = gson.fromJson(response.getResponseBody(), UserResponse.class);

                    textIndex.setText("Bem vindo " + userResponse.getName());
                    Log.d("response",response.getResponseBody());
                }else if(response.getResponseCode() == ResponseAPI.HTTP_UNAUTHORIZED){
                    Log.d("objeto", response.getResponseBody());
                    Log.d("code", String.valueOf(response.getResponseCode()));
                }else if(response.getResponseCode() == ResponseAPI.HTTP_BAD_REQUEST){
                    Toast.makeText(getApplicationContext(),"Email ou senha incorreta", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("objeto", response.getResponseBody());
                    Log.d("code", String.valueOf(response.getResponseCode()));
                }

            }
        }.execute(htppCall);

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

        btnIndexProject = findViewById(R.id.btnIndexProject);

        btnIndexProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(
                        getApplicationContext(),
                        IndexProject.class
                );
                startActivity(main);
                finish();
            }
        });


    }
}