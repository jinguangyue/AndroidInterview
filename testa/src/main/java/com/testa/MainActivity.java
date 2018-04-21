package com.testa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "yue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent();
                //包名 包名+类名（全路径）
                intent.setClassName("com.testb", "com.testb.TestCActivity");
                startActivity(intent);*/
                testHashSet();
            }
        });
    }

    private void testHasMap(){

        HashSet<String> strings = new HashSet<>();

        Map<String, String> map = new HashMap<>();

        map.put("peach", "桃子");
        map.put("watermelon", "西瓜");
        map.put("banana", "香蕉");
        map.put("apple", "苹果");


        Log.i(TAG, map.put("peach", "666"));

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Log.i("顺序", entry.getKey() + "=" + entry.getValue());
        }
    }

    private void testHashSet(){
        HashSet<String> strings = new HashSet<>();
        strings.add("桃子");
        strings.add("西瓜");
        strings.add("香蕉");
        strings.add("苹果");

        Log.e(TAG, strings.add("888") + "");

        Iterator iterator = strings.iterator();
        while (iterator.hasNext()){
            Log.i(TAG, iterator.next() + "");
        }

    }
}
