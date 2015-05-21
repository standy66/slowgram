package com.letsplaydota.slowgram;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by andrew on 21.05.15.
 */
public class ContactMessagesFragment extends Fragment {

    ListView messagesListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_messages_fragment, container, false);
        messagesListView = (ListView) v.findViewById(R.id.message_list_view);
        messagesListView.setAdapter(new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.single_message));
        messagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.main_layout_fragment, new VievMessageFragment()).commit();
            }
        });
        return v;
    }
}
