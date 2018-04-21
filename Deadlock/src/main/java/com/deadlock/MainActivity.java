package com.deadlock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "yue";

    private Semaphore semaphore1 = new Semaphore(1);
    private Semaphore semaphore2 = new Semaphore(1);

    private Object object1 = new Object(), object2 = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ThreadDieLock1 threadDieLock1 = new ThreadDieLock1();
        threadDieLock1.start();

        ThreadDieLock2 threadDieLock2 = new ThreadDieLock2();
        threadDieLock2.start();
    }


    class ThreadDieLock1 extends Thread {
        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    if (semaphore1.tryAcquire(1, TimeUnit.SECONDS)){
                        Log.i(TAG, "o1 locked");

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (semaphore2.tryAcquire(1, TimeUnit.SECONDS)){
                            synchronized (object2){
                                Log.i(TAG, "Thread 1 o2 locked");
                            }
                        }

                        semaphore1.release();
                        semaphore2.release();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class ThreadDieLock2 extends Thread {
        @Override
        public void run() {
            super.run();
            synchronized (object2){
                Log.i(TAG, "o2 locked");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                synchronized (object1){
                    Log.i(TAG, "Thread 2 o1 locked");
                }
            }
        }
    }
}
