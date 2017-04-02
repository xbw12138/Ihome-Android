package cn.ilell.ihome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import cn.ilell.ihome.R;
import cn.ilell.ihome.base.BaseFragment;

/**
 * Created by Monkey on 2015/6/29.
 */
public class IndoorFragment extends BaseFragment{

    private Switch switch_all = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_indoor, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewThis();
        setListener();

    }

    /*public void onPause() {
        audioClient.stop();
    }*/
    /**
     * 调用finish方法时，这方法将被激发
     * 设置输入流为空，调用父类的onDestroy销毁资源
     */

    protected void initViewThis() {
        initView();
        switch_all = (Switch) mView.findViewById(R.id.indoor_switch);
    }

    private void setListener() {
        switch_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {//打开
                    web.loadUrl("file:///android_asset/indoormonitor.html");
                } else {// 关闭
                    web.loadUrl("about:blank");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

           /* case R.id.outdoor_btn_stop:
                break;*/

        }
    }
}
