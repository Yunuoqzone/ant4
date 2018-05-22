package com.example.androidlibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author wuxueming
 * @date 2018/5/15
 * @ToDo 规范化Activity
 */

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initLayout();
        loadData();
    }

    /**
     * 初始化变量，包括Intent带的数据和Activity内的变量
     */
    protected abstract void initVariables();

    /**
     * 加载layout布局文件
     * @param savedInstanceState
     */
    protected abstract void initLayout();

    /**
     * 调用MobileAPI获取数据
     */
    protected abstract void loadData();
}
