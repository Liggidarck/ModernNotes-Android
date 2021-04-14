package com.george.modern_notes.links;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.SimpleCursorAdapter;
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

        if(theme_app.equals("Orange"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab))); //#f59619

        if(theme_app.equals("Dark")) {
            toolbar.setNavigationIcon(R.drawable.ic_white_baseline_arrow_back_24);
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_primary)));
            empty_text_links.setTextColor(Color.WHITE);
        }

        if(theme_app.equals("Blue"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));

        if(theme_app.equals("AquaBlue"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blueaqua_color_fab)));

        if(theme_app.equals("Green"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));

        if(theme_app.equals("Red"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));

        if(theme_app.equals("Fiolet"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));


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