package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            String notificationID = remoteMessage.getData().get("notificationId");
            Intent viewNotificationsIntent = new Intent(this, ViewNotifications.class);
            viewNotificationsIntent.putExtra("notification_ID", notificationID);
            viewNotificationsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyFirebaseMessagingService.this.startActivity(viewNotificationsIntent);
        }
    }
}
