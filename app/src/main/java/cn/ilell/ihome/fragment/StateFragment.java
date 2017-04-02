package cn.ilell.ihome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ilell.ihome.LoginActivity;
import cn.ilell.ihome.R;
import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.base.BaseFragment;

/**
 * Created by Monkey on 2015/6/29.
 */
public class StateFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_webonly, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        //检测登录
        //initLogin();
        if (BaseData.logined == false) {
            Intent intent = new Intent();
            ///制定intent要启动的类
            intent.setClass(mContext, LoginActivity.class);
            //启动一个新的Activity
            startActivity(intent);
            getActivity().finish();
        }
        setListener();
        web.loadUrl(ip+"/ihome/z-status.php");
    }

    /*private void initLogin() {
        if (BaseData.logined == false) {
            SharedPreferences data = mContext.getSharedPreferences("IHomeAccount", 0);
            String user = data.getString("user","");
            String pwd = data.getString("password","");
            if (!user.isEmpty() && !pwd.isEmpty()) {
                String url = "http://115.159.127.79/ihome/backdeal_mobile/CheckLogin.php?Username="+
                        user+"&Password="+pwd;
                String result = HttpXmlClient.get(url);
                if (result.charAt(0) == 'l') {
                    web.loadUrl(url);
                    web.loadUrl("http://115.159.127.79/ihome/z-status.php");
                    String resdata[] = result.split(";");
                    BaseData.account_name = resdata[1];
                    BaseData.home_id = resdata[2];
                    BaseData.logined = true;    //已登陆
                }
                else {
                    Intent intent = new Intent();
                    ///制定intent要启动的类
                    intent.setClass(mContext, LoginActivity.class);
                    //启动一个新的Activity
                    startActivity(intent);
                    getActivity().finish();
                }
            }
            else {
                Intent intent = new Intent();
                ///制定intent要启动的类
                intent.setClass(mContext, LoginActivity.class);
                //启动一个新的Activity
                startActivity(intent);
                getActivity().finish();
            }
        }
        else
            web.loadUrl("http://115.159.127.79/ihome/z-status.php");
        //Toast.makeText(mContext, BaseData.logined+"", Toast.LENGTH_SHORT).show();
    }*/

    private void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

           /* case R.id.state_button:

                //do something

                break;
*/
           /* case R.id. myButton2:

                //do something

                break;*/

        }
    }
}
