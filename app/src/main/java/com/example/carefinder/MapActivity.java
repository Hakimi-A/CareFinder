package com.example.carefinder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Vector;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    FirebaseAuth mAuth;
    Toolbar mapToolbar;
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    Location currentLoc;
    Vector<com.google.android.gms.maps.model.MarkerOptions> MarkerOptions;
    MarkerOptions marker;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        mAuth = FirebaseAuth.getInstance();

        mapToolbar = findViewById(R.id.tbMap);
        setSupportActionBar(mapToolbar);
        getSupportActionBar().setTitle("Nearby Hopsital/Clinic");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user_locations");

        // Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        MarkerOptions = new Vector<>();

        MarkerOptions.add(new MarkerOptions().title("Klinik Bharu Machang")
                .position(new LatLng(5.764401114270023, 102.21879310898015))
                .snippet("8:30am-10:00pm"));

        MarkerOptions.add(new MarkerOptions().title("Klinik Wan Fuad")
                .position(new LatLng(5.765118312402815, 102.22070959101664))
                .snippet("9:00am-10:00pm"));

        MarkerOptions.add(new MarkerOptions().title("Machang Hospital")
                .position(new LatLng(5.763775796267755, 102.22590768408412))
                .snippet("Open 24 hours"));

        MarkerOptions.add(new MarkerOptions().title("Klinik Perdana Machang")
                .position(new LatLng(5.76649711897015, 102.21832908479372))
                .snippet("Open 8:00am-10pm"));

        MarkerOptions.add(new MarkerOptions().title("KLINIK KOTA HARMONI MACHANG")
                .position(new LatLng(5.777997034991855, 102.2134723641465))
                .snippet("Open 9:00am-10:00pm"));

        MarkerOptions.add(new MarkerOptions().title("Klinik Primer Impian")
                .position(new LatLng(5.809295018591888, 102.22803935029829))
                .snippet("Open 24 hours"));

        MarkerOptions.add(new MarkerOptions().title("Unit Kesihatan UiTM Cawangan Kelantan")
                .position(new LatLng(5.762381873182241, 102.27373508615413))
                .snippet("Open 8:00am-5:00pm"));

        MarkerOptions.add(new MarkerOptions().title("Machang Health Clinic")
                .position(new LatLng(5.768173187899359, 102.21614493918386))
                .snippet("Open 8:00am-10:00pm"));

        MarkerOptions.add(new MarkerOptions().title("Klinik Desa Wakaf Beta")
                .position(new LatLng(5.7805161706947485, 102.20383395203439))
                .snippet("Open 9:30am-10:00pm"));

        MarkerOptions.add(new MarkerOptions().title("Klinik Primer Machang")
                .position(new LatLng(5.773060136969911, 102.17539632388328))
                .snippet("Open 9:30am-10pm"));

        MarkerOptions.add(new MarkerOptions().title("Klinik Familia")
                .position(new LatLng(5.771693809508772, 102.1751099827854))
                .snippet("Open 9:00am-10:30pm"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        else if (item.getItemId() == R.id.item_homepage) {
            startActivity(new Intent(MapActivity.this, MainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.item_logout) {
            mAuth.signOut();
            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return false;
    }

    private void getLastLocation(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLoc = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapActivity.this);

                }
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        LatLng myLoc = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
        myMap.addMarker(new MarkerOptions()
                .position(myLoc)
                .title("My Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)) // Change marker color
        );

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 13));

        for (MarkerOptions mark:MarkerOptions){
            myMap.addMarker(mark);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(this, "Permission is denied, Please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}