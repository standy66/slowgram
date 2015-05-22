package com.letsplaydota.slowgram;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.letsplaydota.slowgram.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 16.11.14.
 */
public class ChatArrayAdapter extends ArrayAdapter {
    private List<Message> chatMessageList = new ArrayList();

    public void add(Message object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId, List<Message> messages) {
        super(context, textViewResourceId);
        this.chatMessageList = messages;
        //add(new Message());
        //add(new Message());
        //add(new Message());
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public Message getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_message_entry, parent, false);
        }
        LinearLayout singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        Message chatMessageObj = getItem(position);
        TextView chatText = (TextView) row.findViewById(R.id.singleMessage);
        chatText.setText(chatMessageObj.toString());
        chatText.setBackgroundResource(chatMessageObj.isOwn() ? R.drawable.bubble_right : R.drawable.bubble_left);
        singleMessageContainer.setGravity(chatMessageObj.isOwn() ? Gravity.RIGHT : Gravity.LEFT);
        return row;
    }
}
