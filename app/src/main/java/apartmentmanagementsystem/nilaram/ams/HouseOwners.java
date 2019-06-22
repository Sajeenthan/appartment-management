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

public class HouseOwners extends Fragment {

    private static final String MY_PREFS_NAME = "AMSPrefsFile";

    public HouseOwners() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null)
            getActivity().setTitle("House Owners");

        View view=inflater.inflate(R.layout.fragment_house_owners, container, false);

        ConnectionClass connectionClass = new ConnectionClass();

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        final String userID = prefs.getString("userID", null);

        final List<HashMap<String, String>> houseOwners = connectionClass.GetHouseOwners(getContext(), userID);

        String[] from = {"fName", "lName", "address", "contact", "houseId"};
        int[] to = {R.id.houseOwnerFirstName, R.id.houseOwnerLastName, R.id.houseOwnerAddress, R.id.houseOwnerContact, R.id.houseOwnerHouseId};

        SimpleAdapter listViewAdapter = new SimpleAdapter(view.getContext(), houseOwners, R.layout.house_owner_list_view, from, to);
        ListView listView = view.findViewById(R.id.listview1);
        listView.setAdapter(listViewAdapter);

        return view;
    }
}
