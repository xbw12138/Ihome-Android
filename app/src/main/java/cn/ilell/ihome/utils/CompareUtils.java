package cn.ilell.ihome.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by xubowen on 16/10/2.
 */
public class CompareUtils extends AsyncTask<String, String, String>{
    Context context;
    SharedPreference sharedPreference;
    public String home;
    public CompareUtils(Context context) {
        this.context = context;
    }
    public interface MysqlListenercom {
        public void Success();

        public void Fail();

    }
    private MysqlListenercom mysqlListener = null;

    public void setMysqlListenercom(MysqlListenercom mysqlListener) {
        this.mysqlListener = mysqlListener;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        sharedPreference=new SharedPreference(context);
    }
    @Override
    protected String doInBackground(String... strings) {
        home=Compare(String_Int(strings[0]));
        return "1";
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
    //定位字符坐标转整型数组
    private int [] String_Int(String hh){
        int a[]=new int[2];
        if(!hh.equals("")){
            a[0]=Integer.parseInt(hh.split("\\|")[0]);
            a[1]=Integer.parseInt(hh.split("\\|")[1]);
        }else{
            a[0]=0;
            a[1]=0;
        }
        Log.d("定位¥¥¥¥¥¥",a[0]+"|"+a[1]);
        return a;
    }
    //定位误差
    int wucha=10;
    //定位房间，通过当前坐标与存储坐标匹配，返回房间名称
    private String Compare(int a[]){
        String message="";
        if(String_Int(sharedPreference.getHP1())[0]-wucha<=a[0]&&String_Int(sharedPreference.getHP1())[0]+wucha>=a[0]){
            if(String_Int(sharedPreference.getHP1())[1]-wucha<=a[1]&&String_Int(sharedPreference.getHP1())[1]+wucha>=a[1]){
                message=sharedPreference.getHN1();
            }
        }else
        if(String_Int(sharedPreference.getHP2())[0]-wucha<=a[0]&&String_Int(sharedPreference.getHP2())[0]+wucha>=a[0]){
            if(String_Int(sharedPreference.getHP2())[1]-wucha<=a[1]&&String_Int(sharedPreference.getHP2())[1]+wucha>=a[1]){
                message=sharedPreference.getHN2();
            }
        }else
        if(String_Int(sharedPreference.getHP3())[0]-wucha<=a[0]&&String_Int(sharedPreference.getHP3())[0]+wucha>=a[0]){
            if(String_Int(sharedPreference.getHP3())[1]-wucha<=a[1]&&String_Int(sharedPreference.getHP3())[1]+wucha>=a[1]){
                message=sharedPreference.getHN3();
            }
        }else
        if(String_Int(sharedPreference.getHP4())[0]-wucha<=a[0]&&String_Int(sharedPreference.getHP4())[0]+wucha>=a[0]){
            if(String_Int(sharedPreference.getHP4())[1]-wucha<=a[1]&&String_Int(sharedPreference.getHP4())[1]+wucha>=a[1]){
                message=sharedPreference.getHN4();
            }
        }else
        if(String_Int(sharedPreference.getHP5())[0]-wucha<=a[0]&&String_Int(sharedPreference.getHP5())[0]+wucha>=a[0]){
            if(String_Int(sharedPreference.getHP5())[1]-wucha<=a[1]&&String_Int(sharedPreference.getHP5())[1]+wucha>=a[1]){
                message=sharedPreference.getHN5();
            }
        }else
        if(String_Int(sharedPreference.getHP6())[0]-wucha<=a[0]&&String_Int(sharedPreference.getHP6())[0]+wucha>=a[0]){
            if(String_Int(sharedPreference.getHP6())[1]-wucha<=a[1]&&String_Int(sharedPreference.getHP6())[1]+wucha>=a[1]){
                message=sharedPreference.getHN6();
            }
        }else
        if(String_Int(sharedPreference.getHP7())[0]-wucha<=a[0]&&String_Int(sharedPreference.getHP7())[0]+wucha>=a[0]){
            if(String_Int(sharedPreference.getHP7())[1]-wucha<=a[1]&&String_Int(sharedPreference.getHP7())[1]+wucha>=a[1]){
                message=sharedPreference.getHN7();
            }
        }else {
            message="无";
        }
        return message;
    }
}
