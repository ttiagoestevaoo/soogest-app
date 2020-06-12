package activities;

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
import http_requests.HttpCall;

import http_requests.OkHttpRequest;
import com.google.gson.Gson;

import java.util.HashMap;

import http_requests.TokenAccess;
import http_responses.ResponseAPI;
import http_responses.UserResponse;


public class Index extends AppCompatActivity {
    Button btnLogout, btnIndexProject, btnIndexTask;
    TextView textIndex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        textIndex = findViewById(R.id.textIndex);

        HttpCall htppCall = new HttpCall();
        htppCall.setMethodType(HttpCall.GET);
        htppCall.setUrl("http://soogest-api.herokuapp.com/api/user");
        htppCall.setToken(TokenAccess.getInstance().getToken());
        HashMap<String,String> params = new HashMap<>();
        htppCall.setParams(params);

        new OkHttpRequest(){
            @Override
            public void onResponse(ResponseAPI response) {
                if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                    super.onResponse(response);
                    Gson gson = new Gson();
                    UserResponse userResponse = gson.fromJson(response.getResponseBody(), UserResponse.class);
                    textIndex.setText("Bem vindo " + userResponse.getName() + "!");
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

                HttpCall htppCall = new HttpCall();
                htppCall.setMethodType(HttpCall.POST);
                htppCall.setUrl("http://soogest-api.herokuapp.com/api/logout");
                htppCall.setToken(TokenAccess.getInstance().getToken());
                HashMap<String,String> params = new HashMap<>();
                htppCall.setParams(params);

                new OkHttpRequest(){
                    @Override
                    public void onResponse(ResponseAPI response) {
                        if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                            Toast.makeText(getApplicationContext(),"Logout realizado com sucesso", Toast.LENGTH_SHORT).show();
                            TokenAccess.getInstance().resetToken();

                            Intent main = new Intent(
                                    getApplicationContext(),
                                    MainActivity.class
                            );
                            startActivity(main);
                            finish();
                        }else if(response.getResponseCode() == ResponseAPI.HTTP_UNAUTHORIZED){
                            TokenAccess.getInstance().resetToken();

                            Intent main = new Intent(
                                    getApplicationContext(),
                                    MainActivity.class
                            );
                            startActivity(main);
                            finish();
                            Toast.makeText(getApplicationContext(),"Logout realizado com sucesso", Toast.LENGTH_SHORT).show();
                        }else if(response.getResponseCode() == ResponseAPI.HTTP_BAD_REQUEST){
                            Toast.makeText(getApplicationContext(),"Não foi possível realizar o logout", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Alguma coisa deu errado no servidor, e não foi possível realizar o logout", Toast.LENGTH_SHORT).show();
                        }

                    }
                }.execute(htppCall);

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

        btnIndexTask = findViewById(R.id.btnIndexTask);

        btnIndexTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(
                        getApplicationContext(),
                        IndexTask.class
                );
                startActivity(main);
                finish();
            }
        });


    }
}