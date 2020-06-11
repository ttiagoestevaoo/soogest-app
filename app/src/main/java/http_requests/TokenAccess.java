package http_requests;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TokenAccess {

    private static TokenAccess mInstance;
    private Context mContext;
    //
    private SharedPreferences mMyPreferences;

    private TokenAccess(){ }

    public static TokenAccess getInstance(){
        if (mInstance == null) mInstance = new TokenAccess();
        return mInstance;
    }

    public void Initialize(Context ctxt){
        mContext = ctxt;
        //
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

    }

    public String getToken(){
        return mMyPreferences.getString("token","");
    }

    public void resetToken(){
        SharedPreferences.Editor editor = mMyPreferences.edit();
        editor.putString("token", "");
        editor.apply();
    }

    public void setToken(String token){
        SharedPreferences.Editor editor = mMyPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

}
