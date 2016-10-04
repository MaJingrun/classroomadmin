package com.example.majingrun.classroommanage;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class AdminActivity extends TabActivity {


    OkHttpClient ok=new OkHttpClient();
    List<String> data;
    List<String> data2;
    Map<String,String> map;
    Map<String,String> map2;
    ListView lv;
    ListView lv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        CreateTabHost();
        getData();
        getData2();
        Log.i("info","8888888888888888888");
        ListView listView=(ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                lv=(ListView)parent;
                Log.i("info",""+lv.getItemAtPosition(position).toString());
                AlertDialog.Builder ab=new AlertDialog.Builder(AdminActivity.this);
                ab.setTitle("审核");
                ab.setMessage("审核是否通过?");
                ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Pass(lv.getItemAtPosition(position).toString());
                    }
                });
                ab.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        NotPass(lv.getItemAtPosition(position).toString());
                    }
                });
                AlertDialog ad=ab.create();
                ad.show();
            }
        });

        ListView listView2=(ListView)findViewById(R.id.listView2);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                lv2=(ListView)parent;
                Log.i("info",""+lv2.getItemAtPosition(position).toString());
                AlertDialog.Builder ab=new AlertDialog.Builder(AdminActivity.this);
                ab.setTitle("终止");
                ab.setMessage("是否终止使用?");
                ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StopApply(lv2.getItemAtPosition(position).toString());
                    }
                });
                AlertDialog ad=ab.create();
                ad.show();
            }
        });
        Log.i("info","1234567890");
    }

    public void refresh(){
        onCreate(null);
    }

    private void getData(){

        String url="http://"+getResources().getString(R.string.ip)+"/web/AdminGetApply.php";
        Request request=new Request.Builder().url(url).build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()){
                    String str=response.body().string();
                    Log.i("info",str);
                    data = new ArrayList<String>();
                    map=new Gson().fromJson(str,HashMap.class);
                    Iterator it=map.entrySet().iterator();
                    while(it.hasNext()){
                        Map.Entry en=(Map.Entry)it.next();
                        data.add((String)en.getValue());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView lv=(ListView)findViewById(R.id.listView);
                            lv.setAdapter(new ArrayAdapter<String>(AdminActivity.this,android.R.layout.simple_expandable_list_item_1,data));
                        }
                    });

                }
            }
        });

    }

    private void getData2(){
        String url="http://"+getResources().getString(R.string.ip)+"/web/AdminGetApply2.php";
        Request request=new Request.Builder().url(url).build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()){
                    String str=response.body().string();
                    Log.i("info",str);
                    data2 = new ArrayList<String>();
                    map2=new Gson().fromJson(str, HashMap.class);
                    Iterator it=map2.entrySet().iterator();
                    while(it.hasNext()){
                        Map.Entry en=(Map.Entry)it.next();
                        data2.add((String)en.getValue());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView lv=(ListView)findViewById(R.id.listView2);
                            lv.setAdapter(new ArrayAdapter<String>(AdminActivity.this,android.R.layout.simple_expandable_list_item_1,data2));
                        }
                    });

                }
            }
        });
    }

    public void CreateTabHost(){
        TabHost tb=getTabHost();
        tb.addTab(tb.newTabSpec("tab1").setIndicator("未审核").setContent(R.id.linearLayout4));
        tb.addTab((tb.newTabSpec("tab2").setIndicator("已审核").setContent(R.id.linearLayout5)));

    }

    public void Pass(String in){
        String url="http://"+getResources().getString(R.string.ip)+"/web/Pass.php";
        Map<String,String> map=new HashMap<String,String>();
        map.put("info",in);
        String json=new Gson().toJson(map);
        RequestBody requestBody=RequestBody.create(LoginActivity.JSON,json);
        Request request=new Request.Builder().url(url).post(requestBody).build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                        }
                    });
                }

            }
        });
    }
    public void NotPass(String in){
        String url="http://"+getResources().getString(R.string.ip)+"/web/NotPass.php";
        Map<String,String> map=new HashMap<String,String>();
        map.put("info",in);
        String json=new Gson().toJson(map);
        RequestBody requestBody=RequestBody.create(LoginActivity.JSON,json);
        Request request=new Request.Builder().url(url).post(requestBody).build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           refresh();
                       }
                   });
                }

            }
        });

    }
    public void StopApply(String in){
        String url="http://"+getResources().getString(R.string.ip)+"/web/StopApply.php";
        Map<String,String> map=new HashMap<String,String>();
        map.put("info",in);
        String json=new Gson().toJson(map);
        RequestBody requestBody=RequestBody.create(LoginActivity.JSON,json);
        Request request=new Request.Builder().url(url).post(requestBody).build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                        }
                    });
                }

            }
        });
    }
}
