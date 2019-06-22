package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
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

public class Events extends Fragment {

    public Events() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null)
            getActivity().setTitle("Events");

        View view=inflater.inflate(R.layout.fragment_events, container, false);
        ListView listView = view.findViewById(R.id.listview1);

        ConnectionClass connectionClass = new ConnectionClass();

        final HashMap<String, String> events = connectionClass.GetEvents(getContext());

        int length = events.values().toArray().length;

        final String[] listEvents = Arrays.copyOf(events.values().toArray(), length, String[].class);
        final String[] listEventId = Arrays.copyOf(events.keySet().toArray(), length, String[].class);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                listEvents
        );

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewEventsIntent = new Intent(getActivity(), ViewEvents.class);
                viewEventsIntent.putExtra("event_ID", Integer.valueOf(listEventId[position]));
                Events.this.startActivity(viewEventsIntent);
            }
        });

        return view;
    }
}
