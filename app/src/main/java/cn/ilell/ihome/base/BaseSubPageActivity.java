package cn.ilell.ihome.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.ilell.ihome.R;
import cn.ilell.ihome.view.StatusBarCompat;

/**
 * Created by lhc35 on 2016/5/1.
 */
public class BaseSubPageActivity extends Activity implements View.OnClickListener {
    protected Context mContext;

    protected void INIT(int pageid,String title) {
        setContentView(pageid);
        mContext = this;
        //设置状态栏的颜色
        StatusBarCompat.compat(this, getResources().getColor(R.color.main_blue_light));
        TextView title_name = (TextView) findViewById(R.id.title_bar_name);
        title_name.setText(title);	//修改页面标题
    }   //初始化

    public void onTitleBarClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_back_btn:
                this.finish();
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
