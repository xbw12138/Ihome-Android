package cn.ilell.ihome;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.http.impl.client.DefaultHttpClient;

import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.service.MsgService;
import cn.ilell.ihome.service.MyService;
import cn.ilell.ihome.service.OnProgressListener;
import cn.ilell.ihome.utils.HttpXmlClient;
import cn.ilell.ihome.view.StatusBarCompat;

/**
 * Created by lhc35 on 2016/5/1.
 */
public class WelcomeActivity extends Activity {
    private WebView web;
    String user,pwd,url;
    Context mContext;
    //服务
    private MsgService msgService;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            msgService = ((MsgService.MsgBinder) service).getService();

            //注册回调接口来接收下载进度的变化
            msgService.setOnProgressListener(new OnProgressListener() {
                @Override
                public void onProgress(String recvMsg) {
                    //SnackbarUtil.show(findViewById(R.id.main_floatingactionbutton), recvMsg, 0);
                    /*TextView textView = (TextView) findViewById(R.id.main_textView);
                    textView.setText(recvMsg);*/
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //设置状态栏的颜色
        StatusBarCompat.compat(this, getResources().getColor(R.color.main_white));
        mContext = this;
        initViewAndListener();
        Thread t = new Thread(new Runnable(){
            public void run(){
                try {
                    SharedPreferences data_local = getSharedPreferences("IHomeAccount", 0);
                    user = data_local.getString("user","");
                    pwd = data_local.getString("password","");
                    url = "http://"+ BaseData.IP+"/ihome/backdeal_mobile/CheckLogin.php?Username="+
                            user+"&Password="+pwd;
                    HttpXmlClient.httpclient = new DefaultHttpClient(); //初始化http
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (initData()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            web.loadUrl(url);
                        }
                    });

                }
            }
        });
        t.start();

    }

    private boolean initData() {
        if (!user.isEmpty() && !pwd.isEmpty()) {
            String result = HttpXmlClient.get(url);
            if (result.charAt(0) == 'l') {
                bindMsgService();
                String data[] = result.split(";");
                BaseData.account_name = data[1];
                BaseData.home_id = data[2];
                BaseData.logined = true;    //已登陆
                return true;
            }
            else
            {
                WelcomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WelcomeActivity.this, "网络连接失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        /*Intent intent = new Intent();
        ///制定intent要启动的类
        intent.setClass(WelcomeActivity.this, LoginActivity.class);
        //启动一个新的Activity
        startActivity(intent);
        finish();*/
        Intent intent = new Intent();
        //制定intent要启动的类
        intent.setClass(WelcomeActivity.this, StateActivity.class);
        //启动一个新的Activity
        startActivity(intent);
        //关闭当前的
        finish();
        return false;
    }

    private void initViewAndListener() {
        web = (WebView) findViewById(R.id.webView);
        web.setVisibility(View.INVISIBLE);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient(){
            //打开网页时不调用系统浏览器， 而是在本WebView中显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            //网页加载完成时
            @Override
            public void onPageFinished(WebView view,String url)
            {
                Toast.makeText(WelcomeActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                //制定intent要启动的类
                intent.setClass(WelcomeActivity.this, StateActivity.class);
                //启动一个新的Activity
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in,
                        R.anim.push_up_out);
                //关闭当前的
                finish();
            }
        });
    }

    private void bindMsgService() {
        //绑定Service
        Intent intent = new Intent();
        intent.setAction("cn.msgservice");
        intent.setPackage(getPackageName());
        bindService(intent, conn, BIND_AUTO_CREATE);
    }
}
