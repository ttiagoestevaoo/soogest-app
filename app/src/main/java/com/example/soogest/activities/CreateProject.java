package com.example.soogest.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soogest.R;
import com.example.soogest.http_requests.HttpCall;
import com.example.soogest.http_requests.OkHttpRequest;
import com.google.gson.Gson;

import java.util.HashMap;

import responses.ProjectResponse;
import responses.ResponseAPI;


public class CreateProject extends AppCompatActivity {
    Button btnProjectCreateBack,btnProjectCreateSave;
    EditText editProjectCreateName,editProjectCreateDescription,editProjectCreateDeadline;
    ListView listProjects;



    public void resetToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", "");
        editor.apply();
    }

    public String getToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

    protected boolean validacao(){
        editProjectCreateName = findViewById(R.id.editProjectCreateName);
        editProjectCreateDeadline = findViewById(R.id.editProjectCreateDeadline);
        editProjectCreateDescription = findViewById(R.id.editProjectCreateDescription);

        if(editProjectCreateDescription.getText().length() == 0 && editProjectCreateName.getText().length() == 0 && editProjectCreateDescription.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"Digite os dados corretamente para criar o projeto", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_projeto);

        listProjects = findViewById(R.id.listProjects);


        btnProjectCreateBack = findViewById(R.id.btnProjectCreateBack);
        btnProjectCreateBack.setOnClickListener(new View.OnClickListener() {
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

        btnProjectCreateSave = findViewById(R.id.btnProjectCreateSave);
        btnProjectCreateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProjectCreateName = findViewById(R.id.editProjectCreateName);
                editProjectCreateDeadline = findViewById(R.id.editProjectCreateDeadline);
                editProjectCreateDescription = findViewById(R.id.editProjectCreateDescription);


                if(validacao()){
                    HttpCall htppCall = new HttpCall();
                    htppCall.setMethodType(HttpCall.POST);
                    htppCall.setUrl("http://soogest-api.herokuapp.com/api/projects");
                    htppCall.setToken(getToken());
                    HashMap<String,String> params = new HashMap<>();
                    params.put("name", editProjectCreateName.getText().toString());
                    params.put("description", editProjectCreateDescription.getText().toString());
                    params.put("deadline", editProjectCreateDeadline.getText().toString());
                    htppCall.setParams(params);

                    new OkHttpRequest(){
                        @Override
                        public void onResponse(ResponseAPI response) {
                            if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                                super.onResponse(response);
                                Toast.makeText(getApplicationContext(),"Projeto criado com sucesso", Toast.LENGTH_SHORT).show();

                                Intent main = new Intent(
                                        getApplicationContext(),
                                        IndexProject.class
                                );
                                startActivity(main);
                                finish();
                            }else if(response.getResponseCode() == ResponseAPI.HTTP_UNAUTHORIZED){
                                Toast.makeText(getApplicationContext(),"Voce precisa fazer o login novamente", Toast.LENGTH_SHORT).show();
                                resetToken();
                                Intent main = new Intent(
                                        getApplicationContext(),
                                        MainActivity.class
                                );
                                startActivity(main);
                                finish();
                            }else if(response.getResponseCode() == ResponseAPI.HTTP_BAD_REQUEST){
                                Toast.makeText(getApplicationContext(),"Não foi posśivel criar o projeto", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Aconteuceu alguma coisa no servidor", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }.execute(htppCall);
                }
            }
        });



    }
}