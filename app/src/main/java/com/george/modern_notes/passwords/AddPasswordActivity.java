package com.george.modern_notes.passwords;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.george.modern_notes.R;
import com.george.modern_notes.common.StarterActivity;
import com.george.modern_notes.database.PasswordsDatabase;

import java.util.Objects;

public class AddPasswordActivity extends AppCompatActivity {

    TextInputEditText webAddress, PasswordText, nameText;
    TextInputLayout webAddressLayout, passwordLayout, usernameLayout;

    FloatingActionButton delButton, saveButton;

    MaterialToolbar toolbar, toolbarDark;

    View theme;
    String checkThemePassword = "Default";

    PasswordsDatabase sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long PasswordId = 0;

    private static final String TAG = "addPass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = sharedPreferences.getString("theme_app", "Fiolet");

        assert theme_app != null;
        if(theme_app.equals("Orange"))
            setTheme(R.style.Orange);

        if(theme_app.equals("Blue"))
            setTheme(R.style.BlueTheme);

        if(theme_app.equals("Dark"))
            setTheme(R.style.DarckTheme);

        if(theme_app.equals("AquaBlue"))
            setTheme(R.style.AquaBlueTheme);

        if(theme_app.equals("Green"))
            setTheme(R.style.GreenTheme);

        if(theme_app.equals("Red"))
            setTheme(R.style.RedTheme);

        if(theme_app.equals("Fiolet"))
            setTheme(R.style.FioletTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adViewAddPassword);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        theme = findViewById(R.id.theme_pass);

        webAddress = findViewById(R.id.web_address);
        nameText = findViewById(R.id.username);
        PasswordText =findViewById(R.id.password);

        webAddressLayout = findViewById(R.id.web_address_layout);
        passwordLayout = findViewById(R.id.password_layout);
        usernameLayout = findViewById(R.id.username_layout);

        saveButton = findViewById(R.id.add_password);
        delButton = findViewById(R.id.del_password);
        toolbar = findViewById(R.id.toolbar_add_password);
        toolbarDark = findViewById(R.id.toolbar_add_password_dark);

        if(theme_app.equals("Dark")){
            setSupportActionBar(toolbarDark);
            toolbarDark.setNavigationIcon(R.drawable.ic_white_baseline_arrow_back_24);
            toolbarDark.setVisibility(View.VISIBLE);
            toolbarDark.setNavigationOnClickListener(view -> goHome_password());
        } else {
            setSupportActionBar(toolbar);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setNavigationOnClickListener(view -> goHome_password());
        }

        //Это запуск первой активити (страртовой).
        String name_user = sharedPreferences.getString("full_name", "empty_user_name");
        assert name_user != null;
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(AddPasswordActivity.this, StarterActivity.class));

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            PasswordId = extras.getLong("id");

        sqlHelper = new PasswordsDatabase(this);
        db = sqlHelper.getWritableDatabase();

        if (PasswordId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + PasswordsDatabase.TABLE + " where " +
                    PasswordsDatabase.ID + "=?", new String[] {String.valueOf(PasswordId)} );
            userCursor.moveToFirst();

            nameText.setText(userCursor.getString(1));
            PasswordText.setText(userCursor.getString(2));
            webAddress.setText(userCursor.getString(3));
            checkThemePassword = userCursor.getString(4);

            if(checkThemePassword.equals("Default")){
                if(theme_app.equals("Dark"))
                    Log.i(TAG, "Ничего не делаем!");
                else {
                    theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
                    toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));
                }
            }

            if(checkThemePassword.equals("Red")){
                theme.setBackgroundColor(Color.parseColor("#ff9e9e"));
                toolbar.setBackgroundColor(Color.parseColor("#ff9e9e"));
            }

            if(checkThemePassword.equals("Light Yellow")){
                theme.setBackgroundColor(Color.parseColor("#ffeaa7"));
                toolbar.setBackgroundColor(Color.parseColor("#ffeaa7"));
            }

            if(checkThemePassword.equals("Yellow")){
                theme.setBackgroundColor(Color.parseColor("#fdcb6f"));
                toolbar.setBackgroundColor(Color.parseColor("#fdcb6f"));
            }

            if(checkThemePassword.equals("Green")){
                theme.setBackgroundColor(Color.parseColor("#a3ff9e"));
                toolbar.setBackgroundColor(Color.parseColor("#a3ff9e"));
            }

            if(checkThemePassword.equals("Blue")){
                theme.setBackgroundColor(Color.parseColor("#9eb8ff"));
                toolbar.setBackgroundColor(Color.parseColor("#9eb8ff"));
            }

            if(checkThemePassword.equals("Aqua blue")){
                theme.setBackgroundColor(Color.parseColor("#74cdfc"));
                toolbar.setBackgroundColor(Color.parseColor("#74cdfc"));
            }

            if(checkThemePassword.equals("fiolet")){
                theme.setBackgroundColor(Color.parseColor("#e9a6ff"));
                toolbar.setBackgroundColor(Color.parseColor("#e9a6ff"));
            }

            if(checkThemePassword.equals("pink")){
                theme.setBackgroundColor(Color.parseColor("#ff9cbe"));
                toolbar.setBackgroundColor(Color.parseColor("#ff9cbe"));
            }

            userCursor.close();
        } else
            delButton.setVisibility(View.GONE);

        saveButton.setOnClickListener(view -> save_password());
        delButton.setOnClickListener(view -> delete_password());

        webAddressLayout.setEndIconOnClickListener(v -> {
            if(validateWeb()) {
                String url = Objects.requireNonNull(webAddressLayout.getEditText()).getText().toString();

                if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://"))
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                else {
                    String urlCheck = "http://" + url;
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlCheck)));
                }

            }

        });

        usernameLayout.setEndIconOnClickListener(v -> {
            if(validateUsername()){
                usernameLayout.setError(null);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", Objects.requireNonNull(usernameLayout.getEditText()).getText().toString());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Snackbar.make(v, getString(R.string.username_copied), Snackbar.LENGTH_LONG).setAction("done", null).show();
            }
        });

        Objects.requireNonNull(webAddressLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                webAddressLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Objects.requireNonNull(passwordLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Objects.requireNonNull(usernameLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                usernameLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(theme_app.equals("Orange")) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab))); //#f59619
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab)));
        }

        if(theme_app.equals("Blue")) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));
        }

        if(theme_app.equals("Dark")) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_color_fab)));
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_color_fab)));
        }

        if(theme_app.equals("AquaBlue")) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blueaqua_color_fab)));
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blueaqua_color_fab)));
        }

        if(theme_app.equals("Green")) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));
        }

        if(theme_app.equals("Red")) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));
        }

        if(theme_app.equals("Fiolet")) {
            saveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));
        }

    }

    public void save_password() {
        if(validatePass() | validateWeb()) {
            ContentValues cv = new ContentValues();
            cv.put(PasswordsDatabase.WEB, Objects.requireNonNull(nameText.getText()).toString());
            cv.put(PasswordsDatabase.PASSWORD, Objects.requireNonNull(PasswordText.getText()).toString());
            cv.put(PasswordsDatabase.NAME_ACCOUNT, Objects.requireNonNull(webAddress.getText()).toString());
            cv.put(PasswordsDatabase.THEME_MODE_PASSWORDS, checkThemePassword);

            if (PasswordId > 0)
                db.update(PasswordsDatabase.TABLE, cv, PasswordsDatabase.ID + "=" + PasswordId, null);
            else
                db.insert(PasswordsDatabase.TABLE, null, cv);
            goHome_password();
        }
    }

    public void delete_password(){
        db.delete(PasswordsDatabase.TABLE, "_id = ?", new String[]{String.valueOf(PasswordId)});
        goHome_password();
    }

    private void goHome_password(){
        db.close();
        Intent intent = new Intent(this, PasswordsListsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void showDialogEditThemePassword() {
        final Dialog dialog = new Dialog(AddPasswordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_theme);
        Log.i(TAG, "Диалог вызван");

        Button done = dialog.findViewById(R.id.button_ok);
        done.setOnClickListener(view -> dialog.dismiss());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String theme_app_check = sharedPreferences.getString("theme_app", "Fiolet");


        ImageView default_th, reed, light_yello, yello, greeen, bluue, aqqua_blue, fiiolet, piink;
        ImageView check_default,  check_red, check_yellow, chek_light_yellow, check_green, check_blue, check_aqua_blue, check_fiolet, chek_pink;

        default_th = dialog.findViewById(R.id.default_theme);
        reed = dialog.findViewById(R.id.red_theme);
        light_yello = dialog.findViewById(R.id.white_yello_theme);
        yello = dialog.findViewById(R.id.yello_theme);
        greeen = dialog.findViewById(R.id.green_theme);
        bluue = dialog.findViewById(R.id.blue_theme);
        aqqua_blue = dialog.findViewById(R.id.aqua_blue_theme);
        fiiolet = dialog.findViewById(R.id.fiolet_theme);
        piink = dialog.findViewById(R.id.pink_theme);

        check_default = dialog.findViewById(R.id.check_default);
        check_red = dialog.findViewById(R.id.check_red);
        chek_light_yellow = dialog.findViewById(R.id.check_tght_yellow);
        check_yellow = dialog.findViewById(R.id.check_yellow);
        check_green = dialog.findViewById(R.id.check_green);
        check_blue = dialog.findViewById(R.id.check_blue);
        check_aqua_blue = dialog.findViewById(R.id.check_aqua_blue);
        check_fiolet = dialog.findViewById(R.id.check_fiolet);
        chek_pink = dialog.findViewById(R.id.check_pink);

        TextView text_default_theme = dialog.findViewById(R.id.text_default_theme);

        Drawable dark_image = getResources().getDrawable(R.drawable.dark_default);

        if(theme_app_check.equals("Dark")) {
            default_th.setImageDrawable(dark_image);
            text_default_theme.setTextColor(Color.WHITE);
            done.setTextColor(Color.WHITE);
        }

        if(checkThemePassword.equals("Default"))
            check_default.setVisibility(View.VISIBLE);

        if(checkThemePassword.equals("Red"))
            check_red.setVisibility(View.VISIBLE);

        if(checkThemePassword.equals("Light Yellow"))
            chek_light_yellow.setVisibility(View.VISIBLE);

        if(checkThemePassword.equals("Yellow"))
            check_yellow.setVisibility(View.VISIBLE);

        if(checkThemePassword.equals("Green"))
            check_green.setVisibility(View.VISIBLE);

        if(checkThemePassword.equals("Blue"))
            check_blue.setVisibility(View.VISIBLE);

        if(checkThemePassword.equals("Aqua blue"))
            check_aqua_blue.setVisibility(View.VISIBLE);

        if(checkThemePassword.equals("fiolet"))
            check_fiolet.setVisibility(View.VISIBLE);

        if(checkThemePassword.equals("pink"))
            chek_pink.setVisibility(View.VISIBLE);


        default_th.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
            toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));

            checkThemePassword = "Default";
            dialog.dismiss();
        });

        reed.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#ff9e9e"));
            toolbar.setBackgroundColor(Color.parseColor("#ff9e9e"));

            checkThemePassword = "Red";
            dialog.dismiss();
        });

        light_yello.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#ffeaa7"));
            toolbar.setBackgroundColor(Color.parseColor("#ffeaa7"));
            checkThemePassword = "Light Yellow";
            dialog.dismiss();
        });

        yello.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#fdcb6f"));
            toolbar.setBackgroundColor(Color.parseColor("#fdcb6f"));
            checkThemePassword = "Yellow";
            dialog.dismiss();
        });

        greeen.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#a3ff9e"));
            toolbar.setBackgroundColor(Color.parseColor("#a3ff9e"));
            checkThemePassword = "Green";
            dialog.dismiss();
        });

        bluue.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#9eb8ff"));
            toolbar.setBackgroundColor(Color.parseColor("#9eb8ff"));
            checkThemePassword = "Blue";
            dialog.dismiss();
        });

        aqqua_blue.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#74cdfc"));
            toolbar.setBackgroundColor(Color.parseColor("#74cdfc"));
            checkThemePassword = "Aqua blue";
            dialog.dismiss();
        });

        fiiolet.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#e9a6ff"));
            toolbar.setBackgroundColor(Color.parseColor("#e9a6ff"));
            checkThemePassword = "fiolet";
            dialog.dismiss();
        });

        piink.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#ff9cbe"));
            toolbar.setBackgroundColor(Color.parseColor("#ff9cbe"));
            checkThemePassword = "pink";
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = sharedPreferences.getString("theme_app", "Fiolet");

        MenuInflater inflater = getMenuInflater();

        if(theme_app.equals("Dark"))
            inflater.inflate(R.menu.menu_empty, menu);
        else
            inflater.inflate(R.menu.menu_theme, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edit_theme) {
            Log.i(TAG, "Кнопка нажата");
            showDialogEditThemePassword();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean validateWeb() {
        String check = Objects.requireNonNull(webAddressLayout.getEditText()).getText().toString().trim();

        if(check.isEmpty()){
            webAddressLayout.setError(getString(R.string.error_empty_field));

            return false;
        } else {
            webAddressLayout.setError(null);
            return true;
        }
    }

    public boolean validatePass() {
        String check = Objects.requireNonNull(passwordLayout.getEditText()).getText().toString().trim();

        if(check.isEmpty()){
            passwordLayout.setError(getString(R.string.error_empty_field));

            return false;
        } else {
            passwordLayout.setError(null);
            return true;
        }

    }

    public boolean validateUsername() {
        String check = Objects.requireNonNull(usernameLayout.getEditText()).getText().toString().trim();

        if(check.isEmpty()){
            usernameLayout.setError(getString(R.string.error_empty_field));

            return false;
        } else {
            usernameLayout.setError(null);
            return true;
        }

    }
}