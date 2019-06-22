package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Profile extends Fragment {

    private static final String MY_PREFS_NAME = "AMSPrefsFile";
    boolean allowRefresh = false;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null)
            getActivity().setTitle("Profile");

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        final String userID = prefs.getString("userID", null);
        final String userType = prefs.getString("userType", null);

        ConnectionClass connectionClass = new ConnectionClass();
        List<String> profile = connectionClass.ViewProfile(getContext(), userID, userType);

        TextView fName,  lName, mail, contact, houseId, address, job, salary;

        ConstraintLayout houseOwnerProfileConstraintLayout = view.findViewById(R.id.houseOwnerProfileView);
        ConstraintLayout employeeProfileConstraintLayout = view.findViewById(R.id.employeeProfileView);

        if(userType != null){
            if (userType.equals("TY002")) {
                houseOwnerProfileConstraintLayout.setVisibility(View.VISIBLE);
                employeeProfileConstraintLayout.setVisibility(View.INVISIBLE);

                fName = view.findViewById(R.id.viewHouseOwnerFName);
                lName = view.findViewById(R.id.viewHouseOwnerLName);
                address = view.findViewById(R.id.viewHouseOwnerAddress);
                houseId = view.findViewById(R.id.viewHouseOwnerHouseId);
                mail = view.findViewById(R.id.viewHouseOwnerMail);
                contact = view.findViewById(R.id.viewHouseOwnerContact);

                fName.setText(profile.get(0));
                lName.setText(profile.get(1));
                address.setText(profile.get(2));
                houseId.setText(profile.get(3));
                mail.setText(profile.get(4));
                contact.setText(profile.get(5));
            } else if (userType.equals("TY003")) {
                employeeProfileConstraintLayout.setVisibility(View.VISIBLE);
                houseOwnerProfileConstraintLayout.setVisibility(View.INVISIBLE);

                fName = view.findViewById(R.id.viewEmployeeFName);
                lName = view.findViewById(R.id.viewEmployeeLName);
                address = view.findViewById(R.id.viewEmployeeAddress);
                job = view.findViewById(R.id.viewEmployeeJob);
                salary = view.findViewById(R.id.viewEmployeeSalary);
                mail = view.findViewById(R.id.viewEmployeeMail);
                contact = view.findViewById(R.id.viewEmployeeContact);

                fName.setText(profile.get(0));
                lName.setText(profile.get(1));
                address.setText(profile.get(2));
                job.setText(profile.get(3));
                salary.setText(profile.get(4));
                mail.setText(profile.get(5));
                contact.setText(profile.get(6));
            }
        }

        Button editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allowRefresh = true;
                Intent editProfileIntent = new Intent(getActivity(), EditProfile.class);
                editProfileIntent.putExtra("userId", userID);
                editProfileIntent.putExtra("userType", userType);
                Profile.this.startActivity(editProfileIntent);
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
