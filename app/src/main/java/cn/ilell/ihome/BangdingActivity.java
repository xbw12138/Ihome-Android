package cn.ilell.ihome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ilell.ihome.base.BaseSubPageActivity;
import cn.ilell.ihome.utils.SharedPreference;

/**
 * Created by xubowen on 16/9/29.
 */
public class BangdingActivity extends BaseSubPageActivity {
    private TextView tvap1;
    private TextView tvap2;
    private ImageView imgap1;
    private ImageView imgap2;
    private TextView btnfinish;
    private SharedPreference sharedPreference;
    private String SSID1;
    private String SSID2;
    private String BSSID1;
    private String BSSID2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_bangding,"室内WIFI绑定");
        findID();
        initEvent();
    }
    public void findID(){
        sharedPreference=new SharedPreference(this);
        tvap1=(TextView)findViewById(R.id.tvap1);
        tvap2=(TextView)findViewById(R.id.tvap2);
        imgap1=(ImageView)findViewById(R.id.imgap1);
        imgap2=(ImageView)findViewById(R.id.imgap2);
        btnfinish=(TextView)findViewById(R.id.btnfinish);
        if(sharedPreference.isKeep(this.getClass().getName())){
            btnfinish.setVisibility(View.VISIBLE);
            tvap1.setText(sharedPreference.getAP1());
            tvap2.setText(sharedPreference.getAP2());
        }else{
            btnfinish.setVisibility(View.GONE);
        }
    }
    public void initEvent(){
        imgap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent=new Intent();
                mIntent.setClass(BangdingActivity.this, WifiListActivity.class);
                mIntent.putExtra("state", "ap1");
                startActivityForResult(mIntent, 1000);
            }
        });
        imgap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent=new Intent();
                mIntent.setClass(BangdingActivity.this, WifiListActivity.class);
                mIntent.putExtra("state", "ap2");
                startActivityForResult(mIntent, 1001);
            }
        });
        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent=new Intent();
                mIntent.setClass(BangdingActivity.this, HomeActivity.class);
                startActivity(mIntent);
                //2017 03 16
                //sharedPreference.KeepFinish();
                //sharedPreference.KeepAP1(SSID1,BSSID1);
                //sharedPreference.KeepAP2(SSID2,BSSID2);

                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == 1000) {
            SSID1 = data.getStringExtra("ap1_name");
            BSSID1 = data.getStringExtra("bap1_name");
            sharedPreference.KeepAP1(SSID1,BSSID1);
            tvap1.setText(SSID1);
        }else if(requestCode==1001){
            SSID2 = data.getStringExtra("ap2_name");
            BSSID2 = data.getStringExtra("bap2_name");
            sharedPreference.KeepAP2(SSID2,BSSID2);
            tvap2.setText(SSID2);
        }
        if(tvap1.getText().toString()!=null&&tvap2.getText().toString()!=null){
            btnfinish.setVisibility(View.VISIBLE);
        }
        //if(sharedPreference.isKeep(this.getClass().getName())){
        //    btnfinish.setVisibility(View.VISIBLE);
        //}
    }

}
