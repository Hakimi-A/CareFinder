package com.example.zakatp.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.zakatp.R;
import com.example.zakatp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText goldWeightInput = binding.goldWeightInput;
        EditText goldValueInput = binding.goldValueInput;
        RadioGroup goldTypeGroup = binding.goldTypeGroup;
        RadioButton wearButton = binding.wearButton;
        RadioButton keepButton = binding.keepButton;
        TextView totalValueOutput = binding.totalValueOutput;
        TextView zakatPayableOutput = binding.zakatPayableOutput;
        TextView totalZakatOutput = binding.totalZakatOutput;

        binding.calculateButton.setOnClickListener(v -> {
            // Validate inputs
            String weightText = goldWeightInput.getText().toString().trim();
            String valueText = goldValueInput.getText().toString().trim();

            if (TextUtils.isEmpty(weightText)) {
                showError("Please enter the weight of gold.");
                return;
            }
            if (TextUtils.isEmpty(valueText)) {
                showError("Please enter the current gold value.");
                return;
            }
            if (goldTypeGroup.getCheckedRadioButtonId() == -1) {
                showError("Please select the type of gold (Keep or Wear).");
                return;
            }

            try {
                double weight = Double.parseDouble(weightText);
                double value = Double.parseDouble(valueText);

                // Validate if values are reasonable
                if (weight <= 0) {
                    showError("Gold weight must be greater than 0.");
                    return;
                }
                if (value <= 0) {
                    showError("Gold value must be greater than 0.");
                    return;
                }

                // Determine the threshold (uruf)
                int threshold = keepButton.isChecked() ? 85 : 200;
                double goldPayableWeight = weight - threshold;
                double goldPayableValue = Math.max(goldPayableWeight, 0) * value;
                double totalZakat = goldPayableValue * 0.025;

                // Display the results
                totalValueOutput.setText(String.format(getString(R.string.total_gold_value_rm_2f), weight * value));
                zakatPayableOutput.setText(String.format(getString(R.string.gold_payable_value_rm_2f), goldPayableValue));
                totalZakatOutput.setText(String.format(getString(R.string.total_zakat_rm_2f), totalZakat));

            } catch (NumberFormatException e) {
                showError("Invalid input. Please enter numeric values for weight and gold value.");
            }
        });

        return root;
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
