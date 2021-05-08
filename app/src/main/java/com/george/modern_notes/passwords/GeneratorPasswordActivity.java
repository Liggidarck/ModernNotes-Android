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
import android.preference.PreferenceManager;
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

    public TextView PasswordText;
    public ImageButton refresh, copy;
    private TextInputEditText PasswordLength;
    private TextInputLayout passwordLengthLayout;
    private CheckBox symbols;

    int passwordLength = 16;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = sharedPreferences.getString(getString(R.string.root_theme_app), getString(R.string.root_theme_violet));

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

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adViewGenerator);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        refresh = findViewById(R.id.generate_password);
        PasswordText = findViewById(R.id.password_text);
        PasswordLength = findViewById(R.id.lengthPassword);
        symbols = findViewById(R.id.checkBoxSymvals);
        passwordLengthLayout = findViewById(R.id.lengthPasswordLayout);
        copy = findViewById(R.id.copyBtn);

        String name_user = sharedPreferences.getString("full_name", "empty_user_name");

        assert name_user != null;
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(GeneratorPasswordActivity.this, StarterActivity.class));

        MaterialToolbar toolbar = findViewById(R.id.toolbar_generator);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        toolbar.setNavigationOnClickListener(view -> startActivity(new Intent(GeneratorPasswordActivity.this, MainActivity.class)));

        final Animation refresh_anim_set = AnimationUtils.loadAnimation(this, R.anim.refresh_anim);
        final Animation refresh_anim_err = AnimationUtils.loadAnimation(this, R.anim.ripple_anim);

        Objects.requireNonNull(passwordLengthLayout.getEditText()).setText("16");

        if(theme_app.equals(getString(R.string.root_theme_orange))){
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_orange));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_orange));
        }

        if(theme_app.equals(getString(R.string.root_theme_blue))){
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_blue));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_blue));
        }

        if(theme_app.equals(getString(R.string.root_theme_aqua_blue))){
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_aqua_blue));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_aqua_blue));
        }

        if(theme_app.equals(getString(R.string.root_theme_green))){
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_green));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_green));
        }

        if(theme_app.equals(getString(R.string.root_theme_red))) {
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_red));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_red));
        }

        if(theme_app.equals(getString(R.string.root_theme_violet))){
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_fiolet));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_fiolet));
        }

        PasswordText.setText(getRandomPasswordSymbols(passwordLength));
        passwordLengthLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordLengthLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Для генератора паролей
        refresh.setOnClickListener(v -> {
            String checkPasswordLength = Objects.requireNonNull(PasswordLength.getText()).toString();
            if(symbols.isChecked()) {
                if(checkPasswordLength.isEmpty()) {
                    passwordLengthLayout.setError(getString(R.string.error_empty_field));
                    refresh.startAnimation(refresh_anim_err);
                }
                else if(Integer.parseInt(PasswordLength.getText().toString()) > 128 ) {
                    passwordLengthLayout.setError(getString(R.string.error_max));
                    refresh.startAnimation(refresh_anim_err);
                } else {
                    refresh.startAnimation(refresh_anim_set);
                    passwordLength = Integer.parseInt(PasswordLength.getText().toString());
                    PasswordText.setText(getRandomPasswordSymbols(passwordLength));
                    passwordLengthLayout.setError(null);
                }
            } else {
                if(checkPasswordLength.isEmpty()) {
                    passwordLengthLayout.setError(getString(R.string.error_empty_field));
                    refresh.startAnimation(refresh_anim_err);
                }
                else if(Integer.parseInt(PasswordLength.getText().toString()) > 128) {
                    passwordLengthLayout.setError(getString(R.string.error_max));
                    refresh.startAnimation(refresh_anim_err);
                } else {
                    refresh.startAnimation(refresh_anim_set);
                    passwordLength = Integer.parseInt(PasswordLength.getText().toString());
                    PasswordText.setText(getRandomPassword(passwordLength));
                    passwordLengthLayout.setError(null);
                }
            }
        });

        copy.setOnClickListener(this::OnCopyBtnClick);

    }


    //Генератор паролей с символами
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

        if(PasswordText.getText().toString().equals("Generate me!")) {
            copy.startAnimation(copy_anim);
            Snackbar.make(view, getText(R.string.generate_password_to_copy), Snackbar.LENGTH_SHORT).setAction("error", null).show();
        } else {
            copy.startAnimation(copy_anim);
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", PasswordText.getText().toString());
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Snackbar.make(view, getText(R.string.password_copied), Snackbar.LENGTH_SHORT).setAction("done", null).show();
        }
    }
}