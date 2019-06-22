package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class Notifications extends Fragment {

    private static final String MY_PREFS_NAME = "AMSPrefsFile";

    public Notifications() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null)
            getActivity().setTitle("Notifications");

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        ListView listView = view.findViewById(R.id.listview1);

        ConnectionClass connectionClass = new ConnectionClass();

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        final String userType = prefs.getString("userType", null);

        final HashMap<String, String> notifications = connectionClass.GetNotifications(getContext(), userType);

        int length = notifications.values().toArray().length;

        final String[] listNotifications = Arrays.copyOf(notifications.values().toArray(), length, String[].class);
        final String[] listNotificationsId = Arrays.copyOf(notifications.keySet().toArray(), length, String[].class);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listNotifications);

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewNotificationsIntent = new Intent(getActivity(), ViewNotifications.class);
                viewNotificationsIntent.putExtra("notification_ID", listNotificationsId[position]);
                Notifications.this.startActivity(viewNotificationsIntent);
            }
        });

        return view;
    }
}
