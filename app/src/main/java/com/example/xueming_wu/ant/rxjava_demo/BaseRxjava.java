package com.example.xueming_wu.ant.rxjava_demo;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.xueming_wu.ant.rxjava_demo.entity.Data;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * @author ：xueming_wu @深圳理才网
 * @version ：1.0
 * @date ：2018/5/22 14:01
 * @desc ：写一些简单的使用范例
 */
public class BaseRxjava {
    private static class BaseRxjavaInstance {
        private static BaseRxjava INSTANCE = new BaseRxjava();
    }

    public static BaseRxjava getInstance() {
        return BaseRxjavaInstance.INSTANCE;
    }


    /**
     * 创建观察者或者订阅者
     */
    public Subscriber<Data> createObserver() {
        return new Subscriber<Data>() {
            @Override
            public void onSubscribe(Subscription s) {
                //控制返回数量
//                s.request(3);
                //控制是否接受事件
//                s.cancel();
            }

            @Override
            public void onNext(Data s) {
                LogUtils.i(s.getId() + "---" + s.getName());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    /**
     * 创建被观察者
     */
    public Observable<String> createObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("helllo world");
            }
        });
    }

    /**
     * 背压（backpressure）
     * 我们了解到数据流发射，处理，响应可能在各自的线程中独立进行，上游在发射数据的时候，
     * 不知道下游是否处理完，也不会等下游处理完之后再发射。
     * 这样，如果上游发射的很快而下游处理的很慢，会怎样呢？
     * 将会产生很多下游没来得及处理的数据，这些数据既不会丢失，也不会被垃圾回收机制回收，
     * 而是存放在一个异步缓存池中，如果缓存池中的数据一直得不到处理，越积越多，最后就会造成内存溢出，这便是Rxjava中的背压问题。
     */
    public void backpressure() {
        Subscriber<Data> subscriber = createObserver();
        List<Data> list = new ArrayList<>();
        list.add(new Data.Builder().id(1).name("张三").build());
        list.add(new Data.Builder().id(2).name("李四").build());
        list.add(new Data.Builder().id(3).name("王五").build());
        Flowable.fromIterable(list).flatMap(new Function<Data, Publisher<Data>>() {
            @Override
            public Publisher<Data> apply(Data data) throws Exception {
                data.setName(data.getName() + "___keji");
                return Flowable.fromArray(data);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
    public void getNetworkData(){

    }
}
