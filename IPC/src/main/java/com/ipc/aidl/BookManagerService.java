package com.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {

    private static final String TAG = "yue";
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
//    private CopyOnWriteArrayList<IOnNewBookArrivedListner> mListnerList = new CopyOnWriteArrayList<>();

    private RemoteCallbackList<IOnNewBookArrivedListner> onNewBookArrivedListnerRemoteCallbackList = new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void register(IOnNewBookArrivedListner iOnNewBookArrivedListner) throws RemoteException {
//            if(!mListnerList.contains(iOnNewBookArrivedListner)){
//                mListnerList.add(iOnNewBookArrivedListner);
//            }

            onNewBookArrivedListnerRemoteCallbackList.register(iOnNewBookArrivedListner);

//            Log.i(TAG, "register---size===" + mListnerList.size());
        }

        @Override
        public void unregister(IOnNewBookArrivedListner iOnNewBookArrivedListner) throws RemoteException {
//            if(mListnerList.contains(iOnNewBookArrivedListner)){
//                mListnerList.remove(iOnNewBookArrivedListner);
//            }

            onNewBookArrivedListnerRemoteCallbackList.unregister(iOnNewBookArrivedListner);

//            Log.i(TAG, "unregister---size===" + mListnerList.size());
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.access_bookservice");

        Log.i(TAG, "check===" + check);
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "Ios"));
        new Thread(new ServiceWoker()).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        atomicBoolean.set(true);
    }

    private class ServiceWoker implements Runnable {
        @Override
        public void run() {
            while (!atomicBoolean.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "newBook" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        int n = onNewBookArrivedListnerRemoteCallbackList.beginBroadcast();
        Log.d(TAG, "Listner---Size===" + n);
        for (int i = 0; i < n; i++) {
            IOnNewBookArrivedListner onNewBookArrivedListner = onNewBookArrivedListnerRemoteCallbackList.getBroadcastItem(i);
            if (onNewBookArrivedListner != null) {
                onNewBookArrivedListner.onBookArrived(book);
            }
        }

        onNewBookArrivedListnerRemoteCallbackList.finishBroadcast();
    }
}
