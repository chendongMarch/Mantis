package com.zfy.mantis;

import com.zfy.mantis.annotation.Lookup;
import com.zfy.mantis.api.Mantis;
import com.zfy.mantis.model.MyService;
import com.zfy.mantis.model.WxInfo;

/**
 * CreateAt : 2019/1/10
 * Describe :
 *
 * @author chendong
 */

public class MainPresenter {

    @Lookup(value = "test1", desc = "我是名字")   byte  test1;
    @Lookup(value = "test2", required = true) short test2;

    @Lookup("test3")  int       test3;
    @Lookup("test4")  long      test4;
    @Lookup("test5")  float     test5;
    @Lookup("test6")  double    test6;
    @Lookup("test7")  boolean   test7;
    @Lookup("test8")  char      test8;
    @Lookup("test9")  String    test9;
    @Lookup("test10") WxInfo    test10;
    @Lookup("test11") MyService test11;

    public MainActivity mMainActivity;

    public MainPresenter(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void init() {
        Mantis.inject(this);
    }
}
