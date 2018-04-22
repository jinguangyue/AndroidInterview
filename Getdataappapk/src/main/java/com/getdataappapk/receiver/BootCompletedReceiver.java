package com.getdataappapk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.getdataappapk.MainActivity;

/**
 * Created by bryanfu123 on 6/4/17.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent appIntent = new Intent(context, MainActivity.class);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(appIntent);
    }
}
