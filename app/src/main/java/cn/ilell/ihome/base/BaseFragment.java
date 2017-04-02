package cn.ilell.ihome.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.ilell.ihome.R;
import cn.ilell.ihome.adapter.MyRecyclerViewAdapter;
import cn.ilell.ihome.adapter.MyStaggeredViewAdapter;

/**
 * Created by lhc35 on 2016/4/13.
 */
public class BaseFragment extends Fragment implements
        MyRecyclerViewAdapter.OnItemClickListener, MyStaggeredViewAdapter.OnItemClickListener,
        View.OnClickListener {
    protected View mView;
    protected Context mContext;
    protected WebView web;
    protected String ip="http://"+BaseData.IP;

    protected void initView() {
        mContext = this.getActivity();
        web = (WebView) mView.findViewById(R.id.webView);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        //打开网页时不调用系统浏览器， 而是在本WebView中显示
        web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    @Override
    public void onItemClick(View view, int position) {
        //SnackbarUtil.show(mRecyclerView, getString(R.string.item_clicked), 0);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        //SnackbarUtil.show(mRecyclerView, getString(R.string.item_longclicked), 0);
    }

    public void onDestroy() {
        super.onDestroy();
        web.loadUrl("about:blank");
    }
    @Override
    public void onClick(View v) {

    }
}
