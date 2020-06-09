package com.example.soogest.http_requests;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import responses.ResponseAPI;

public class OkHttpRequest extends AsyncTask<HttpCall, String, ResponseAPI> {

    @Override
    protected ResponseAPI doInBackground(HttpCall... params) {
        HttpCall httpCall = params[0];

        Response response;
        ResponseAPI responseAPI = new ResponseAPI();
        try {
            OkHttpClient client = new OkHttpClient();

            if(httpCall.getMethodType() == HttpCall.POST){

                Request request = new Request.Builder()
                        .url(httpCall.getUrl())
                        .headers(getHeader(httpCall))
                        .post(this.getFormBuilder(httpCall.getParams(),HttpCall.POST))
                        .build();
                response = client.newCall(request).execute();
                responseAPI.setResponseCode(response.code());
                responseAPI.setResponseBody(response.body().string());

                return responseAPI;


            }else if(httpCall.getMethodType() == HttpCall.PUT){

                Request request = new Request.Builder()
                        .url(httpCall.getUrl())
                        .headers(getHeader(httpCall))
                        .put(this.getFormBuilder(httpCall.getParams(),HttpCall.PUT))
                        .build();
                response = client.newCall(request).execute();
                responseAPI.setResponseCode(response.code());
                responseAPI.setResponseBody(response.body().string());

                return responseAPI;


            }else if(httpCall.getMethodType() == HttpCall.DELETE){

                Request request = new Request.Builder()
                        .url(httpCall.getUrl())
                        .headers(getHeader(httpCall))
                        .delete(this.getFormBuilder(httpCall.getParams(),HttpCall.DELETE))
                        .build();
                response = client.newCall(request).execute();
                responseAPI.setResponseCode(response.code());
                responseAPI.setResponseBody(response.body().string());

                return responseAPI;


            }else{
                Request request = new Request.Builder()
                        .url(httpCall.getUrl())
                        .headers(getHeader(httpCall))
                        .build();
                response = client.newCall(request).execute();
                responseAPI.setResponseCode(response.code());
                responseAPI.setResponseBody(response.body().string());
                return responseAPI;

            }



        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseAPI;
    }

    public void onResponse(ResponseAPI response){

    }

    @Override
    protected void onPostExecute(ResponseAPI response) {
        super.onPostExecute(response);
        onResponse(response);
    }

    private RequestBody getFormBuilder(HashMap<String,String> params, int methodType) throws UnsupportedEncodingException {
        boolean isFirst = true;
        FormBody.Builder formBody = new FormBody.Builder();

        for(String key : params.keySet()){
            String value = params.get(key);
            formBody.add(key,value);
        }
        formBody.build();

        return formBody.build();
    }

    private Headers getHeader(HttpCall httpCall){
        HashMap<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");
        if (httpCall.getToken() != ""){
            header.put("Authorization",httpCall.getToken());
        }
        Headers headerBuild = Headers.of(header);
        return headerBuild;
    }

}