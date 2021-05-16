package com.george.modern_notes.passwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.george.modern_notes.common.MainActivity;
import com.george.modern_notes.R;
import com.george.modern_notes.common.StarterActivity;

import java.util.Objects;
import java.util.Random;

public class GeneratorPasswordActivity extends AppCompatActivity {

    TextView password_text_view;
    ImageButton refresh_btn, copy_btn;
    TextInputEditText password_length_edit_text;
    TextInputLayout password_length_text_layout;
    CheckBox symbols_check_box;
    MaterialToolbar toolbar;

    int password_length = 16;

    String theme_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        theme_app = preferences.getString(getString(R.string.root_theme_app), getString(R.string.root_theme_violet));

        assert theme_app != null;
        if(theme_app.equals(getString(R.string.root_theme_orange)))
            setTheme(R.style.Orange);

        if(theme_app.equals(getString(R.string.root_theme_blue)))
            setTheme(R.style.BlueTheme);

        if(theme_app.equals(getString(R.string.root_theme_aqua_blue)))
            setTheme(R.style.AquaBlueTheme);

        if(theme_app.equals(getString(R.string.root_theme_green)))
            setTheme(R.style.GreenTheme);

        if(theme_app.equals(getString(R.string.root_theme_red)))
            setTheme(R.style.RedTheme);

        if(theme_app.equals(getString(R.string.root_theme_violet)))
            setTheme(R.style.VioletTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AdView mAdView = findViewById(R.id.adViewGenerator);
        refresh_btn = findViewById(R.id.generate_password);
        password_text_view = findViewById(R.id.password_text);
        password_length_edit_text = findViewById(R.id.lengthPassword);
        symbols_check_box = findViewById(R.id.checkBoxSymbols);
        password_length_text_layout = findViewById(R.id.lengthPasswordLayout);
        copy_btn = findViewById(R.id.copyBtn);
        toolbar = findViewById(R.id.toolbar_generator);

        draw_theme();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setNavigationOnClickListener(view -> startActivity(new Intent(GeneratorPasswordActivity.this, MainActivity.class)));

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String name_user = preferences.getString(getString(R.string.root_full_name), "empty_user_name");
        assert name_user != null;
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(GeneratorPasswordActivity.this, StarterActivity.class));

        final Animation refresh_anim_set = AnimationUtils.loadAnimation(this, R.anim.refresh_anim);
        final Animation refresh_anim_err = AnimationUtils.loadAnimation(this, R.anim.ripple_anim);

        password_text_view.setText(getRandomPasswordSymbols(password_length));
        Objects.requireNonNull(password_length_text_layout.getEditText()).setText("16");

        refresh_btn.setOnClickListener(v -> {
            String checkPasswordLength = Objects.requireNonNull(password_length_edit_text.getText()).toString();
            if(symbols_check_box.isChecked()) {
                if(checkPasswordLength.isEmpty()) {
                    password_length_text_layout.setError(getString(R.string.error_empty_field));
                    refresh_btn.startAnimation(refresh_anim_err);
                }
                else if(Integer.parseInt(password_length_edit_text.getText().toString()) > 128 ) {
                    password_length_text_layout.setError(getString(R.string.error_max));
                    refresh_btn.startAnimation(refresh_anim_err);
                } else {
                    refresh_btn.startAnimation(refresh_anim_set);
                    password_length = Integer.parseInt(password_length_edit_text.getText().toString());
                    password_text_view.setText(getRandomPasswordSymbols(password_length));
                    password_length_text_layout.setError(null);
                }
            } else {
                if(checkPasswordLength.isEmpty()) {
                    password_length_text_layout.setError(getString(R.string.error_empty_field));
                    refresh_btn.startAnimation(refresh_anim_err);
                }
                else if(Integer.parseInt(password_length_edit_text.getText().toString()) > 128) {
                    password_length_text_layout.setError(getString(R.string.error_max));
                    refresh_btn.startAnimation(refresh_anim_err);
                } else {
                    refresh_btn.startAnimation(refresh_anim_set);
                    password_length = Integer.parseInt(password_length_edit_text.getText().toString());
                    password_text_view.setText(getRandomPassword(password_length));
                    password_length_text_layout.setError(null);
                }
            }
        });

        copy_btn.setOnClickListener(this::OnCopyBtnClick);

        password_length_text_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password_length_text_layout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void draw_theme() {
        if(theme_app.equals(getString(R.string.root_theme_orange))){
            refresh_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_orange));
            copy_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_orange));
        }

        if(theme_app.equals(getString(R.string.root_theme_blue))){
            refresh_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_blue));
            copy_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_blue));
        }

        if(theme_app.equals(getString(R.string.root_theme_aqua_blue))){
            refresh_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_aqua_blue));
            copy_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_aqua_blue));
        }

        if(theme_app.equals(getString(R.string.root_theme_green))){
            refresh_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_green));
            copy_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_green));
        }

        if(theme_app.equals(getString(R.string.root_theme_red))) {
            refresh_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_red));
            copy_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_red));
        }

        if(theme_app.equals(getString(R.string.root_theme_violet))){
            refresh_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_fiolet));
            copy_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_fiolet));
        }

    }

    //Генератор паролей с символами
    @SuppressWarnings("SpellCheckingInspection")
    public static String getRandomPasswordSymbols(int passwordLength) {
        final String charsPassword ="1234567890qwertyuiopasdfghjklzxcvbnm%*)?@#$~QWERTYUIOPASDFGHJKLZXCVBNM%*)?@#$~1234567890";
        StringBuilder result = new StringBuilder();
        while(passwordLength > 0) {
            Random rand = new Random();
            result.append(charsPassword.charAt(rand.nextInt(charsPassword.length())));
            passwordLength--;
        }
        return result.toString();
    }

    //Генератор паролей без символов
    @SuppressWarnings("SpellCheckingInspection")
    public static String getRandomPassword(int passwordLength) {
        final String noSymbolsCharsPassword ="1234567980qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder result = new StringBuilder();
        while(passwordLength > 0) {
            Random rand = new Random();
            result.append(noSymbolsCharsPassword.charAt(rand.nextInt(noSymbolsCharsPassword.length())));
            passwordLength--;
        }
        return result.toString();
    }

    public void OnCopyBtnClick(View view) {
        Animation copy_anim;
        copy_anim = AnimationUtils.loadAnimation(this, R.anim.ripple_anim);

        if(password_text_view.getText().toString().equals("Generate me!")) {
            copy_btn.startAnimation(copy_anim);
            Snackbar.make(view, getText(R.string.generate_password_to_copy), Snackbar.LENGTH_SHORT).setAction("error", null).show();
        } else {
            copy_btn.startAnimation(copy_anim);
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", password_text_view.getText().toString());
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Snackbar.make(view, getText(R.string.password_copied), Snackbar.LENGTH_SHORT).setAction("done", null).show();
        }
    }
}