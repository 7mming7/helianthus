package http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;

/**
 * User: shuiqing
 * DateTime: 17/5/8 上午11:48
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HttpClientTest {

    public static String callInterface(){
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet("http://localhost:8080/ha-admin/httpService/req/siteAccount/2");

        String entityStr = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            System.out.println("statusCode:"+statusCode);
            entityStr = EntityUtils.toString(entity);
            System.out.println("响应返回内容:"+entityStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return entityStr;
    }

    //post提交调用方法
    public static String callAddUserInfo() throws UnsupportedEncodingException {


        return null;
    }

    public static void main(String[] args) throws Exception {
        String callInterface = callInterface();
        System.out.println("调用成功："+callInterface);
        //callAddUserInfo();

    }
}
