package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    private String username;
    private List<Tutor> tutorRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        username = "TO BE COMPLETED";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin_main, menu);
        MenuItem btnLogout = menu.findItem(R.id.menuLogout);
        btnLogout.setTitle(getResources().getString(R.string.action_logout) + " (" + username + ")");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.menuManageCategory:
                Toast.makeText(this, "\"Manage Category\" selected", Toast.LENGTH_SHORT).show();
                break;
            // action with ID action_settings was selected
            case R.id.menuLogout:
                logout();
                break;
            default:
                break;
        }

        return true;
    }

    public void logout() {
        Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
