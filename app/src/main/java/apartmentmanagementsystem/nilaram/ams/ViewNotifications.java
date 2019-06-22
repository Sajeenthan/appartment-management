package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class ViewNotifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("View Notification");

        Intent intent = getIntent();
        String notificationID = intent.getStringExtra("notification_ID");

        ConnectionClass connectionClass = new ConnectionClass();
        List<String> notification = connectionClass.ViewNotifications(getApplicationContext(), notificationID);

        TextView title = findViewById(R.id.viewNotificationTitle);
        TextView date = findViewById(R.id.viewNotificationDate);
        TextView description = findViewById(R.id.viewNotificationDescription);

        if(notification.isEmpty()) {
            Log.d("List error: ", "empty list");
        }else {
            title.setText(notification.get(0));
            description.setText(notification.get(1));
            date.setText(notification.get(2));
        }
    }
}
