package com.example.bookapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookapp2.databinding.ActivityBinocularsBinding;

import java.util.HashMap;

public class Binoculars extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityBinocularsBinding binding;

    UserManagment userManagment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBinocularsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarBinoculars.toolbar);
        binding.appBarBinoculars.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_testediting)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_binoculars);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.binoculars, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Toast.makeText(getApplicationContext(),"Тест ящика",Toast.LENGTH_LONG).show();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View mnavView = navigationView.getHeaderView(0);

        userManagment = new UserManagment(this);
        userManagment.checkLogin();
        HashMap<String, String> user = userManagment.userDetails();

        TextView mfName = mnavView.findViewById(R.id.fName);
        TextView mLName = mnavView.findViewById(R.id.lName);

        mfName.setText(user.get(userManagment.FIRSTNAME));
        mLName.setText(user.get(UserManagment.LASTNAME));

//        setContentView(R.layout.nav_header_binoculars);
//        fName = findViewById(R.id.fName);
//        userManagment = new UserManagment(this);
//        userManagment.checkLogin();
//        HashMap<String, String> user = userManagment.userDetails();
//        String mFirstname = user.get(userManagment.FIRSTNAME);
//        fName.setText(mFirstname);
        return super.onOptionsItemSelected(item);
    }







    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_binoculars);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    public void goSettings(MenuItem item) {
        Intent intent = new Intent(Binoculars.this, ProfileActivity.class);
        startActivity(intent);
    }



}