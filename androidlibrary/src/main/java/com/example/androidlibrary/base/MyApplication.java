package com.example.androidlibrary.base;

import android.app.Application;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.example.androidlibrary.utils.MyAppException;


/**
 * @author xueming_wu
 * @date 2018/5/9 0009
 * TODO: 重写 Application 以做一些全局化的处理
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        MyAppException.getInstance().init(this);
        Utils.init(this);
        LogUtils.getConfig().setLogHeadSwitch(false);
        LogUtils.getConfig().setBorderSwitch(false);
    }

}
