package com.george.modern_notes.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.george.modern_notes.R;

import java.util.Objects;

public class FragmentPro extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pro, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity().getBaseContext());
        String theme_app = sharedPreferences.getString(getString(R.string.root_theme_app), getString(R.string.root_theme_violet));

        CardView cardProAd = view.findViewById(R.id.pro_card);

        assert theme_app != null;
        if(theme_app.equals(getString(R.string.root_theme_orange)))
            cardProAd.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(FragmentPro.this.getActivity()), R.drawable.top_note_background_orange));

        if(theme_app.equals(getString(R.string.root_theme_blue)))
            cardProAd.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(FragmentPro.this.getActivity()), R.drawable.top_note_back_blue));

        if(theme_app.equals(getString(R.string.root_theme_aqua_blue)))
            cardProAd.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(FragmentPro.this.getActivity()), R.drawable.top_note_back_aqua_blue));

        if(theme_app.equals(getString(R.string.root_theme_green)))
            cardProAd.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(FragmentPro.this.getActivity()), R.drawable.top_note_back_green));

        if(theme_app.equals(getString(R.string.root_theme_red)))
            cardProAd.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(FragmentPro.this.getActivity()), R.drawable.top_note_back_red));

        if(theme_app.equals(getString(R.string.root_theme_violet)))
            cardProAd.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(FragmentPro.this.getActivity()), R.drawable.top_note_back_fiolet));

            cardProAd.setOnClickListener(view1 -> {
                final String appPackageName = "george.note";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException ange) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            });

        return view;
    }
}
