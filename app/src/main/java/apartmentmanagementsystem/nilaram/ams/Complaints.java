package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class Complaints extends Fragment {

    private static final String MY_PREFS_NAME = "AMSPrefsFile";
    boolean allowRefresh = false;

    SharedPreferences prefs;
    String userID;

    public Complaints() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null)
            getActivity().setTitle("Complaints");

        View view = inflater.inflate(R.layout.fragment_complaints, container, false);
        ListView listView = view.findViewById(R.id.listview1);

        prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("userID", null);

        ConnectionClass connectionClass = new ConnectionClass();

        final HashMap<String, String> complaints = connectionClass.GetComplaints(getContext(), userID);

        int length = complaints.values().toArray().length;

        final String[] listComplaints = Arrays.copyOf(complaints.values().toArray(), length, String[].class);
        final String[] listComplaintId = Arrays.copyOf(complaints.keySet().toArray(), length, String[].class);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                listComplaints
        );

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewComplaintsIntent = new Intent(getActivity(), ViewComplaints.class);
                viewComplaintsIntent.putExtra("complaint_ID", listComplaintId[position]);
                Complaints.this.startActivity(viewComplaintsIntent);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.newComplaint);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allowRefresh = true;
                Intent newComplaintsIntent = new Intent(getActivity(), NewComplaint.class);
                Complaints.this.startActivity(newComplaintsIntent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (allowRefresh) {
            allowRefresh = false;
            FragmentManager fm = getFragmentManager();

            if(fm != null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
            }
        }
    }
}
