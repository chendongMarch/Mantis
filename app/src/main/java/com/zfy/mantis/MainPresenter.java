package com.zfy.mantis;

import com.zfy.mantis.annotation.LookupArgs;
import com.zfy.mantis.api.Mantis;
import com.zfy.mantis.model.WxInfo;

/**
 * CreateAt : 2019/1/10
 * Describe :
 *
 * @author chendong
 */

public class MainPresenter {

    @LookupArgs(value = "test1", desc = "我是名字")   byte  test1;
    @LookupArgs(value = "test2", required = true) short test2;

    @LookupArgs("test3")  int       test3;
    @LookupArgs("test4")  long      test4;
    @LookupArgs("test5")  float     test5;
    @LookupArgs("test6")  double    test6;
    @LookupArgs("test7")  boolean   test7;
    @LookupArgs("test8")  char      test8;
    @LookupArgs("test9")  String    test9;
    @LookupArgs("test10") WxInfo    test10;
    @LookupArgs("test11") MyService test11;

    public MainActivity mMainActivity;

    public MainPresenter(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void init() {
        Mantis.getInst().injectArgs(this);
    }
}
