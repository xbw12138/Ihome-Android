package cn.ilell.ihome.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lhc35 on 2016/5/4.
 */
public class HttpXmlClient {
    public static DefaultHttpClient httpclient;;
    //private static Logger log = Logger.getLogger(HttpXmlClient.class);

    public static String post(String url, Map<String, String> params) {
        //httpclient = new DefaultHttpClient();
        HttpPost post;
        String body = null;

        //log.info("create httppost:" + url);
        post = postForm(url, params);

        body = invoke(post);

        //httpclient.getConnectionManager().shutdown();

        return body;
    }

    public static String get(String url) {
        /*httpclient = new DefaultHttpClient();
        String body = null;

        //log.info("create httppost:" + url);

        HttpGet get = new HttpGet(url);


        body = invoke(get);

        //httpclient.getConnectionManager().shutdown();*/
        try{
            //httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode()==200){
                String result = EntityUtils.toString(httpResponse.getEntity());
                return result;
            }
        }catch(Exception e){}
        return "failed";
    }


    private static String invoke(HttpUriRequest httpost) {

        HttpResponse response = sendRequest(httpost);
        String body = paseResponse(response);

        return body;
    }

    private static String paseResponse(HttpResponse response) {
        //log.info("get response from http server..");
        HttpEntity entity = response.getEntity();

        //log.info("response status: " + response.getStatusLine());
        String charset = EntityUtils.getContentCharSet(entity);
        //log.info(charset);

        String body = null;
        try {
            body = EntityUtils.toString(entity);
            //log.info(body);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

    private static HttpResponse sendRequest(HttpUriRequest httpost) {
        //log.info("execute post...");
        HttpResponse response = null;

        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static HttpPost postForm(String url, Map<String, String> params){

        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();

        Set<String> keySet = params.keySet();
        for(String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }

        try {
            //log.info("set utf-8 form entity to httppost");
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }
}


