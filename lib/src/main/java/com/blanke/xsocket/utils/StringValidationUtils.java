package com.blanke.xsocket.utils;

/**
 * 输入检查
 * 正则检查String是否符合某些格式
 */
public class StringValidationUtils {

    public final static String RegexIP = "^(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
    public final static String RegexPort = "^6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{0,3}$";
    public final static String RegexAllChinese = "^[\\u4e00-\\u9fa5]*$";
    public final static String RegexPhoneNumber = "^(((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8})|((\\d{3,4}-)?\\d{7,8}(-\\d{1,4})?)$";
    public final static String RegexEmail = "w+([-+.]w+)*@w+([-.]w+)*.w+([-.]w+)*";

    public static boolean validateRegex(String string, String regex) {
        if (string == null) {
            return false;
        }
        return string.matches(regex);
    }
}