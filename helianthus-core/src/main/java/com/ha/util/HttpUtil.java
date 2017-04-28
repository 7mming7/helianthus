package com.ha.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * http 请求工具
 * User: shuiqing
 * DateTime: 17/4/27 下午5:05
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public final class HttpUtil {

    private static Logger LOG = LoggerFactory.getLogger(HttpUtil.class);

    private HttpUtil() {
    }

    public static final String httpClientPost(String url) {
        String result = "";
        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        try {
            client.executeMethod(getMethod);
            result = getMethod.getResponseBodyAsString();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            getMethod.releaseConnection();
        }
        return result;
    }

    public static final String httpClientPost(String url, ArrayList<NameValuePair> list) {
        String result = "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        try {
            NameValuePair[] params = new NameValuePair[list.size()];
            for (int i = 0; i < list.size(); i++) {
                params[i] = list.get(i);
            }
            postMethod.addParameters(params);
            client.executeMethod(postMethod);
            result = postMethod.getResponseBodyAsString();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            postMethod.releaseConnection();
        }
        return result;
    }
}
