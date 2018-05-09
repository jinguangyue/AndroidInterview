package com.ipc.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ipc.R;

import java.util.List;

public class BookManagerActivity extends Activity {

    private static final String TAG = "yue";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager iBookManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmanager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
             iBookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> list = iBookManager.getBookList();
                Log.i(TAG, "query book list, list type:" + list.getClass().getCanonicalName());

                Log.i(TAG, "query book list:" + list.toString());

                Book newBook = new Book(3, "Android 开发艺术探索");
                iBookManager.addBook(newBook);

                list = iBookManager.getBookList();
                Log.i(TAG, "query book list:" + list.toString());

                iBookManager.register(onNewBookArrivedListner);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected---name===" + name);
            iBookManager = null;
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.i(TAG, "receive new book===" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private IOnNewBookArrivedListner onNewBookArrivedListner = new IOnNewBookArrivedListner.Stub() {
        @Override
        public void onBookArrived(Book newBook) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };

    @Override
    protected void onDestroy() {

        if(iBookManager != null && iBookManager.asBinder().isBinderAlive()){
            try {
                Log.i(TAG, "unregister listner:" + onNewBookArrivedListner);
                iBookManager.unregister(onNewBookArrivedListner);
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        unbindService(serviceConnection);

        super.onDestroy();

    }
}
