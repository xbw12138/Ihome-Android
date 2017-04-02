package cn.ilell.ihome.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.ilell.ihome.R;
import cn.ilell.ihome.view.StatusBarCompat;

/**
 * Created by lhc35 on 2016/5/6.
 */
public class BaseFamilyActivity extends Activity{
    protected Context mContext;

    protected Button btn;
    protected EditText edit;
    protected WebView web;

    protected String backUrl;   //后台响应地址

    protected void INIT(String title,String btn_text,String edit_hint) {
        setContentView(R.layout.activity_family);
        mContext = this;
        //设置状态栏的颜色
        StatusBarCompat.compat(this, getResources().getColor(R.color.main_blue_light));
        TextView title_name = (TextView) findViewById(R.id.title_bar_name);
        title_name.setText(title);	//修改页面标题
        btn = (Button) findViewById(R.id.family_button);
        edit = (EditText) findViewById(R.id.family_editText);
        web = (WebView) findViewById(R.id.family_webView);
        btn.setText(btn_text);
        edit.setHint(edit_hint);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        //打开网页时不调用系统浏览器， 而是在本WebView中显示
        web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }   //初始化

    public void onTitleBarClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_back_btn:
                this.finish();
                break;
        }
    }

}
