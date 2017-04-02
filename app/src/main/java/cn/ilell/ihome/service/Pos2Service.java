package cn.ilell.ihome.service;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ilell.ihome.BangdingActivity;
import cn.ilell.ihome.R;
import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.utils.CompareUtils;
import cn.ilell.ihome.utils.LocationUtils;
import cn.ilell.ihome.utils.SharedPreference;

/**
 * Created by xubowen on 2017/3/5.
 */
public class Pos2Service extends Service{
    public static final String TAG = "Pos2Service";
    private final IBinder binder = new MyBinder();
    float x = 0;
    float y = 0;
    Map<String, Integer> now_status = new HashMap<String, Integer>();
    Map<Point, Map<String, Integer>> db = new HashMap<Point, Map<String, Integer>>();
    private WifiManager wifiManager;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
        Log.d(TAG, "onCreate() executed");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        public Pos2Service getService(){
            return Pos2Service.this;
        }
    }
    private  void creatdb(String s){
        System.out.println("create db");
        String[] pos = s.split("@");
        for(int j = 0; j < pos.length; ++j) {
            String one_pos = pos[j];
            if (one_pos.length() == 0) continue;
            Map<String, Integer> tmp = new HashMap<String, Integer>();
            String[] c = one_pos.split(" ");
            Point now = new Point();
            now.x = Float.parseFloat(c[0]);
            now.y = Float.parseFloat(c[1]);
            for (int i = 2; i < c.length; i+=2){
                tmp.put(c[i].replaceAll("\\s*", ""), Integer.valueOf( c[i+1].replaceAll("\\s*", "")));
            }
            db.put(now, tmp);
        }
    }
    private  void update_pos(){
        double sim = 0;
        String content = new String();
        for (Point point: db.keySet()) {
            double tmp = 0;
            double length = 0;
            int st=0;
            int ft = 0;
            for (String mac: db.get(point).keySet()) {
                if (now_status.containsKey(mac)) {
                    tmp += now_status.get(mac) * db.get(point).get(mac);
                    st ++;
                } else {
                    ft ++;
                }
                length += db.get(point).get(mac) * db.get(point).get(mac);
            }
            content += "\n" + (int)point.x + " " + (int)point.y + " : " + (int)(Math.abs(tmp) / Math.sqrt(length));
            System.out.println(point.x + " " + point.y + " : " + Math.abs(tmp) / Math.sqrt(length) + "   " +
                    st + " " + ft);
            if (Math.abs(tmp) / Math.sqrt(length) > sim) {
                x = point.x;
                y = point.y;
                sim  = Math.abs(tmp) / Math.sqrt(length);
            }
        }
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        int height = wm.getDefaultDisplay().getHeight();
        Log.i(width+"xbw",height+"12138");
        //Log.d(x+"",y+"");
        //pos2ResultListener.Pos2R(x+"|"+y);
        if(posResultListener!=null){
            Log.d("!!!!!!","@@@@@@");
            //if(x>=100.0&&x<=800.0&&y>=100.0&&y<=800.0){
            if(x>=0&&x<=width/2){//屏幕左边为客厅，右边为洗手间
                BaseData.local="客厅";
                pos2ResultListener.Pos2R("客厅");
            }else{
                BaseData.local="洗手间";
                pos2ResultListener.Pos2R("洗手间");
            }
            //pos2ResultListener.Pos2R(x+"|"+y);
        }
        //posResultListener.PosR("NO");
    }
    private Handler handler = new Handler();
    private Runnable task =new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            if(handler==null){
                return;
            }
            handler.postDelayed(this,1*1000);//设置延迟时间，此处是1秒
            wifiManager.startScan();
            List<ScanResult> list = wifiManager.getScanResults();
            now_status.clear();
            for (ScanResult result : list) {
                now_status.put(result.BSSID, result.level);
            }
            update_pos();
        }
    };
    private void init(){
        try {
            String name = String.valueOf("map.txt");
            String db_contnet = getStringFromFile(name);
            creatdb(db_contnet);
        }
        catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        openWifi();
        handler.post(task);//立即调用

    }
    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }
    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    private String getStringFromFile (String filePath) throws Exception {
        FileInputStream in = openFileInput(filePath);
        String ret = convertStreamToString(in);
        in.close();
        System.out.println("Open file ok");
        return ret;
    }
    class Point {
        float x, y;
    }
}
