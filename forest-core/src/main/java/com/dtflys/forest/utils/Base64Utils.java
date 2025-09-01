package com.dtflys.forest.utils;


import java.util.Base64;

/**
 * Base64字符串与字节码转换工具
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2020-08-04 19:05
 */

public class Base64Utils {

	private final static Base64.Decoder DECODER = Base64.getDecoder();
	private final static Base64.Encoder ENCODER = Base64.getEncoder();


    /**
     * 对字符串进行 Base64 编码，并返回编码后的字符串
     * @param str 原字符串
     * @return Base64 编码后的字符串
     */
    public static String encode(String str) {
		return ENCODER.encodeToString(str.getBytes());
    }

    /**
     * 对字符串进行 Base64 编码，并返回编码后的字节数组
     * @param str 原字符串
     * @return Base64 编码后的字节数组
     */
    public static byte[] encodeToByteArray(String str) {
        return ENCODER.encode(str.getBytes());
    }

    /**
     * 对 Base64 编码后的字符串进行解码，并返回解码后的字节数组
     * @param str Base64 编码后的字符串
     * @return Base64 解码后的字节数组
     */
    public static byte[] decode(String str) {
		return DECODER.decode(str);
    }

}
