package com.george.modern_notes.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.george.modern_notes.MainActivity;
import com.george.modern_notes.R;

import java.util.Objects;

public class StarterActivity extends AppCompatActivity {
    private static final String TAG = "StarterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.FioletThemeStartPage);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final TextInputLayout username_layout = findViewById(R.id.username_layout_start_page);

        ImageView signin = findViewById(R.id.sign_in_btn_1);
        signin.setOnClickListener(view -> {
            String userName = Objects.requireNonNull(username_layout.getEditText()).getText().toString();
            if (userName.isEmpty()) {
                username_layout.setError(getString(R.string.error_empty_field));
            } else {

                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("full_name", userName);
                editor.apply();

                startActivity(new Intent(StarterActivity.this, MainActivity.class));
            }
        });

        Objects.requireNonNull(username_layout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username_layout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "Ха-ха не прокатит.");
    }
}