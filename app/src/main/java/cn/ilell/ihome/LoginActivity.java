package cn.ilell.ihome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.base.BaseSubPageActivity;
import cn.ilell.ihome.utils.HttpXmlClient;

/**
 * Created by lhc35 on 2016/5/1.
 */
public class LoginActivity extends BaseSubPageActivity {
    private WebView web;
    private HttpClient http;
    private Button btn_login;
    private Button btn_regedit;
    private EditText text_user;
    private EditText text_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_login,"登陆");

        initViewAndListener();

        initData();
    }

    private void initData() {
        SharedPreferences data = getSharedPreferences("IHomeAccount", 0);
        String user = data.getString("user","");
        String pwd = data.getString("password","");
        if (!user.isEmpty())
            text_user.setText(user);
        if (!pwd.isEmpty())
            text_pwd.setText(pwd);
    }

    private void initViewAndListener() {
        web = (WebView) findViewById(R.id.webView);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        http = new DefaultHttpClient();

        btn_login = (Button) findViewById(R.id.login_btn_login);
        btn_regedit = (Button) findViewById(R.id.login_btn_regist);
        text_user = (EditText) findViewById(R.id.login_text_user);
        text_pwd = (EditText) findViewById(R.id.login_text_pwd);

        btn_login.setOnClickListener(this);
        btn_regedit.setOnClickListener(this);
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

                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                String user = text_user.getText().toString();
                String pwd = text_pwd.getText().toString();
                if (!user.isEmpty() && !pwd.isEmpty()) {
                    String url = "http://"+ BaseData.IP+"/ihome/backdeal_mobile/CheckLogin.php?Username="+
                            user+"&Password="+pwd;
                   // HttpUtils httpUtils = new HttpUtils();
                   // String result = httpUtils.HttpPost(user,pwd);
                    String result = HttpXmlClient.get(url);
                    if (result.charAt(0) == 'l') {
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        web.loadUrl(url);
                        String data[] = result.split(";");
                        BaseData.account_name = data[1];
                        BaseData.home_id = data[2];
                        BaseData.logined = true;    //已登陆
                        BaseData.user_id=user;
                        SharedPreferences settings = getSharedPreferences("IHomeAccount", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("user",user);
                        editor.putString("password",pwd);
                        editor.commit();

                        Intent intent = new Intent();
                        ///制定intent要启动的类
                        intent.setClass(LoginActivity.this, StateActivity.class);
                        //启动一个新的Activity
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(LoginActivity.this, "请输入用户名密码", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onTitleBarClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_back_btn:
                if (BaseData.logined == true) {
                    Intent intent = new Intent();
                    ///制定intent要启动的类
                    intent.setClass(LoginActivity.this, StateActivity.class);
                    //启动一个新的Activity
                    startActivity(intent);
                }
                this.finish();
                break;
        }
    }
}
