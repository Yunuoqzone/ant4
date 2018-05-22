package com.example.androidlibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：xueming_wu @深圳理才网
 * @version ：1.0
 * @date ：2018/5/9 14:47
 * @desc ：做了一个全局的崩溃捕获处理
 */

public class MyAppException implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * CrashHandler实例
     */
    public static MyAppException INSTANCE = new MyAppException();
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> info = new HashMap<String, String>();

    /**
     * 保证只有一个CrashHandler实例
     */
    private MyAppException() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static MyAppException getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该重写的方法来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义的没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                // 如果处理了，让程序继续运行5秒再退出，保证文件保存并上传到服务器
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ActivityUtils.finishAllActivities();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 是否弹出闪退提示
     */
    private boolean show = true;

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    public boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        if (show) {
            CachedThreadPoolUtil.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "程序有点不给力,请稍后重试", Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                }
            });
        }
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    public void collectDeviceInfo(Context context) {
        try {
            //获得包管理器
            PackageManager pm = context.getPackageManager();
            //得到该应用的信息，即主Activity
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // 反射机制
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
                LogUtils.dTag(TAG, field.getName() + ":" + field.get(""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        // 保存文件
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        String time = format.format(new Date());
        String fileName = "appFrame-" + time + ".txt";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + "appFrame" + File.separator + "log");
                LogUtils.iTag("OkHttpClientManager", dir.toString() + File.separator + fileName);
                LogUtils.iTag("OkHttpClientManager", "sb = " + sb.toString());
                //判断如果是云通讯的SDK的崩溃 不弹出提示
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                boolean s = dir.exists();
                FileOutputStream fos = new FileOutputStream(new File(dir,
                        fileName));
                fos.write(sb.toString().getBytes());
                fos.close();
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
