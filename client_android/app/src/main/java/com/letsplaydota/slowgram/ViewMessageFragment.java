package com.letsplaydota.slowgram;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.letsplaydota.slowgram.models.Message;

/**
 * Created by andrew on 22.05.15.
 */
public class ViewMessageFragment extends Fragment {
    private Message message;

    public void setMessage(Message message) {
        this.message = message;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_message_fragment, container, false);
        TextView messageSubject = (TextView) v.findViewById(R.id.message_subject);
        TextView messageBody = (TextView) v.findViewById(R.id.message_body);

        messageSubject.setText(message.getTitle());
        messageBody.setText(message.getBody());



        return v;
    }
}
