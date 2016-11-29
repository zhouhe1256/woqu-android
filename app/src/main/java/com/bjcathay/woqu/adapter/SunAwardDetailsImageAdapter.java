
package com.bjcathay.woqu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.util.SizeUtil;
import com.bjcathay.woqu.util.SystemUtil;

import java.util.List;

/**
 * Created by zhouh on 15-10-13.
 */
public class SunAwardDetailsImageAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> pahts;
    int width;
    LinearLayout.LayoutParams layoutParams;

    public SunAwardDetailsImageAdapter(Context context, List<String> pahts) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        width = SystemUtil.getScriptWidth()-30;
        layoutParams = new LinearLayout.LayoutParams(width/3, width/3);
        this.pahts = pahts;
    }
    public void setData(List<String> pahts){
        this.pahts=pahts;
        // notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return pahts.size();
    }

    @Override
    public Object getItem(int position) {
        return pahts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int postion, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_sunawarddetails_grida, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_sgrida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageViewAdapter.adapt(holder.imageView,pahts.get(postion)+"@360x360",true);
        holder.imageView.setLayoutParams(layoutParams);
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
    }
}
