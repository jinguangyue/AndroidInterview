package com.ipc.binderpool;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ipc.R;

public class BinderPoolActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmanager);
        this.context = this;
        new Thread(new BinderPoolRunnable()).start();
    }


    class BinderPoolRunnable implements Runnable {
        @Override
        public void run() {
            BinderPool binderPool = BinderPool.getInstance(context);

            IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
            ISecurityCenter securityCenter = SecurityImpl.asInterface(securityBinder);
            String msg = "Hello anzhuo";

            try {
                String password = securityCenter.encrypt(msg);
                Log.e("yue", "password===" + password);

                String decryt = securityCenter.decrypt(password);
                Log.e("yue","decrypt===" + decryt);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
            IComput iComput = computeImpl.asInterface(computeBinder);


            try {
                Log.e("yue", "3+5===" + iComput.add(3,5));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
