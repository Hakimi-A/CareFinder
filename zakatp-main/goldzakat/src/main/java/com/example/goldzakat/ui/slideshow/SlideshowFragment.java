package com.example.goldzakat.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.goldzakat.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private SlideshowViewModel slideshowViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);

        // Bind ViewModel data to UI
        slideshowViewModel.getInstructionTitle().observe(getViewLifecycleOwner(), binding.instructionTitle::setText);
        slideshowViewModel.getInstructions().observe(getViewLifecycleOwner(), instructions -> {
            binding.step1.setText(instructions[0]);
            binding.step2.setText(instructions[1]);
            binding.step3.setText(instructions[2]);
            binding.step4.setText(instructions[3]);
            binding.step5.setText(instructions[4]);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
