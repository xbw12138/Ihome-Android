package cn.ilell.ihome;

import android.os.Bundle;

import java.util.ArrayList;

import cn.ilell.ihome.base.BaseActivity;
import cn.ilell.ihome.fragment.BrightFragment;
import cn.ilell.ihome.fragment.HumidFragment;
import cn.ilell.ihome.fragment.RecordFragment;
import cn.ilell.ihome.fragment.TempFragment;

public class HistoryActivity extends BaseActivity {

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
        mClass = HistoryActivity.class;

        //mFloatingActionButton.setVisibility(View.INVISIBLE);
    }

    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.history_tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();

        Bundle tempBundle = new Bundle();
        tempBundle.putInt("flag", 0);
        TempFragment tempFragment = new TempFragment();
        tempFragment.setArguments(tempBundle);
        mFragments.add(0, tempFragment);

        Bundle humidBundle = new Bundle();
        humidBundle.putInt("flag", 1);
        HumidFragment humidFragment = new HumidFragment();
        humidFragment.setArguments(humidBundle);
        mFragments.add(1, humidFragment);

        Bundle brightBundle = new Bundle();
        brightBundle.putInt("flag", 2);
        BrightFragment brightFragment = new BrightFragment();
        brightFragment.setArguments(brightBundle);
        mFragments.add(2, brightFragment);

        Bundle recordBundle = new Bundle();
        recordBundle.putInt("flag", 3);
        RecordFragment recordFragment = new RecordFragment();
        recordFragment.setArguments(recordBundle);
        mFragments.add(3, recordFragment);

    }


    @Override
    public void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }


}
