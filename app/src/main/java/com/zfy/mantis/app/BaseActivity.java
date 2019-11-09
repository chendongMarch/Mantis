package com.zfy.mantis.app;

import android.support.v7.app.AppCompatActivity;

import com.zfy.mantis.annotation.Lookup;

import javax.security.auth.Destroyable;

/**
 * CreateAt : 2019/6/24
 * Describe :
 *
 * @author chendong
 */
public abstract class BaseActivity<D extends String> extends AppCompatActivity implements Destroyable {

    @Lookup(value = "key_testParent", desc = "我是父类") protected String testParent;

    D data;
}
