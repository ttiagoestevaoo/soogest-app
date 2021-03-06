package activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soogest.R;

import adapters.DateInputAdapter;
import http_requests.HttpCall;
import http_requests.OkHttpRequest;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;

import http_requests.TokenAccess;
import http_responses.ProjectResponse;
import http_responses.ResponseAPI;


public class EditProject extends AppCompatActivity {
    Button btnProjectCreateBack,btnProjectCreateSave;
    EditText editProjectCreateName,editProjectCreateDescription;
    ListView listProjects;
    TextView textProjectCreate,textProjectCreateDeadline;
    DatePickerDialog.OnDateSetListener mDateSetListener;


    protected boolean validacao(){
        editProjectCreateName = findViewById(R.id.editProjectCreateName);
        textProjectCreateDeadline = findViewById(R.id.textProjectCreateDeadline);
        editProjectCreateDescription = findViewById(R.id.editProjectCreateDescription);

        if(editProjectCreateDescription.getText().length() == 0 || editProjectCreateName.getText().length() == 0 || textProjectCreateDeadline.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"Digite os dados corretamente para criar o projeto", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_projeto);

        textProjectCreate = findViewById(R.id.textProjectCreate);
        textProjectCreate.setText("Editar projeto");

        Intent itProject = getIntent();
        final ProjectResponse project = (ProjectResponse) itProject.getExtras().getSerializable("project");

        editProjectCreateName = findViewById(R.id.editProjectCreateName);
        textProjectCreateDeadline = findViewById(R.id.textProjectCreateDeadline);
        editProjectCreateDescription = findViewById(R.id.editProjectCreateDescription);

        editProjectCreateName.setText(project.getName());
        editProjectCreateDescription.setText(project.getDescription());
        textProjectCreateDeadline.setText(DateInputAdapter.fromDateFormat(project.getDeadline()));


        textProjectCreateDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditProject.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date  = dayOfMonth +"/"+month+"/"+year;
                textProjectCreateDeadline.setText(date);
            }
        };

        HttpCall htppCall = new HttpCall();
        htppCall.setMethodType(HttpCall.GET);
        htppCall.setUrl("http://soogest-api.herokuapp.com/api/projects/" + project.getId());
        htppCall.setToken( TokenAccess.getInstance().getToken());
        HashMap<String,String> params = new HashMap<>();
        htppCall.setParams(params);

        new OkHttpRequest(){
            @Override
            public void onResponse(ResponseAPI response) {
                if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                    super.onResponse(response);
                    Gson gson = new Gson();
                    ProjectResponse projectResponse = gson.fromJson(response.getResponseBody(), ProjectResponse.class);
                    editProjectCreateName.setText(projectResponse.getName());
                    editProjectCreateDescription.setText(projectResponse.getDescription());
                    textProjectCreateDeadline.setText(DateInputAdapter.fromDateFormat(projectResponse.getDeadline()));

                }else if(response.getResponseCode() == ResponseAPI.HTTP_UNAUTHORIZED){
                    Toast.makeText(getApplicationContext(),"Voce precisa fazer o login novamente", Toast.LENGTH_SHORT).show();
                    TokenAccess.getInstance().resetToken();
                    Intent main = new Intent(
                            getApplicationContext(),
                            MainActivity.class
                    );
                    startActivity(main);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Aconteceu alguma coisa errada no servidor", Toast.LENGTH_SHORT).show();

                }

            }
        }.execute(htppCall);

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



                if(validacao()){
                    HttpCall htppCall = new HttpCall();
                    htppCall.setMethodType(HttpCall.PUT);
                    htppCall.setUrl("http://soogest-api.herokuapp.com/api/projects/" + project.getId());
                    htppCall.setToken( TokenAccess.getInstance().getToken());
                    HashMap<String,String> params = new HashMap<>();
                    params.put("name", editProjectCreateName.getText().toString());
                    params.put("description", editProjectCreateDescription.getText().toString());
                    params.put("deadline", DateInputAdapter.toDateFormat(textProjectCreateDeadline.getText().toString()));
                    htppCall.setParams(params);

                    new OkHttpRequest(){
                        @Override
                        public void onResponse(ResponseAPI response) {
                            if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                                super.onResponse(response);
                                Toast.makeText(getApplicationContext(),"Projeto editado com sucesso", Toast.LENGTH_SHORT).show();

                                Intent main = new Intent(
                                        getApplicationContext(),
                                        IndexProject.class
                                );
                                startActivity(main);
                                finish();
                            }else if(response.getResponseCode() == ResponseAPI.HTTP_UNAUTHORIZED){
                                Toast.makeText(getApplicationContext(),"Voce precisa fazer o login novamente", Toast.LENGTH_SHORT).show();
                                TokenAccess.getInstance().resetToken();
                                Intent main = new Intent(
                                        getApplicationContext(),
                                        MainActivity.class
                                );
                                startActivity(main);
                                finish();
                            }else if(response.getResponseCode() == ResponseAPI.HTTP_BAD_REQUEST){
                                Toast.makeText(getApplicationContext(),"Não foi posśivel salvar o projeto", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Aconteceu alguma coisa errada no servidor", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }.execute(htppCall);
                }
            }
        });



    }
}