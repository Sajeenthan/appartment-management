package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

public class Login extends AppCompatActivity {
    EditText inputUserID, inputPassword;
    Button btnLogin;

    private static final String MY_PREFS_NAME = "AMSPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUserID = findViewById(R.id.userName);
        inputPassword = findViewById(R.id.userPassword);
        btnLogin = findViewById(R.id.loginButton);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailID = inputUserID.getText().toString();
                String userPassword = inputPassword.getText().toString();

                if (TextUtils.isEmpty(mailID)) {
                    Toast.makeText(getApplicationContext(), "Enter the user ID!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(getApplicationContext(), "Enter the password!", Toast.LENGTH_SHORT).show();
                } else {
                    final ConnectionClass connectionClass = new ConnectionClass();

                    if (connectionClass.CheckLogin(getApplicationContext(), mailID, userPassword)) {
                        Toast.makeText(getApplicationContext(), "Login success!", Toast.LENGTH_SHORT).show();
                        final List<String> userInfo = connectionClass.GetUserType(getApplicationContext(), mailID);

                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("loginSuccess", true);
                        editor.putString("userID", userInfo.get(0));
                        editor.putString("userType", userInfo.get(1));
                        editor.apply();

                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            return;
                                        }

                                        String token = task.getResult().getToken();
                                        connectionClass.UpdateToken(getApplicationContext(), userInfo.get(0), token);

                                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        editor.putString("deviceToken", token);
                                        editor.apply();
                                    }
                                });

                        if (userInfo.get(1) != null) {
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("userType", userInfo.get(1));
                            Login.this.startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Login failure!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
