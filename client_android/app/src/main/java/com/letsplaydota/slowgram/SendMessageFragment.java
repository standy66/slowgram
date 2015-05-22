package com.letsplaydota.slowgram;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.letsplaydota.slowgram.models.Contact;
import com.letsplaydota.slowgram.models.Dialog;
import com.letsplaydota.slowgram.models.ServerConnector;

/**
 * Created by andrew on 22.05.15.
 */
public class SendMessageFragment extends Fragment {
    private Button sendMessage;
    private EditText messageSubject;
    private EditText messageBody;
    private Dialog dialog;


    public void setContact(Dialog contact) {
        this.dialog = contact;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.send_message_fragment, container, false);

        sendMessage = (Button) v.findViewById(R.id.send_message);
        messageSubject = (EditText) v.findViewById(R.id.new_message_subject);
        messageBody = (EditText) v.findViewById(R.id.new_message_body);


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Send something here", Toast.LENGTH_LONG).show();;
                ServerConnector.get().sendMessage(ServerConnector.getToken(),
                        dialog.getId(), messageSubject.getText().toString(), messageBody.getText().toString());
            }
        });
        return v;
    }
}
