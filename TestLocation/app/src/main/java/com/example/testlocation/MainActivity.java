package com.example.testlocation;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.Manifest;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    Location currentLoc;
    Vector<MarkerOptions> MarkerOptions;
    MarkerOptions marker;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        MarkerOptions = new Vector<>();
        MarkerOptions.add(new MarkerOptions().title("Hospital Machang")
                .position(new LatLng(5.7633533521758595, 102.22587511138855))
                .snippet("Open 9am-10pm"));
        MarkerOptions.add(new MarkerOptions().title("Klinik Perdana Machang")
                .position(new LatLng(5.76649711897015, 102.21832908479372))
                .snippet("Open 9am-10pm"));
        MarkerOptions.add(new MarkerOptions().title("Klinik Kota Harmoni Machang")
                .position(new LatLng(5.776783907878391, 102.21368841918454))
                .snippet("Open 9am-10pm"));
        MarkerOptions.add(new MarkerOptions().title("Klinik Primer Impian")
                .position(new LatLng(5.809271832292243, 102.22738524459405))
                .snippet("Open 9am-10pm"));
        MarkerOptions.add(new MarkerOptions().title("Unit Kesihatan UiTM Cawangan Kelantan")
                .position(new LatLng(5.762381873182241, 102.27373508615413))
                .snippet("Open 9am-10pm"));
        MarkerOptions.add(new MarkerOptions().title("Machang Health Clinic")
                .position(new LatLng(5.769513337314534, 102.21585895868246))
                .snippet("Open 9am-10pm"));

    }

    private void getLastLocation(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
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
                    mapFragment.getMapAsync(MainActivity.this);

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

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));

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