package cn.ilell.ihome.utils;

import java.util.HashMap;
import java.util.Map;

import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.service.MsgService;

/**
 * Created by lhc35 on 2016/5/23.
 */
public class OperatingCommand {
    Map<String,String> comMap = null;

    public OperatingCommand() {
        comMap = new HashMap<String,String>();
        initMapData();
    }
    private void initMapData() {
        comMap.put("打开客厅灯","0001;true");
        comMap.put("关闭客厅灯","0001;false");

        comMap.put("打开客厅空调","0002;true");
        comMap.put("关闭客厅空调","0002;false");

        comMap.put("升高客厅空调温度","000206;false");
        comMap.put("降低客厅空调温度","000207;false");

        comMap.put("设置客厅空调风向自动","000212;false");
        comMap.put("设置客厅空调风向手动","000213;false");

        comMap.put("设置客厅空调风速自动","000208;false");
        comMap.put("设置客厅空调风速一级","000209;false");
        comMap.put("设置客厅空调风速二级","000210;false");
        comMap.put("设置客厅空调风速三级","000211;false");

        comMap.put("设置客厅空调模式自动","000214;false");
        comMap.put("设置客厅空调模式制冷","000202;false");
        comMap.put("设置客厅空调模式除湿","000203;false");
        comMap.put("设置客厅空调模式送风","000204;false");
        comMap.put("设置客厅空调模式制暖","000205;false");

        comMap.put("打开卧室调光灯","0004;true");
        comMap.put("关闭卧室调光灯","0004;false");

        comMap.put("打开卧室风扇","0003;true");
        comMap.put("关闭卧室风扇","0003;false");

        comMap.put("打开卧室窗帘","0008;true");
        comMap.put("关闭卧室窗帘","0008;false");

        comMap.put("打开厨房灯","0006;true");
        comMap.put("关闭厨房灯","0006;false");

        comMap.put("打开洗手间灯","0005;true");
        comMap.put("关闭洗手间灯","0005;false");
    }

    public String dealCommand(String comnd) {
        //将识别后的文字发送给服务器分析执行
        MsgService.sendMsg("1/12/"+ BaseData.home_id+"/"+comnd);
        return comnd;
        /*if (!comMap.containsKey(comnd))
            return "无法识别的操作指令";
        String result,comvalue;
        comvalue = comMap.get(comnd);
        String[] com = comvalue.split(";");
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceID", com[0]);
        params.put("deviceState",com[1]);
        result = HttpXmlClient.post("http://115.159.127.79/ihome/backdeal/SetState.php", params);
        return result;*/
    }
}
