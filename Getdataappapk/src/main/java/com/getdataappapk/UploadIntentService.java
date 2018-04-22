package com.getdataappapk;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class UploadIntentService extends Service {
    private String message;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("yue", "onStartCommand");
        init();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private ArrayList<ResolveInfo> getApps() {
        ArrayList<ResolveInfo> mApps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN); // 动作匹配
        intent.addCategory(Intent.CATEGORY_LAUNCHER); // 类别匹配
        List<ResolveInfo> apps = getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : apps) {
            final String appDir;
            try {
                ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(
                        resolveInfo.activityInfo.packageName, 0);
                appDir = applicationInfo.sourceDir;

//                Log.e("yue", "name===" + applicationInfo.packageName);

                if (appDir.contains("/data/app/")) {
                    mApps.add(resolveInfo);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
        return mApps;
    }

    private void init() {

        ArrayList<ResolveInfo> mApps = getApps();
        upload(mApps);
    }

    private void upload(final ArrayList<ResolveInfo> apps) {
        try {
            if (apps != null && apps.size() > 0) {
                ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(
                        apps.get(0).activityInfo.packageName, 0);
                final String appDir = applicationInfo.sourceDir;

                File upFile = new File(appDir);

                if (upFile == null || !upFile.exists() || appDir.contains(getPackageName())) {
                    apps.remove(0);
                    upload(apps);
                    return;
                }
                Log.e("yue", "上传目录===" + appDir);
                Log.e("yue", "fileName===" + upFile.getName());
                Log.e("yue", "applicationInfo.packageName===" + applicationInfo.packageName);

                OkHttpUtils
                        .post()
                        .tag(this)
                        .url("http://192.168.10.31/index.php?ctrl=usb&action=uploadFile")
                        .addFile("myfile", applicationInfo.packageName+".apk", upFile)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e("yue", e.getMessage());
                                message = "上传 " + appDir + " 失败 原因：" + e.getMessage();
                                notifyMessage(message);

                                apps.remove(0);
                                upload(apps);

                            }

                            @Override
                            public void onResponse(String response, int id) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject != null) {
                                        int code = jsonObject.getInt("code");
                                        if (code == 0) {
                                            //success
                                            Log.e("yue", "上传成功一个了, 剩余" + apps.size());
                                            message = "上传 " + appDir + " 成功";
                                            notifyMessage(message);
                                            apps.remove(0);

                                        } else {
                                            message = jsonObject.getString("msg");
                                            notifyMessage(message);
                                        }
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                upload(apps);
                                            }
                                        }, 1000);
                                    }
                                } catch (JSONException e) {
                                    Log.e("yue", "JSONException===" + e.getMessage() + " 返回的数据===" + response);
                                    message = e.getMessage() + " 返回的数据===" + response;
                                    notifyMessage(message);
                                    apps.remove(0);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            upload(apps);
                                        }
                                    }, 1000);
//                                    e.printStackTrace();
                                }
                            }
                        });
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        upload(getApps());
                    }
                }, 3000);


                message = "文件上传完毕， 开始删除";
                notifyMessage(message);
                Log.e("yue", "文件上传完毕， 开始删除");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (ResolveInfo resolveInfo : getApps()) {
                            String appDir = null;
                            try {
                                // 指定包名的程序源文件路径
                                appDir = getPackageManager().getApplicationInfo(
                                        resolveInfo.activityInfo.packageName, 0).sourceDir;
                                Log.e("yue", appDir + "删除中");

                                if (appDir.contains("/data/app/")) {
                                    File file = new File(appDir);
                                    if (file != null && file.exists()) {

                                        if(!appDir.contains(getPackageName())){
                                            if(AExecuteAsRoot.canRunRootCommands()){
                                                AExecuteAsRoot.execRootCmd("rm -r " + appDir);
                                            }
                                        }

                                        Message message1 = new Message();
                                        message1.obj = "删除完毕";
                                        message1.what = 0;
                                        handler.sendMessage(message1);


//                                        deleteDirectory(file);
//                                        boolean isDelete = file.delete();
//                                        Log.e("yue", "删除" + file.exists());
                                    }
                                }

                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    notifyMessage((String)msg.obj);
                    break;
            }
        }
    };


    public void deleteDirectory(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
            file.delete();
        }
    }

    private void notifyMessage(String message) {
        Intent intent = new Intent(MainActivity.MESSAGR_ACTION);
        intent.putExtra(MainActivity.KEY_MESSAGE, message);
        UploadIntentService.this.sendBroadcast(intent);
    }


}
