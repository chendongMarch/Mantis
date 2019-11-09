package com.zfy.mantis.annotation;

/**
 * CreateAt : 2019/6/27
 * Describe :
 *
 * @author chendong
 */
public class LookupOpts {

    // 被注入的对象的宿主类
    public Object target;

    // 注解声明，注入的 key
    public String   key;
    // 注解声明，注入的 key，支持 int 类型
    public int      numKey;
    // 注解声明，附带参数
    public int      extra;
    // 注解声明，将注入分成多个组，可以只指定注入某个组的数据
    public int      group;
    // 注解声明，注入的 class
    public Class<?> clazz;

    // 属性的 class
    public Class  fieldClazz;
    // 属性的名字
    public String fieldName;

    public void set(int numKey, String key, Class clazz, String fieldName, Class fieldClazz, int extra) {
        this.key = key;
        this.numKey = numKey;
        this.clazz = void.class.equals(clazz) ? null : clazz;
        this.fieldName = fieldName;
        this.fieldClazz = fieldClazz;
        this.extra = extra;
    }

    public void clear() {
        target = null;
        key = Lookup.AUTO;
        numKey = Lookup.AUTO_NUM;
        group = Lookup.DEF_GROUP;
        clazz = null;
        fieldClazz = null;
        fieldName = null;
    }
}
