package com.letsplaydota.slowgram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by andrew on 21.05.15.
 */
public class ContactListAdapter extends BaseAdapter {
    private Context context;
    private int resourceId;

    public ContactListAdapter(Context context, int resourceId) {
        this.context = context;
        this.resourceId = resourceId;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return new Object();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resourceId, viewGroup, false);
        }

        return view;
    }
}
