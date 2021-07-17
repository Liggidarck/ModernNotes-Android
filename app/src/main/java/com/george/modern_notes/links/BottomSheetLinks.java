package com.george.modern_notes.links;

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

public class BottomSheetLinks extends BottomSheetDialogFragment {

    private BottomSheetLinks.BottomSheetListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_links, container, false);

        RelativeLayout del = view.findViewById(R.id.delete_link_layout);
        del.setOnClickListener(v -> {
            mListener.onButtonClicked("Button delete clicked");
            dismiss();
        });

        RelativeLayout copy = view.findViewById(R.id.copy_content_bottom_link);
        copy.setOnClickListener(v -> {
            mListener.onButtonClicked("Button copy clicked");
            dismiss();
        });

        RelativeLayout share = view.findViewById(R.id.share_content_bottom_link);
        share.setOnClickListener(v -> {
            mListener.onButtonClicked("Button share clicked");
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
            mListener = (BottomSheetLinks.BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement BottomSheetListener");
        }
    }

}
