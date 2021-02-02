package com.dwl.common_utils.util;

import io.swagger.annotations.ApiModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密解密操作类
 */
@ApiModel("MD5加密解密操作类")
public class MD5Util {

    /**
     * 加密解密算法
     *
     * @param strSrc
     * @return
     */
    public static String encrypt(String strSrc) {
        try {
            char hexChars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            byte[] bytes = strSrc.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                chars[k++] = hexChars[b >>> 4 & 0xf];
                chars[k++] = hexChars[b & 0xf];
            }
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("MD5加密出错！！+" + e);
        }
    }

    /**
     * 生成一个4位数的随机数，暂时用来替代短信验证码使用
     *
     * @return
     */
    public static int randomNumber() {
        int max = 10000, min = 1;
        int ran2 = (int) (Math.random() * (max - min) + min);
        return ran2;
    }

}
