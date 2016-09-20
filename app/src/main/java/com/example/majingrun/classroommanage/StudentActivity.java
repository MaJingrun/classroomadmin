package com.example.majingrun.classroommanage;

import android.app.TabActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
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

    CalendarView cv;
    ClassRoom[] cr;
    String area="";
    String floor="";
    LinearLayout ll;
    LinearLayout []lls=new LinearLayout[5];
    Button check;
    Button apply;
    EditText edit_number_of_room;
    DatePicker dp;
    Calendar ca;
    int year2;
    int month;
    int day;
    String number;
    boolean checked=false;
    Apply ap;
    String time;
    static String apply_info;
    String allpy_information=" ";
    OkHttpClient ok=new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        CreateTabHost();
        CreateSpinner();
        CheckApply();

        Button bu=(Button)findViewById(R.id.button_calcel);
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CancelApply();
            }
        });

       ca=Calendar.getInstance();

        dp=(DatePicker)findViewById(R.id.datePicker);
        year2=ca.get(Calendar.YEAR);
        month=ca.get(Calendar.MONTH);
        day=ca.get(Calendar.DAY_OF_MONTH);
        dp.init(year2,month ,day , new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                year2=view.getYear();
                month=view.getMonth();
                day=view.getDayOfMonth();
            }
        });



    check=(Button)findViewById(R.id.button_check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et=(EditText)findViewById(R.id.edittext_room_number);
                number=et.getText().toString();
                CheckRoom();
            }
        });
        apply=(Button)findViewById(R.id.button_apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checked)
                Apply();
                else{
                    Toast.makeText(StudentActivity.this,"请先查询教室",Toast.LENGTH_SHORT).show();
                }
            }
        });



        final Spinner spinner_of_area=(Spinner)findViewById(R.id.spinner_area);
        final Spinner spinner_of_floor=(Spinner)findViewById(R.id.spinner_num);
        final Spinner spinner=(Spinner)findViewById(R.id.spinner_jie);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time=spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        final TabHost tb=getTabHost();
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

        Spinner spinner1=(Spinner)findViewById(R.id.spinner_jie);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(
                this,
                R.array.keshu,
                android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter1);
    }

    public void Check(String number_of_area,String floor){
        String str=number_of_area+floor;
        String url="http://"+getResources().getString(R.string.ip)+"/web/CheckBlanketRoom.php";
        //String url="http://192.168.1.104/web/CheckBlanketRoom.php";
        Map<String,String> map=new HashMap<String,String>();
        map.put("id",str);
        String json=new Gson().toJson(map);
        Log.i("info","aaaaaaaaaaaaaaaaaaaaaaaaaaa"+" "+json);
        RequestBody requestBody=RequestBody.create(JSON,json);
        final Request request=new Request.Builder().
                url(url).post(requestBody).build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String s=response.body().string();
                    Log.i("info","6"+s);
                    cr=new Gson().fromJson(s,ClassRoom[].class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowInfoOfRoom();
                        }
                    });
                }
            }
        });
    }

    public void ShowInfoOfRoom(){
        LinearLayout la=(LinearLayout)findViewById(R.id.linearLayout);



        for(int i=0;i<lls.length;i++){
            la.removeView(lls[i]);

        }

        for(int i=0;i<cr.length;i++){
            lls[i]=new LinearLayout(StudentActivity.this);
            lls[i].setOrientation(LinearLayout.HORIZONTAL);
            lls[i].setPadding(20,20,20,20);
            //lls[i].setId(R.id.view1);

            TextView tx=new TextView(StudentActivity.this);
            tx.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            tx.setGravity(Gravity.CENTER);
            tx.setTextSize(30);
            tx.setText(""+cr[i].GetId());
            lls[i].addView(tx);

            for(int j=1;j<6;j++){
                ImageView iv=new ImageView(StudentActivity.this);
                boolean is=false;
                for(int k=0;k<cr[i].GetTime().length;k++){
                    if(j==cr[i].GetTime()[k]){
                        is=true;
                    }
                }
                if(is){
                    iv.setImageResource(R.drawable.course);
                }else{
                    iv.setImageResource(R.drawable.nothing5);
                }
                lls[i].addView(iv);
            }



            la.addView(lls[i]);

        }
    }
    public void CheckRoom(){

        String url="http://"+getResources().getString(R.string.ip)+"/web/CheckRoom.php";
        ap=new Apply(LoginActivity.user.getId(),year2,(month+1),day,number,time.substring(0,1));
        String json=new Gson().toJson(ap);
        Log.i("info","111111111111111111111111111111111111111"+json);
        RequestBody requestBody=RequestBody.create(JSON,json);
        final Request request=new Request.Builder().
                url(url).post(requestBody).build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()){
                    String s=response.body().string();
                    Log.i("info","66666666666666666666666666"+s);
                    if(s.equals("1")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StudentActivity.this,"有课",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if(s.equals("0")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StudentActivity.this,"空闲",Toast.LENGTH_SHORT).show();
                                checked=true;
                            }
                        });
                    }else if(s.equals("2")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StudentActivity.this,"已被申请",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }
            }
        });

    }

    public void Apply(){

        EditText et=(EditText)findViewById(R.id.edit_descripstion);
        ap.description=et.getText().toString();
        if(ap.description.equals(""))ap.description="nothing";
        String json=new Gson().toJson(ap);
        String url="http://"+getResources().getString(R.string.ip)+"/web/Apply.php";
        //String url="http://192.168.1.104/web/Login.php";
        RequestBody requestBody= RequestBody.create(JSON,json);
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()){
                    String str=response.body().string();
                    if(str.equals("1")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StudentActivity.this,"申请完成，请等待",Toast.LENGTH_SHORT).show();
                                checked=false;
                                TextView tx=(TextView)findViewById(R.id.textview_number);
                                apply_info=ap.number+" "+ap.month+"-"+ap.day+"-"+"第"+ap.time+"节";
                                tx.setText(apply_info);
                            }
                        });
                    }else if(str.equals("0")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StudentActivity.this,"申请失败",Toast.LENGTH_SHORT).show();
                                checked=false;
                            }
                        });

                    }else if(str.equals("2")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StudentActivity.this,"限量申请",Toast.LENGTH_SHORT).show();
                                checked=false;
                            }
                        });
                    }
                }
            }
        });

        checked=false;
    }
    public void CancelApply(){
        String url="http://"+getResources().getString(R.string.ip)+"/web/CancelApply.php";
        String json=new Gson().toJson(LoginActivity.user);
        Log.i("info","111111111111111111111111111111111111111"+json);
        Log.i("info","111111111111111111111111111111111111111"+json);
        RequestBody requestBody=RequestBody.create(JSON,json);
        Log.i("info","111111111111111111111111111111111111111"+json);
        Request request=new Request.Builder().
                url(url).post(requestBody).build();
        Log.i("info","111111111111111111111111111111111111111"+json);
        Log.i("info","22222222");
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String s=response.body().string();
                Log.i("info","-----------------------------"+s);
                if(s.equals("1")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StudentActivity.this,"取消成功",Toast.LENGTH_SHORT).show();
                            TextView tx=(TextView)findViewById(R.id.textview_number);
                            tx.setText("");
                        }
                    });
                }else if(s.equals("0")){
                    Log.i("info","ssssssssssssss");
                }
            }
        });

    }
    public void CheckApply(){
        String json=new Gson().toJson(LoginActivity.user);
        String url="http://"+getResources().getString(R.string.ip)+"/web/CheckApply.php";
        Log.i("info",json+"         "+url);
        //String url="http://192.168.1.104/web/Login.php";
        RequestBody requestBody= RequestBody.create(JSON,json);
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()){
                    allpy_information=response.body().string();
                    Log.i("info",allpy_information);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tx=(TextView)findViewById(R.id.textview_number);
                            Log.i("info",allpy_information);
                            if(tx!=null)
                                tx.setText(allpy_information);
                        }
                    });

                }

            }
        });
    }
}
