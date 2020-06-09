package com.example.soogest.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soogest.R;
import com.example.soogest.http_requests.HttpCall;
import com.example.soogest.http_requests.OkHttpRequest;
import com.google.gson.Gson;

import java.util.HashMap;

import responses.ProjectResponse;
import responses.ResponseAPI;
import responses.UserResponse;


public class ShowProject extends AppCompatActivity {
    Button btnProjectShowBack,btnProjectShowDestroy,btnProjectShowEdit;
    TextView textProjectName, textProjectDescription, textProjectDeadline;

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
        setContentView(R.layout.activity_view_projeto);

        textProjectName = findViewById(R.id.textProjectName);
        textProjectDescription = findViewById(R.id.textProjectDescription);
        textProjectDeadline = findViewById(R.id.textProjectDeadline);

        btnProjectShowBack = findViewById(R.id.btnProjectShowBack);

        btnProjectShowBack.setOnClickListener(new View.OnClickListener() {
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

        btnProjectShowDestroy = findViewById(R.id.btnProjectShowBack);

        btnProjectShowDestroy.setOnClickListener(new View.OnClickListener() {
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

        Intent itProject = getIntent();
        final ProjectResponse project = (ProjectResponse) itProject.getExtras().getSerializable("project");

        textProjectName.setText(project.getName());
        textProjectDescription.setText(project.getDescription());
        textProjectDeadline.setText(project.getDeadline());

        HttpCall htppCall = new HttpCall();
        htppCall.setMethodType(HttpCall.GET);
        htppCall.setUrl("http://soogest-api.herokuapp.com/api/projects/" + project.getId());
        htppCall.setToken(getToken());
        HashMap<String,String> params = new HashMap<>();
        htppCall.setParams(params);

        new OkHttpRequest(){
            @Override
            public void onResponse(ResponseAPI response) {
                if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                    super.onResponse(response);
                    Gson gson = new Gson();
                    final ProjectResponse projectApi = gson.fromJson(response.getResponseBody(), ProjectResponse.class);
                    textProjectName.setText(projectApi.getName());
                    textProjectDescription.setText(projectApi.getDescription());
                    textProjectDeadline.setText(projectApi.getDeadline());

                }else if(response.getResponseCode() == ResponseAPI.HTTP_UNAUTHORIZED){
                    Toast.makeText(getApplicationContext(),"Voce precisa fazer o login novamente", Toast.LENGTH_SHORT).show();
                    resetToken();
                    Intent indexProjects = new Intent(
                            getApplicationContext(),
                            IndexProject.class
                    );
                    startActivity(indexProjects);
                    finish();
                }else if(response.getResponseCode() == ResponseAPI.HTTP_BAD_REQUEST){
                    Toast.makeText(getApplicationContext(),"Não foi possível atualizar os dados do projeto", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Aconteuceu alguma coisa no servidor", Toast.LENGTH_SHORT).show();

                }

            }
        }.execute(htppCall);

        btnProjectShowEdit = findViewById(R.id.btnProjectShowEdit);

        btnProjectShowEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProject = new Intent(
                        getApplicationContext(),
                        EditProject.class
                );
                editProject.putExtra("project", project);

                startActivity(editProject);

            }
        });



    }
}