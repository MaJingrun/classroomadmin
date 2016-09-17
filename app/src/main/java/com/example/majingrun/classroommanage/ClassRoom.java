package com.example.majingrun.classroommanage;

/**
 * Created by 96358 on 2016/9/17.
 */
public class ClassRoom {
    private int id;
    private int[] time;

    public ClassRoom(int i,int[] in){
        id=i;time=in;
    }

    public int GetId(){
        return id;
    }

    public int[] GetTime(){
        return time;
    }
}
