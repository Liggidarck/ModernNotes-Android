package com.gradient.free;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class TopNoteActivity extends AppCompatActivity {

    EditText name_note_top, note_top;
    MaterialToolbar toolbar;
    private static final String TAG = "TopNoteActivity";

    View theme;
    String checkThemeTopNote = "Default";

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
        setContentView(R.layout.activity_top_note);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.topAppBar_note_top);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();            }
        });
        theme = findViewById(R.id.theme_top_note);

        name_note_top = findViewById(R.id.name_note_top);
        note_top = findViewById(R.id.note_text_top);
        Log.i(TAG, checkThemeTopNote + "До получения данных");
        LoadData();
        Log.i(TAG, checkThemeTopNote + "После получения данных");

        ExtendedFloatingActionButton save = findViewById(R.id.save_new_note);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData();
            }
        });

        if(theme_app.equals("Orange"))
            save.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab))); //#f59619

        if(theme_app.equals("Blue"))
            save.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));

        if(theme_app.equals("Green"))
            save.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));

        if(theme_app.equals("Red"))
            save.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));

        if(theme_app.equals("Fiolet"))
            save.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));


    }

    public void LoadData() {
        String name_note_def = getString(R.string.default_name_top_note);
        String note_top_def = getString(R.string.default_text_top_note);
        SharedPreferences sharedPreferencesSS = getSharedPreferences("top", Context.MODE_PRIVATE);
        String top_note_string = sharedPreferencesSS.getString("note", note_top_def);
        String name_top_note_string = sharedPreferencesSS.getString("name_note", name_note_def);
        checkThemeTopNote = sharedPreferencesSS.getString("theme_top_note", "Default");
        Log.i(TAG, checkThemeTopNote + " Полученные данные");

        assert checkThemeTopNote != null;
        if(checkThemeTopNote.equals("Default")){
            theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
            toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));
        }

        if(checkThemeTopNote.equals("Darcker")){
            theme.setBackgroundColor(Color.parseColor("#303030"));
            toolbar.setBackgroundColor(Color.parseColor("#303030"));
        }

        if(checkThemeTopNote.equals("Red")){
            theme.setBackgroundColor(Color.parseColor("#ee7576"));
            toolbar.setBackgroundColor(Color.parseColor("#ee7576"));
        }

        if(checkThemeTopNote.equals("Light Yellow")){
            theme.setBackgroundColor(Color.parseColor("#ffeaa7"));
            toolbar.setBackgroundColor(Color.parseColor("#ffeaa7"));
        }

        if(checkThemeTopNote.equals("Yellow")){
            theme.setBackgroundColor(Color.parseColor("#fdcb6f"));
            toolbar.setBackgroundColor(Color.parseColor("#fdcb6f"));
        }

        if(checkThemeTopNote.equals("Green")){
            theme.setBackgroundColor(Color.parseColor("#2dab61"));
            toolbar.setBackgroundColor(Color.parseColor("#2dab61"));
        }

        if(checkThemeTopNote.equals("Blue")){
            theme.setBackgroundColor(Color.parseColor("#337dc0"));
            toolbar.setBackgroundColor(Color.parseColor("#337dc0"));
        }

        if(checkThemeTopNote.equals("Aqua blue")){
            theme.setBackgroundColor(Color.parseColor("#80b4e2"));
            toolbar.setBackgroundColor(Color.parseColor("#80b4e2"));
        }

        if(checkThemeTopNote.equals("fiolet")){
            theme.setBackgroundColor(Color.parseColor("#905aa1"));
            toolbar.setBackgroundColor(Color.parseColor("#905aa1"));
        }

        if(checkThemeTopNote.equals("pink")){
            theme.setBackgroundColor(Color.parseColor("#e54591"));
            toolbar.setBackgroundColor(Color.parseColor("#e54591"));
        }


        name_note_top.setText(name_top_note_string);
        note_top.setText(top_note_string);
    }

    public void UpdateData() {
        String saveNewName = name_note_top.getText().toString();
        String saveNewNote = note_top.getText().toString();
        SharedPreferences sharedPreferences = this.getSharedPreferences("top", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(saveNewName.isEmpty() && saveNewNote.isEmpty()) {
            RelativeLayout relativeLayout_topNote = findViewById(R.id.relativeLayout_topNote);
            Snackbar.make(relativeLayout_topNote, "Пустая заметка не сохраняеться", Snackbar.LENGTH_SHORT).setAction("done", null).show();
            Log.i(TAG, "Полностью пустая заметка");
        } else {
            Log.i(TAG, "Нормальная заметка");
            editor.putString("name_note", saveNewName);
            editor.putString("note", saveNewNote);

            editor.apply();
            goHome();
        }

        if(saveNewName.isEmpty() && saveNewNote.length() > 0) {
            Log.i(TAG, "Имя пустое а заметка нет");

            saveNewName = "Заметка без имени";
            name_note_top.setText(saveNewName);
            editor.putString("name_note", saveNewName);
            editor.putString("note", saveNewNote);

            editor.apply();
            goHome();
        }

        if(saveNewName.length() > 0 && saveNewNote.isEmpty()) {
            Log.i(TAG, "Заметка пустая а имя нет");
            saveNewNote = "Пустая заметка";
            note_top.setText(saveNewNote);

            editor.putString("name_note", saveNewName);
            editor.putString("note", saveNewNote);

            editor.apply();
            goHome();
        }

    }

    @Override
    public void onBackPressed() {
        goHome();
    }

    public void goHome() {
        startActivity(new Intent(TopNoteActivity.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top_note, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_del) {
            String name_note_def = getString(R.string.default_name_top_note);
            String note_top_def = getString(R.string.default_text_top_note);

            SharedPreferences sharedPreferences = this.getSharedPreferences("top", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("name_note", name_note_def);
            editor.putString("note", note_top_def);

            editor.apply();
            goHome();
        }

        if(item.getItemId() == R.id.edit_theme_top_note) {
            showDialog();
            Log.i(TAG, checkThemeTopNote + "Во время нажатия на элемент в toolbar");

        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(TopNoteActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_theme);

        Button done = dialog.findViewById(R.id.button_ok);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView default_th, reed, light_yello, yello, greeen, bluue, aqqua_blue, fiiolet, piink;
        ImageView check_default, check_red, check_yellow, chek_light_yellow, check_green, check_blue, check_aqua_blue, check_fiolet, chek_pink;

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

        if(checkThemeTopNote.equals("Default"))
            check_default.setVisibility(View.VISIBLE);

        if(checkThemeTopNote.equals("Red"))
            check_red.setVisibility(View.VISIBLE);

        if(checkThemeTopNote.equals("Light Yellow"))
            chek_light_yellow.setVisibility(View.VISIBLE);

        if(checkThemeTopNote.equals("Yellow"))
            check_yellow.setVisibility(View.VISIBLE);

        if(checkThemeTopNote.equals("Green"))
            check_green.setVisibility(View.VISIBLE);

        if(checkThemeTopNote.equals("Blue"))
            check_blue.setVisibility(View.VISIBLE);

        if(checkThemeTopNote.equals("Aqua blue"))
            check_aqua_blue.setVisibility(View.VISIBLE);

        if(checkThemeTopNote.equals("fiolet"))
            check_fiolet.setVisibility(View.VISIBLE);

        if(checkThemeTopNote.equals("pink"))
            chek_pink.setVisibility(View.VISIBLE);


        SharedPreferences sharedPreferences = this.getSharedPreferences("top", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        default_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
                toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));
                checkThemeTopNote = "Default";
                editor.putString("theme_top_note", checkThemeTopNote);
                editor.apply();
                dialog.dismiss();
            }
        });

        reed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#ee7576"));
                toolbar.setBackgroundColor(Color.parseColor("#ee7576"));
                checkThemeTopNote = "Red";
                editor.putString("theme_top_note", checkThemeTopNote);
                editor.apply();
                dialog.dismiss();
            }
        });

        light_yello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#ffeaa7"));
                toolbar.setBackgroundColor(Color.parseColor("#ffeaa7"));
                checkThemeTopNote = "Light Yellow";
                editor.putString("theme_top_note", checkThemeTopNote);
                editor.apply();
                dialog.dismiss();
            }
        });

        yello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#fdcb6f"));
                toolbar.setBackgroundColor(Color.parseColor("#fdcb6f"));
                checkThemeTopNote = "Yellow";
                editor.putString("theme_top_note", checkThemeTopNote);
                editor.apply();
                dialog.dismiss();
            }
        });

        greeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#2dab61"));
                toolbar.setBackgroundColor(Color.parseColor("#2dab61"));
                checkThemeTopNote = "Green";
                editor.putString("theme_top_note", checkThemeTopNote);
                editor.apply();
                dialog.dismiss();
            }
        });

        bluue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#337dc0"));
                toolbar.setBackgroundColor(Color.parseColor("#337dc0"));
                checkThemeTopNote = "Blue";
                editor.putString("theme_top_note", checkThemeTopNote);
                editor.apply();
                dialog.dismiss();
            }
        });

        aqqua_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#80b4e2"));
                toolbar.setBackgroundColor(Color.parseColor("#80b4e2"));
                checkThemeTopNote = "Aqua blue";
                editor.putString("theme_top_note", checkThemeTopNote);
                editor.apply();
                dialog.dismiss();
            }
        });

        fiiolet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#905aa1"));
                toolbar.setBackgroundColor(Color.parseColor("#905aa1"));
                checkThemeTopNote = "fiolet";
                editor.putString("theme_top_note", checkThemeTopNote);
                editor.apply();
                dialog.dismiss();
            }
        });

        piink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#e54591"));
                toolbar.setBackgroundColor(Color.parseColor("#e54591"));
                checkThemeTopNote = "pink";
                editor.putString("theme_top_note", checkThemeTopNote);
                editor.apply();
                dialog.dismiss();
            }
        });

        Log.i(TAG, checkThemeTopNote + " Внутри theme dialog");

        dialog.show();
    }

}