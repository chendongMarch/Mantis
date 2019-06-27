package com.zfy.mantis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zfy.mantis.annotation.Lookup;
import com.zfy.mantis.api.Mantis;
import com.zfy.mantis.app.BaseActivity;
import com.zfy.mantis.model.MyService;
import com.zfy.mantis.model.MyService2;
import com.zfy.mantis.model.MyService2Impl;
import com.zfy.mantis.model.WxInfo;

/**
 * CreateAt : 2019/1/10
 * Describe :
 *
 * @author chendong
 */

public class MainActivity extends BaseActivity {

    @Lookup(value = "test1", desc = "我是名字")   byte      test1;
    @Lookup(value = "test2", required = true) short     test2;
    @Lookup(value = "test3")                  int       test3;
    @Lookup(value = "test4")                  long      test4;
    @Lookup(value = "test5")                  float     test5;
    @Lookup(value = "test6")                  double    test6;
    @Lookup(value = "test7")                  boolean   test7;
    @Lookup(value = "test8")                  char      test8;
    @Lookup(value = "test9")                  String    test9;
    @Lookup(value = "test10", group = 3)      WxInfo    test10;
    @Lookup(value = "test11", group = 3)      MyService test11;
    @Lookup(value = "test12", group = 3)      WxInfo    test12;

    @Lookup(group = 3)                               MyService      test13;
    @Lookup(group = 3, clazz = MyService2Impl.class) MyService2     test14;
    @Lookup(group = 11)                              MyService2Impl test15;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getIntent().putExtra("test1", (byte) 1);
        getIntent().putExtra("test2", (short) 10);
        getIntent().putExtra("test3", 100);
        getIntent().putExtra("test4", 1000L);
        getIntent().putExtra("test5", 1000.1f);
        getIntent().putExtra("test6", 1000.123d);
        getIntent().putExtra("test7", true);
        getIntent().putExtra("test8", '1');
        getIntent().putExtra("test9", "hahahha");
        getIntent().putExtra("test10", new WxInfo(100L, "nickName"));
        getIntent().putExtra("test12", new WxInfo(100L, "nickName"));
        getIntent().putExtra("testsss", "父类");

        findViewById(R.id.btn).setOnClickListener(v -> {
            Mantis.inject(3, this);
            Mantis.inject(11, this);
            Mantis.inject(this);
            MainPresenter mainPresenter = new MainPresenter(this);
            mainPresenter.init();
            Log.e("chendong", "finish");
        });

    }

}
