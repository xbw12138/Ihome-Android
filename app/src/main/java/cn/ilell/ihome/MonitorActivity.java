package cn.ilell.ihome;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.iflytek.sunflower.task.e;

import java.util.ArrayList;

import cn.ilell.ihome.base.BaseActivity;
import cn.ilell.ihome.fragment.IndoorFragment;
import cn.ilell.ihome.fragment.OutdoorFragment;
import cn.ilell.ihome.service.MsgService;
import cn.ilell.ihome.service.OnProgressListener;
import cn.ilell.ihome.utils.SnackbarUtil;

public class MonitorActivity extends BaseActivity {
    //楼宇对讲是否响铃
    public boolean ring = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
            ring = bundle.getBoolean("Ring", false);

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
        mClass = MonitorActivity.class;

    }

    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.monitor_tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();

        Bundle outdoorBundle = new Bundle();
        outdoorBundle.putInt("flag", 0);
        outdoorBundle.putBoolean("Ring", ring);
        OutdoorFragment outdoorFragment = new OutdoorFragment();
        outdoorFragment.setArguments(outdoorBundle);
        mFragments.add(0, outdoorFragment);

        Bundle indoorBundle = new Bundle();
        indoorBundle.putInt("flag", 1);
        IndoorFragment indoorFragment = new IndoorFragment();
        indoorFragment.setArguments(indoorBundle);
        mFragments.add(1, indoorFragment);

    }

    @Override
    public void onDestroy() {
        unbindService(conn);
        if (OutdoorFragment.audioClient != null)
            OutdoorFragment.audioClient.stop();
        if (OutdoorFragment.mMediaPlayer != null)
            OutdoorFragment.mMediaPlayer.stop();
        super.onDestroy();
    }

}
