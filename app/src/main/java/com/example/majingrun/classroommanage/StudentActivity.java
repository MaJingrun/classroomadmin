package com.example.majingrun.classroommanage;

import android.app.TabActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentActivity extends TabActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    ClassRoom[] cr;
    String area="";
    String floor="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        CreateTabHost();
        CreateSpinner();

        final Spinner spinner_of_area=(Spinner)findViewById(R.id.spinner_area);
        final Spinner spinner_of_floor=(Spinner)findViewById(R.id.spinner_num);
        spinner_of_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area=spinner_of_area.getSelectedItem().toString().substring(0,1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_of_floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                floor=spinner_of_floor.getSelectedItem().toString().substring(0,1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button_of_no_course=(Button)findViewById(R.id.button_no_course);
        button_of_no_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check(area,floor);
            }
        });
    }

    public void CreateTabHost(){
        TabHost tb=getTabHost();
        tb.addTab(tb.newTabSpec("tab1").setIndicator("查询").setContent(R.id.linearLayout));
        tb.addTab((tb.newTabSpec("tab2").setIndicator("申请").setContent(R.id.linearLayout2)));
        tb.addTab((tb.newTabSpec("tab3").setIndicator("我的").setContent(R.id.linearLayout3)));
    }

    public void CreateSpinner(){
        Spinner spinner=(Spinner)findViewById(R.id.spinner_area);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(
                this,
                R.array.buildings,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        Spinner spinner_num=(Spinner)findViewById(R.id.spinner_num);
        ArrayAdapter<CharSequence> adapter_num=ArrayAdapter.createFromResource(
                this,
                R.array.number,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_num.setAdapter(adapter_num);
    }

    public void Check(String number_of_area,String floor){
        String str=number_of_area+floor;
        String url="http://192.168.1.104/web/CheckRoom.php";
        Map<String,String> map=new HashMap<String,String>();
        map.put("id",str);
        String json=new Gson().toJson(map);
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=RequestBody.create(JSON,json);
        final Request request=new Request.Builder().
                url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String s=response.body().string();
                    cr=new Gson().fromJson(s,ClassRoom[].class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        });
    }

    public void ShowInfoOfRoom(){

    }
}
