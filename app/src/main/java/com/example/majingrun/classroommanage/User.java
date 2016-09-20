package com.example.majingrun.classroommanage;

/**
 * Created by 96358 on 2016/9/15.
 */
public class User {

    private String id;
    private String password;
    private int type;

    public User(String n,String p,int t){
        this.id=n;
        this.password=p;
        this.type=t;
    }

    public String getId(){
        return id;
    }
}
