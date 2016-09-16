package com.example.majingrun.classroommanage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.RunnableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 96358 on 2016/9/14.
 */
public class httpClient {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient okHttpClient=null;
    RequestBody requestBody=null;
    Request request=null;
    Response response=null;

    public httpClient(){
        if(okHttpClient==null){
            okHttpClient=new OkHttpClient();
        }
    }

    public String PostJson(String url,String json)throws IOException{
        requestBody=RequestBody.create(JSON,json);

        request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        response = okHttpClient.newCall(request).execute();
        if(response.isSuccessful())
            return response.body().string();
        else throw new IOException();

    }

    public void Post(String url)throws IOException{
        //requestBody=new

    }

    public String GetJson(String url)throws IOException{
        request=new Request.Builder().header("id","123").url(url).build();
        response=okHttpClient.newCall(request).execute();
        return response.body().string();
    }
}
