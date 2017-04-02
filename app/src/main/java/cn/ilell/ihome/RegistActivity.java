package cn.ilell.ihome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.HashMap;
import java.util.Map;

import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.base.BaseSubPageActivity;
import cn.ilell.ihome.utils.HttpXmlClient;

/**
 * Created by lhc35 on 2016/5/1.
 */
public class RegistActivity extends BaseSubPageActivity {
    private HttpClient http;
    private Button btn_regedit,
            btn_verify;
    private EditText text_homeid,
            text_homepwd,
            text_mail,
            text_verify,
            text_name,
            text_phone,
            text_pwd,
            text_pwdenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_regist, "新用户注册");

        initViewAndListener();
        //HttpUtils http = new HttpUtils();
        //Toast.makeText(LoginActivity.this,http.HttpPost("admin","123"), Toast.LENGTH_SHORT).show();
    }

    private void initViewAndListener() {
        http = new DefaultHttpClient();

        btn_regedit = (Button) findViewById(R.id.regist_btn_regist);
        btn_verify = (Button) findViewById(R.id.regist_btn_verify);

        text_homeid = (EditText) findViewById(R.id.regist_text_homeid);
        text_homepwd = (EditText) findViewById(R.id.regist_text_homepwd);
        text_mail = (EditText) findViewById(R.id.regist_text_mail);
        text_verify = (EditText) findViewById(R.id.regist_text_verify);
        text_name = (EditText) findViewById(R.id.regist_text_name);
        text_phone = (EditText) findViewById(R.id.regist_text_phone);
        text_pwd = (EditText) findViewById(R.id.regist_text_pwd);
        text_pwdenter = (EditText) findViewById(R.id.regist_text_pwdenter);

        btn_regedit.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist_btn_verify:
                String str_mail = text_mail.getText().toString();
                if (str_mail.isEmpty()) {
                    Toast.makeText(RegistActivity.this, "请填写登陆邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> params_temp = new HashMap<String, String>();
                params_temp.put("email", str_mail);
                String rul = HttpXmlClient.post("http://"+ BaseData.IP+"/ihome/backdeal/Mailer.php", params_temp);
                Toast.makeText(RegistActivity.this, rul, Toast.LENGTH_SHORT).show();
                break;
            // FloatingActionButton的点击事件
            case R.id.regist_btn_regist:
                String homeid = text_homeid.getText().toString(),
                        homepwd = text_homepwd.getText().toString(),
                        mail = text_mail.getText().toString(),
                        verify = text_verify.getText().toString(),
                        name = text_name.getText().toString(),
                        phone = text_phone.getText().toString(),
                        pwd = text_pwd.getText().toString(),
                        pwdenter = text_pwdenter.getText().toString();

                if (homeid.isEmpty()) {
                    Toast.makeText(RegistActivity.this, "HomeID不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (homepwd.isEmpty()) {
                    Toast.makeText(RegistActivity.this, "家庭密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mail.isEmpty()) {
                    Toast.makeText(RegistActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (verify.isEmpty()) {
                    Toast.makeText(RegistActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.isEmpty()) {
                    Toast.makeText(RegistActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.isEmpty()) {
                    Toast.makeText(RegistActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwd.equals(pwdenter)) {
                    Toast.makeText(RegistActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("homeid", homeid);
                params.put("homeid_passwd", homepwd);
                params.put("email", mail);
                params.put("verify", verify);
                params.put("username", name);
                params.put("phonenum", phone);
                params.put("passwd", pwd);

                String xml = HttpXmlClient.post("http://"+ BaseData.IP+"/ihome/backdeal/SaveNewUser.php", params);
                Toast.makeText(RegistActivity.this, xml, Toast.LENGTH_SHORT).show();
                if (xml.equals("注册成功")) {
                    SharedPreferences settings = getSharedPreferences("IHomeAccount", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("user", mail);
                    editor.putString("password", pwd);
                    editor.commit();

                    Intent intent = new Intent();
                    ///制定intent要启动的类
                    intent.setClass(RegistActivity.this, LoginActivity.class);
                    //启动一个新的Activity
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }
}
