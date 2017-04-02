package cn.ilell.ihome;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.ilell.ihome.base.BaseData;
import cn.ilell.ihome.base.BaseFamilyActivity;
import cn.ilell.ihome.utils.HttpXmlClient;

/**
 * Created by lhc35 on 2016/5/6.
 */
public class FamilyMemoActivity extends BaseFamilyActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT("家庭备忘录","添加","例如:这周六全家郊游");
        web.loadUrl("http://"+ BaseData.IP+"/ihome/z-familymemo.php");
        backUrl = "http://"+ BaseData.IP+"/ihome/backdeal/AddNote.php";
    }

    public void onFamilyClick(View v) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Note", edit.getText().toString());
        String result = HttpXmlClient.post(backUrl, params);
        if (result.equals("添加成功"))
            edit.setText("");
        Toast.makeText(FamilyMemoActivity.this, result, Toast.LENGTH_SHORT).show();
    }
}
