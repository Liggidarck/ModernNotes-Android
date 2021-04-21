package com.george.modern_notes.passwords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.modern_notes.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetPasswords extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_password, container, false);



        return view;
    }
}
