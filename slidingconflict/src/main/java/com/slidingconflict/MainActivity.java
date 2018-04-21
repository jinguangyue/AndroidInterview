package com.slidingconflict;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.slidingconflict.view.HorizontalScrollView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private HorizontalScrollView horizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);


        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        for (int i = 0; i < 3; i++) {
            ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.content_layout, horizontalScrollView, false);
            viewGroup.getLayoutParams().width = dm.widthPixels;

            ArrayList<String> datas = new ArrayList<String>();
            for (int a = 0; a < 50; a++) {
                datas.add("name " + a);
            }

            ListView listView = viewGroup.findViewById(R.id.list);
            listView.setAdapter(new ArrayAdapter<String>(this, R.layout.content_list_item, R.id.name, datas));

            TextView textView = viewGroup.findViewById(R.id.title);
            textView.setText("page " + (i + 1));
            viewGroup.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            horizontalScrollView.addView(viewGroup);
        }


    }
}
