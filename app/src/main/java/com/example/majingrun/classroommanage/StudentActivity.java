package com.example.majingrun.classroommanage;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class StudentActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        TabHost tb=getTabHost();
        tb.addTab(tb.newTabSpec("tab1").setIndicator("查询").setContent(R.id.text111));
        tb.addTab((tb.newTabSpec("tab2").setIndicator("shenqing").setContent(R.id.button2)));
        tb.addTab((tb.newTabSpec("tab3").setIndicator("shenqing").setContent(R.id.button3)));


    }
}
