package com.example.soogest.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soogest.R;
import com.example.soogest.http_requests.HttpCall;
import com.example.soogest.http_requests.OkHttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import responses.ProjectResponse;
import responses.ResponseAPI;
import responses.UserResponse;


public class IndexProject extends AppCompatActivity {
    Button btnProjectIndexCreate, btnSave,btnProjectIndexBack;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_projeto);
        listProjects = findViewById(R.id.listProjects);


        btnProjectIndexBack = findViewById(R.id.btnProjectIndexBack);
        btnProjectIndexBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(
                        getApplicationContext(),
                        Index.class
                );
                startActivity(main);
                finish();
            }
        });

        btnProjectIndexCreate = findViewById(R.id.btnProjectIndexCreate);
        btnProjectIndexCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(
                        getApplicationContext(),
                        CreateProject.class
                );
                startActivity(main);
                finish();
            }
        });

        HttpCall htppCall = new HttpCall();
        htppCall.setMethodType(HttpCall.GET);
        htppCall.setUrl("http://soogest-api.herokuapp.com/api/projects");
        htppCall.setToken(getToken());
        HashMap<String,String> params = new HashMap<>();
        htppCall.setParams(params);

        new OkHttpRequest(){
            @Override
            public void onResponse(ResponseAPI response) {
                if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                    super.onResponse(response);
                    Gson gson = new Gson();
                    ProjectResponse[] projects = gson.fromJson(response.getResponseBody(), ProjectResponse[].class);
                    ArrayAdapter<ProjectResponse> projectAdapter = new ArrayAdapter<>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            projects);
                    listProjects.setAdapter(projectAdapter);
                    listProjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ProjectResponse project = (ProjectResponse) listProjects.getItemAtPosition(position);
                            Intent itProject = new Intent(
                                    getApplicationContext(),
                                    ShowProject.class
                            );
                            itProject.putExtra("project", project);
                            startActivity(itProject);
                        }
                    });
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
                    Toast.makeText(getApplicationContext(),"Não foi posśivel carregar os dados do projeto", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Aconteuceu alguma coisa no servidor", Toast.LENGTH_SHORT).show();

                }

            }
        }.execute(htppCall);

    }
}