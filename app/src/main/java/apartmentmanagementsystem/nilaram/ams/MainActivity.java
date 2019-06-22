package apartmentmanagementsystem.nilaram.ams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String MY_PREFS_NAME = "AMSPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();

        Intent intent = getIntent();
        String userType = intent.getStringExtra("userType");

        if (userType.equals("TY003")) {
            MenuItem employees = menu.findItem(R.id.nav_employees);
            employees.setVisible(false);
            MenuItem maintenance = menu.findItem(R.id.nav_maintenance);
            maintenance.setVisible(false);
            MenuItem visitors = menu.findItem(R.id.nav_visitors);
            visitors.setVisible(false);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainA, new Notifications());
        ft.commit();

        navigationView.setCheckedItem(R.id.nav_notifications);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_profile) {
            ft.replace(R.id.mainA, new Profile());
        } else if (id == R.id.nav_notifications) {
            ft.replace(R.id.mainA, new Notifications());
        } else if (id == R.id.nav_complaints) {
            ft.replace(R.id.mainA, new Complaints());
        } else if (id == R.id.nav_events) {
            ft.replace(R.id.mainA, new Events());
        } else if (id == R.id.nav_employees) {
            ft.replace(R.id.mainA, new Employees());
        } else if (id == R.id.nav_houseowner) {
                ft.replace(R.id.mainA, new HouseOwners());
        } else if (id == R.id.nav_maintenance) {
            ft.replace(R.id.mainA, new Maintenance());
        } else if (id == R.id.nav_visitors) {
            ft.replace(R.id.mainA, new Visitors());
        } else if (id == R.id.nav_logout) {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String deviceToken = prefs.getString("deviceToken", null);

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();

            ConnectionClass connectionClass = new ConnectionClass();
            connectionClass.RemoveToken(getApplicationContext(), deviceToken);

            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            MainActivity.this.startActivity(loginIntent);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            finish();

            return true;
        }

        ft.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
