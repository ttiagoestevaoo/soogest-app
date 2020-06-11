package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soogest.R;
import http_requests.HttpCall;
import http_requests.OkHttpRequest;

import java.util.HashMap;

import http_requests.TokenAccess;
import http_responses.ResponseAPI;


public class CreateProject extends AppCompatActivity {
    Button btnProjectCreateBack,btnProjectCreateSave;
    EditText editProjectCreateName,editProjectCreateDescription,editProjectCreateDeadline;
    TextView textProjectCreate;
    ListView listProjects;


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

        textProjectCreate = findViewById(R.id.textProjectCreate);
        textProjectCreate.setText("Novo projeto");

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
                    htppCall.setToken( TokenAccess.getInstance().getToken());
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
                                Toast.makeText(getApplicationContext(),"Você precisa fazer o login novamente", Toast.LENGTH_SHORT).show();
                                TokenAccess.getInstance().resetToken();
                                Intent main = new Intent(
                                        getApplicationContext(),
                                        MainActivity.class
                                );
                                startActivity(main);
                                finish();
                            }else if(response.getResponseCode() == ResponseAPI.HTTP_BAD_REQUEST){
                                Toast.makeText(getApplicationContext(),"Não foi possível criar o projeto", Toast.LENGTH_SHORT).show();
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