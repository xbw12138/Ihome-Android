package cn.ilell.ihome;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import cn.ilell.ihome.base.BaseSubPageActivity;
import cn.ilell.ihome.utils.Progress_Dialog;
import cn.ilell.ihome.utils.SharedPreference;

/**
 * Created by xubowen on 16/10/2.
 */
public class PipeiActivity extends Activity {
    //private Handler handler1;
    //private Handler handler2;
    private Handler handler3;
    private Handler handler4;
    private WifiManager wifiManager;
    private List<ScanResult> list;
    private SharedPreference sharedPreference;
    private ScanResult scan1;
    private ScanResult scan2;
    private boolean open1=false;
    private boolean open2=false;
    private int count1=0;
    private int count2=0;
    private int jingdu=10;
    private int ap1;
    private int ap2;
    private int qap1;
    private int qap2;
    private String ids="";
    ProgressDialog dialog;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipei);
        tv=(TextView)findViewById(R.id.textView2);
        tv.setVisibility(View.GONE);
        ids=getIntent().getStringExtra("ids");
        sharedPreference=new SharedPreference(this);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiManager.startScan();
        dialog(getIntent().getStringExtra("id"));
    }
    private void dialog(String id){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("请确保您的位置在（"+id+"）正中央")
                .setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Time();
                    }
                })
                .show();
    }
    private void Time(){
        dialog = Progress_Dialog.CreateProgressDialog(this);
        dialog.show();
        //每秒获得一次信号强度值
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                wifiManager.startScan();
                list = wifiManager.getScanResults();
                for(int i=0;i<list.size();i++){
                    if(list.get(i).BSSID.equals(sharedPreference.getBAP1())&&count1!=jingdu){
                        scan1=list.get(i);
                        ap1+=scan1.level;
                        count1++;
                        //handler1.sendEmptyMessage(0);
                        Log.i(scan1.level+"","AP1");
                        open1=true;
                    }else if(list.get(i).BSSID.equals(sharedPreference.getBAP2())&&count2!=jingdu){
                        scan2=list.get(i);
                        ap2+=scan2.level;
                        count2++;
                        //handler2.sendEmptyMessage(0);
                        Log.d(scan2.level+"","AP2");
                        open2=true;
                    }
                    if(open1&&open2) {
                        open1=false;
                        open2=false;
                        break;
                    }
                    if(i==list.size()-2) {
                        handler3.sendEmptyMessage(0);
                        open1=false;
                        open2=false;
                    }
                }
                if(count1==jingdu){
                    qap1=ap1/jingdu;
                }
                if(count2==jingdu){
                    qap2=ap2/jingdu;
                }
                if(count1==jingdu&&count2==jingdu){
                    //取消定时器
                    cancel();
                    //返回匹配结果
                    result();
                    //取消加载进度窗口
                    dialog.cancel();
                    handler4.sendEmptyMessage(0);
                }
            }
        }, 1000, 1000);
        //handler1 = new Handler() {
        //    @Override
        //    public void handleMessage(Message msg) {
        //        //txap1.setText(scan1.level + "");
        //    }
        //};
        //handler2 = new Handler() {
        //    @Override
        //    public void handleMessage(Message msg) {
        //        //txap2.setText(scan2.level + "");
        //    }
        //};
        handler3 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(PipeiActivity.this,"信号差，请重新匹配",Toast.LENGTH_LONG).show();
            }
        };
        handler4 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(rand());
                new AlertDialog.Builder(PipeiActivity.this)
                        .setTitle(getString(R.string.app_name))
                        .setMessage("匹配成功")
                        .setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //result();
                                finish();
                            }
                        })
                        .show();
            }
        };
    }
    private void result(){
        Intent mIntent = new Intent();
        mIntent.putExtra("ii",Math.abs(qap1)+"|"+Math.abs(qap2));
        if(ids.equals("1")){
            setResult(1000, mIntent);
        }else if(ids.equals("2")){
            setResult(1001, mIntent);
        }else if(ids.equals("3")){
            setResult(1002, mIntent);
        }else if(ids.equals("4")){
            setResult(1003, mIntent);
        }else if(ids.equals("5")){
            setResult(1004, mIntent);
        }else if(ids.equals("6")){
            setResult(1005, mIntent);
        }else if(ids.equals("7")){
            setResult(1006, mIntent);
        }
    }
    private String tt[]={"你是怎么发现我的","不寻常的一个人","真是的，非要看","兄弟，不可以这样按的","兄弟，一定要按确定哦","如果你想看我，就别按确定"};
    private String rand(){
        Random random=new Random();
        int i=random.nextInt(tt.length);
        return tt[i];
    }
}
