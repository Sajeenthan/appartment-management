package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Edit Profile");

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userId");
        final String userType = intent.getStringExtra("userType");

        final ConnectionClass connectionClass = new ConnectionClass();
        List<String> profile = connectionClass.ViewProfile(getApplicationContext(), userID, userType);

        final EditText hFName, hLName, hAddress, hMail, hContact, eFName, eLName, eAddress, eMail, eContact;
        final TextView hHouseId, eJob, eSalary;

        hFName = findViewById(R.id.editHouseOwnerFName);
        hLName = findViewById(R.id.editHouseOwnerLName);
        hAddress = findViewById(R.id.editHouseOwnerAddress);
        hHouseId = findViewById(R.id.editHouseOwnerHouseId);
        hMail = findViewById(R.id.editHouseOwnerMail);
        hContact = findViewById(R.id.editHouseOwnerContact);

        eFName = findViewById(R.id.editEmployeeFName);
        eLName = findViewById(R.id.editEmployeeLName);
        eAddress = findViewById(R.id.editEmployeeAddress);
        eJob = findViewById(R.id.editEmployeeJob);
        eSalary = findViewById(R.id.editEmployeeSalary);
        eMail = findViewById(R.id.editEmployeeMail);
        eContact = findViewById(R.id.editEmployeeContact);

        ConstraintLayout houseOwnerProfileConstraintLayout = findViewById(R.id.houseOwnerProfileEdit);
        ConstraintLayout employeeProfileConstraintLayout = findViewById(R.id.employeeProfileEdit);

        if(userType != null){
            if (userType.equals("TY002")) {
                houseOwnerProfileConstraintLayout.setVisibility(View.VISIBLE);
                employeeProfileConstraintLayout.setVisibility(View.INVISIBLE);

                hFName.setText(profile.get(0));
                hLName.setText(profile.get(1));
                hAddress.setText(profile.get(2));
                hHouseId.setText(profile.get(3));
                hMail.setText(profile.get(4));
                hContact.setText(profile.get(5));
            } else if (userType.equals("TY003")) {
                employeeProfileConstraintLayout.setVisibility(View.VISIBLE);
                houseOwnerProfileConstraintLayout.setVisibility(View.INVISIBLE);

                eFName.setText(profile.get(0));
                eLName.setText(profile.get(1));
                eAddress.setText(profile.get(2));
                eJob.setText(profile.get(3));
                eSalary.setText(profile.get(4));
                eMail.setText(profile.get(5));
                eContact.setText(profile.get(6));
            }
        }

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> newProfile = new ArrayList<>();

                if(userType != null){
                    if (userType.equals("TY002")) {
                        newProfile.add(hFName.getText().toString());
                        newProfile.add(hLName.getText().toString());
                        newProfile.add(hAddress.getText().toString());
                        newProfile.add(hMail.getText().toString());
                        newProfile.add(hContact.getText().toString());
                    } else if (userType.equals("Ty003")) {
                        newProfile.add(eFName.getText().toString());
                        newProfile.add(eLName.getText().toString());
                        newProfile.add(eAddress.getText().toString());
                        newProfile.add(eMail.getText().toString());
                        newProfile.add(eContact.getText().toString());
                    }
                }

                boolean updateSuccess = connectionClass.UpdateProfile(getApplicationContext(), userID, newProfile);

                if(updateSuccess) {
                    finish();
                }
            }
        });
    }
}
