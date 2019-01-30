package com.zfy.mantis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zfy.mantis.annotation.LookUp;
import com.zfy.mantis.api.Mantis;
import com.zfy.mantis.model.WxInfo;

/**
 * CreateAt : 2019/1/10
 * Describe :
 *
 * @author chendong
 */

public class MainActivity extends AppCompatActivity {

    @LookUp(value = "test1", desc = "我是名字")   byte  test1;
    @LookUp(value = "test2", required = true) short test2;
    @LookUp("test3")  int       test3;
    @LookUp("test4")  long      test4;
    @LookUp("test5")  float     test5;
    @LookUp("test6")  double    test6;
    @LookUp("test7")  boolean   test7;
    @LookUp("test8")  char      test8;
    @LookUp("test9")  String    test9;
    @LookUp("test10") WxInfo    test10;
    @LookUp("test11") MyService test11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getIntent().putExtra("test1", (byte) 1);
        getIntent().putExtra("test2", (short) 10);
        getIntent().putExtra("test3", 100);
        getIntent().putExtra("test4", 1000L);
        getIntent().putExtra("test5", 1000.1);
        getIntent().putExtra("test6", 1000.123d);
        getIntent().putExtra("test7", true);
        getIntent().putExtra("test8", '1');
        getIntent().putExtra("test9", "hahahha");
        getIntent().putExtra("test10", new WxInfo(100L, "nickName"));

        findViewById(R.id.btn).setOnClickListener(v -> {
            Mantis.getInst().inject(this);
            MainPresenter mainPresenter = new MainPresenter(this);
            mainPresenter.init();
            Log.e("chendong", "finish");
        });

    }

}
