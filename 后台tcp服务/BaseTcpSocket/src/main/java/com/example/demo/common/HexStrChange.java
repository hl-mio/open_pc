package com.example.demo.common;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class HexStrChange {

    /**
     * 将普通字符串转换为16进制字符串
     * @param str 普通字符串
     * @param lowerCase 转换后的字母为是否为小写  可不传默认为true
     * @param charset 编码格式  可不传默认为Charset.defaultCharset()
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String str2HexStr(String str,boolean lowerCase,String charset) throws UnsupportedEncodingException {
        return Hex.encodeHexString(str.getBytes(charset),lowerCase);
    }


    /**
     * 将16进制字符串转换为普通字符串
     * @param hexStr 16进制字符串
     * @param charset 编码格式 可不传默认为Charset.defaultCharset()
     * @return
     * @throws DecoderException
     * @throws UnsupportedEncodingException
     */
    public static String hexStr2Str(String hexStr,String charset) throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = Hex.decodeHex(hexStr);
        return new String(bytes,charset);
    }


    /**
     * 将16进制字符串转换为byte数组
     * @param hexItr 16进制字符串
     * @return
     */
    public static byte[] hexItr2Arr(String hexItr) throws DecoderException {
        return Hex.decodeHex(hexItr);
    }


    /**
     * byte数组转化为16进制字符串
     * @param arr 数组
     * @param lowerCase 转换后的字母为是否为小写 可不传默认为true
     * @return
     */
    public static String arr2HexStr(byte[] arr,boolean lowerCase){
        return Hex.encodeHexString(arr, lowerCase);
    }


    /**
     * 将普通字符串转换为指定格式的byte数组
     * @param str 普通字符串
     * @param charset 编码格式 可不传默认为Charset.defaultCharset()
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] str2Arr(String str,String charset) throws UnsupportedEncodingException {
        return str.getBytes(charset);
    }

    /**
     * 将byte数组转换为指定编码格式的普通字符串
     * @param arr byte数组
     * @param charset 编码格式 可不传默认为Charset.defaultCharset()
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String arr2Str(byte[] arr,String charset) throws UnsupportedEncodingException {
        return new String(arr,charset);
    }

}
