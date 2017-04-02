package cn.ilell.ihome;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.base.BaseSubPageActivity;
import cn.ilell.ihome.utils.HttpXmlClient;

/**
 * Created by lhc35 on 2016/5/8.
 */
public class ScheduleActivity extends BaseSubPageActivity {
    private Spinner spinnerTask,spinnerMode;
    private Button btn_add;
    private TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_schedule,"添加计划任务");

        initView();
        initData();
    }

    private void initView() {
        spinnerTask = (Spinner) findViewById(R.id.schedule_spinner_task);
        spinnerMode = (Spinner) findViewById(R.id.schedule_spinner_mode);
        btn_add = (Button) findViewById(R.id.schedule_btn_add);
        btn_add.setOnClickListener(this);
        timePicker = (TimePicker) findViewById(R.id.schedule_time);
    }

    private void initData() {
        List<String> listTask = new ArrayList<String>();
        //第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
        listTask.add("客厅灯关");
        listTask.add("客厅空调关");
        listTask.add("卧室风扇关");
        listTask.add("卧室调光灯关");
        listTask.add("洗手间灯关");
        listTask.add("厨房灯关");
        listTask.add("卧室窗帘关");
        listTask.add("客厅空调开");
        listTask.add("卧室调光灯开");
        listTask.add("客厅灯开");
        listTask.add("卧室风扇开");
        listTask.add("洗手间灯开");
        listTask.add("厨房灯开");
        listTask.add("卧室窗帘开");
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        ArrayAdapter<String> adapterTask;
        adapterTask = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listTask);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapterTask.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        spinnerTask.setAdapter(adapterTask);


        List<String> listMode = new ArrayList<String>();
        //第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
        listMode.add("每天");
        listMode.add("仅一次");
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        ArrayAdapter<String> adapterMode;
        adapterMode = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listMode);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapterMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        spinnerMode.setAdapter(adapterMode);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.schedule_btn_add:
                String task = (String) spinnerTask.getSelectedItem();
                String mode = (String) spinnerMode.getSelectedItem();
                int hour = timePicker.getCurrentHour();
                int minu = timePicker.getCurrentMinute();
                Map<String, String> params = new HashMap<String, String>();
                params.put("operation", task);
                params.put("frequency", mode);
                params.put("hour", hour+"");
                params.put("minute", minu+"");

                String xml = HttpXmlClient.post("http://"+ BaseData.IP+"/ihome/backdeal/SetTiming.php", params);
                Toast.makeText(ScheduleActivity.this,xml, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
