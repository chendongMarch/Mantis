package com.zfy.mantis.api.provider;

/**
 * CreateAt : 2019/1/29
 * Describe :
 *
 * @author chendong
 */
public interface IObjProvider {

    Object getObject(String key, Class<?> clazz);
}
