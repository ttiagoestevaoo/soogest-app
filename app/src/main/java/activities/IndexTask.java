package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soogest.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import http_requests.HttpCall;
import http_requests.OkHttpRequest;
import http_requests.TokenAccess;
import http_responses.ProjectResponse;
import http_responses.ResponseAPI;
import http_responses.TaskResponse;
import adapters.TaskListAdapter;


public class IndexTask extends AppCompatActivity {
    Button btnProjectIndexCreate, btnTaskIndexCreate,btnTaskIndexBack;
    EditText editTaskIndexName;
    ListView listTaskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_tarefa);
        listTaskIndex = findViewById(R.id.listTaskIndex);


        btnTaskIndexBack = findViewById(R.id.btnTaskIndexBack);
        btnTaskIndexCreate = findViewById(R.id.btnTaskIndexCreate);
        editTaskIndexName = findViewById(R.id.editTaskIndexName);

        btnTaskIndexBack.setOnClickListener(new View.OnClickListener() {
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


        HttpCall htppCall = new HttpCall();
        htppCall.setMethodType(HttpCall.GET);
        htppCall.setUrl("http://soogest-api.herokuapp.com/api/tasks");
        htppCall.setToken(TokenAccess.getInstance().getToken());
        HashMap<String,String> params = new HashMap<>();
        htppCall.setParams(params);

        new OkHttpRequest(){
            @Override
            public void onResponse(ResponseAPI response) {
                if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                    super.onResponse(response);
                    Gson gson = new Gson();
                    ArrayList<TaskResponse> tasks = gson.fromJson(response.getResponseBody(), new TypeToken<ArrayList<TaskResponse>>(){}.getType());

                    TaskListAdapter taskListAdapter = new TaskListAdapter(
                            getApplicationContext(),
                            R.layout.row_list_task,
                            tasks);

                    listTaskIndex.setAdapter(taskListAdapter);
                    listTaskIndex. setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ProjectResponse project = (ProjectResponse) listTaskIndex.getItemAtPosition(position);

                        }
                    });
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
                    Toast.makeText(getApplicationContext(),"Não foi posśivel carregar os dados do projeto", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Aconteuceu alguma coisa no servidor", Toast.LENGTH_SHORT).show();

                }

            }
        }.execute(htppCall);

        btnTaskIndexCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTask = editTaskIndexName.getText().toString();

                if(!nameTask.isEmpty()){
                    HttpCall htppCall = new HttpCall();
                    htppCall.setMethodType(HttpCall.POST);
                    htppCall.setUrl("http://soogest-api.herokuapp.com/api/tasks");
                    htppCall.setToken( TokenAccess.getInstance().getToken());
                    HashMap<String,String> params = new HashMap<>();
                    params.put("name", nameTask);
                    htppCall.setParams(params);

                    new OkHttpRequest(){
                        @Override
                        public void onResponse(ResponseAPI response) {
                            if(response.getResponseCode() == ResponseAPI.HTTP_CREATED){
                                super.onResponse(response);
                                Toast.makeText(getApplicationContext(),"Tarefa criada com sucesso", Toast.LENGTH_SHORT).show();

                                Intent intent = getIntent();
                                startActivity(intent);
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
                                Toast.makeText(getApplicationContext(),"Não foi possível cria a tarefa", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Aconteceu alguma coisa errada no servidor", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }.execute(htppCall);

                }else{
                    Toast.makeText(getApplicationContext(), "Digite alguma nome para criar a tarefa", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}