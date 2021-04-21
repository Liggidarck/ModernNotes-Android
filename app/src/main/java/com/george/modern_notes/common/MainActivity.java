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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.modern_notes.R;
import com.george.modern_notes.common.future.SettingsActivity;
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

    CardView notebook, listOfLinks, passwordsList, generator, topNote;
    TextView text_topNote, text_topNote_name, textUserName, text_top_note;

    FloatingActionButton fab_add;
    EditText textHeader;

    TextView notebook_text, links_text, password_tex, generatoe_text;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = sharedPreferences.getString("theme_app", "Fiolet");

        assert theme_app != null;
        if(theme_app.equals("Orange"))
            setTheme(R.style.Orange);

        if(theme_app.equals("Blue"))
            setTheme(R.style.BlueTheme);

        if(theme_app.equals("AquaBlue"))
            setTheme(R.style.AquaBlueTheme);

        if(theme_app.equals("Green"))
            setTheme(R.style.GreenTheme);

        if(theme_app.equals("Red"))
            setTheme(R.style.RedTheme);

        if(theme_app.equals("Fiolet"))
            setTheme(R.style.FioletTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        notebook_text = findViewById(R.id.notebook_text);
        links_text = findViewById(R.id.links_text);
        password_tex = findViewById(R.id.password_tex);
        generatoe_text = findViewById(R.id.generatoe_text);

        fab_add = findViewById(R.id.fab_add);
        textUserName = findViewById(R.id.textUsername);
        textHeader = findViewById(R.id.text_header);

        topNote = findViewById(R.id.layout_card_top);
        text_top_note = findViewById(R.id.textNoteTop);
        text_topNote  = findViewById(R.id.text_topNote);
        text_topNote_name = findViewById(R.id.text_name_note);
        ConstraintLayout header = findViewById(R.id.layoutHeader);

        notebook = findViewById(R.id.motebook_card);
        listOfLinks = findViewById(R.id.link_card);
        passwordsList = findViewById(R.id.password_card);
        generator = findViewById(R.id.generator_card);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String name_user = sharedPreferences.getString("full_name", "empty_user_name");
        String text_header = sharedPreferences.getString("welcome_text_header", "");
        String number_of_lines_top_note = sharedPreferences.getString("number_lines_top_note", "3");
        String color_fab_add = sharedPreferences.getString("color_fab_add", "Orange");
        String on_click_fab = sharedPreferences.getString("fab_on_click_val", "note");
        boolean ad_pro_version = sharedPreferences.getBoolean("show_pro_ad", true);
        boolean fab_enabled = sharedPreferences.getBoolean("fab_enabled", false);

        assert number_of_lines_top_note != null;
        assert name_user != null;
        assert color_fab_add != null;
        assert on_click_fab != null;

        if(number_of_lines_top_note.equals("infinity")){
            Log.i(TAG, "Top notes lines - infinity");
        } else {
            int num = Integer.parseInt(number_of_lines_top_note);
            text_topNote.setMaxLines(num);
        }

        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(MainActivity.this, StarterActivity.class));

        textHeader.setFocusable(false);
        textHeader.setEnabled(false);
        textHeader.setCursorVisible(false);
        textHeader.setKeyListener(null);
        textHeader.setHint(text_header);
        textUserName.setText(name_user);

        LoadTopNote();

        if(ad_pro_version)
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_card_free, new FragmentPro()).commit();

        if(theme_app.equals("Orange")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_background_orange));
            text_top_note.setTextColor(Color.parseColor("#f59619"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_background_orange));
        }

        if(theme_app.equals("Blue")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_blue));
            text_top_note.setTextColor(Color.parseColor("#6a6cfc"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_blue));
        }

        if(theme_app.equals("AquaBlue")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_aqua_blue));
            text_top_note.setTextColor(Color.parseColor("#14d5ff"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_aqua_blue));
        }

        if(theme_app.equals("Green")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.heder_back_green));
            text_top_note.setTextColor(Color.parseColor("#2ecc71"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_green));
        }

        if(theme_app.equals("Red")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_red));
            text_top_note.setTextColor(Color.parseColor("#c0392b"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_red));
        }

        if(theme_app.equals("Fiolet")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_filot));
            text_top_note.setTextColor(Color.parseColor("#FF9B3F"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_fiolet));
        }

        if(!fab_enabled)
            fab_add.setVisibility(View.INVISIBLE);
        else
            fab_add.setVisibility(View.VISIBLE);

        if(color_fab_add.equals("Red"))
            fab_add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));

        if(color_fab_add.equals("Orange"))
            fab_add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab))); //#f59619

        if(color_fab_add.equals("Green"))
            fab_add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));

        if(color_fab_add.equals("Blue"))
            fab_add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));

        if(color_fab_add.equals("Fiolet"))
            fab_add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));

        if(on_click_fab.equals("note"))
            fab_add.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddNoteActivity.class)));

        if(on_click_fab.equals("link"))
            fab_add.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddLinkActivity.class)));

        if(on_click_fab.equals("password"))
            fab_add.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddPasswordActivity.class)));

        ImageView imageSettings = findViewById(R.id.imageSettings);
        imageSettings.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        notebook.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, NotebookActivity.class)));
        listOfLinks.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ListOfLinksActivity.class)));
        passwordsList.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, PasswordsListsActivity.class)));
        generator.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, GeneratorPasswordActivity.class)));

        topNote.setOnClickListener(view -> {
            String name_note_def = getString(R.string.default_name_top_note);
            String note_top_def = getString(R.string.default_text_top_note);

            SharedPreferences sharedPreferencesSS = getSharedPreferences("top", Context.MODE_PRIVATE);
            String empty_note = sharedPreferencesSS.getString("note", note_top_def);
            String empty_name_top_note = sharedPreferencesSS.getString("name_note", name_note_def);

            assert empty_note != null;
            assert empty_name_top_note != null;
            if(empty_name_top_note.equals(name_note_def) && empty_note.equals(note_top_def))
                Snackbar.make(view, getText(R.string.error_empty_note), Snackbar.LENGTH_SHORT).setAction("error", null).show();
            else
                startActivity(new Intent(MainActivity.this, TopNoteActivity.class));
        });

        Log.i(TAG, name_user + " - имя пользователя");
        Log.i(TAG, text_header + " - текст header");
        Log.i(TAG, number_of_lines_top_note + " - колличество строк в TOP NOTE");
        Log.i(TAG, color_fab_add + " - Цвет кнопки добавить");
        Log.i(TAG, on_click_fab + " - OnClick кнопки добавить");
        Log.i(TAG, theme_app + " - тема приложения");

    }

    public void LoadTopNote() {
        String name_note_def = getString(R.string.default_name_top_note);
        String note_top_def = getString(R.string.default_text_top_note);
        SharedPreferences sharedPreferencesSS = getSharedPreferences("top", Context.MODE_PRIVATE);
        String top_note = sharedPreferencesSS.getString("note", note_top_def);
        String name_top_note = sharedPreferencesSS.getString("name_note", name_note_def);

        Log.i(TAG, name_note_def+ " - полученные OnCreate");
        Log.i(TAG, note_top_def+ " - полученные OnCreate");

        text_topNote.setText(top_note);
        text_topNote_name.setText(name_top_note);
    }

}