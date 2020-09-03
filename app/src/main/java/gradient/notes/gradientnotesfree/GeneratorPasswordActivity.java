package gradient.notes.gradientnotesfree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.Random;

public class GeneratorPasswordActivity extends AppCompatActivity {

    public TextView PasswordText;
    public ImageButton refresh, copy;
    private TextInputEditText PasswordLength;
    private TextInputLayout passwordLenghtLAyoutl;
    private CheckBox symwals;

    int passwordLength = 16;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = sharedPreferences.getString("theme_app", "Fiolet");

        assert theme_app != null;
        if(theme_app.equals("Orange"))
            setTheme(R.style.Orange);

        if(theme_app.equals("Blue"))
            setTheme(R.style.BlueTheme);

        if(theme_app.equals("Green"))
            setTheme(R.style.GreenTheme);

        if(theme_app.equals("Red"))
            setTheme(R.style.RedTheme);

        if(theme_app.equals("Fiolet"))
            setTheme(R.style.FioletTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adViewGenerator);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        refresh = findViewById(R.id.generate_password);
        PasswordText = findViewById(R.id.password_text);
        PasswordLength = findViewById(R.id.lengthPassword);
        symwals = findViewById(R.id.checkBoxSymvals);
        passwordLenghtLAyoutl = findViewById(R.id.lengthPasswordLayout);
        copy = findViewById(R.id.copyBtn);

        String name_user = sharedPreferences.getString("full_name", "empty_user_name");

        assert name_user != null;
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(GeneratorPasswordActivity.this, StarterActivity.class));

        MaterialToolbar toolbar = findViewById(R.id.toolbar_generator);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GeneratorPasswordActivity.this, MainActivity.class));
            }
        });

        final Animation refresh_anim_set = AnimationUtils.loadAnimation(this, R.anim.refresh_anim);
        final Animation refresh_anim_err = AnimationUtils.loadAnimation(this, R.anim.ripple_anim);

        Objects.requireNonNull(passwordLenghtLAyoutl.getEditText()).setText("16");

        if(theme_app.equals("Orange")){
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_orange));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_orange));
        }

        if(theme_app.equals("Blue")){
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_blue));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_blue));
        }

        if(theme_app.equals("Green")){
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_green));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_green));
        }

        if(theme_app.equals("Red")) {
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_red));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_red));
        }

        if(theme_app.equals("Fiolet")){
            refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_fiolet));
            copy.setBackground(ContextCompat.getDrawable(this, R.drawable.for_buttons_fiolet));
        }

        PasswordText.setText(getRandomPasswordSymwals((int) passwordLength));
        passwordLenghtLAyoutl.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordLenghtLAyoutl.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Для генератора паролей
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkPasswordLength = Objects.requireNonNull(PasswordLength.getText()).toString();
                if(symwals.isChecked()) {
                    if(checkPasswordLength.isEmpty()) {
                        passwordLenghtLAyoutl.setError(getString(R.string.error_empty_field));
                        refresh.startAnimation(refresh_anim_err);
                    }
                    else if(Integer.parseInt(PasswordLength.getText().toString()) > 128 ) {
                        passwordLenghtLAyoutl.setError(getString(R.string.error_max));
                        refresh.startAnimation(refresh_anim_err);
                    } else {
                        refresh.startAnimation(refresh_anim_set);
                        passwordLength = Integer.parseInt(PasswordLength.getText().toString());
                        PasswordText.setText(getRandomPasswordSymwals(passwordLength));
                        passwordLenghtLAyoutl.setError(null);
                    }
                } else {
                    if(checkPasswordLength.isEmpty()) {
                        passwordLenghtLAyoutl.setError(getString(R.string.error_empty_field));
                        refresh.startAnimation(refresh_anim_err);
                    }
                    else if(Integer.parseInt(PasswordLength.getText().toString()) > 128) {
                        passwordLenghtLAyoutl.setError(getString(R.string.error_max));
                        refresh.startAnimation(refresh_anim_err);
                    } else {
                        refresh.startAnimation(refresh_anim_set);
                        passwordLength = Integer.parseInt(PasswordLength.getText().toString());
                        PasswordText.setText(getRandomPassword(passwordLength));
                        passwordLenghtLAyoutl.setError(null);
                    }
                }
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnCopyBtnClick(view);
            }
        });

    }


    //Генератор паролей с символами
    public static String getRandomPasswordSymwals(int passwordLength) {
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
        final String noSymvalsCharsPassword ="1234567980qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder result = new StringBuilder();
        while(passwordLength > 0) {
            Random rand = new Random();
            result.append(noSymvalsCharsPassword.charAt(rand.nextInt(noSymvalsCharsPassword.length())));
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