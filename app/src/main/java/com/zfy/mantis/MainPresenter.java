package com.zfy.mantis;

import com.zfy.mantis.annotation.LookUp;
import com.zfy.mantis.api.Mantis;
import com.zfy.mantis.model.WxInfo;

/**
 * CreateAt : 2019/1/10
 * Describe :
 *
 * @author chendong
 */

public class MainPresenter {

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

    public MainActivity mMainActivity;

    public MainPresenter(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void init() {
        Mantis.getInst().inject(this);
    }
}
