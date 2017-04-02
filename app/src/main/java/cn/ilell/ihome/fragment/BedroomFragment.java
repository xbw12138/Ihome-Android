package cn.ilell.ihome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ilell.ihome.R;
import cn.ilell.ihome.base.BaseFragment;

/**
 * Created by Monkey on 2015/6/29.
 */
public class BedroomFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_webonly, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.bedroom_swiperefreshlayout);


        // 刷新时，指示器旋转后变化的颜色
        //mSwipeRefreshLayout.setColorSchemeResources(R.color.main_blue_light, R.color.main_blue_dark);
        //mSwipeRefreshLayout.setOnRefreshListener(this);

        initView();
        setListener();

        web.loadUrl(ip+"/ihome/z-bedroom.php");
    }

    private void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

          /*  case R.id.bedroom_button:

                //do something
                text.setText("123");
                break;

           /* case R.id. myButton2:

                //do something

                break;*/

        }
    }
}
