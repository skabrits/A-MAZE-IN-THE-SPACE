package com.example.sevak.themaze;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int struct[];
    LayoutInflater inflter;

    private static class ViewHolder {
        ImageView imageViewc;
    }

    public CustomAdapter(Context applicationContext, int[] struct) {
        this.context = applicationContext;
        this.struct = struct;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return struct.length;
    }

    @Override
    public Integer getItem(int position) {
        return struct[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder ViewHolder;
        View iv = convertView;
        if (convertView == null) {
            iv = inflter.inflate(R.layout.linres, parent, false);
            ViewHolder = new ViewHolder();
            ViewHolder.imageViewc = (ImageView) iv.findViewById(R.id.iconV);
            iv.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) iv.getTag();
        }
        ViewHolder.imageViewc.setImageDrawable(context.getDrawable(struct[position]));
        return iv;
    }
}