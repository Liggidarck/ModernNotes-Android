package com.george.modern_notes.links;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.modern_notes.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomSheetLinks extends BottomSheetDialogFragment {

    private BottomSheetLinks.BottomSheetListener mListener;

    private static final String TAG = "bottomSheetLinks";
    CircleImageView check_defualt, check_red, check_orange, check_yellow, check_green,
            check_green_secondary, check_blue_ligth, check_blue, check_violet, check_pink, check_gray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_links, container, false);

        assert this.getArguments() != null;
        String theme = this.getArguments().getString("theme");
        Log.i(TAG, "val - " + theme);

        check_defualt = view.findViewById(R.id.check_defualt);
        check_red = view.findViewById(R.id.check_red);
        check_orange = view.findViewById(R.id.check_orange);
        check_yellow = view.findViewById(R.id.check_yellow);
        check_green = view.findViewById(R.id.check_green);
        check_green_secondary = view.findViewById(R.id.check_green_secondary);
        check_blue_ligth = view.findViewById(R.id.check_blue_ligth);
        check_blue = view.findViewById(R.id.check_blue);
        check_violet = view.findViewById(R.id.check_violet);
        check_pink = view.findViewById(R.id.check_pink);
        check_gray = view.findViewById(R.id.check_gray);

        if(theme.equals("Default"))
            check_defualt.setVisibility(View.VISIBLE);

        if(theme.equals("Red"))
            check_red.setVisibility(View.VISIBLE);

        if(theme.equals("Orange"))
            check_orange.setVisibility(View.VISIBLE);

        if(theme.equals("Yellow"))
            check_yellow.setVisibility(View.VISIBLE);

        if(theme.equals("Green"))
            check_green.setVisibility(View.VISIBLE);

        if(theme.equals("Light Green"))
            check_green_secondary.setVisibility(View.VISIBLE);

        if(theme.equals("Blue"))
            check_blue.setVisibility(View.VISIBLE);

        if(theme.equals("Ligth Blue"))
            check_blue_ligth.setVisibility(View.VISIBLE);

        if(theme.equals("violet"))
            check_violet.setVisibility(View.VISIBLE);

        if(theme.equals("Pink"))
            check_pink.setVisibility(View.VISIBLE);

        if(theme.equals("Gray"))
            check_gray.setVisibility(View.VISIBLE);

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

        RelativeLayout default_theme = view.findViewById(R.id.white_layout);
        default_theme.setOnClickListener(v -> {
            mListener.onButtonClicked("Default");
            dismiss();
        });

        RelativeLayout red_theme = view.findViewById(R.id.red_layout);
        red_theme.setOnClickListener(v -> {
            mListener.onButtonClicked("Red");
            dismiss();
        });

        RelativeLayout orange_theme = view.findViewById(R.id.orange_layout);
        orange_theme.setOnClickListener(v -> {
            mListener.onButtonClicked("Orange");
            dismiss();
        });

        RelativeLayout yeloow_theme = view.findViewById(R.id.yellow_layout);
        yeloow_theme.setOnClickListener(v -> {
            mListener.onButtonClicked("Yellow");
            dismiss();
        });

        RelativeLayout green_theme = view.findViewById(R.id.green_layout);
        green_theme.setOnClickListener(v -> {
            mListener.onButtonClicked("Green");
            dismiss();
        });

        RelativeLayout green_ligth_layout = view.findViewById(R.id.green_secondary_layout);
        green_ligth_layout.setOnClickListener(v -> {
            mListener.onButtonClicked("Light Green");
            dismiss();
        });

        RelativeLayout ligth_blue = view.findViewById(R.id.ligth_blue_layout);
        ligth_blue.setOnClickListener(v -> {
            mListener.onButtonClicked("Ligth Blue");
            dismiss();
        });

        RelativeLayout blue_layout = view.findViewById(R.id.blue_layout);
        blue_layout.setOnClickListener(v -> {
            mListener.onButtonClicked("Blue");
            dismiss();
        });

        RelativeLayout violet_layout = view.findViewById(R.id.violet_layout);
        violet_layout.setOnClickListener(v -> {
            mListener.onButtonClicked("violet");
            dismiss();
        });

        RelativeLayout pink_layout = view.findViewById(R.id.pink_layout);
        pink_layout.setOnClickListener(v -> {
            mListener.onButtonClicked("Pink");
            dismiss();
        });

        RelativeLayout gray_layout = view.findViewById(R.id.gray_layout);
        gray_layout.setOnClickListener(v -> {
            mListener.onButtonClicked("Gray");
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
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

}
