package com.ipc.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

public class BinderPool {
    private static final String TAG = "yue";
    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;

    private IBinderPool iBinderPool;
    private static BinderPool instance;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private Context context;

    public BinderPool(Context context) {
        this.context = context.getApplicationContext();
        connectPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (instance == null) {
            synchronized (BinderPool.class) {
                if (instance == null) {
                    instance = new BinderPool(context);
                }
            }
        }
        return instance;
    }

    public IBinder queryBinder(int binderCode) {
        IBinder mBinder = null;
        if (iBinderPool != null) {
            try {
                mBinder = iBinderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return mBinder;
    }

    private synchronized void connectPoolService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(context, BinderPoolService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBinderPool = IBinderPool.Stub.asInterface(service);

            try {
                iBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
                mConnectBinderPoolCountDownLatch.countDown();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            iBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            iBinderPool = null;
            connectPoolService();
        }
    };

    public static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityImpl();
                    break;

                case BINDER_COMPUTE:
                    binder = new computeImpl();
                    break;
            }
            return binder;
        }
    }
}
