package com.example.soogest.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soogest.R;
import com.example.soogest.http_requests.HttpCall;
import com.example.soogest.http_requests.HttpRequest;

import java.util.HashMap;


public class Index extends AppCompatActivity {
    Button btnLogout;
    TextView textIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_projeto);

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textIndex = findViewById(R.id.textIndex);
                try {

                    Toast.makeText(getApplicationContext(),"Começando criar usuario", Toast.LENGTH_SHORT).show();

                    HttpCall htppCall = new HttpCall();
                    htppCall.setMethodType(HttpCall.POST);
                    htppCall.setUrl("http://soogest-api.herokuapp.com/api/");

                    HashMap<String,String> params = new HashMap<>();

                    htppCall.setParams(params);

                    new HttpRequest(){
                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();

                        }
                    }.execute(htppCall);
                    Toast.makeText(getApplicationContext(),"Sucesso", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Erro",
                            Toast.LENGTH_LONG).show();
                }


//                Intent main = new Intent(
//                        getApplicationContext(),
//                        MainActivity.class
//                );
//                startActivity(main);
//                finish();
            }
        });
    }
}