package com.zfy.mantis;

import com.zfy.mantis.annotation.KVDao;
import com.zfy.mantis.model.WxInfo;

import java.util.List;
import java.util.Map;

/**
 * CreateAt : 2019/1/11
 * Describe :
 *
 * @author chendong
 */
@KVDao
public class UserInfo {

    private int                 age;
    private String              name;
    private WxInfo              wxInfo;
    private List<WxInfo>        wxInfoList;
    private Map<String, WxInfo> wxInfoMap;
}
