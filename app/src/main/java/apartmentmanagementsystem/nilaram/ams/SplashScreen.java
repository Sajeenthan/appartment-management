package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    private int SPLASH_TIME=3000;
    private static final String MY_PREFS_NAME = "AMSPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread timer= new Thread(){
            public void run(){
                try{
                    sleep(SPLASH_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    Boolean loginSuccess = prefs.getBoolean("loginSuccess", false);
                    String userType = prefs.getString("userType", null);

                    if(loginSuccess) {
                        if(userType != null) {
                            Intent intent = getIntent();
                            if(intent.getExtras() != null){
                                String notificationId = intent.getExtras().getString("notificationId");
                                Intent viewNotificationsIntent = new Intent(SplashScreen.this, ViewNotifications.class);
                                viewNotificationsIntent.putExtra("notification_ID", notificationId);
                                viewNotificationsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                SplashScreen.this.startActivity(viewNotificationsIntent);
                            } else {
                                Intent mainIntant = new Intent(SplashScreen.this, MainActivity.class);
                                mainIntant.putExtra("userType", userType);
                                SplashScreen.this.startActivity(mainIntant);
                            }
                        }
                    } else {
                        Intent intent =new Intent(SplashScreen.this,Login.class);
                        SplashScreen.this.startActivity(intent);
                    }

                    finish();
                }
            }
        };
        timer.start();
    }
}
