package com.zfy.mantis.annotation;

/**
 * CreateAt : 2019/6/27
 * Describe :
 *
 * @author chendong
 */
public class LookupOpts {

    // 被注入的对象
    public Object target;

    // 注入的 key
    public String   key;
    // 本次注入的类型的分组，使用分组将注入分成多个模块
    public int      group;
    // 注入的 class
    public Class<?> clazz;

    // 属性的 class
    public Class  fieldClazz;
    // 属性的名字
    public String fieldName;

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setAnnotation(String key, int group, Class clazz) {

        this.key = key;
        this.group = group;
        this.clazz = null;

        if (void.class != clazz) {
            this.clazz = clazz;
        }

    }

    public void setField(Class fieldClazz, String fieldName) {
        this.fieldClazz = fieldClazz;
        this.fieldName = fieldName;
    }

}
