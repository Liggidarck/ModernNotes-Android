package com.george.modern_notes.passwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.george.modern_notes.common.MainActivity;
import com.george.modern_notes.R;
import com.george.modern_notes.database.PasswordsDatabase;

public class PasswordsListsActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    FloatingActionButton add_password;
    View empty;

    ListView passwords_list;
    PasswordsDatabase database_helper;
    SQLiteDatabase db;
    Cursor user_cursor;
    SimpleCursorAdapter user_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = preferences.getString(getString(R.string.root_theme_app), getString(R.string.root_theme_violet));

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
        setContentView(R.layout.activity_passwords_lists);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AdView mAdView = findViewById(R.id.adView);
        toolbar = findViewById(R.id.toolbar_passwords_list);
        add_password = findViewById(R.id.floatingActionButton_add_password);
        passwords_list = findViewById(R.id.list_passwords);
        empty = findViewById(R.id.empty_password);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setNavigationOnClickListener(view -> startActivity(new Intent(PasswordsListsActivity.this, MainActivity.class)));

        MobileAds.initialize(this, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        passwords_list.setEmptyView(empty);

        add_password.setOnClickListener(view -> startActivity(new Intent(PasswordsListsActivity.this, AddPasswordActivity.class)));

        passwords_list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), AddPasswordActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        database_helper = new PasswordsDatabase(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        db = database_helper.getReadableDatabase();
        user_cursor = db.rawQuery("select * from " + PasswordsDatabase.TABLE, null);

        String[] headers = new String[] {PasswordsDatabase.NAME_ACCOUNT, PasswordsDatabase.WEB};

        user_adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, user_cursor, headers,
                new int[] {android.R.id.text1, android.R.id.text2}, 0);
        passwords_list.setAdapter(user_adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        user_cursor.close();
    }
}