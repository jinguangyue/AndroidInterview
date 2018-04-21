package com.viewdrag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ScrollVIew scrollVIew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         button = findViewById(R.id.btn_drag);
        scrollVIew = findViewById(R.id.img_scroll);

        button.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollVIew.smoothScrollTo(-300, 10);
                //view动画 并不改变布局位置
                /*TranslateAnimation translateAnimation = new TranslateAnimation(0, 300, 0 ,0);
                translateAnimation.setDuration(300);
                translateAnimation.setFillAfter(true);
                translateAnimation.start();
                button.startAnimation(translateAnimation);*/


                //3.0 以上使用属性动画 改变位置
//                ObjectAnimator.ofFloat(button, "translationX", 0 ,300).setDuration(100).start();

//                button.scrollTo(10, 0);

                /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
                params.leftMargin += 100;
                button.setLayoutParams(params);

                button.requestLayout();*/

            }
        });
    }
}
