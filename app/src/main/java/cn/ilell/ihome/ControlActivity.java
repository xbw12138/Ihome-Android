package cn.ilell.ihome;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import cn.ilell.ihome.base.BaseActivity;
import cn.ilell.ihome.fragment.BedroomFragment;
import cn.ilell.ihome.fragment.KitchenFragment;
import cn.ilell.ihome.fragment.ParlorFragment;
import cn.ilell.ihome.fragment.ToiletFragment;
import cn.ilell.ihome.service.YyService;
import cn.ilell.ihome.service.YyService.MyBinder;
import cn.ilell.ihome.service.YyService.YyResultListener;

public class ControlActivity extends BaseActivity {


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
        mClass = ControlActivity.class;
    }

    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.control_tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        Bundle parlorBundle = new Bundle();
        parlorBundle.putInt("flag", 0);
        ParlorFragment parlorFragment = new ParlorFragment();
        parlorFragment.setArguments(parlorBundle);
        mFragments.add(0, parlorFragment);

        Bundle kitchenBundle = new Bundle();
        kitchenBundle.putInt("flag", 1);
        KitchenFragment kitchenFragment = new KitchenFragment();
        kitchenFragment.setArguments(kitchenBundle);
        mFragments.add(1, kitchenFragment);

        Bundle toiletBundle = new Bundle();
        toiletBundle.putInt("flag", 2);
        ToiletFragment toiletFragment = new ToiletFragment();
        toiletFragment.setArguments(toiletBundle);
        mFragments.add(2, toiletFragment);

        Bundle bedroomBundle = new Bundle();
        bedroomBundle.putInt("flag", 3);
        BedroomFragment bedroomFragment = new BedroomFragment();
        bedroomFragment.setArguments(bedroomBundle);
        mFragments.add(3, bedroomFragment);

    }

    @Override
    public void onDestroy() {
        unbindService(conn);
        unbindService(mConnection);
        unbindService(mPos);
        super.onDestroy();
    }

}
