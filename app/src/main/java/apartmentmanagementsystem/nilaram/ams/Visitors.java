package apartmentmanagementsystem.nilaram.ams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Visitors extends Fragment {

    private static final String MY_PREFS_NAME = "AMSPrefsFile";

    SharedPreferences prefs;
    String userID;

    public Visitors() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getActivity() != null)
            getActivity().setTitle("Visitors");

        View view = inflater.inflate(R.layout.fragment_visitors, container, false);

        prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("userID", null);

        ConnectionClass connectionClass = new ConnectionClass();

        final List<HashMap<String, String>> visitors = connectionClass.GetVisitors(getContext(), userID);

        String[] from = {"visitorName", "visitorAddress", "visitorContact", "in", "out"};
        int[] to = {R.id.visitorName, R.id.visitorAddress, R.id.visitorContact, R.id.visitorIn, R.id.visitorOut};

        SimpleAdapter listViewAdapter = new SimpleAdapter(view.getContext(), visitors, R.layout.visitor_list_view, from, to);
        ListView listView = view.findViewById(R.id.listview1);
        listView.setAdapter(listViewAdapter);

        return view;
    }
}