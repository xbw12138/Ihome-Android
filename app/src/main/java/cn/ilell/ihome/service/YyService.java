package cn.ilell.ihome.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import cn.ilell.ihome.utils.PocketSphinxUtil;

import cn.ilell.ihome.utils.PocketSphinxUtil;
import cn.ilell.ihome.utils.PocketSphinxUtil2;
import edu.cmu.pocketsphinx.demo.RecognitionListener;
/**
 * Created by xubowen on 2017/3/5.
 */
public class YyService extends Service implements RecognitionListener {
    public static final String TAG = "YyService";
    private Context context;
    private final IBinder binder = new MyBinder();
    @Override
    public void onCreate() {
        super.onCreate();
        PocketSphinxUtil.get(this).setListener(this).start();
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
        PocketSphinxUtil.get(this).stop();
    }
    public interface YyResultListener {
        public void YyR(String str);
    }
    private YyResultListener yyResultListener=null;                                     ///
    public void setYyResultListener(YyResultListener yyListener) {                   ///
        this.yyResultListener = yyListener;                                       ///
    }
    @Override
    public void onPartialResults(String b) {
        Log.d("hhhh","shuchu隐隐约约");
        yyResultListener.YyR(b);
    }
    @Override
    public void onResults(String b) {
        Log.d("hhhh","shuchu啊哈哈哈哈哈哈");
        yyResultListener.YyR(b);
    }
    @Override
    public void onError(int err) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class MyBinder extends Binder {
        public YyService getService(){
            return YyService.this;
        }
    }
}
