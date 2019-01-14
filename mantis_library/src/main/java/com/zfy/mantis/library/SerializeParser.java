package com.zfy.mantis.library;

import java.util.List;
import java.util.Map;

/**
 * CreateAt : 2019/1/14
 * Describe :
 *
 * @author chendong
 */
public interface SerializeParser {

    // 序列化成字符串
    String toJson(Object object);

    // 反序列化成对象
    <T> T toObj(String json, Class<T> clazz);

    // 反序列化成列表
    <T> List<T> toList(String key, Class<T> clazz);

    // 反序列化成 map
    <K, V> Map<K, V> toMap(String key, Class<K> kClazz, Class<V> vClazz);
}
