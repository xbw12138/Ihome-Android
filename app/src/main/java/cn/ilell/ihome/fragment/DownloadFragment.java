package cn.ilell.ihome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.ilell.ihome.R;
import cn.ilell.ihome.base.BaseFragment;
import cn.ilell.ihome.utils.HttpXmlClient;

/**
 * Created by Monkey on 2015/6/29.
 */
public class DownloadFragment extends BaseFragment{

    private Button btn_add;
    private EditText edit_url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_download, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.temp_swiperefreshlayout);


        // 刷新时，指示器旋转后变化的颜色
        //mSwipeRefreshLayout.setColorSchemeResources(R.color.main_blue_light, R.color.main_blue_dark);
        //mSwipeRefreshLayout.setOnRefreshListener(this);
        btn_add = (Button)  mView.findViewById(R.id.download_button);
        edit_url = (EditText) mView.findViewById(R.id.download_editText);
        initView();
        setListener();

        web.loadUrl(ip+"/ihome/z-download.php");
    }


    private void setListener() {
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.download_button:

                Map<String, String> params = new HashMap<String, String>();
                params.put("data", "1"+edit_url.getText().toString());
                String result = HttpXmlClient.post(ip+"/ihome/backdeal/ManageDownload.php", params);
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();

                break;

           /* case R.id. myButton2:

                //do something

                break;*/

        }
    }
}
