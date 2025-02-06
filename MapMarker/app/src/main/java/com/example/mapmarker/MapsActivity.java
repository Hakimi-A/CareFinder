package com.example.mapmarker;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapmarker.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    Location currentLoc;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityMapsBinding binding;
    MarkerOptions marker;
    LatLng centerLoc;
    Vector<MarkerOptions> MarkerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        //centerLoc=new LatLng (5.77141407353668, 102.22311181937711);
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private void getLastLocation(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLoc = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        /*
        Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        myMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

        for (MarkerOptions mark:MarkerOptions){
            myMap.addMarker(mark);

        }

        LatLng MyLocation = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
        myMap.addMarker(new MarkerOptions().position(MyLocation).title("My Location "));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerLoc, 13));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}