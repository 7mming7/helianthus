package com.ha.util;

import com.ha.exception.SystemException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 各种加解密方法的包都不一样，使用方法太过复杂，因此对常用的加解密做适度封装，以降低使用时的复杂度。
 * 本工具类统一使用apache-commons-codec的实现
 * User: shuiqing
 * DateTime: 17/4/26 上午11:46
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class CodecUtils {

    /**
     * 将给定字符串加密为Base64密文字节码。
     * @param src 待加密的原始字符串。
     * @return Base64密文摘要。
     */
    public static final byte[] string2Base64Bytes(String src) {
        byte[] result = null;
        try {
            result = bytes2Base64Bytes(src.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //不可能不支持UTF-8编码。
            throw new RuntimeException("UTf-8 编码不受支持");
        }

        return result;
    }

    /**
     * 将给定字符串加密为Base64密文字符串。
     * @param src 待加密的原始字符串。
     * @return Base64密文摘要。
     */
    public static final String string2Base64String(String src) {
        String result = null;
        if (!StringUtils.isBlank(src)) {
            try {
                result = new String(string2Base64Bytes(src), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                //不可能不支持UTF-8编码。
                throw new RuntimeException("UTf-8 编码不受支持");
            }
        }

        return result;
    }

    /**
     * 将给定字节码加密为Base64密文字节码。
     * @param src 待加密的原始字符串。
     * @return Base64密文摘要。
     */
    public static final byte[] bytes2Base64Bytes(byte[] src) {
        byte[] result = null;
        if (src != null && src.length > 0) {
            result = Base64.encodeBase64(src);
        }

        return result;
    }

    /**
     * 将给定字节码加密为Base64密文字节码。
     * @param src 待加密的原始字符串。
     * @return Base64密文摘要。
     */
    public static final String bytes2Base64String(byte[] src) {
        String result = null;
        try {
            result = new String(bytes2Base64Bytes(src), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //不可能不支持UTF-8编码。
            throw new RuntimeException("UTf-8 编码不受支持");
        }

        return result;
    }

    /**
     * 将给定Base64密文字符串解密字节码。
     * @param src 待加密的原始字符串。
     * @return Base64密文摘要。
     */
    public static final byte[] base64String2Bytes(String src) {
        byte[] result = null;
        if (!StringUtils.isBlank(src)) {
            try {
                result = base64Bytes2Bytes(src.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                //不可能不支持UTF-8编码。
                throw new RuntimeException("UTf-8 编码不受支持");
            }
        }

        return result;
    }

    /**
     * 将给定Base64密文字符串解密为字符串。
     * @param src 待加密的原始字符串。
     * @return Base64密文摘要。
     */
    public static final String base64String2String(String src) {
        String result;
        try {
            if (StringUtils.isBlank(src)) {
                result = "";
            } else {
                result = new String(base64String2Bytes(src), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            //不可能不支持UTF-8编码。
            throw new RuntimeException("UTf-8 编码不受支持");
        }

        return result;
    }

    /**
     * 将给定Base64密文解密为字节码。
     * @param src 待加密的原始字符串。
     * @return Base64密文摘要。
     */
    public static final byte[] base64Bytes2Bytes(byte[] src) {
        return Base64.decodeBase64(src);
    }

    /**
     * 将给定Base64密文解密为字符串。
     * @param src 待加密的原始字符串。
     * @return Base64密文摘要。
     */
    public static final String base64Bytes2String(byte[] src) {
        String result;
        try {
            result = new String(base64Bytes2Bytes(src), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //不可能不支持UTF-8编码。
            throw new RuntimeException("UTf-8 编码不受支持");
        }

        return result;
    }


    /**
     * 将给定字符串加密为MD5密文。
     * @param src 待加密的原始字符串。
     * @return MD5密文摘要。
     */
    public static final byte[] string2Md5Bytes(String src) {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException("不支持MD5加密算法？？");
        } catch (UnsupportedEncodingException e) {
            throw new SystemException("不支持UTF-8字符集？？");
        }
        byte[] result = md.digest();

        return result;
    }

    /**
     * 将给定字符串加密为MD5密文，再将MD5密文使用Base64编码为可见字符。
     * @param src 待加密的原始字符串。
     * @return MD5密文摘要。
     */
    public static final String string2Md5String(String src) {
        String result = CodecUtils.bytes2Base64String(string2Md5Bytes(src));

        return result;
    }

    /**
     * 字节数组转为16进制字符串
     *
     * @param salt
     * @return
     */
    public static String byteToHexString(byte[] src) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < src.length; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }

        return hexString.toString();
    }
}
