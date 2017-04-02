package cn.ilell.ihome.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ilell.ihome.base.BaseData;

/**
 * Created by lhc35 on 2016/5/1.
 */
public class HttpUtils {

    public String HttpPost(String usr,String pwd) {
        String path="http://"+ BaseData.IP+"/ihome/backdeal/CheckLogin.php";

        Map<String, String> params = new HashMap<String, String>();
        params.put("Username", usr);
        params.put("Password", pwd);

        String result=sendHttpClientPost(path, params, "utf-8");
        return result;
    }
    /**
     *
     * @param path
     * @param map
     * @param encode
     * @return
     */

    public static String sendHttpClientPost(String path,
                                            Map<String, String> map, String encode) {
        List<NameValuePair> list=new ArrayList<NameValuePair>();
        for(Map.Entry<String, String> entry:map.entrySet()){

            list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));

        }
        try {
            //实现将请求中的参数封装到请求参数中，请求体中
            UrlEncodedFormEntity entity=new UrlEncodedFormEntity(list,encode);
            //使用post方式提交
            HttpPost httpPost=new HttpPost(path);
            httpPost.setEntity(entity);
            //指定post方式提交数据
            DefaultHttpClient client =new DefaultHttpClient();

            HttpResponse httpResponse=client.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()==200){
                return changeInputStream(httpResponse.getEntity().getContent(),encode);

            }

        } catch (Exception e) {
            // TODO: handle exception
        }


        return "";
    }
    /**
     *
     * @param inputStream
     * @param encode
     * @return
     */
    private static String changeInputStream(InputStream inputStream,
                                            String encode) {
        // TODO Auto-generated method stub

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] date = new byte[1024];
        String result = "";
        try {

            while ((len = inputStream.read(date)) != -1) {

                outputStream.write(date, 0, len);
            }
            result = new String(outputStream.toByteArray(), encode);

            return result;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
