package com.george.modern_notes.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.george.modern_notes.common.MainActivity;
import com.george.modern_notes.R;

public class TopNoteActivity extends AppCompatActivity {

    EditText name_note_top, note_top;
    MaterialToolbar toolbar;
    private static final String TAG = "TopNoteActivity";

    View theme;
    String check_theme_top_note = "Default";

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
        setContentView(R.layout.activity_top_note);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.topAppBar_note_top);
        theme = findViewById(R.id.theme_top_note);
        name_note_top = findViewById(R.id.name_note_top);
        note_top = findViewById(R.id.note_text_top);
        ExtendedFloatingActionButton save = findViewById(R.id.save_new_note);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> goHome());

        loadData();
        save.setOnClickListener(view -> updateData());
    }

    public void loadData() {
        String name_note_def = getString(R.string.default_name_top_note);
        String note_top_def = getString(R.string.default_text_top_note);
        SharedPreferences sharedPreferencesSS = getSharedPreferences("top", Context.MODE_PRIVATE);
        String top_note_string = sharedPreferencesSS.getString("note", note_top_def);
        String name_top_note_string = sharedPreferencesSS.getString("name_note", name_note_def);
        check_theme_top_note = sharedPreferencesSS.getString("theme_top_note", "Default");
        Log.i(TAG, check_theme_top_note + " Полученные данные");

        assert check_theme_top_note != null;
        if(check_theme_top_note.equals("Default")){
            theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
            toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));
        }

        if(check_theme_top_note.equals("Red")){
            theme.setBackgroundColor(Color.parseColor("#ee7576"));
            toolbar.setBackgroundColor(Color.parseColor("#ee7576"));
        }

        if(check_theme_top_note.equals("Light Yellow")){
            theme.setBackgroundColor(Color.parseColor("#ffeaa7"));
            toolbar.setBackgroundColor(Color.parseColor("#ffeaa7"));
        }

        if(check_theme_top_note.equals("Yellow")){
            theme.setBackgroundColor(Color.parseColor("#fdcb6f"));
            toolbar.setBackgroundColor(Color.parseColor("#fdcb6f"));
        }

        if(check_theme_top_note.equals("Green")){
            theme.setBackgroundColor(Color.parseColor("#2dab61"));
            toolbar.setBackgroundColor(Color.parseColor("#2dab61"));
        }

        if(check_theme_top_note.equals("Blue")){
            theme.setBackgroundColor(Color.parseColor("#337dc0"));
            toolbar.setBackgroundColor(Color.parseColor("#337dc0"));
        }

        if(check_theme_top_note.equals("Aqua blue")){
            theme.setBackgroundColor(Color.parseColor("#80b4e2"));
            toolbar.setBackgroundColor(Color.parseColor("#80b4e2"));
        }

        if(check_theme_top_note.equals("violet")){
            theme.setBackgroundColor(Color.parseColor("#905aa1"));
            toolbar.setBackgroundColor(Color.parseColor("#905aa1"));
        }

        if(check_theme_top_note.equals("pink")){
            theme.setBackgroundColor(Color.parseColor("#e54591"));
            toolbar.setBackgroundColor(Color.parseColor("#e54591"));
        }

        name_note_top.setText(name_top_note_string);
        note_top.setText(top_note_string);
    }

    public void updateData() {
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
        inflater.inflate(R.menu.menu_delete, menu);

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

        return super.onOptionsItemSelected(item);
    }
}