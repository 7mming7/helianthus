package com.ha.util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 字符串处理相关工具类.
 * User: shuiqing
 * Date: 2015/2/20
 * Time: 20:46
 * Email: shuiqing301@gmail.com
 * GitHub: https://github.com/ShuiQing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class StringUtils {
    protected StringUtils() {
    }

    public static boolean isEmpty(String text) {
        return org.apache.commons.lang3.StringUtils.isEmpty(text);
    }

    public static boolean isBlank(String text) {
        return org.apache.commons.lang3.StringUtils.isBlank(text);
    }

    public static boolean isNotBlank(String text) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(text);
    }

    public static String capitalize(String text) {
        return org.apache.commons.lang3.StringUtils.capitalize(text);
    }

    public static String substring(String text, int offset, int limit) {
        return org.apache.commons.lang3.StringUtils.substring(text, offset,
                limit);
    }

    public static String substringBefore(String text, String token) {
        return org.apache.commons.lang3.StringUtils
                .substringBefore(text, token);
    }

    public static String substringAfter(String text, String token) {
        return org.apache.commons.lang3.StringUtils.substringAfter(text, token);
    }

    public static String[] splitByWholeSeparator(String text, String separator) {
        return org.apache.commons.lang3.StringUtils.splitByWholeSeparator(text,
                separator);
    }

    public static String join(List list, String separator) {
        return org.apache.commons.lang3.StringUtils.join(list, separator);
    }

    public static String[] split(String str, String separatorChar) {
        return org.apache.commons.lang3.StringUtils.split(str, separatorChar);
    }

    public static String escapeHtml(String text) {
        return StringEscapeUtils.escapeHtml4(text);
    }

    public static String unescapeHtml(String text) {
        return StringEscapeUtils.unescapeHtml4(text);
    }

    public static String escapeXml(String text) {
        return StringEscapeUtils.escapeHtml4(text);
    }

    public static String unescapeXml(String text) {
        return StringEscapeUtils.unescapeXml(text);
    }

    public static String trim(String text) {
        if (text == null) {
            return null;
        }

        text = text.replace("" + ((char) 160), " ");

        text = org.apache.commons.lang3.StringUtils.trim(text);
        text = org.apache.commons.lang3.StringUtils.strip(text, "　");

        return text;
    }

    public static final char SINGLE_QUOTE = '\'';
    public static final char DOUBLE_QUOTE = '\"';

    public static String shellQuote(String s, char quoteCh) {
        StringBuffer buf = new StringBuffer(s.length() + 2);

        buf.append(quoteCh);
        for (int i = 0; i < s.length(); i++) {
            final char ch = s.charAt(i);
            if (ch == quoteCh) {
                buf.append('\\');
            }
            buf.append(ch);
        }
        buf.append(quoteCh);

        return buf.toString();
    }

    /**
     * Use this when you don't want to include Apache Common's string for plugins.
     *
     * @param list
     * @param delimiter
     * @return
     */
    public static String join(Collection<String> list, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        for (String str : list) {
            buffer.append(str);
            buffer.append(delimiter);
        }

        return buffer.toString();
    }

    /**
     * Don't bother to add delimiter for last element
     *
     * @param list
     * @param delimiter
     * @return String - elements in the list separated by delimiter
     */
    public static String join2(Collection<String> list, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        boolean first = true;
        for (String str : list) {
            if (!first) {
                buffer.append(delimiter);
            }
            buffer.append(str);
            first = false;

        }

        return buffer.toString();
    }

    private static final Pattern BROWSWER_PATTERN = Pattern
            .compile(".*Gecko.*|.*AppleWebKit.*|.*Trident.*|.*Chrome.*");

    public static boolean isFromBrowser(String userAgent) {
        if (userAgent == null) {
            return false;
        }

        return BROWSWER_PATTERN.matcher(userAgent).matches();
    }
}