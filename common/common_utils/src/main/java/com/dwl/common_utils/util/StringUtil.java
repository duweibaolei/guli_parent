package com.dwl.common_utils.util;

import io.swagger.annotations.ApiModel;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作类
 */
@ApiModel("字符串操作类")
public class StringUtil {

    /**
     * 替换字符串中的变量
     *
     * @param template
     * @param map
     * @return
     * @throws Exception
     */
    public static String replaceVariable(String template, Map<String, String> map) throws Exception {
        Pattern regex = Pattern.compile("\\{(.*?)\\}");
        Matcher regexMatcher = regex.matcher(template);
        while (regexMatcher.find()) {
            String key = regexMatcher.group(1);
            String toReplace = regexMatcher.group(0);
            String value = map.get(key);
            if (value != null)
                template = template.replace(toReplace, value);
            else {
                throw new Exception(new StringBuilder().append("没有找到[")
                        .append(key).append("]对应的变量值，请检查表变量配置!").toString());
            }
        }
        return template;
    }

    /**
     * 判断字符串是否在是否包函内容
     *
     * @param content
     * @param begin
     * @param end
     * @return
     */
    public static boolean isExist(String content, String begin, String end) {
        String tmp = content.toLowerCase();
        begin = begin.toLowerCase();
        end = end.toLowerCase();
        int beginIndex = tmp.indexOf(begin);
        int endIndex = tmp.indexOf(end);
        if ((beginIndex != -1) && (endIndex != -1) && (beginIndex < endIndex))
            return true;
        return false;
    }

    /**
     * 去掉前面的指定字符
     *
     * @param toTrim
     * @param trimStr
     * @return
     */
    public static String trimPrefix(String toTrim, String trimStr) {
        while (toTrim.startsWith(trimStr)) {
            toTrim = toTrim.substring(trimStr.length());
        }
        return toTrim;
    }

    /**
     * 删除后面指定的字符
     *
     * @param toTrim
     * @param trimStr
     * @return
     */
    public static String trimSufffix(String toTrim, String trimStr) {
        while (toTrim.endsWith(trimStr)) {
            toTrim = toTrim.substring(0, toTrim.length() - trimStr.length());
        }
        return toTrim;
    }

    /**
     * 删除指定的字符
     *
     * @param toTrim
     * @param trimStr
     * @return
     */
    public static String trim(String toTrim, String trimStr) {
        return trimSufffix(trimPrefix(toTrim, trimStr), trimStr);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null)
            return true;
        if (str.trim().equals(""))
            return true;
        return false;
    }

    /**
     * 判断字符串非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 截取字符串 中文为两个字节，英文为一个字节。 两个英文为一个中文。
     *
     * @param str
     * @param len
     * @return
     */
    public static String subString(String str, int len) {
        int strLen = str.length();
        if (strLen < len)
            return str;
        char[] chars = str.toCharArray();
        int cnLen = len * 2;
        String tmp = "";
        int iLen = 0;
        for (int i = 0; i < chars.length; i++) {
            int iChar = (int) chars[i];
            if (iChar <= 128)
                iLen = iLen + 1;
            else
                iLen = iLen + 2;
            if (iLen >= cnLen)
                break;
            tmp += String.valueOf(chars[i]);
        }
        return tmp;
    }

    /**
     * 在字符串后面添加对应的字符
     *
     * @param stringList
     * @param string
     * @return
     */
    public static String join(List<String> stringList, String string) {
        return org.apache.commons.lang.StringUtils.join(stringList.toArray(), string);
    }
}
