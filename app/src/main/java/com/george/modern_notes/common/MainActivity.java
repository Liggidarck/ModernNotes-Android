package com.george.modern_notes.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.modern_notes.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.george.modern_notes.links.AddLinkActivity;
import com.george.modern_notes.links.ListOfLinksActivity;
import com.george.modern_notes.notebook.AddNoteActivity;
import com.george.modern_notes.notebook.NotebookActivity;
import com.george.modern_notes.notebook.TopNoteActivity;
import com.george.modern_notes.passwords.AddPasswordActivity;
import com.george.modern_notes.passwords.GeneratorPasswordActivity;
import com.george.modern_notes.passwords.PasswordsListsActivity;

public class MainActivity extends AppCompatActivity {

    CardView notebook, list_of_links, passwords_list, generator, top_note;
    TextView text_top_note, text_top_note_name, text_user_name, label_top_note;
    ConstraintLayout header;
    FloatingActionButton fab_add;
    EditText text_header;

    private static final String TAG = "MainActivity";

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
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fab_add = findViewById(R.id.fab_add);
        text_user_name = findViewById(R.id.textUsername);
        text_header = findViewById(R.id.text_header);
        top_note = findViewById(R.id.layout_card_top);
        label_top_note = findViewById(R.id.textNoteTop);
        text_top_note = findViewById(R.id.text_topNote);
        text_top_note_name = findViewById(R.id.text_name_note);
        header = findViewById(R.id.layoutHeader);
        notebook = findViewById(R.id.notebook_card);
        list_of_links = findViewById(R.id.link_card);
        passwords_list = findViewById(R.id.password_card);
        generator = findViewById(R.id.generator_card);


        ImageView image_settings = findViewById(R.id.imageSettings);
        AdView ad_view = findViewById(R.id.adViewMain);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        ad_view.loadAd(adRequest);

        String name_user = sharedPreferences.getString(getString(R.string.root_full_name), "empty_user_name");
        String text_header = sharedPreferences.getString(getString(R.string.root_welcome_text_header), "");
        String number_of_lines_top_note = sharedPreferences.getString(getString(R.string.root_number_lines_top_note), "3");
        String color_fab_add = sharedPreferences.getString(getString(R.string.root_color_fab_add), "Orange");
        String on_click_fab = sharedPreferences.getString(getString(R.string.root_fab_on_click_val), "note");
        boolean fab_enabled = sharedPreferences.getBoolean(getString(R.string.root_fab_enabled), false);

        assert number_of_lines_top_note != null;
        assert name_user != null;
        assert color_fab_add != null;
        assert on_click_fab != null;

        if(number_of_lines_top_note.equals("infinity")) {
            Log.i(TAG, "Top notes lines - infinity");
        } else {
            int num = Integer.parseInt(number_of_lines_top_note);
            text_top_note.setMaxLines(num);
        }

        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(MainActivity.this, StarterActivity.class));

        this.text_header.setFocusable(false);
        this.text_header.setEnabled(false);
        this.text_header.setCursorVisible(false);
        this.text_header.setKeyListener(null);
        this.text_header.setHint(text_header);
        text_user_name.setText(name_user);

        loadTopNote();

        if(theme_app.equals("Orange")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_background_orange));
            label_top_note.setTextColor(Color.parseColor("#f59619"));
            top_note.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_background_orange));
        }

        if(theme_app.equals("Blue")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_blue));
            label_top_note.setTextColor(Color.parseColor("#6a6cfc"));
            top_note.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_blue));
        }

        if(theme_app.equals("AquaBlue")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_aqua_blue));
            label_top_note.setTextColor(Color.parseColor("#14d5ff"));
            top_note.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_aqua_blue));
        }

        if(theme_app.equals("Green")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.heder_back_green));
            label_top_note.setTextColor(Color.parseColor("#2ecc71"));
            top_note.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_green));
        }

        if(theme_app.equals("Red")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_red));
            label_top_note.setTextColor(Color.parseColor("#c0392b"));
            top_note.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_red));
        }

        if(theme_app.equals("Violet")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_filot));
            label_top_note.setTextColor(Color.parseColor("#FF9B3F"));
            top_note.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_fiolet));
        }

        if(!fab_enabled)
            fab_add.setVisibility(View.INVISIBLE);
        else
            fab_add.setVisibility(View.VISIBLE);

        if(color_fab_add.equals("Red"))
            fab_add.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red_color_fab)));

        if(color_fab_add.equals("Orange"))
            fab_add.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange_color_fab))); //#f59619

        if(color_fab_add.equals("Green"))
            fab_add.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green_color_fab)));

        if(color_fab_add.equals("Blue"))
            fab_add.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_color_fab)));

        if(color_fab_add.equals("Violet"))
            fab_add.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.violet_color_fab)));

        if(on_click_fab.equals("note"))
            fab_add.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddNoteActivity.class)));

        if(on_click_fab.equals("link"))
            fab_add.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddLinkActivity.class)));

        if(on_click_fab.equals("password"))
            fab_add.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddPasswordActivity.class)));

        image_settings.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        notebook.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, NotebookActivity.class)));
        list_of_links.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ListOfLinksActivity.class)));
        passwords_list.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, PasswordsListsActivity.class)));
        generator.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, GeneratorPasswordActivity.class)));

        top_note.setOnClickListener(view -> {
            String name_note_def = getString(R.string.default_name_top_note);
            String note_top_def = getString(R.string.default_text_top_note);

            SharedPreferences preferences = getSharedPreferences("top", Context.MODE_PRIVATE);
            String empty_note = preferences.getString("note", note_top_def);
            String empty_name_top_note = preferences.getString("name_note", name_note_def);

            assert empty_note != null;
            assert empty_name_top_note != null;
            if(empty_name_top_note.equals(name_note_def) && empty_note.equals(note_top_def))
                Snackbar.make(view, getText(R.string.error_empty_note), Snackbar.LENGTH_SHORT).setAction("error", null).show();
            else
                startActivity(new Intent(MainActivity.this, TopNoteActivity.class));
        });

    }

    public void loadTopNote() {
        String name_note_def = getString(R.string.default_name_top_note);
        String note_top_def = getString(R.string.default_text_top_note);
        SharedPreferences preferences = getSharedPreferences("top", Context.MODE_PRIVATE);
        String top_note = preferences.getString("note", note_top_def);
        String name_top_note = preferences.getString("name_note", name_note_def);

        text_top_note.setText(top_note);
        text_top_note_name.setText(name_top_note);
    }

}