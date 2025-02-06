package com.example.zakatp.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private final MutableLiveData<String> instructionTitle;
    private final MutableLiveData<String[]> instructions;

    public SlideshowViewModel() {
        instructionTitle = new MutableLiveData<>();
        instructionTitle.setValue("How to Calculate Zakat");

        instructions = new MutableLiveData<>();
        instructions.setValue(new String[]{
                "Step 1: Enter the weight of your gold, its type (keep or wear), and the current gold value per gram.",
                "Step 2: The application calculates the zakat threshold (uruf): 85g for keep and 200g for wear.",
                "Step 3: Subtract the uruf from the gold weight to determine the gold payable weight.",
                "Step 4: Multiply the gold payable weight by the gold value per gram to get the total payable value.",
                "Step 5: Multiply the total payable value by 2.5% to calculate your Zakat."
        });
    }

    public LiveData<String> getInstructionTitle() {
        return instructionTitle;
    }

    public LiveData<String[]> getInstructions() {
        return instructions;
    }
}
