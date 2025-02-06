package com.example.basicunitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etMeter;
    Button btnConvert;
    TextView tvOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etMeter = (EditText) findViewById(R.id.editTextNumberDecimal);
        btnConvert = (Button) findViewById(R.id.btnConvert);
        tvOutput = (TextView) findViewById(R.id.tvOutput);



        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bttnConvert.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnConvert:
                double meter = Double.parseDouble(etMeter.getText().toString());
                double feet = meter * 3.28084;

                tvOutput.setText(feet + " feet");

                break;


        }
    }
}