package com.example.xueming_wu.ant;

import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.example.androidlibrary.activity.BaseActivity;
import com.example.xueming_wu.ant.rxjava_demo.BaseRxjava;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.SchedulerWhen;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ：xueming_wu @深圳理才网
 * @version ：1.0
 * @date ：2018/5/21 0021 14:22
 * @desc ： 主页面
 */

public class MainActivity extends BaseActivity {
    private int m = 0;

    private static final String TAG = "MainActivity";

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseRxjava.getInstance().backpressure();
                LogUtils.i("~~~~~按钮点击~~~~~~~~~~~~~~~~");
            }
        });
    }

    @Override
    protected void loadData() {
        BaseRxjava.getInstance().backpressure();
    }

}
