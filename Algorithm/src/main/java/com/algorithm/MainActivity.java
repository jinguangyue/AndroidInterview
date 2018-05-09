package com.algorithm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    int[] a = {1,2,3,4,5,6,7,8,9,10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //快速排序
//        quicksort(a, 0, a.length - 1);

        //冒泡排序
//        bubbleSort(a);

        //二分法查找
        searchRecursive(a, 0, a.length - 1, 3);

        /*for (int i = 0; i < a.length; i++) {
            Log.i("yue", a[i] + "");
        }*/
    }

    private void searchRecursive(int[] a, int left, int end, int findValue) {
        if (a == null) {
            Log.i("yue", "-1");
        }
        if (left <= end) {
            int middle = (left + end) / 2;
            int middleValue = a[middle];

            if (middleValue < findValue) {
                searchRecursive(a, middle + 1, end, findValue);
            } else if (middleValue > findValue) {
                searchRecursive(a, left, middle - 1, findValue);
            } else {
                Log.i("yue", "middle===" + middle);
            }
        } else {
            Log.e("yue", "Not find");
        }
    }

    /**
     * 快速排序
     *
     * @param a
     * @param left
     * @param end
     */
    private void quicksort(int[] a, int left, int end) {
        if (left > end) {
            return;
        }

        if (left < end) {
            int dp = divide(a, left, end);
            quicksort(a, left, dp - 1);
            quicksort(a, dp + 1, end);
        }

    }

    private int divide(int[] a, int left, int end) {
        int base = a[end];

        while (left < end) {
            while (left < end && a[left] < base)
                left++;

            if (left < end) {
                int temp = a[left];
                a[left] = a[end];
                a[end] = temp;
                end--;
            }

            while (left < end && a[end] > base)
                end--;

            if (left < end) {
                int temp = a[end];
                a[end] = base;
                a[left] = temp;
                left++;
            }
        }

        return end;
    }

    /**
     * 冒泡排序
     *
     * @param a
     */
    private void bubbleSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1; j++) {
                if (a[j] < a[j + 1]) {
                    int temp = a[j + 1];
                    a[j + 1] = a[j];
                    a[j] = temp;
                }
            }
        }
    }
}
