package com.example.majingrun.classroommanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements Runnable{

    EditText editText_id;
    EditText editText_p;
    Button button;
    RadioGroup radio;
    User user;
    int type=0;
    Gson gson=new Gson();
    httpClient okHttp=new httpClient();
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
                        new Thread(LoginActivity.this).start();
                        while(s.equals("null"));
                        if(s.equals("1")){
                            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                            Intent in=new Intent(LoginActivity.this,AdminActivity.class);
                            startActivity(in);
                            finish();
                        }else if(s.equals("2")){
                            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                            Intent in=new Intent(LoginActivity.this,AdminActivity.class);
                            startActivity(in);
                            finish();
                        }else if(s.equals("0")){
                            Toast.makeText(LoginActivity.this,"账号密码不正确",Toast.LENGTH_SHORT).show();
                            s="null";
                        }else{
                            Toast.makeText(LoginActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
                            s="null";
                        }
                    }
                }
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
    public String Login(User user){
        String json=gson.toJson(user);

        String url="http://192.168.1.104/web/Login.php";
        String re="";
        try {
            re = okHttp.PostJson(url,json);
        }catch(IOException e){
            e.printStackTrace();
        }
        Log.i("info","111111111111111111111111111"+json);
        Log.i("info","333333333333333333333333333"+re);
        return re;
    }

    @Override
    public void run() {
         s=Login(user);
    }
}
