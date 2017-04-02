package cn.ilell.ihome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import cn.ilell.ihome.base.BaseActivity;
import cn.ilell.ihome.fragment.ModeFragment;
import cn.ilell.ihome.fragment.ScheduleFragment;
import cn.ilell.ihome.fragment.StateFragment;

public class StateActivity extends BaseActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //2016/11/23 扫码弹窗
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化各种控件
        initViews();

        // 初始化mTitles、mFragments等ViewPager需要的数据
        //这里的数据都是模拟出来了，自己手动生成的，在项目中需要从网络获取数据
        initData();

        // 对各种控件进行设置、适配、填充数据
        configViews();

        //与后台服务捆绑
        bindMsgService();

        mContext = this;
        mClass = StateActivity.class;

    }

    @Override
    public void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }

    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.state_tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();

        Bundle stateBundle = new Bundle();
        stateBundle.putInt("flag", 0);
        StateFragment stateFragment = new StateFragment();
        stateFragment.setArguments(stateBundle);
        mFragments.add(0, stateFragment);

        Bundle modeBundle = new Bundle();
        modeBundle.putInt("flag", 1);
        ModeFragment modeFragment = new ModeFragment();
        modeFragment.setArguments(modeBundle);
        mFragments.add(1, modeFragment);

        Bundle sheduleBundle = new Bundle();
        sheduleBundle.putInt("flag", 2);
        ScheduleFragment sheduleFragment = new ScheduleFragment();
        sheduleFragment.setArguments(sheduleBundle);
        mFragments.add(2, sheduleFragment);
    }
    //2016/11/23 扫码回调
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null)
            return;
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == 10004) {
                String result_value = intent.getStringExtra("describe");
                if (result_value != null)
                    showScanDialog(result_value);
                else
                    showScanDialog("扫码失败");
                return;
            }
        }
    }

    //2016/11/23 扫码回调弹窗
    private void showScanDialog(String info){
        builder.setTitle("添加节点");
        builder.setMessage(info);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //添加向服务器post节点
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
    }*/


}
