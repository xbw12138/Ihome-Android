package cn.ilell.ihome.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnClickListener;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.sunflower.FlowerCollector;

import cn.ilell.ihome.BangdingActivity;
import cn.ilell.ihome.R;
import cn.ilell.ihome.base.BaseActivity;
import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.utils.CompareUtils;
import cn.ilell.ihome.utils.LocationUtils;
import cn.ilell.ihome.utils.PocketSphinxUtil;
import cn.ilell.ihome.utils.SharedPreference;
import edu.cmu.pocketsphinx.demo.RecognitionListener;

/**
 * Created by xubowen on 2017/3/5.
 */
public class PosService extends Service{
    public static final String TAG = "PosService";
    private Context context;
    private final IBinder binder = new MyBinder();
    private SharedPreference sharedPreference;
    protected LocationUtils locationUtils;
    private Builder builder;
    public Boolean dialogOpen=false;
    private Boolean dialogWifi=false;
    @Override
    public void onCreate() {
        super.onCreate();
        //handler.postDelayed(task,2000);//延迟调用
        handler.post(task);//立即调用
        sharedPreference=new SharedPreference(getApplicationContext());
        builder = new AlertDialog.Builder(getApplicationContext());
        dialogOpen=false;
        dialogWifi=false;
        Log.d(TAG, "onCreate() executed");
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
    private Handler handler = new Handler();
    private Runnable task =new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            if(handler==null){
                return;
            }
            handler.postDelayed(this,1*1000);//设置延迟时间，此处是1秒
            //需要执行的代码
            if(isWifi(getApplicationContext())){
                //是否已经绑定wifi
                if(sharedPreference.isKeep(this.getClass().getName())){
                    locationUtils =new LocationUtils(getApplicationContext());
                    locationUtils.setMysqlListeners(new LocationUtils.MysqlListeners() {
                        @Override
                        public void Success() {
                            //数据对比需要异步，开个线程
                            final CompareUtils compareUtils=new CompareUtils(getApplicationContext());
                            compareUtils.setMysqlListenercom(new CompareUtils.MysqlListenercom() {
                                @Override
                                public void Success() {
                                    if(!compareUtils.home.equals("")&&!compareUtils.home.equals("无")){
                                        BaseData.local=compareUtils.home;
                                        pos2ResultListener.Pos2R(compareUtils.home);
                                    }
                                    //Toast.makeText(getApplicationContext(),compareUtils.home,Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void Fail() {

                                }
                            });
                            compareUtils.execute(locationUtils.data);
                        }
                        @Override
                        public void Fail() {
                            Toast.makeText(getApplicationContext(),"定位失败,WIFI没开启;绑定AP信号弱",Toast.LENGTH_SHORT).show();
                        }
                    });
                    locationUtils.execute();
                }else{
                    if(!dialogOpen){
                        dialogOpen=true;
                        builder.setTitle(getString(R.string.app_name));
                        builder.setMessage("用于室内定位的WIFI绑定");
                        builder.setNegativeButton("暂不绑定", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialogOpen=false;
                                posResultListener.PosR("NO");
                            }
                        });
                        builder.setPositiveButton("立即绑定", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(getApplicationContext(), BangdingActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        final AlertDialog dialog = builder.create();
                        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                        dialog.show();
                    }
                    /*new AlertDialog.Builder(getApplicationContext())
                            .setTitle(getString(R.string.app_name))
                            .setMessage("用于室内定位的WIFI绑定")
                            .setPositiveButton("暂不绑定", null)
                            .setNegativeButton("立即绑定", new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(), BangdingActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                     */
                }
            }else{
                //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"检测到WIFI没有开启，请打开WIFI",Toast.LENGTH_SHORT).show();
                return ;
            }
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler=null;
        Log.d(TAG, "onDestroy() executed");
    }
    public interface PosResultListener {
        public void PosR(String str);
    }
    public interface Pos2ResultListener{
        public void Pos2R(String str);
    }
    private PosResultListener posResultListener=null;
    private Pos2ResultListener pos2ResultListener=null;
    public void setPosResultListener(PosResultListener posListener) {                   ///
        this.posResultListener = posListener;                                       ///
    }
    public void setPos2ResultListener(Pos2ResultListener pos2Listener) {                   ///
        this.pos2ResultListener = pos2Listener;                                       ///
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class MyBinder extends Binder {
        public PosService getService(){
            return PosService.this;
        }
    }
}
