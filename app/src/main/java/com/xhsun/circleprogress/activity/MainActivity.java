package com.xhsun.circleprogress.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xhsun.circleprogress.R;
import com.xhsun.circleprogress.view.CirclePercentByArcView;
import com.xhsun.circleprogress.view.CirclePercentView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        final CirclePercentView cpView1 = findViewById(R.id.cpView1);
        final CirclePercentByArcView cpView2 = findViewById(R.id.cpView2);

        Button btnStart = findViewById(R.id.btnStart);

        final int totalProgress = 100;

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {

                        try {
                            for (int i = 0; i <= totalProgress; i++) {

                                Thread.sleep(30);

                                cpView1.setPercent(i);

                                cpView2.setPercent(i);

                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }
}