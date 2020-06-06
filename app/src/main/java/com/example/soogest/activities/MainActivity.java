package com.example.soogest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soogest.R;
import com.example.soogest.http_requests.HttpCall;
import com.example.soogest.http_requests.OkHttpRequest;
import com.google.gson.Gson;

import java.util.HashMap;

import responses.AccessTokenResponse;
import responses.ResponseAPI;

public class MainActivity extends AppCompatActivity {
    TextView textCreateUser, textResetPassword;
    EditText editEmail, editPassword;
    Button btnLogin;
    public static Context contextOfApplication;

    protected boolean validacao(){
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        if(editEmail.getText().length() == 0 && editPassword.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"Digite um email e senha para fazer o login", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public String getToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

    public void resetToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", "");
        editor.apply();
    }

    public void setToken(String token){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);

        textCreateUser = findViewById(R.id.textCreateUser);
        textResetPassword = findViewById(R.id.textResetPassword);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);


        if(getToken() != ""){
            Intent intentIndex = new Intent(getApplicationContext(), Index.class);
            startActivity(intentIndex);
            finish();
        }



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

                if(validacao() == true){

                    HttpCall htppCall = new HttpCall();
                    htppCall.setMethodType(HttpCall.POST);
                    htppCall.setUrl("http://soogest-api.herokuapp.com/api/login");
                    HashMap<String,String> params = new HashMap<>();

                    params.put("username",editEmail.getText().toString());
                    params.put("password",editPassword.getText().toString());
                    htppCall.setParams(params);

                    new OkHttpRequest(){
                        @Override
                        public void onResponse(ResponseAPI response) {

                            if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                                super.onResponse(response);


                                Gson gson = new Gson();
                                AccessTokenResponse accessToken = gson.fromJson(response.getResponseBody(), AccessTokenResponse.class);

                                setToken(accessToken.getToken_type() + " " + accessToken.getAccess_token());
                                Toast.makeText(getApplicationContext(), getToken(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(),"Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                                Intent intentIndex = new Intent(getApplicationContext(), Index.class);
                                startActivity(intentIndex);
                                finish();

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

                }


            }
        });
    }
}
