package com.george.modern_notes.passwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.george.modern_notes.MainActivity;
import com.george.modern_notes.R;
import com.george.modern_notes.database.PasswordsDatabase;

public class PasswordsListsActivity extends AppCompatActivity {

    ListView pas_list;
    PasswordsDatabase databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;


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
        setContentView(R.layout.activity_passwords_lists);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        MaterialToolbar toolbar = findViewById(R.id.toolbar_passwords_list);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        toolbar.setNavigationOnClickListener(view -> startActivity(new Intent(PasswordsListsActivity.this, MainActivity.class)));

        FloatingActionButton add_password = findViewById(R.id.floatingActionButton_add_password);
        add_password.setOnClickListener(view -> startActivity(new Intent(PasswordsListsActivity.this, AddPasswordActivity.class)));

        if(theme_app.equals("Orange"))
            add_password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab))); //#f59619

        if(theme_app.equals("Blue"))
            add_password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));

        TextView empty_text_passwords = findViewById(R.id.empty_text_passwords);

        if(theme_app.equals("Dark")) {
            toolbar.setNavigationIcon(R.drawable.ic_white_baseline_arrow_back_24);
            empty_text_passwords.setTextColor(Color.WHITE);
            add_password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_color_fab)));
        }

        if(theme_app.equals("AquaBlue"))
            add_password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blueaqua_color_fab)));

        if(theme_app.equals("Green"))
            add_password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));

        if(theme_app.equals("Red"))
            add_password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));

        if(theme_app.equals("Fiolet"))
            add_password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));


        pas_list = findViewById(R.id.list_passwords);

        pas_list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), AddPasswordActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        databaseHelper = new PasswordsDatabase(getApplicationContext());

        View empty = findViewById(R.id.empty_passw);
        pas_list.setEmptyView(empty);


    }

    @Override
    public void onResume() {
        super.onResume();

        db = databaseHelper.getReadableDatabase();
        userCursor = db.rawQuery("select * from " + PasswordsDatabase.TABLE, null);

        String[] headers = new String[] {PasswordsDatabase.NAME_ACCOUNT, PasswordsDatabase.WEB};

        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, userCursor, headers, new int[] {android.R.id.text1, android.R.id.text2}, 0);
        pas_list.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }
}