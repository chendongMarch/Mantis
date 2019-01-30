package com.zfy.mantis.api.provider;

/**
 * CreateAt : 2019/1/29
 * Describe :
 *
 * @author chendong
 */
public interface ProviderCallback {

    // 获取数据读取
    IDataProvider getDataProvider(Object target);

    // 获取对象注入
    IObjProvider getObjProvider(Object target, IDataProvider dataProvider);
}
