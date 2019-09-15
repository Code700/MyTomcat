package com.wlt.tomcat.utils;

/**
 * @author 王立腾
 * @date 2019-09-15 12:49
 */
public class StringUtils {

    public static boolean isValid(String string) {
        if (null != string && string.length() > 0)
            return true;
        return false;
    }

}
