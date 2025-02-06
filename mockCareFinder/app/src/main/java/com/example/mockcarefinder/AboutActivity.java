package com.example.mockcarefinder;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class AboutActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Toolbar aboutToolbar;
    Button websiteURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        mAuth = FirebaseAuth.getInstance();

        aboutToolbar = findViewById(R.id.tbAbout);
        setSupportActionBar(aboutToolbar);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        websiteURL = findViewById(R.id.githubLink);
        websiteURL.setOnClickListener(view -> {
            String url = "https://github.com/Hakimi-A/CareFinder";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });

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
            startActivity(new Intent(AboutActivity.this, MainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.item_logout) {
            mAuth.signOut();
            Intent intent = new Intent(AboutActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
}