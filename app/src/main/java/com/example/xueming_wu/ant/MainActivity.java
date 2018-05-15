package com.example.xueming_wu.ant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.androidlibrary.activity.BaseActivity;

import org.w3c.dom.Node;

import java.util.LinkedList;

/**
 *
 */
public class MainActivity extends BaseActivity {
    private Manager manager=null;
    private int m=0;

    private static final String TAG = "MainActivity";

    @Override
    protected void initVariables() {
        if(manager==null){
            manager=new Manager();
        }
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.doWork();


            }
        });
    }

    @Override
    protected void loadData() {

    }

    private class  Manager{
        public Manager(){
            
        }
        public void doWork(){
            m=++m;
        }
    }
}
