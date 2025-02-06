package com.example.mockcarefinder;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class LocationTracker {
    private final Activity activity;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseAuth mAuth;

    public LocationTracker(Activity activity) {
        this.activity = activity;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mAuth = FirebaseAuth.getInstance();
    }

    public void sendUserLocationToServer(Location location) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        String fullName = (user.getDisplayName() != null) ? user.getDisplayName() : "Unknown User";
        String userAgent = System.getProperty("http.agent");
        String url = "http://192.168.0.64/carefinder/save_location.php"; // Replace with your local IP

        // Creating the request queue
        RequestQueue queue = Volley.newRequestQueue(activity);

        // Sending the location to the server
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("ServerResponse", response); // Handle the server response
                        Log.d("ServerResponse", "Response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("ServerError", "Failed to send data: " + error.getMessage()); // Handle errors
                        Log.e("ServerError", "Failed to send data: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("full_name", fullName);
                params.put("latitude", String.valueOf(location.getLatitude()));
                params.put("longitude", String.valueOf(location.getLongitude()));
                params.put("user_agent", userAgent);
                return params;
            }
        };

        // Adding the request to the queue
        queue.add(request);
    }
}