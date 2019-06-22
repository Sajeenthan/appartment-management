package apartmentmanagementsystem.nilaram.ams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewComplaint extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "AMSPrefsFile";

    EditText newComplaintTitle, newComplaintDescription;
    Button btnSubmit;

    SharedPreferences prefs;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("New Complaint");

        newComplaintTitle = findViewById(R.id.newComplaintTitle);
        newComplaintDescription = findViewById(R.id.newComplaintDescription);
        btnSubmit = findViewById(R.id.complaintSubmit);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("userID", null);

        final String[] newComplaint = new String[3];
        final ConnectionClass connectionClass = new ConnectionClass();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newComplaint[0] = newComplaintTitle.getText().toString();
                newComplaint[1] = newComplaintDescription.getText().toString();
                newComplaint[2] = userID;

                boolean updateSuccess = connectionClass.NewComplaint(getApplicationContext(), newComplaint);

                if(updateSuccess) {
                    finish();
                }
            }
        });
    }
}
