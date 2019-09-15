package com.wlt.tomcat.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author 王立腾
 * @date 2019-09-15 13:08
 */
public class URLUtils {

    public static String enCoderToUtf(String str) {
        return URLDecoder.decode(str);
    }

    public static String utfToEnCoderToUtf(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
