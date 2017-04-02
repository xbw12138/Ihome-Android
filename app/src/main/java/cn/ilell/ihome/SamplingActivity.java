package cn.ilell.ihome;

/**
 * Created by wan on 5/27/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.utils.Progress_Dialog;
import cn.ilell.ihome.utils.SharedPreference;

public class SamplingActivity extends Activity {
    private ImageButton saveButton, stopButton;
    TextView info, info_time;
    float x = 0;
    float y = 0;
    int sample_time;
    Map<String, SampleResult> sampleResultMap = new HashMap<String, SampleResult>();
    List<String> selectWIFI=new ArrayList<>(); 
    private WifiManager wifiManager;
    private FileOutputStream out;
    private SharedPreference sharedPreference;
    private boolean stop=false;
    ProgressDialog dialog;
    private OnClickListener listener = new OnClickListener(){//这个没有写好
        @Override
        public void onClick(View v) {
            if(x==0&&y==0){
                Toast.makeText(SamplingActivity.this,"请选择当前位置",Toast.LENGTH_SHORT).show();
                return;
            }
            if(v==saveButton){
                dialog = Progress_Dialog.CreateProgressDialog(SamplingActivity.this);
                dialog.show();
                sampleResultMap.clear();
                sample_time = 0;
                stop=true;
                try {
                    //EditText filename = (EditText) findViewById(R.id.filename);
                    //String name = String.valueOf(filename.getText());

                    out = openFileOutput("map.txt", Context.MODE_APPEND);
                }
                catch (IOException e) {
                    Log.e("Exception", e.toString());
                }
            }
            /*if(v==stopButton){
                System.out.println("save");
                try {
                    String out_content = String.format("@%f %f", x,y);
                    System.out.println(out_content);
                    out.write(out_content.getBytes("UTF-8"));
                }
                catch (IOException e) {
                    Log.e("Exception", e.toString());
                }
                for(Map.Entry<String, SampleResult>entry:sampleResultMap.entrySet()){
                    SampleResult one_sample = entry.getValue();
                    one_sample.rssi = one_sample.rssi/one_sample.time;
                    String out_content =
                            String.format(" %s %d", entry.getKey(), one_sample.rssi);
                    System.out.println(out_content);
                    try {
                        out.write(out_content.getBytes("UTF-8"));
                    }
                    catch (IOException e) {
                        Log.e("Exception", e.toString());
                    }
                }
                sample_time = 0;
                try {
                    out.close();
                    sharedPreference.KeepFinish();
                }
                catch (IOException e) {
                    Log.e("Exception", e.toString());
                }

            }*/
        }

    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sampling);
        sharedPreference=new SharedPreference(this);
        RelativeLayout rl_Main;
		rl_Main = (RelativeLayout) findViewById(R.id.rl_main);
		rl_Main.addView( new MyView(this));
        saveButton = (ImageButton)this.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(listener);
        saveButton.bringToFront();
        //stopButton = (Button)this.findViewById(R.id.stopButton);
        //stopButton.setOnClickListener(listener);
        info=(TextView) this.findViewById(R.id.info);
        info.bringToFront();
        info_time=(TextView) this.findViewById(R.id.sample_time);
        info_time.bringToFront();
        if(!BaseData.listWifi.isEmpty()){
            for(int i=0;i<BaseData.listWifi.size();i++){
                String a=BaseData.listWifi.get(i).toString();
                String b[]=a.split("-");
                selectWIFI.add(b[2]);
            }
            //Toast.makeText(SamplingActivity.this, BaseData.listWifi.get(0).toString(),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SamplingActivity.this, "空的",Toast.LENGTH_SHORT).show();
        }
        if(!selectWIFI.isEmpty()){
            for(int i=0;i<selectWIFI.size();i++){
                Log.i("xbw14250",selectWIFI.get(i).toString());
            }
        }

        init();
        File f=new File(getFilesDir(), "map.txt");
        if(f.exists()){
            Toast.makeText(SamplingActivity.this, "删除成功", 1).show();
            f.delete();
        }
	}
    private void init(){
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        openWifi();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    sample_time++;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SamplingActivity.this.info_time.setText(Integer.toString(sample_time));
                        }
                    });
                    try {
                        Thread.sleep(1000);
                        wifiManager.startScan();
                        List<ScanResult> list = wifiManager.getScanResults();
                        for (ScanResult result : list) {
                            for(String str : selectWIFI){
                                if(str.equals(result.BSSID)){
                                    Log.i("xbw14250",str);
                                    SampleResult tmp = new SampleResult();
                                    try{
                                        tmp = sampleResultMap.get(result.BSSID);
                                        tmp.rssi += result.level;
                                        tmp.time++;
                                    }
                                    catch(NullPointerException e){
                                        tmp = new SampleResult();
                                        tmp.rssi = result.level;
                                        tmp.time = 1;
                                    }
                                    sampleResultMap.put(result.BSSID, tmp);
                                    break;
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(sample_time>=15&&stop){
                        dialog.dismiss();

                        System.out.println("save");
                        try {
                            String out_content = String.format("@%f %f", x,y);
                            System.out.println(out_content);
                            out.write(out_content.getBytes("UTF-8"));
                        }
                        catch (IOException e) {
                            Log.e("Exception", e.toString());
                        }
                        for(Map.Entry<String, SampleResult>entry:sampleResultMap.entrySet()){
                            SampleResult one_sample = entry.getValue();
                            one_sample.rssi = one_sample.rssi/one_sample.time;
                            String out_content =
                                    String.format(" %s %d", entry.getKey(), one_sample.rssi);
                            System.out.println(out_content);
                            try {
                                out.write(out_content.getBytes("UTF-8"));
                            }
                            catch (IOException e) {
                                Log.e("Exception", e.toString());
                            }
                        }
                        sample_time = 0;
                        stop=false;
                        try {
                            out.close();
                            sharedPreference.KeepFinish();
                        }
                        catch (IOException e) {
                            Log.e("Exception", e.toString());
                        }
                    }
                }
            }
        }).start();
    }
    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

	class MyView extends View {
		Paint paint = new Paint();
		Point point = new Point();
		public MyView(Context context) {
			super(context);
            this.setY(0);
            paint.setColor(Color.RED);
			paint.setStrokeWidth(15);
			paint.setStyle(Paint.Style.STROKE);
		}
		@Override
		protected void onDraw(Canvas canvas) {
            float scalY=getHeight();
            float scalX=getWidth();
			Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.pingmiantu);

			//canvas.drawBitmap(b, 0, 0, paint);
            //canvas.drawCircle(point.x, point.y, 5, paint);
            canvas.save();
            Matrix matrix=new Matrix();
            int width = b.getWidth();//获取资源位图的宽
            int height = b.getHeight();//获取资源位图的高
            float w = scalX/b.getWidth();
            float h = scalY/b.getHeight();
            matrix.postScale(w, h);//获取缩放比例
            Bitmap dstbmp = Bitmap.createBitmap(b,0,0,
                    width,height,matrix,true);//根据缩放比例获取新的位图
            canvas.drawBitmap(dstbmp,0,0, paint); //在屏幕上画出位图
            canvas.drawCircle(point.x, point.y, 5, paint);
            canvas.restore();

		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
					point.x = event.getX();
					point.y = event.getY();
                    SamplingActivity.this.info.setText("x="+event.getX()+"y="+event.getY());
			}
			invalidate();
			return true;
		}
	}

	class Point {
		float x, y;
	}

    class SampleResult{
        String BSSID;
        int rssi;
        int time;
    }

}
