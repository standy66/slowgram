package com.letsplaydota.slowgram;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by andrew on 21.05.15.
 */
public class ContactMessagesFragment extends Fragment {

    ListView messagesListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_messages_fragment, container);
        messagesListView = (ListView) v.findViewById(R.id.message_list_view);



        return v;
    }
}
