package cn.ilell.ihome.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.apache.http.HttpConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.ilell.ihome.BangdingActivity;
import cn.ilell.ihome.BangdingsActivity;
import cn.ilell.ihome.ControlActivity;
import cn.ilell.ihome.FamilyFaceActivity;
import cn.ilell.ihome.FamilyMemoActivity;
import cn.ilell.ihome.FamilyMsgActivity;
import cn.ilell.ihome.HistoryActivity;
import cn.ilell.ihome.LoginActivity;
import cn.ilell.ihome.MediaActivity;
import cn.ilell.ihome.MonitorActivity;
import cn.ilell.ihome.R;
import cn.ilell.ihome.RegistActivity;
import cn.ilell.ihome.SamplingActivity;
import cn.ilell.ihome.ScanActivity;
import cn.ilell.ihome.ScheduleActivity;
import cn.ilell.ihome.StateActivity;
import cn.ilell.ihome.adapter.MyViewPagerAdapter;
import cn.ilell.ihome.fragment.OutdoorFragment;
import cn.ilell.ihome.service.MsgService;
import cn.ilell.ihome.service.OnProgressListener;
import cn.ilell.ihome.service.Pos2Service;
import cn.ilell.ihome.service.YyService;
import cn.ilell.ihome.utils.CompareUtils;
import cn.ilell.ihome.utils.HttpXmlClient;
import cn.ilell.ihome.utils.JsonParser;
import cn.ilell.ihome.utils.LocationUtils;
import cn.ilell.ihome.utils.OperatingCommand;
import cn.ilell.ihome.utils.SharedPreference;
import cn.ilell.ihome.utils.SnackbarUtil;
import cn.ilell.ihome.view.RoundedImageView;
import cn.ilell.ihome.view.StatusBarCompat;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by lhc35 on 2016/4/13.
 */
public class BaseActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {


    //语音识别部分
    private static String TAG = ControlActivity.class.getSimpleName();
    //语音听写结果
    private String voiceResult = "";
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    int ret = 0; // 函数调用返回值

    // 语音合成对象
    private SpeechSynthesizer mTts;
    //语音识别部分

    private OperatingCommand operatingCommand;  //语音操作指令处理

    //服务
    protected MsgService msgService;
    //protected YyService yyService;

    //初始化各种控件，照着xml中的顺序写
    protected DrawerLayout mDrawerLayout;
    protected CoordinatorLayout mCoordinatorLayout;
    protected AppBarLayout mAppBarLayout;
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected FloatingActionButton mFloatingActionButton;
    protected NavigationView mNavigationView;

    protected View headView;
    protected RoundedImageView mRoundedImageView;

    // TabLayout中的tab标题
    protected String[] mTitles;
    // 填充到ViewPager中的Fragment
    protected List<Fragment> mFragments;
    // ViewPager的数据适配器
    protected MyViewPagerAdapter mViewPagerAdapter;
    protected Context mContext;
    protected Class mClass;
    protected SharedPreference sharedPreference;
    protected LocationUtils locationUtils;

    private YyService serviceBinder;
    private Pos2Service servicePosBinder;
    protected Switch posSwitch;
    protected TextView posText;

    //2016/11/23 扫码弹窗
    private AlertDialog.Builder builder;

    //离线语音服务
    protected ServiceConnection mConnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Called when the connection is made.
            serviceBinder = ((YyService.MyBinder)service).getService();
            serviceBinder.setYyResultListener(new YyService.YyResultListener() {
                @Override
                public void YyR(String s) {
                    //2017 0316 室内定位+离线语音
                    if(!BaseData.local.equals("")){
                        Toast.makeText(mContext,"定位："+BaseData.local+"命令："+s+"",Toast.LENGTH_LONG).show();
                        OperatingCommand o =new OperatingCommand();
                        if(s.equals("关闭")){
                            o.dealCommand("关"+BaseData.local+"灯");
                        }else if(s.equals("打开")){
                            o.dealCommand("开"+BaseData.local+"灯");
                        }

                    }else{
                        Toast.makeText(mContext,"定位失败",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        public void onServiceDisconnected(ComponentName className) {
            // Received when the service unexpectedly disconnects.
            serviceBinder = null;
        }
    };
    //室内定位服务
    protected ServiceConnection mPos = new ServiceConnection(){
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Called when the connection is made.
            servicePosBinder = ((Pos2Service.MyBinder)service).getService();
            servicePosBinder.setPosResultListener(new Pos2Service.PosResultListener() {
                @Override
                public void PosR(String s) {
                    if(s.equals("NO")){
                        posSwitch.setChecked(false);
                    }
                }
            });
            servicePosBinder.setPos2ResultListener(new Pos2Service.Pos2ResultListener() {
                @Override
                public void Pos2R(String s) {
                    posText.setText("位置:"+s);
                }
            });
        }
        public void onServiceDisconnected(ComponentName className) {
            // Received when the service unexpectedly disconnects.
            servicePosBinder = null;
        }
    };

    protected ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            msgService = ((MsgService.MsgBinder) service).getService();

            //注册回调接口来接收下载进度的变化
            msgService.setOnProgressListener(new OnProgressListener() {
                @Override
                public void onProgress(String recvMsg) {
                    //SnackbarUtil.show(findViewById(R.id.main_floatingactionbutton), recvMsg, 0);
                    if (mClass.equals(MonitorActivity.class)) {
                        if (recvMsg.equals("0/4/6")){
                            if (OutdoorFragment.mMediaPlayer != null && OutdoorFragment.mMediaPlayer.isPlaying())
                                OutdoorFragment.mMediaPlayer.stop();
                            //finish();
                        }//关闭楼宇对讲
                        else {
                            String[] cmd = recvMsg.split("/");
                            if (cmd[0].equals("1") && cmd[1].equals("3")) {
                                if (OutdoorFragment.text_state!=null)
                                    OutdoorFragment.text_state.setText(cmd[2]);
                            }
                        }
                    }
                    String[] msg = recvMsg.split("/");
                    if(msg[0].equals("1") && msg[1].equals("1") && msg[2].equals("指令未识别") ){
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    String finalResult = AIRequest(URLEncoder.encode(voiceResult, "UTF-8"));
                                    if (null != finalResult) {//AI回复结果
                                        JSONObject json = new JSONObject(finalResult);
                                        finalResult = json.getString("text");

                                        FlowerCollector.onEvent(BaseActivity.this, "tts_play");
                                        mTts.startSpeaking(finalResult, mTtsListener);

                                        //SnackbarUtil.show(findViewById(R.id.main_floatingactionbutton), finalResult, 0);
                                        final String finalResult1 = finalResult;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BaseActivity.this, finalResult1, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            //获取AI的回复
                            public String AIRequest(final String content) {
                                String result = null;
                                String httpUrl = "http://apis.baidu.com/turing/turing/turing?key=f88d75ec75a045da827d0b34f8abbe5b&info=";
                                String httpArg = "&userid=eb2edb736";
                                httpUrl = httpUrl  + content + httpArg;
                                BufferedReader reader = null;
                                StringBuffer sbf = new StringBuffer();
                                try {
                                    URL url = new URL(httpUrl);
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setRequestMethod("GET");
                                    connection.setRequestProperty("apikey", "92e10d91b8c56e3698f2b2efb6f2a4c0");
                                    connection.connect();
                                    InputStream is = connection.getInputStream();
                                    reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                                    String strRead = null;
                                    while ((strRead = reader.readLine()) != null) {
                                        sbf.append(strRead);
                                        sbf.append("\r\n");
                                    }
                                    reader.close();
                                    result = sbf.toString();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return result;
                            }
                        }).start();
                    }
                    else if(msg[0].equals("1") && msg[1].equals("1")) {
                        Toast.makeText(BaseActivity.this, msg[2], Toast.LENGTH_SHORT).show();
                    }
                    /*TextView textView = (TextView) findViewById(R.id.main_textView);
                    textView.setText(recvMsg);*/
                }
            });

        }
    };
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(BaseActivity.this, "初始化失败，错误码：" + code, Toast.LENGTH_LONG ).show();
            }
        }
    };
    //打印结果
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        //语音结果
        voiceResult = resultBuffer.toString();
        operatingCommand.dealCommand(voiceResult);
        //Toast.makeText(BaseActivity.this, operatingCommand.dealCommand(voiceResult), Toast.LENGTH_LONG).show();
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if (!isLast)
                printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Toast.makeText(BaseActivity.this,error.getPlainDescription(true), Toast.LENGTH_LONG ).show();
        }
    };

    protected void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinatorlayout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbarlayout);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.main_floatingactionbutton);
        mNavigationView = (NavigationView) findViewById(R.id.main_navigationview);
        posText = (TextView) findViewById(R.id.textPos);
        posSwitch = (Switch)findViewById(R.id.switchPos);

        posSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 开启switch，设置提示信息
                    //startService(intent);
                    //绑定室内定位服务
                    if(sharedPreference.isKeep(this.getClass().getName())){
                        Intent mIntent = new Intent(BaseActivity.this, Pos2Service.class);
                        bindService(mIntent, mPos, Context.BIND_AUTO_CREATE);
                        posText.setVisibility(View.VISIBLE);
                        posText.setText("位置:");
                        Toast.makeText(BaseActivity.this,"开启室内定位",Toast.LENGTH_LONG).show();
                    }else{
                        posSwitch.setChecked(false);
                        Intent intent = new Intent();
                        intent.setClass(mContext, BangdingsActivity.class);
                        startActivity(intent);
                    }

                } else {
                    // 关闭swtich，设置提示信息
                    //stopService(intent);
                    unbindService(mPos);
                    posText.setVisibility(View.GONE);
                    Toast.makeText(BaseActivity.this,"关闭室内定位",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void initNavHead() {
        TextView text_name = (TextView) headView.findViewById(R.id.id_header_authorname);
        TextView text_homeid = (TextView) headView.findViewById(R.id.id_header_homeid);
        text_name.setText(BaseData.account_name);
        text_homeid.setText(BaseData.home_id);
        sharedPreference=new SharedPreference(this);
        //Toast.makeText(BaseActivity.this, BaseData.account_name, Toast.LENGTH_SHORT).show();
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onCompleted(SpeechError error) {
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }

        @Override
        public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
        }

        @Override
        public void onSpeakProgress(int arg0, int arg1, int arg2) {
        }
    };

    protected void bindMsgService() {
        //绑定Service
        Intent intent = new Intent();
        intent.setAction("cn.msgservice");
        intent.setPackage(getPackageName());
        bindService(intent, conn, BIND_AUTO_CREATE);

        //绑定离线语音服务
        Intent bindIntent = new Intent(this, YyService.class);
        bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
        //增加start确保解绑后服务继续运行
        startService(bindIntent);

        //绑定室内定位服务
        //Intent mIntent = new Intent(this, PosService.class);
        //bindService(mIntent, mPos, Context.BIND_AUTO_CREATE);
        //startService(mIntent);
    }

    protected void configViews() {
        //设置状态栏的颜色
        StatusBarCompat.compat(this, getResources().getColor(R.color.main_blue_light));
        // 设置显示Toolbar
        setSupportActionBar(mToolbar);

        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/header_nav"来设置
        headView = mNavigationView.inflateHeaderView(R.layout.header_nav);
        mRoundedImageView = (RoundedImageView) headView.findViewById(R.id.id_header_face);
        //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/menu_nav"来设置
        mNavigationView.inflateMenu(R.menu.menu_nav);

        // 自己写的方法，设置NavigationView中menu的item被选中后要执行的操作
        onNavgationViewMenuItemSelected(mNavigationView);

        // 初始化ViewPager的适配器，并设置给它
        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(5);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);

        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);

        //设置侧边栏头部显示
        initNavHead();

        operatingCommand = new OperatingCommand();

        //语音识别部分
        SpeechUtility.createUtility(BaseActivity.this, "appid=573f022f");
        mIatDialog = new RecognizerDialog(BaseActivity.this, mInitListener);
        mTts = SpeechSynthesizer.createSynthesizer(BaseActivity.this, mInitListener);
    }

    /**
     * 设置NavigationView中menu的item被选中后要执行的操作
     *
     * @param mNav
     */
    private void onNavgationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                String msgString = "";

                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_state:
                        if (mClass != StateActivity.class)
                            changeActivity(StateActivity.class);
                        break;
                    case R.id.nav_menu_control:
                        if (mClass != ControlActivity.class)
                            changeActivity(ControlActivity.class);
                        break;
                    case R.id.nav_menu_history:
                        if (mClass != HistoryActivity.class)
                            changeActivity(HistoryActivity.class);
                        break;
                    case R.id.nav_menu_monitor:
                        if (mClass != MonitorActivity.class)
                            changeActivity(MonitorActivity.class);
                        break;
                    case R.id.nav_menu_media:
                        if (mClass != MediaActivity.class)
                            changeActivity(MediaActivity.class);
                        break;
                }

                // Menu item点击后选中，并关闭Drawerlayout
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                // android-support-design兼容包中新添加的一个类似Toast的控件。
                //SnackbarUtil.show(mViewPager, msgString, 0);
                return true;
            }
        });
    }

    private void changeActivity(final Class orderClass) {
        new Thread() {
            public void run() {
                //休眠0.256
                try {
                    Thread.sleep(256);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*Intent newIntent = new Intent(mContext, orderClass);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(newIntent);*/
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        //制定intent要启动的类
                        intent.setClass(mContext, orderClass);
                        if (orderClass.equals(MonitorActivity.class)) {
                            //传参设置不响铃
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("Ring",false);
                            intent.putExtras(bundle);
                        }
                        //启动一个新的Activity
                        mContext.startActivity(intent);
                        //关闭当前的
                        finish();
                        overridePendingTransition(R.anim.push_left_in_quickly, R.anim.push_left_out_quickly);
                    }
                });
            }
        }.start();
    }
    // 判断wifi是否打开，不是wifi是否连接
    public static boolean isWifi(Context context){
        WifiManager wifiManager=(WifiManager) context.getSystemService(context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()){
            return true;
        }else{
            return false;
        }
    }
    public void onFloatingactionButtonClick(View v) {
        //检测WIFI状态
        if(isWifi(this)){
            //是否已经绑定wifi
            if(sharedPreference.isKeep(this.getClass().getName())){
                mTts.stopSpeaking();
                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(BaseActivity.this, "iat_recognize");
                mIatResults.clear();
                mIatDialog.setParameter(SpeechConstant.ASR_PTT, "0");
                mIatDialog.setListener(mRecognizerDialogListener);
                mIatDialog.show();
                locationUtils =new LocationUtils(this);
                locationUtils.setMysqlListeners(new LocationUtils.MysqlListeners() {
                    @Override
                    public void Success() {
                        //数据对比需要异步，开个线程
                        final CompareUtils compareUtils=new CompareUtils(BaseActivity.this);
                        compareUtils.setMysqlListenercom(new CompareUtils.MysqlListenercom() {
                            @Override
                            public void Success() {
                                Toast.makeText(BaseActivity.this,compareUtils.home,Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void Fail() {

                            }
                        });
                        compareUtils.execute(locationUtils.data);
                    }
                    @Override
                    public void Fail() {
                        Toast.makeText(BaseActivity.this,"定位失败,WIFI没开启;绑定AP信号弱",Toast.LENGTH_SHORT).show();
                    }
                });
                locationUtils.execute();
            }else{
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.app_name))
                        .setMessage("用于室内定位的WIFI绑定")
                        .setPositiveButton("暂不绑定", null)
                        .setNegativeButton("立即绑定", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(mContext, BangdingsActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }else{
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            Toast.makeText(this,"检测到WIFI没有开启，请打开WIFI",Toast.LENGTH_SHORT).show();
            return ;
        }

    }   //浮动按钮单击事件

    public void onRoundedImageViewClick(View v) {
        Intent intent = new Intent();
        //制定intent要启动的类
        intent.setClass(mContext, LoginActivity.class);
        //启动一个新的Activity
        startActivity(intent);
        overridePendingTransition(R.anim.scale_translate,
                R.anim.my_alpha_action);
    }   //侧边栏图片按钮单击事件

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bar_menu_msg) {
            Intent intent = new Intent();
            //制定intent要启动的类
            intent.setClass(mContext, FamilyMsgActivity.class);
            //启动一个新的Activity
            mContext.startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
            return true;
        }
        else if (id == R.id.bar_menu_memo) {
            Intent intent = new Intent();
            //制定intent要启动的类
            intent.setClass(mContext, FamilyMemoActivity.class);
            //启动一个新的Activity
            mContext.startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
            return true;
        }
        else if (id == R.id.bar_menu_face) {
            Intent intent = new Intent();
            //制定intent要启动的类
            intent.setClass(mContext, FamilyFaceActivity.class);
            //启动一个新的Activity
            mContext.startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
            return true;
        }
        else if (id == R.id.bar_menu_regist) {
            Intent intent = new Intent();
            //制定intent要启动的类
            intent.setClass(mContext, RegistActivity.class);
            //启动一个新的Activity
            mContext.startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
            return true;
        }
        else if (id == R.id.bar_menu_schedule) {
            Intent intent = new Intent();
            //制定intent要启动的类
            intent.setClass(mContext, ScheduleActivity.class);
            //启动一个新的Activity
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
            return true;
        }
        //2016/10/02 重新绑定AP
        else if (id == R.id.bar_menu_rekeep) {
            Intent intent = new Intent();
            //制定intent要启动的类
            intent.setClass(mContext, BangdingsActivity.class);
            //启动一个新的Activity
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
            return true;
        }
        //2016/11/23 二维码扫描添加节点
        else if (id == R.id.bar_menu_scan) {
            Intent intent = new Intent();
            //制定intent要启动的类
            intent.setClass(mContext, ScanActivity.class);
            //启动一个新的Activity
            startActivity(intent);
            //startActivityForResult(intent, 10004);
            overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

    }

    protected void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(BaseActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(BaseActivity.this);
        super.onPause();
    }
    //2016/11/23 扫码回调
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null)
            return;
        if (requestCode == 10001) {
            String result_value = intent.getStringExtra("describe");
            if(result_value!=null)
                showScanDialog(result_value);
            else
                showScanDialog("扫码失败");
            return;
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
