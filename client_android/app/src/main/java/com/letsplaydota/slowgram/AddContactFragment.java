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

import com.letsplaydota.slowgram.models.ServerConnector;

/**
 * Created by andrew on 22.05.15.
 */
public class AddContactFragment extends Fragment {
    EditText phone;
    Button addContact;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_contact_fragment, container, false);

        addContact = (Button) v.findViewById(R.id.add_contact);
        phone = (EditText) v.findViewById(R.id.contact_phone);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Added", Toast.LENGTH_LONG).show();
                String sPhone = phone.getText().toString();
                ServerConnector.get().createDialog(ServerConnector.getToken(), sPhone);
            }
        });
        return v;
    }
}
