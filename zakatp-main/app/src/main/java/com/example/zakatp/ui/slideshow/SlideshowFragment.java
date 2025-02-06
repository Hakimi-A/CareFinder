package com.example.zakatp.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.zakatp.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set title
        final TextView titleTextView = binding.instructionTitle;
        slideshowViewModel.getInstructionTitle().observe(getViewLifecycleOwner(), titleTextView::setText);

        // Set instructions
        final TextView[] stepTextViews = {
                binding.step1,
                binding.step2,
                binding.step3,
                binding.step4,
                binding.step5
        };

        slideshowViewModel.getInstructions().observe(getViewLifecycleOwner(), instructions -> {
            for (int i = 0; i < instructions.length && i < stepTextViews.length; i++) {
                stepTextViews[i].setText(instructions[i]);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
