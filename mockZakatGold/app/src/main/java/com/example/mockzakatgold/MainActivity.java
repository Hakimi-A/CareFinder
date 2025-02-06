package com.example.mockzakatgold;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar AppToolbar;
    EditText etGoldWeight, etGoldValue;
    Button btCalcZakat;
    TextView tvOutputGoldValue, tvOutputGoldValuePayable, tvOutputTotalZakat;
    RadioGroup rgGoldType;
    RadioButton rbKeep, rbWear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etGoldWeight = findViewById(R.id.etxtWeight);
        etGoldValue = findViewById(R.id.etxtValue);
        rgGoldType = findViewById(R.id.rgGoldType);
        rbKeep = findViewById(R.id.rbKeep);
        rbWear = findViewById(R.id.rbWear) ;
        btCalcZakat = findViewById(R.id.btCalcZakat);
        tvOutputGoldValue = findViewById(R.id.tvOutGoldVal);
        tvOutputGoldValuePayable = findViewById(R.id.tvOutZakatPayable);
        tvOutputTotalZakat = findViewById(R.id.tvOutTotalZakat);

        btCalcZakat.setOnClickListener(this);

        AppToolbar = findViewById(R.id.tbAppTitle);
        setSupportActionBar(AppToolbar);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btCalcZakat) {
            try {
                double weight = Double.parseDouble(etGoldWeight.getText().toString());
                double valuePerGram = Double.parseDouble(etGoldValue.getText().toString());
                double totalGoldValue = weight * valuePerGram;

                double uruf;
                int selectedRadioId = rgGoldType.getCheckedRadioButtonId();
                if (selectedRadioId == R.id.rbKeep) {
                    uruf = 85;
                } else if (selectedRadioId == R.id.rbWear) {
                    uruf = 200;
                } else {
                    Toast.makeText(this, "Please select a gold type!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double goldValuePayable = 0;
                if (weight > uruf) {
                    goldValuePayable = (weight - uruf) * valuePerGram;
                }

                double totalZakat = goldValuePayable * 0.025;

                tvOutputGoldValue.setText(String.format("RM %.2f", totalGoldValue));
                tvOutputGoldValuePayable.setText(String.format("RM %.2f", goldValuePayable));
                tvOutputTotalZakat.setText(String.format("RM %.2f", totalZakat));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid inputs!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_Share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/Hakimi-A/ICT602-Zakat2U");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        } else if (item.getItemId() == R.id.item_About) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        } else if (item.getItemId() == R.id.item_Instruction) {
            Intent aboutIntent = new Intent(this, InstructionActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        return false;
    }
}