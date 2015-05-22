package com.letsplaydota.slowgram;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.letsplaydota.slowgram.models.Contact;
import com.letsplaydota.slowgram.models.Dialog;
import com.letsplaydota.slowgram.models.ServerConnector;

import java.util.List;

/**
 * Created by andrew on 21.05.15.
 */
public class DialogListFragment extends Fragment {

    Button refresh;
    ListView listView;
    DialogListAdapter dialogListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_list_fragment, container, false);
        final String token = ServerConnector.getToken();
        dialogListAdapter = new DialogListAdapter(getActivity().getApplicationContext(), R.layout.dialog_list_entry, ServerConnector.get().getDialogs(token, 0, 0));

        listView = (ListView) v.findViewById(R.id.dialog_list_view);
        listView.setAdapter(dialogListAdapter);
        refresh = (Button) v.findViewById(R.id.refresh_dialog);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int old_size = dialogListAdapter.getCount();
                List<Dialog> dialogs = ServerConnector.get().getDialogs(token, 0, 0);
                for (int i = old_size; i < dialogs.size(); i++) {
                    dialogListAdapter.add(dialogs.get(i));
                }
                listView.setAdapter(dialogListAdapter);
                listView.setSelection(dialogListAdapter.getCount() - 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onListItemClick(adapterView, view, i, l);
            }
        });
        return v;
    }


    public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
        Toast.makeText(getActivity(), "It works", Toast.LENGTH_LONG).show();

        DialogMessagesFragment fragment = new DialogMessagesFragment();
        fragment.setDialog((Dialog) dialogListAdapter.getItem(position));

        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.main_layout_fragment, fragment).addToBackStack(null).commit();
    }
}
