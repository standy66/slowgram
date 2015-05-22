package com.letsplaydota.slowgram;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.letsplaydota.slowgram.models.Contact;
import com.letsplaydota.slowgram.models.Dialog;
import com.letsplaydota.slowgram.models.Message;
import com.letsplaydota.slowgram.models.ServerConnector;

import java.util.List;

/**
 * Created by andrew on 21.05.15.
 */
public class DialogMessagesFragment extends Fragment {

    private ListView messagesListView;
    private Button sendNewMessageButton;
    private Button refreshButton;

    private Dialog dialog;
    private String token;

    public DialogMessagesFragment() {
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
        this.token = ServerConnector.getToken();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_messages_fragment, container, false);
        final List<Message> messages = ServerConnector.get().getMessages(token, dialog.getId(), 0, 0);
        messagesListView = (ListView) v.findViewById(R.id.message_list_view);
        final ChatArrayAdapter chatArrayAdapter = new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.single_message_entry, messages);
        messagesListView.setAdapter(chatArrayAdapter);

        refreshButton = (Button) v.findViewById(R.id.refresh);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int old_size = messages.size();
                List<Message> messages = ServerConnector.get().getMessages(token, dialog.getId(), 0, 0);
                for (int i = old_size; i < messages.size(); i++) {
                    chatArrayAdapter.add(messages.get(i));
                }
                messagesListView.setAdapter(chatArrayAdapter);
                messagesListView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        messagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message m = (Message) chatArrayAdapter.getItem(i);
                ViewMessageFragment fragment = new ViewMessageFragment();
                fragment.setMessage(m);

                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.main_layout_fragment, fragment).addToBackStack(null).commit();
            }
        });

        sendNewMessageButton = (Button) v.findViewById(R.id.send_new_message);
        sendNewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessageFragment fragment = new SendMessageFragment();
                fragment.setContact(dialog);
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.main_layout_fragment, fragment).addToBackStack(null).commit();
            }
        });

        return v;
    }
}
