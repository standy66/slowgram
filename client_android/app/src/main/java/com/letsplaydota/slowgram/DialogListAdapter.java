package com.letsplaydota.slowgram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.letsplaydota.slowgram.models.Contact;
import com.letsplaydota.slowgram.models.Dialog;

import java.util.Collection;
import java.util.List;

/**
 * Created by andrew on 21.05.15.
 */
public class DialogListAdapter extends BaseAdapter {
    private Context context;
    private int resourceId;
    private List<Dialog> contactCollection;

    public void add(Dialog d) {
        contactCollection.add(d);
    }

    public DialogListAdapter(Context context, int resourceId, List<Dialog> contactCollection) {
        this.context = context;
        this.resourceId = resourceId;
        this.contactCollection = contactCollection;
    }

    @Override
    public int getCount() {
        return contactCollection.size();
    }

    @Override
    public Object getItem(int i) {
        return contactCollection.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            Dialog dialog = (Dialog) getItem(i);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resourceId, viewGroup, false);
            TextView contactName = (TextView) view.findViewById(R.id.contact_name);
            TextView lastMessage = (TextView) view.findViewById(R.id.last_message);
            contactName.setText(dialog.getRecipient().getPhone());
            if (dialog.getLastMessage() != null) {
                lastMessage.setText(dialog.getLastMessage().getBody());
            }


        }

        return view;
    }
}
