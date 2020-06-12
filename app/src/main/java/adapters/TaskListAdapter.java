package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.soogest.R;

import java.util.HashMap;
import java.util.List;

import http_requests.HttpCall;
import http_requests.OkHttpRequest;
import http_requests.TokenAccess;
import http_responses.ResponseAPI;
import http_responses.TaskResponse;

public class TaskListAdapter extends ArrayAdapter<TaskResponse> {

    private Context mContext;
    private int mResource;

    public TaskListAdapter(@NonNull Context context, int resource, @NonNull List<TaskResponse> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent,false);

        TextView textTaskItemName = (TextView) convertView.findViewById(R.id.textTaskItemName);
        final CheckBox textTaskItemComplete = (CheckBox) convertView.findViewById(R.id.textTaskItemComplete);

        textTaskItemName.setText(getItem(position).getName());
        textTaskItemComplete.setChecked(getItem(position).isComplete());
        final int idTask = getItem(position).getId();

        textTaskItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Voce clicou no item "+ idTask ,Toast.LENGTH_SHORT).show();
            }
        });

        textTaskItemComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HttpCall htppCall = new HttpCall();
                htppCall.setMethodType(HttpCall.PUT);
                htppCall.setUrl("http://soogest-api.herokuapp.com/api/tasks/" + idTask +"/complete");
                htppCall.setToken(TokenAccess.getInstance().getToken());
                HashMap<String,String> params = new HashMap<>();
                params.put("complete", String.valueOf(textTaskItemComplete.isChecked() ? 1 : 0));
                Log.d("complete",String.valueOf(textTaskItemComplete.isChecked()));
                htppCall.setParams(params);

                new OkHttpRequest(){
                    @Override
                    public void onResponse(ResponseAPI response) {
                        if(response.getResponseCode() == ResponseAPI.HTTP_OK){
                            super.onResponse(response);
                            Log.d("complete","Tarefa atualizada");
                        }else if(response.getResponseCode() == ResponseAPI.HTTP_UNAUTHORIZED){
                            Log.d("complete","Tarefa nao autorizada");

                        }else if(response.getResponseCode() == ResponseAPI.HTTP_BAD_REQUEST){
                            Log.d("complete","Tarefa nao atualizada");

                        }else{
                            Log.d("complete","Erro servidor");
                        }

                    }
                }.execute(htppCall);

            }
        });

        return convertView;
    }
}
