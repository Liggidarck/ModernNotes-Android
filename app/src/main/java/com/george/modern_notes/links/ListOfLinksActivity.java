package com.george.modern_notes.links;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.george.modern_notes.common.MainActivity;
import com.george.modern_notes.R;
import com.george.modern_notes.database.LinksDatabase;

public class ListOfLinksActivity extends AppCompatActivity {

    FloatingActionButton fab_add_link;

    ListView userList;
    LinksDatabase databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;


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
        setContentView(R.layout.activity_list_of_links);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adViewLinks);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TextView empty_text_links = findViewById(R.id.empty_text_links);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_list_of_links);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> startActivity(new Intent(ListOfLinksActivity.this, MainActivity.class)));

        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));


        fab_add_link = findViewById(R.id.add_link);
        fab_add_link.setOnClickListener(view -> startActivity(new Intent(ListOfLinksActivity.this, AddLinkActivity.class)));


        userList = findViewById(R.id.list_of_links);
        userList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), AddLinkActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        View empty = findViewById(R.id.empty_link_layout);
        userList.setEmptyView(empty);

        databaseHelper = new LinksDatabase(getApplicationContext());


    }

    @Override
    public void onResume(){
        super.onResume();

        db = databaseHelper.getReadableDatabase();
        userCursor = db.rawQuery("select * from "+
                LinksDatabase.TABLE, null);

        String[] headers = new String[] {LinksDatabase.COLUMN_NAME_LINK,
                LinksDatabase.COLUMN_LNK};

        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, userCursor,
                headers, new int[] {android.R.id.text1, android.R.id.text2}, 0);
        userList.setAdapter(userAdapter);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }
}