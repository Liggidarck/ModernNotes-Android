package com.george.modern_notes.notebook;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.modern_notes.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetNotes extends BottomSheetDialogFragment {

    private BottomSheetListener bottomSheetListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_notes, container, false);

        RelativeLayout del = view.findViewById(R.id.delete_note_layout);
        del.setOnClickListener(v -> {
            bottomSheetListener.onButtonClicked("Button delete clicked");
            dismiss();
        });

        RelativeLayout copy = view.findViewById(R.id.copy_content_bottom);
        copy.setOnClickListener(v -> {
            bottomSheetListener.onButtonClicked("Button copy clicked");
            dismiss();
        });

        RelativeLayout share = view.findViewById(R.id.share_content_bottom);
        share.setOnClickListener(v -> {
            bottomSheetListener.onButtonClicked("Button share clicked");
            dismiss();
        });

        return view;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement BottomSheetListener");
        }
    }
}
