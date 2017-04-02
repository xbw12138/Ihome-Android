package cn.ilell.ihome;

import android.os.Bundle;

import java.util.ArrayList;

import cn.ilell.ihome.base.BaseActivity;
import cn.ilell.ihome.fragment.DownloadFragment;
import cn.ilell.ihome.fragment.MediaFragment;

public class MediaActivity extends BaseActivity {

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
        mClass = MediaActivity.class;

        //mFloatingActionButton.setVisibility(View.INVISIBLE);
    }

    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.media_tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();

        Bundle mediaBundle = new Bundle();
        mediaBundle.putInt("flag", 0);
        MediaFragment mediaFragment = new MediaFragment();
        mediaFragment.setArguments(mediaBundle);
        mFragments.add(0, mediaFragment);

        Bundle downloadBundle = new Bundle();
        downloadBundle.putInt("flag", 1);
        DownloadFragment downloadFragment = new DownloadFragment();
        downloadFragment.setArguments(downloadBundle);
        mFragments.add(1, downloadFragment);

    }
    @Override
    public void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }

}
