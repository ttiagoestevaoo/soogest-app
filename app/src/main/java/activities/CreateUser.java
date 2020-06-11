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

import http_responses.ResponseAPI;


public class CreateUser extends AppCompatActivity {
    Button btnUserCreateBack,btnUserCreateSave;
    EditText editUserCreateName,editUserCreateEmail,editUserCreatePassword,editUserCreatePasswordConfirm;
    TextView textProjectCreate;
    ListView listProjects;

    protected boolean validacao(){
        editUserCreateName = findViewById(R.id.editUserCreateName);
        editUserCreateEmail = findViewById(R.id.editUserCreateEmail);
        editUserCreatePassword = findViewById(R.id.editUserCreatePassword);
        editUserCreatePasswordConfirm = findViewById(R.id.editUserCreatePasswordConfirm);

        if(editUserCreateName.getText().length() == 0 && editUserCreateEmail.getText().length() == 0 && editUserCreatePassword.getText().length() == 0 && editUserCreatePasswordConfirm.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"Digite os dados corretamente para criar o usuário", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!editUserCreatePassword.getText().toString().equals(editUserCreatePasswordConfirm.getText().toString())){
            Toast.makeText(getApplicationContext(),"As senhas devem ser iguais", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);


        btnUserCreateBack = findViewById(R.id.btnUserCreateBack);
        btnUserCreateBack.setOnClickListener(new View.OnClickListener() {
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

        btnUserCreateSave = findViewById(R.id.btnUserCreateSave);
        btnUserCreateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserCreateName = findViewById(R.id.editUserCreateName);
                editUserCreateEmail = findViewById(R.id.editUserCreateEmail);
                editUserCreatePassword = findViewById(R.id.editUserCreatePassword);
                editUserCreatePasswordConfirm = findViewById(R.id.editUserCreatePasswordConfirm);


                if(validacao()){
                    HttpCall htppCall = new HttpCall();
                    htppCall.setMethodType(HttpCall.POST);
                    htppCall.setUrl("http://soogest-api.herokuapp.com/api/register");
                    HashMap<String,String> params = new HashMap<>();
                    params.put("name", editUserCreateName.getText().toString());
                    params.put("email", editUserCreateEmail.getText().toString());
                    params.put("password", editUserCreatePassword.getText().toString());
                    params.put("c_password", editUserCreatePasswordConfirm.getText().toString());
                    htppCall.setParams(params);

                    new OkHttpRequest(){
                        @Override
                        public void onResponse(ResponseAPI response) {
                            if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                                super.onResponse(response);
                                Toast.makeText(getApplicationContext(),"Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();

                                Intent main = new Intent(
                                        getApplicationContext(),
                                        MainActivity.class
                                );
                                startActivity(main);
                                finish();

                            }else if(response.getResponseCode() == ResponseAPI.HTTP_BAD_REQUEST){
                                Toast.makeText(getApplicationContext(),"Não foi possível realizar seu cadastro", Toast.LENGTH_SHORT).show();
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