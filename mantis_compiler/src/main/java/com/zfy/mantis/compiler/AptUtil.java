package com.zfy.mantis.compiler;

/**
 * CreateAt : 2019/6/24
 * Describe :
 *
 * @author chendong
 */
public class AptUtil {


    /**
     * @param str 字符串
     * @return 首字母大写后的字符串
     */
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * @param str 字符串
     * @return 首字母小写后的字符串
     */
    public static String uncapitalize(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}


