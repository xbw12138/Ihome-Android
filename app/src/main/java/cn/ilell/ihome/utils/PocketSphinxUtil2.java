package cn.ilell.ihome.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import edu.cmu.pocketsphinx.demo.RecognitionListener;
import edu.cmu.pocketsphinx.demo.RecognizerTask;

/**
 * Created by xubowen on 2017/3/4.
 */
public class PocketSphinxUtil2 {
    public static final int DELAY_MILLIS = 2 * 1000;//2秒

    //PocketSphinxUtil(){
    static {
        System.loadLibrary("pocketsphinx_jni");
    }

    private boolean isStop;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("重启");
            rec.stop();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(isStop)
                    {
                        return;
                    }
                    rec.start();
                    mHandler.sendEmptyMessageDelayed(1, DELAY_MILLIS);
                }
            }).start();

        }
    };

    private static PocketSphinxUtil2 instance = null;
    private RecognizerTask rec;
    private Thread rec_thread;

    public static PocketSphinxUtil2 get(Context context) {
        if (instance == null) {
            instance = new PocketSphinxUtil2(context);
        }
        return instance;
    }

    public PocketSphinxUtil2 setListener(RecognitionListener listener) {
        rec.setRecognitionListener(listener);
        return this;
    }

    public PocketSphinxUtil2 start() {
        isStop=false;
        rec.start();
        mHandler.sendEmptyMessageDelayed(1, DELAY_MILLIS);
        return this;
    }

    public PocketSphinxUtil2 stop() {
        isStop=true;
        rec.stop();
        mHandler.removeMessages(1);
        return this;
    }

    private PocketSphinxUtil2(Context context) {
        rec = new RecognizerTask(context);
        rec_thread = new Thread(rec);
        rec_thread.start();
    }

}
