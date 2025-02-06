package com.example.mockcarefinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LocationTracker locationTracker;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    Toolbar AppToolbar;
    private Button btnViewMap, btnAbout;
    private TextView txtGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        locationTracker = new LocationTracker(this);

        mAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseRef = FirebaseDatabase.getInstance().getReference("user_locations");

        // Set up Toolbar
        AppToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(AppToolbar);


        // Initialize buttons
        btnViewMap = findViewById(R.id.btnViewMap);
        btnAbout = findViewById(R.id.btnAbout);
        txtGreeting = findViewById(R.id.welcomeMessage);

        displayUserGreeting();
        sendUserLocationToFirebase();

        // Button Click Listeners
        btnViewMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void displayUserGreeting() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            if (name != null && !name.isEmpty()) {
                txtGreeting.setText("Welcome, " + name + "!");
            } else {
                txtGreeting.setText("Welcome, User!");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.item_homepage).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_logout) {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        return false;
    }

    private void sendUserLocationToFirebase() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    saveLocationToFirebase(location);
                    locationTracker.sendUserLocationToServer(location); //send user location to web server-side app
                } else {
                    Toast.makeText(MainActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveLocationToFirebase(Location location) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String fullName = user.getDisplayName();
        String userId = user.getUid();
        String userAgent = System.getProperty("http.agent");
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        DatabaseReference userRef = databaseRef.child(userId);
        userRef.child("fullName").setValue(fullName);
        userRef.child("latitude").setValue(location.getLatitude());
        userRef.child("longitude").setValue(location.getLongitude());
        userRef.child("timestamp").setValue(timestamp);
        userRef.child("userAgent").setValue(userAgent);

        Toast.makeText(this, "Location updated successfully!", Toast.LENGTH_SHORT).show();
        Log.d("Firebase", "Location saved: " + location.getLatitude() + ", " + location.getLongitude());
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int [] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                sendUserLocationToFirebase();
            }
            else{
                Toast.makeText(this, "Permission is denied, Please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }


}