package com.example.soogest.http_requests;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HttpRequest extends AsyncTask<HttpCall, String, String> {

    @Override
    protected String doInBackground(HttpCall... params) {
       HttpURLConnection urlConnection = null;
       HttpCall httpCall = params[0];
       StringBuilder response = new StringBuilder();
       try {
           String dataParams = getDataString(httpCall.getParams(), httpCall.getMethodType());
           URL url = new URL(httpCall.getMethodType() == HttpCall.GET ? httpCall.getUrl()+ dataParams : httpCall.getUrl());

           urlConnection = (HttpURLConnection) url.openConnection();
           urlConnection.setRequestMethod("DELETE");
           urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
           urlConnection.setRequestProperty( "charset", "utf-8");
           urlConnection.setDoOutput(true);
           urlConnection.setDoInput(true);

           if(httpCall.getParams() != null && httpCall.getMethodType() == HttpCall.POST){
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.append(dataParams);
                writer.flush();
                writer.close();
            }

            int responseCode = urlConnection.getResponseCode();

            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = br.readLine()) != null){
                response.append(line);
            }

           Log.d("response_code", String.valueOf(responseCode));
           Log.d("method_type", String.valueOf(httpCall.getMethodType()));
           Log.d("RESPONSE", response.toString());

       }catch (UnsupportedEncodingException e){
           e.printStackTrace();
       } catch (MalformedURLException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onResponse(s);
    }

    public void onResponse(String response){

    }

    private String getDataString(HashMap<String,String> params, int methodType) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for(Map.Entry<String,String> entry : params.entrySet()){
            if(isFirst){
                isFirst = false;
                if(methodType == HttpCall.GET){
                    result.append("?");
                }else{
                    result.append("&");
                }
            }
            result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
        }
        return result.toString();
    }

}
