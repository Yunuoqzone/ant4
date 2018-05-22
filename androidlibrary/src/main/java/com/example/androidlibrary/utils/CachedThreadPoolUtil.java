package com.example.androidlibrary.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ：xueming_wu @深圳理才网
 * @version ：1.0
 * @date ：2018/5/9 0009 15:06
 * @desc ：自定义线程池
 */
public class CachedThreadPoolUtil {
    /**
     *cpu 核心线程数
     */
    private static final  int CPU_COUNT= Runtime.getRuntime().availableProcessors();
    /**
     * 线程池中核心线程的数量
     */
    private static int CORE_POOL_SIZE = CPU_COUNT+1;
    /**
     * 线程池中最大线程数
     */
    private static int MAX_POOL_SIZE = CPU_COUNT*2+1;
    /**
     * 非核心线程的超时时长，当系统中非核心线程闲置时间超过 KEEP_ALIVE_TIME 则会被回收
     */
    private static int KEEP_ALIVE_TIME = 1;
    /**
     * 阻塞队列 当核心线程都被占用，且阻塞队列已满的情况下才会开启额外线程
     * 主要用来存储被提交但是尚未执行的任务
     */
    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(CORE_POOL_SIZE);
    /**
     * 为线程池提供创建新线程的功能
     */
    private ThreadFactory factory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "ThreadPool thread:" + integer.getAndIncrement());
        }
    };

    private static class CachedThreadPoolUtilInstance {
        private static CachedThreadPoolUtil INSTANCE =new CachedThreadPoolUtil();
    }
    public static CachedThreadPoolUtil getInstance(){
        return CachedThreadPoolUtilInstance.INSTANCE ;
    }

    private ExecutorService cachedThreadPool;
    private CachedThreadPoolUtil() {
        cachedThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, workQueue, factory);
    }

    /**
     * 运行一个线程
     * @param runnable
     */
    public void execute(Runnable runnable) {
        cachedThreadPool.execute(runnable);
    }
}
