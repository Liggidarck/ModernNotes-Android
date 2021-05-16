package com.george.modern_notes.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.george.modern_notes.common.MainActivity;
import com.george.modern_notes.R;
import com.george.modern_notes.database.NotesDatabase;

import java.util.Objects;

public class NotebookActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    FloatingActionButton add_note;

    NotesDatabase database_helper;
    SQLiteDatabase db;
    Cursor user_cursor;
    SimpleCursorAdapter user_adapter;
    ListView notes_list;

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
        setContentView(R.layout.activity_notebook);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);
        AdView mAdView = findViewById(R.id.adViewNotebook);
        toolbar = findViewById(R.id.toolbar_notebook);
        add_note = findViewById(R.id.add_note);
        notes_list = findViewById(R.id.notebook_list);
        View empty = findViewById(R.id.empty_layout);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setNavigationOnClickListener(view -> startActivity(new Intent(NotebookActivity.this, MainActivity.class)));

        add_note.setOnClickListener(view -> startActivity(new Intent(NotebookActivity.this, AddNoteActivity.class)));
        notes_list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        database_helper = new NotesDatabase(getApplicationContext());

        notes_list.setEmptyView(empty);

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            String chek_empty = Objects.requireNonNull(arguments.get("empty")).toString();

            if(chek_empty.equals("Empty boxes")) {
                Snackbar.make(coordinatorLayout, getString(R.string.empty_note_do_not_save), Snackbar.LENGTH_SHORT)
                        .setAction("error", null).show();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        db = database_helper.getReadableDatabase();
        user_cursor = db.rawQuery("select * from "+ NotesDatabase.TABLE, null);

        String[] headers = new String[] {NotesDatabase.COLUMN_NAME_NOTE,
                NotesDatabase.COLUMN_NOTE};

        user_adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, user_cursor, headers, new int[] {android.R.id.text1, android.R.id.text2}, 0);
        notes_list.setAdapter(user_adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        user_cursor.close();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}