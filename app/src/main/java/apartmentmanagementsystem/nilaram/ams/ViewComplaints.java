package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class ViewComplaints extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaints);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("View Complaint");

        Intent intent = getIntent();
        String complaintID = intent.getStringExtra("complaint_ID");

        ConnectionClass connectionClass = new ConnectionClass();
        List<String> complaint = connectionClass.ViewComplaints(getApplicationContext(), complaintID);

        TextView title = findViewById(R.id.viewComplaintTitle);
        TextView date = findViewById(R.id.viewComplaintDate);
        TextView remark = findViewById(R.id.viewComplaintRemark);
        TextView description = findViewById(R.id.viewComplaintDescription);
        TextView response = findViewById(R.id.viewComplaintResponse);

        if(complaint.isEmpty()) {
            Log.d("List error: ", "empty list");
        }else {
            title.setText(complaint.get(0));
            description.setText(complaint.get(1));
            date.setText(complaint.get(2));
            remark.setText(complaint.get(3));
            response.setText(complaint.get(4));
        }
    }
}
