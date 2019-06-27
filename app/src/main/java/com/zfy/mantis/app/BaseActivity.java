package com.zfy.mantis.app;

import android.support.v7.app.AppCompatActivity;

import com.zfy.mantis.annotation.Lookup;

/**
 * CreateAt : 2019/6/24
 * Describe :
 *
 * @author chendong
 */
public class BaseActivity extends AppCompatActivity {

    @Lookup(value = "testsss", desc = "我是父类") protected String testParent;

}
