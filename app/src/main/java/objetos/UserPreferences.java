package objetos;

import android.app.Application;
import android.content.SharedPreferences;

public class UserPreferences extends Application {

    public String getToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

    public void setToken(String token){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }
}
