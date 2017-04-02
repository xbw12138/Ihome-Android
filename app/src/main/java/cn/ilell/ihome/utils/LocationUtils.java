package cn.ilell.ihome.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;

/**
 * Created by xubowen on 16/10/2.
 */
public class LocationUtils extends AsyncTask<String, String, String> {
    private WifiManager wifiManager;
    private List<ScanResult> list;
    private SharedPreference sharedPreference;
    private ScanResult scan1;
    private ScanResult scan2;
    //private boolean open1 = false;
    //private boolean open2 = false;
    Context context;
    public String data;

    public LocationUtils(Context context) {
        this.context = context;
    }

    public interface MysqlListeners {
        public void Success();

        public void Fail();

    }

    private MysqlListeners mysqlListener = null;

    public void setMysqlListeners(MysqlListeners mysqlListener) {
        this.mysqlListener = mysqlListener;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        sharedPreference = new SharedPreference(context);
        wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
    }

    @Override
    protected String doInBackground(String... strings) {
        String a="";
        wifiManager.startScan();
        list = wifiManager.getScanResults();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).BSSID.equals(sharedPreference.getBAP1())) {
                scan1 = list.get(i);
                //open1 = true;
            } else if (list.get(i).BSSID.equals(sharedPreference.getBAP2())) {
                scan2 = list.get(i);
                //open2 = true;
            }
            /*if (open1 && open2) {
                open1 = false;
                open2 = false;
                a="1";
                break;
            }
            if (i == list.size() - 2) {
                open1 = false;
                open2 = false;
                a="2";
            }*/
        }
        if(scan1!=null&&scan2!=null){
            a="1";
            data=Math.abs(scan1.level)+"|"+Math.abs(scan2.level);
        }else{
            a="2";
        }

        return a;
    }

    protected void onPostExecute(String message) {
        if (mysqlListener != null) {
            //String a[]=message.split("\\|");
            if (message.equals("1")) {
                mysqlListener.Success();
            } else {
                mysqlListener.Fail();
            }
        }
    }


}
