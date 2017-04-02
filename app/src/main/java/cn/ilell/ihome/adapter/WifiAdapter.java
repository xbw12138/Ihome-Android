package cn.ilell.ihome.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ilell.ihome.R;

/**
 * Created by xubowen on 16/9/29.
 */
public class WifiAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<ScanResult> list;
    Context context;
    public WifiAdapter(Context context, List<ScanResult> list) {
        // TODO Auto-generated constructor stub
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context=context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_wifi_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txt_title = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.txt_content = (TextView) convertView.findViewById(R.id.signal_strenth);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ScanResult scanResult = list.get(position);
        viewHolder.txt_title.setText(scanResult.SSID);
        viewHolder.txt_content.setText(String.valueOf(Math.abs(scanResult.level)));
        if (Math.abs(scanResult.level) > 100) {
            viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.wifi5));
        } else if (Math.abs(scanResult.level) > 80) {
            viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.wifi4));
        } else if (Math.abs(scanResult.level) > 70) {
            viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.wifi4));
        } else if (Math.abs(scanResult.level) > 60) {
            viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.wifi3));
        } else if (Math.abs(scanResult.level) > 50) {
            viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.wifi2));
        } else {
            viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.wifi1));
        }
        return convertView;
    }
    static class ViewHolder {
        TextView txt_title;
        TextView txt_content;
        ImageView image;
    }

}

