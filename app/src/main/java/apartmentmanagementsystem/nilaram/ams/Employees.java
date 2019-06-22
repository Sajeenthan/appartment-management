package apartmentmanagementsystem.nilaram.ams;

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

public class Employees extends Fragment {

    public Employees() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null)
            getActivity().setTitle("Employees");

        View view=inflater.inflate(R.layout.fragment_employees, container, false);

        ConnectionClass connectionClass = new ConnectionClass();

        final List<HashMap<String, String>> visitors = connectionClass.GetEmployees(getContext());

        String[] from = {"fName", "lName", "address", "job", "contact", "salary"};
        int[] to = {R.id.employeeFirstName, R.id.employeeLastName, R.id.employeeAddress, R.id.employeeJob, R.id.employeeContact, R.id.employeeSalary};

        SimpleAdapter listViewAdapter = new SimpleAdapter(view.getContext(), visitors, R.layout.employee_list_view, from, to);
        ListView listView = view.findViewById(R.id.listview1);
        listView.setAdapter(listViewAdapter);

        return view;
    }
}
