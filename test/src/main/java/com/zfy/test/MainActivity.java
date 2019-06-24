package com.zfy.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zfy.mantis.annotation.LookupArgs;

/**
 * CreateAt : 2019/1/10
 * Describe :
 *
 * @author chendong
 */

public class MainActivity extends AppCompatActivity {

    @LookupArgs(value = "test1", desc = "我是名字")   byte    test1;
    @LookupArgs(value = "test2", required = true) short   test2;
    @LookupArgs("test3")                          int     test3;
    @LookupArgs("test4")                          long    test4;
    @LookupArgs("test5")                          float   test5;
    @LookupArgs("test6")                          double  test6;
    @LookupArgs("test7")                          boolean test7;
    @LookupArgs("test8")                          char    test8;
    @LookupArgs("test9")                          String  test9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntent().putExtra("test1", (byte) 1);
        getIntent().putExtra("test2", (short) 10);
        getIntent().putExtra("test3", 100);
        getIntent().putExtra("test4", 1000L);
        getIntent().putExtra("test5", 1000.1);
        getIntent().putExtra("test6", 1000.123d);
        getIntent().putExtra("test7", true);
        getIntent().putExtra("test8", '1');
        getIntent().putExtra("test9", "hahahha");

    }

}
