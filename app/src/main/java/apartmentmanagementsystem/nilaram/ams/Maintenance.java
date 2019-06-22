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

public class Maintenance extends Fragment {

    private static final String MY_PREFS_NAME = "AMSPrefsFile";

    SharedPreferences prefs;
    String userID;

    public Maintenance() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null)
            getActivity().setTitle("Maintenance");

        View view=inflater.inflate(R.layout.fragment_maintenance, container, false);

        prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("userID", null);

        ConnectionClass connectionClass = new ConnectionClass();

        final List<HashMap<String, String>> maintenanceList = connectionClass.GetMaintenance(getContext(), userID);

        String[] from = {"month", "year", "gasAmount", "waterAmount", "electAmount", "securityAmount", "otherExpensive", "totalAmount"};
        int[] to = {R.id.maintenanceMonth, R.id.maintenanceYear, R.id.maintenanceGasBill, R.id.maintenanceWaterBill, R.id.maintenanceElectBill, R.id.maintenanceSecurityCharge, R.id.maintenanceOtherExpensive, R.id.maintenanceTotalAmount};

        SimpleAdapter listViewAdapter = new SimpleAdapter(view.getContext(), maintenanceList, R.layout.maintenance_list_view, from, to);
        ListView listView = view.findViewById(R.id.listview1);
        listView.setAdapter(listViewAdapter);

        return view;
    }
}
