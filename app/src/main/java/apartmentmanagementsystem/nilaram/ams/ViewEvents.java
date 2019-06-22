package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class ViewEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("View Events");

        Intent intent = getIntent();
        int eventID = intent.getIntExtra("event_ID", 1);

        ConnectionClass connectionClass = new ConnectionClass();
        List<String> event = connectionClass.ViewEvents(getApplicationContext(), eventID);

        TextView title = findViewById(R.id.viewEventTitle);
        TextView date = findViewById(R.id.viewEventDate);
        TextView description = findViewById(R.id.viewEventDescription);

        if(event.isEmpty()) {
            Log.d("List error: ", "empty list");
        }else {
            title.setText(event.get(0));
            description.setText(event.get(1));
            date.setText(event.get(2));
        }
    }
}