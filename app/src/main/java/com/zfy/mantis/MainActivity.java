package com.zfy.mantis;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class MainActivity<D extends String> extends BaseActivity<D> implements Comparable<String> {

    @Lookup(value = "key_test1", desc = "我是名字") byte      test1;
    @Lookup(value = "key_test2")                short     test2;
    @Lookup(value = "key_test3", numKey = 100)  int       test3;
    @Lookup(value = "key_test4")                long      test4;
    @Lookup(value = "key_test5")                float     test5;
    @Lookup(value = "key_test6")                double    test6;
    @Lookup(value = "key_test7")                boolean   test7;
    @Lookup(value = "key_test8")                char      test8;
    @Lookup(value = "key_test9")                String    test9;
    @Lookup(value = "key_test10", group = 3)    WxInfo    test10;
    @Lookup(value = "key_test11", group = 3)    MyService test11;
    @Lookup(value = "key_test12", group = 3)    WxInfo    test12;

    @Lookup(group = 3) MyService test13;
    @Lookup(group = 3, clazz = MyService2Impl.class, required = true)
    MyService2 test14;
    @Lookup(group = 11) MyService2Impl test15;
    @Lookup("KEY")      MyService2Impl test16;


    // 从 Intent 中获取 Key 为 KEY_INT_VALUE 的数据，默认值为 100
    @Lookup("KEY_INT_VALUE") int dataValue = 100;

    @Lookup(
            group = 101, // 分组，注解的属性会被分组，每次注入一个组，避免重复注入
            value = "KEY", // 字符串类型的 key
            numKey = 100, // 整型类型的 key
            clazz = MyService2Impl.class, // 注入的 class
            extra = 123, // 标记为，可以使用二进制表示32个标记位
            obj = false, // 强制作为对象注入
            required = true, // 如果注入的结果为空，将会抛出 npe
            desc = "我是注释信息" // 自动生成相关的注释信息
    )
    MyService2 objValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getIntent().putExtra("key_test1", (byte) 1);
        getIntent().putExtra("key_test2", (short) 10);
        getIntent().putExtra("key_test3", 100);
        getIntent().putExtra("key_test4", 1000L);
        getIntent().putExtra("key_test5", 1000.1f);
        getIntent().putExtra("key_test6", 1000.123d);
        getIntent().putExtra("key_test7", true);
        getIntent().putExtra("key_test8", '1');
        getIntent().putExtra("key_test9", "hahahha");
        getIntent().putExtra("key_test10", new WxInfo(100L, "nickName"));
        getIntent().putExtra("key_test12", new WxInfo(100L, "nickName"));
        getIntent().putExtra("key_testParent", "父类");

        findViewById(R.id.btn).setOnClickListener(v -> {
            try {
                Mantis.inject(3, this);
                Mantis.inject(11, this);
                Mantis.inject(this);
                MainPresenter mainPresenter = new MainPresenter(this);
                mainPresenter.init();
                Log.e("chendong", "finish");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public int compareTo(@NonNull String o) {
        return 0;
    }
}
