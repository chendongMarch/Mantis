package com.zfy.mantis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.zfy.mantis.annotation.MView;

/**
 * CreateAt : 2019/1/10
 * Describe :
 *
 * @author chendong
 */

public class MainActivity extends AppCompatActivity {


    @MView(R.id.tv)
    TextView tv;

    @MView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        MainActivity_MView.bindView(this);
        try {
            tv.setText("new text");

            btn.setOnClickListener(v -> {
                tv.setText("click text");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
