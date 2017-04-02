package cn.ilell.ihome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.ilell.ihome.adapter.WifiAdapter;
import cn.ilell.ihome.base.BaseSubPageActivity;

public class WifiListActivity extends BaseSubPageActivity {

    private WifiManager wifiManager;
    List<ScanResult> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_list,"WIFI选择");
        init();
    }
    private void init() {
        final String apname=getIntent().getStringExtra("state");
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        list = wifiManager.getScanResults();
        ListView listView = (ListView) findViewById(R.id.listView);
        if (list == null) {
            Toast.makeText(this, "附近没有wifi", Toast.LENGTH_LONG).show();
        }else {
            listView.setAdapter(new WifiAdapter(this,list));
        }
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(apname.equals("ap1")){
                    Intent mIntent = new Intent();
                    mIntent.putExtra("bap1_name",list.get(position).BSSID);
                    mIntent.putExtra("ap1_name",list.get(position).SSID);
                    setResult(1000, mIntent);
                    finish();
                }else if(apname.equals("ap2")){
                    Intent mIntent = new Intent();
                    mIntent.putExtra("bap2_name",list.get(position).BSSID);
                    mIntent.putExtra("ap2_name",list.get(position).SSID);
                    setResult(1001, mIntent);
                    finish();
                }
            }
        });
    }
}
