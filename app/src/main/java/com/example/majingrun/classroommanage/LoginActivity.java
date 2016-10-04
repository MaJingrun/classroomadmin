package com.example.majingrun.classroommanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity{


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static OkHttpClient okHttpClient=new OkHttpClient();
    EditText editText_id;
    EditText editText_p;
    Button button;
    RadioGroup radio;
    static User user;
    int type=0;
    Gson gson=new Gson();
    String s="null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button=(Button)findViewById(R.id.button_of_login);
        editText_id=(EditText)findViewById(R.id.editText_number_of_student);
        editText_p=(EditText)findViewById(R.id.editText_password_of_student);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_id.getText().toString().equals("")||editText_p.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"学号密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    if(type==0){
                        Toast.makeText(LoginActivity.this,"必须选择登陆类型",Toast.LENGTH_SHORT).show();
                    }else {
                        user=new User(editText_id.getText().toString(),editText_p.getText().toString(),type);
                        TestLogin(user);

                    }
                }
            }
        });


        Button button_regist=(Button)findViewById(R.id.button_of_regist);
        button_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(LoginActivity.this,Regist2Activity.class);
                startActivity(in);
            }
        });


        radio=(RadioGroup)findViewById(R.id.radioGroup);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButton_of_student){
                    type=1;
                }else if(checkedId==R.id.radioButton_of_admin){
                    type=2;
                }
            }
        });
    }
    public void TestLogin(User user){
        String json=gson.toJson(user);
        String url="http://"+getResources().getString(R.string.ip)+"/web/Login.php";
        Log.i("info",json+"         "+url);
        //String url="http://192.168.1.104/web/Login.php";
        String re="";
        RequestBody requestBody= RequestBody.create(JSON,json);
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    s=response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowLoginInfo();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    public void ShowLoginInfo(){
        Log.i("info","222222222222"+s);
        if(s.equals("1")){
            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(LoginActivity.this,StudentActivity.class);
            startActivity(in);
            finish();
        }else if(s.equals("2")){
            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(LoginActivity.this,AdminActivity.class);
            startActivity(in);
            finish();
        }else if(s.equals("0")){
            Toast.makeText(LoginActivity.this,"账号密码不正确",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(LoginActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
        }
    }
}
