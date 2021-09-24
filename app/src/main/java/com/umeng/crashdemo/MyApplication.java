package com.umeng.crashdemo;

import android.app.Application;
import android.os.Bundle;

import com.efs.sdk.net.OkHttpInterceptor;
import com.efs.sdk.net.OkHttpListener;
import com.uc.crashsdk.export.CrashApi;
import com.umeng.commonsdk.UMConfigure;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        UMConfigure.setLogEnabled(true);
        //添加注释
        UMConfigure.init(this, "5fd9c794dd28915339214127", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");

//        final Bundle customInfo = new Bundle();
//        customInfo.putBoolean("mCallNativeDefaultHandler", true);
//        CrashApi.getInstance().updateCustomInfo(customInfo);
        initOkhttp();
    }


    public  void  initOkhttp(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //设置事件监听器，OkHttpListener.get()为SDK API
                .eventListenerFactory(OkHttpListener.get())
                //设置拦截器，new OkHttpInterceptor()为SDK API
                .addNetworkInterceptor(new OkHttpInterceptor())
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
