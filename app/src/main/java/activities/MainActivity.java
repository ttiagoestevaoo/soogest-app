package activities;

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
import http_requests.HttpCall;
import http_requests.OkHttpRequest;
import com.google.gson.Gson;

import java.util.HashMap;

import http_requests.TokenAccess;
import http_responses.AccessTokenResponse;
import http_responses.ResponseAPI;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TokenAccess.getInstance().Initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);

        textCreateUser = findViewById(R.id.textCreateUser);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);


        if(TokenAccess.getInstance().getToken() != ""){
            Intent intentIndex = new Intent(getApplicationContext(), Index.class);
            startActivity(intentIndex);
            finish();
        }


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

                                TokenAccess.getInstance().setToken(accessToken.getToken_type() + " " + accessToken.getAccess_token());
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
