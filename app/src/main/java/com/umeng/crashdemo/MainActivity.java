package com.umeng.crashdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.umeng.umcrash.UMCrash;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(MainActivity.this, AActivity.class);
////                startActivity(intent);
//            }
//        });

        findViewById(R.id.button_java_null_potion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crashInJava();
            }
        });

        findViewById(R.id.button_wl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textUrl();
            }
        });

        findViewById(R.id.button_user_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                StringBuffer stringBuffer = new StringBuffer();
//                stringBuffer.append("user exception is eeeeeeeeee");
//                UMCrash.generateCustomLog(stringBuffer.toString(), "UmengException");

                MobclickAgent.reportError(MainActivity.this, "UmengException");

                try {
                    crashInJava();
                } catch (Throwable e){
//                    UMCrash.generateCustomLog(e, "UmengException");
                    MobclickAgent.reportError(MainActivity.this, e);
                }
            }
        });

        findViewById(R.id.button_user_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("new api [user exception is eeeeeeeeee]");
                UMCrash.generateCustomLog(stringBuffer.toString(), "UmengException");
            }
        });

        findViewById(R.id.button_user_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    crashInJava();
                } catch (Throwable e){
                    UMCrash.generateCustomLog(e, "UmengException");
                }
            }
        });

        findViewById(R.id.button_user_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.reportError(MainActivity.this, "UmengException");
            }
        });

        findViewById(R.id.button_user_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    crashInJava();
                } catch (Throwable e){
                    MobclickAgent.reportError(MainActivity.this, e);
                }
            }
        });

//        findViewById(R.id.button_user_5).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MobclickAgent.setCatchUncaughtExceptions(true);
//            }
//        });
//
//        findViewById(R.id.button_user_6).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MobclickAgent.setCatchUncaughtExceptions(false);
//            }
//        });

        findViewById(R.id.button_native_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nativeCrash(0);
            }
        });

        findViewById(R.id.button_anr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (true) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        findViewById(R.id.button_pa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void crashInJava() {
        String nullStr = "1";
        if (nullStr.equals("1")) {
            nullStr = null;
        }
        nullStr.equals("");
    }

    private List<byte[]> mMems = new ArrayList<byte[]>(4096);
    private void javaOOMCrash() {
        final int kInitSize = 10 * 1024 * 1024;
        final int kMinSize = 1024;

        int size = kInitSize;
        int totalAllocSize = 0;
        while (size > 0) {
            try {
                byte[] mem = new byte[size];
                for (int i = 1; i < size; i += 4096) {
                    mem[i] = (byte) i;
                }
                mMems.add(mem);
                totalAllocSize += size;
            } catch (OutOfMemoryError t) {
                if (size < kMinSize) {
                    Log.w("walle", String.format(Locale.US,
                            "=Total %d bytes", totalAllocSize));
                    throw t;
                }
                size /= 2;
            }
        }
        // Crash what ever
        byte[] mem = new byte[kInitSize];
        mMems.add(mem);
    }

    private void nativeCrash(int type) {
//        JNIBridge.nativeCrash(type, 0);
        stringFromJNI();
    }


    public  void  textUrl(){
        String url = "http://www.csdn.net/";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("username", "hyman")
                .addParams("password", "123")
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }

                });
    }
}