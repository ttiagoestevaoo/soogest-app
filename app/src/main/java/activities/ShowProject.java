package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.HashMap;

import http_requests.TokenAccess;
import http_responses.ProjectResponse;
import http_responses.ProjectShowResponse;
import http_responses.ResponseAPI;
import adapters.TaskListAdapter;


public class ShowProject extends AppCompatActivity {
    Button btnProjectShowBack,btnProjectShowDestroy,btnProjectShowEdit,btnProjectShowTaskCreate;
    TextView textProjectName, textProjectDescription, textProjectDeadline;
    EditText editProjectShowTaskCreate;
    ListView listProjectShowListTask;

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
        listProjectShowListTask = findViewById(R.id.listProjectShowListTask);

        textProjectName = findViewById(R.id.textProjectName);
        textProjectDescription = findViewById(R.id.textProjectDescription);
        textProjectDeadline = findViewById(R.id.textProjectDeadline);
        editProjectShowTaskCreate = findViewById(R.id.editProjectShowTaskCreate);



        Intent itProject = getIntent();
        final ProjectResponse project = (ProjectResponse) itProject.getExtras().getSerializable("project");

        textProjectName.setText(project.getName());
        textProjectDescription.setText(project.getDescription());
        textProjectDeadline.setText(DateInputAdapter.fromDateFormat(project.getDeadline()));

        btnProjectShowTaskCreate = findViewById(R.id.btnProjectShowTaskCreate);
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



        btnProjectShowTaskCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTask = editProjectShowTaskCreate.getText().toString();

                if(!nameTask.isEmpty()){
                    HttpCall htppCall = new HttpCall();
                    htppCall.setMethodType(HttpCall.POST);
                    htppCall.setUrl("http://soogest-api.herokuapp.com/api/tasks");
                    htppCall.setToken( TokenAccess.getInstance().getToken());
                    HashMap<String,String> params = new HashMap<>();
                    params.put("name", nameTask);
                    params.put("project_id", String.valueOf(project.getId()) );
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


        HttpCall htppCall = new HttpCall();
        htppCall.setMethodType(HttpCall.GET);
        htppCall.setUrl("http://soogest-api.herokuapp.com/api/projects/" + project.getId());
        htppCall.setToken(TokenAccess.getInstance().getToken());
        HashMap<String,String> params = new HashMap<>();
        htppCall.setParams(params);

        new OkHttpRequest(){
            @Override
            public void onResponse(ResponseAPI response) {
                if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                    super.onResponse(response);
                    Gson gson = new Gson();
                    final ProjectShowResponse projectApi = gson.fromJson(response.getResponseBody(), ProjectShowResponse.class);
                    textProjectName.setText(projectApi.getName());
                    textProjectDescription.setText(projectApi.getDescription());
                    textProjectDeadline.setText(DateInputAdapter.fromDateFormat(projectApi.getDeadline()));


                    TaskListAdapter taskListAdapter = new TaskListAdapter(
                            getApplicationContext(),
                            R.layout.row_list_task,
                            projectApi.getTasks());

                    listProjectShowListTask.setAdapter(taskListAdapter);
                    listProjectShowListTask. setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ProjectResponse project = (ProjectResponse) listProjectShowListTask.getItemAtPosition(position);

                        }
                    });

                }else if(response.getResponseCode() == ResponseAPI.HTTP_UNAUTHORIZED){
                    Toast.makeText(getApplicationContext(),"Voce precisa fazer o login novamente", Toast.LENGTH_SHORT).show();
                    TokenAccess.getInstance().resetToken();
                    Intent indexProjects = new Intent(
                            getApplicationContext(),
                            IndexProject.class
                    );
                    startActivity(indexProjects);
                    finish();
                }else if(response.getResponseCode() == ResponseAPI.HTTP_BAD_REQUEST){
                    Toast.makeText(getApplicationContext(),"Não foi possível atualizar os dados do projeto", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Aconteceu alguma coisa errada no servidor", Toast.LENGTH_SHORT).show();

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

        btnProjectShowDestroy = findViewById(R.id.btnProjectShowDestroy);

        btnProjectShowDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpCall htppCall = new HttpCall();
                htppCall.setMethodType(HttpCall.DELETE);
                htppCall.setUrl("http://soogest-api.herokuapp.com/api/projects/" + project.getId());
                htppCall.setToken(getToken());
                HashMap<String,String> params = new HashMap<>();
                htppCall.setParams(params);
                new OkHttpRequest(){
                    @Override
                    public void onResponse(ResponseAPI response) {
                        if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                            super.onResponse(response);
                            Toast.makeText(getApplicationContext(),"Projeto excluído com sucesso", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(getApplicationContext(),"Aconteceu alguma coisa errada no servidor", Toast.LENGTH_SHORT).show();

                        }

                    }
                }.execute(htppCall);

                Intent indexProject = new Intent(
                        getApplicationContext(),
                        IndexProject.class
                );
                startActivity(indexProject);
                finish();
            }
        });



    }
}