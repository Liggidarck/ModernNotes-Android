package com.gradient.free;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    RelativeLayout notebook, listOfLinks;

    LinearLayout passwordsList, generator;
    EditText textHeader;
    ConstraintLayout topNote;
    TextView text_topNote, text_topNote_name, textUserName, text_TOP_NOTE;

    FloatingActionButton fab_add;

    private static final String TAG = "MainActivity";
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = sharedPreferences.getString("theme_app", "Fiolet");

        assert theme_app != null;
        if(theme_app.equals("Dark"))
            setTheme(R.style.DarckTheme);

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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        fab_add = findViewById(R.id.fab_add);
        textUserName = findViewById(R.id.textUsername);
        textHeader = findViewById(R.id.text_header);

        topNote = findViewById(R.id.layout_card_top);
        text_TOP_NOTE = findViewById(R.id.textNoteTop);
        text_topNote  = findViewById(R.id.text_topNote);
        text_topNote_name = findViewById(R.id.text_name_note);
        ConstraintLayout header = findViewById(R.id.layoutHeader);

        notebook = findViewById(R.id.layoutNote);
        listOfLinks = findViewById(R.id.layoutLinks);
        passwordsList = findViewById(R.id.layoutPasswords);
        generator = findViewById(R.id.layoutGenerator);


        String name_user = sharedPreferences.getString("full_name", "empty_user_name");
        String text_header = sharedPreferences.getString("nizniy_text_header", "");
        String number_of_lines_top_note = sharedPreferences.getString("number_of_lines_top_note", "3");
        String color_fab_add = sharedPreferences.getString("color_fab_add", "Orange");
        String on_click_fab = sharedPreferences.getString("fab_on_click_val", "note");
        boolean ad_pro_version = sharedPreferences.getBoolean("vikluchit_pri_text", true);
        boolean fab_enabled = sharedPreferences.getBoolean("fab_enabled", false);

        assert number_of_lines_top_note != null;
        assert name_user != null;
        assert color_fab_add != null;
        assert on_click_fab != null;

        if(number_of_lines_top_note.equals("infinity")){
            Log.i(TAG, "Top notes lines - infinity");
        } else {
            int killichestvo_strok_top_note_int = Integer.parseInt(number_of_lines_top_note);
            text_topNote.setMaxLines(killichestvo_strok_top_note_int);
        }

        //Это запуск первой стартовой активити
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(MainActivity.this, StarterActivity.class));

        textHeader.setFocusable(false);
        textHeader.setEnabled(false);
        textHeader.setCursorVisible(false);
        textHeader.setKeyListener(null);
        textHeader.setHint(text_header);
        textUserName.setText(name_user);

        LoadTopNote();

        TextView notebook_text = findViewById(R.id.notebook_text);
        TextView links_text = findViewById(R.id.links_text);
        TextView password_tex = findViewById(R.id.password_tex);
        TextView generatoe_text = findViewById(R.id.generatoe_text);

        if(ad_pro_version)
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_card_free, new FragmentPro()).commit();

        if(theme_app.equals("Dark")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_background_dark));
            text_TOP_NOTE.setTextColor(Color.parseColor("#636363"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_dark));

            notebook.setBackground(ContextCompat.getDrawable(this, R.drawable.black_rectangel_background));
            notebook_text.setTextColor(Color.parseColor("#FFFFFF"));

            listOfLinks.setBackground(ContextCompat.getDrawable(this, R.drawable.black_rectangel_background));
            links_text.setTextColor(Color.parseColor("#FFFFFF"));

            passwordsList.setBackground(ContextCompat.getDrawable(this, R.drawable.black_rectangel_background));
            password_tex.setTextColor(Color.parseColor("#FFFFFF"));

            generator.setBackground(ContextCompat.getDrawable(this, R.drawable.black_rectangel_background));
            generatoe_text.setTextColor(Color.parseColor("#FFFFFF"));

            textHeader.setHintTextColor(Color.BLACK);
        }

        if(theme_app.equals("Orange")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_background_orange));
            text_TOP_NOTE.setTextColor(Color.parseColor("#f59619"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_background_orange));
        }

        if(theme_app.equals("Blue")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_blue));
            text_TOP_NOTE.setTextColor(Color.parseColor("#6a6cfc"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_blue));
        }

        if(theme_app.equals("AquaBlue")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_aqua_blue));
            text_TOP_NOTE.setTextColor(Color.parseColor("#14d5ff"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_aqua_blue));
        }

        if(theme_app.equals("Green")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.heder_back_green));
            text_TOP_NOTE.setTextColor(Color.parseColor("#2ecc71"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_green));
        }

        if(theme_app.equals("Red")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_red));
            text_TOP_NOTE.setTextColor(Color.parseColor("#c0392b"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_red));
        }

        if(theme_app.equals("Fiolet")) {
            header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_back_filot));
            text_TOP_NOTE.setTextColor(Color.parseColor("#FF9B3F"));
            topNote.setBackground(ContextCompat.getDrawable(this, R.drawable.top_note_back_fiolet));
        }


        //Это видимость кнопки добавить
        if(!fab_enabled)
            fab_add.setVisibility(View.INVISIBLE);
        else
            fab_add.setVisibility(View.VISIBLE);

        //Это цвет кнопки добавить
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

        //Это клик по кнопке добавить
        if(on_click_fab.equals("note")) {
            fab_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
                }
            });
        }

        if(on_click_fab.equals("link")) {
            fab_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, AddLinkActivity.class));
                }
            });
        }

        if(on_click_fab.equals("password")){
            fab_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, AddPasswordActivity.class));
                }
            });
        }

        //Это нажатие по карточкам и разным кнопкам карточки
        ImageView imageSettings = findViewById(R.id.imageSettings);
        imageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        notebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotebookActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        listOfLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListOfLinksActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        passwordsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PasswordsListsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GeneratorPasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        topNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_note_def = getString(R.string.default_name_top_note);
                String note_top_def = getString(R.string.default_text_top_note);

                SharedPreferences sharedPreferencesSS = getSharedPreferences("top", Context.MODE_PRIVATE);
                String empty_note = sharedPreferencesSS.getString("note", note_top_def);
                String empty_name_top_note = sharedPreferencesSS.getString("name_note", name_note_def);

                assert empty_note != null;
                assert empty_name_top_note != null;
                if(empty_name_top_note.equals(name_note_def) && empty_note.equals(note_top_def))
                    Snackbar.make(view, getText(R.string.error_empty_note), Snackbar.LENGTH_SHORT).setAction("error", null).show();
                else {
                    startActivity(new Intent(MainActivity.this, TopNoteActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}