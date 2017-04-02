package cn.ilell.ihome;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import cn.ilell.ihome.adapter.WifiAdapter;
import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.base.BaseSubPageActivity;
import cn.ilell.ihome.utils.SharedPreference;

/**
 * Created by xubowen on 16/9/29.
 */
public class BangdingsActivity extends BaseSubPageActivity {
    private ListView listViewWifi;
    private List<ScanResult> listWifiScan;
    private List<String> listWifi;
    private List<ScanResult> listWifiScanSave;
    private Button btnSaveWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.fragment_wifi,"室内WIFI绑定");
        findID();
        listWifiScan();
        loadWifiScan();
        initButtonSave();
    }
    private void onListItemClick(View v,int position,long id){
        try {
            CheckedTextView item = (CheckedTextView) v;
            if (position == 0) {
                onAllChecksClick(v, item.isChecked());
            }
            else{
                item.setChecked(item.isChecked());
            }
            btnSaveWifiEnabledDisabbled();
        }catch (Exception ex){
            Log.e(this.getClass().getName(), "onListItemClick--->" + ex.getMessage());
        }
    }
    public void onAllChecksClick (View view, boolean selected) {
        try {
            int count = listViewWifi.getCount();
            for (int i = 0; i < count; i++) {
                //CheckedTextView item = (CheckedTextView) listViewWifi.getSelectedItem();
                listViewWifi.setItemChecked(i, selected);
            }
        }catch (Exception ex){
            Log.e(this.getClass().getName(), "onAllChecksClick--->" + ex.getMessage());
        }
    }
    private void btnSaveWifiEnabledDisabbled() {
        try{
            SparseBooleanArray resultArray = listViewWifi.getCheckedItemPositions();
            int size = resultArray.size();
            for (int i = 0; i < size; i++) {
                if (resultArray.valueAt(i)) {
                    btnSaveWifi.setEnabled(true);
                    Log.i("CodecTestActivity", listViewWifi.getAdapter().getItem(resultArray.keyAt(i)).toString());
                    return;
                }
            }
            btnSaveWifi.setEnabled(false);
        }catch (Exception ex){
            Log.e(this.getClass().getName(), "onSelectedChecksClick--->" + ex.getMessage());
        }
    }
    public void findID(){
        listViewWifi = (ListView)findViewById(R.id.list_wifi);
        listViewWifi.setEmptyView(findViewById(R.id.empty_list_wifi));
        listViewWifi.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onListItemClick(view, position, id);
            }
        });
    }
    private void initButtonSave(){
        btnSaveWifi = (Button)findViewById(R.id.btn_Save_wifi);
        if (listWifiScan != null) {
            btnSaveWifi.setText(android.R.string.ok);
            btnSaveWifi.setEnabled(false);
            btnSaveWifi.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    onSelectedChecksClick ();
                }
            });
        }
        else
            btnSaveWifi.setVisibility(View.GONE);
    }
    public void onSelectedChecksClick () {
        try{
            SparseBooleanArray resultArray = listViewWifi.getCheckedItemPositions();
            int size = resultArray.size();
            if(!BaseData.listWifi.isEmpty()){
                BaseData.listWifi.clear();
            }
            for (int i = 0; i < size; i++)
                if (resultArray.valueAt(i)) {
                    BaseData.listWifi.add(listViewWifi.getAdapter().getItem(resultArray.keyAt(i)).toString());
                    Log.i("CodecTestActivity", listViewWifi.getAdapter().getItem(resultArray.keyAt(i)).toString());
                }
            Intent intent = new Intent();
            intent.setClass(mContext, SamplingActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception ex){
            Log.e(this.getClass().getName(), "onSelectedChecksClick--->" + ex.getMessage());
        }
    }
    private void listWifiScan(){
        try {
            WifiManager wifi = (WifiManager) this
                    .getSystemService(Context.WIFI_SERVICE);
            wifi.startScan();
            List<ScanResult> listWifiScanAll = wifi.getScanResults();
            if (listWifiScanAll != null) {
                StringBuilder strWifi;
                listWifi = new ArrayList<String>();
                listWifiScan = new ArrayList<ScanResult>();
                for (ScanResult scanResult:listWifiScanAll) {
                    if (scanResult == null || scanResult.level >= 0)
                        continue;
                    strWifi = new StringBuilder();
                    if (TextUtils.isEmpty(scanResult.SSID))
                        strWifi.append("....");
                    else
                        strWifi.append(scanResult.SSID);
                    strWifi.append("--");
                    strWifi.append( scanResult.BSSID);
                    strWifi.append("--");
                    strWifi.append(scanResult.level);
                    strWifi.append(" dBm");
                    listWifi.add(strWifi.toString());
                    listWifiScan.add(scanResult);
                }
                if (listWifiScan.size() > 0){
                    strWifi = new StringBuilder();
                    strWifi.append("选择全部");
                    listWifi.add(0,strWifi.toString());
                    listWifiScan.add(0,null);
                }
            }
        }catch (Exception ex){
            listWifi.clear();
            listWifiScan.clear();
            Log.e(this.getClass().getName(), "listWifiScan--->" + ex.getMessage());
        }
    }
    private void loadWifiScan(){
        try {
            ArrayAdapter<String> adaptador =
                    new ArrayAdapter<String>(this,
                            R.layout.simple_list_item_checked, listWifi);
            listViewWifi.setAdapter(adaptador);
            //listViewWifi.setAdapter(new AdapterWifi(this,listWifiScan));
        }catch (Exception ex){
            listWifi.clear();
            Log.e(this.getClass().getName(), "loadWifiScan--->" + ex.getMessage());
        }
    }

}
