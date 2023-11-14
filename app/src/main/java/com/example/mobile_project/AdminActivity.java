package com.example.mobile_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;

import com.example.mobile_project.session.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mobile_project.databinding.ActivityAdminBinding;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminBinding binding;

    private DrawerLayout drawer; // Add this member variable
    private ActionBarDrawerToggle toggle; // Add this member variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarAdmin.toolbar);

        // Initialize the navigation drawer and ActionBarDrawerToggle
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_user_home, R.id.nav_users, R.id.nav_posts)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize the ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawer, binding.appBarAdmin.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Load user details from SessionManager
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String username = sessionManager.getUserName();
        String email = sessionManager.getEmail();

        // Populate the views with user details
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.userName);
        TextView emailTextView = headerView.findViewById(R.id.email);

        usernameTextView.setText(username);
        emailTextView.setText(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_user_home) {
            // User Home item is selected, start MainActivity2
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_users) {
            // User List item is selected, navigate to the User List fragment
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
            navController.navigate(R.id.userListFragment);
            drawer.closeDrawer(GravityCompat.START); // Close the drawer after navigation
            return true;
        }

        // Handle other menu items here if needed
        return false;
    }

    // Add this method to support back navigation
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, com.example.mobile_project.R.id.nav_host_fragment_content_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}
