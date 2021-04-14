package com.george.modern_notes.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.george.modern_notes.R;
import com.george.modern_notes.common.StarterActivity;
import com.george.modern_notes.database.NotesDatabase;

public class AddNoteActivity extends AppCompatActivity {

    MaterialToolbar toolbar, toolbarDark;

    EditText nameBox;
    EditText noteBox;
    FloatingActionButton save_button;

    NotesDatabase sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long notesId = 0;

    private static final String TAG = "addNote";

    View theme;
    String checkTheme = "Default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = sharedPreferences.getString("theme_app", "Fiolet");

        Log.e(TAG, theme_app+" Global Theme");

        assert theme_app != null;
        if(theme_app.equals("Orange"))
            setTheme(R.style.Orange);

        if(theme_app.equals("Dark"))
            setTheme(R.style.DarckTheme);

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
        setContentView(R.layout.activity_add_note);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, initializationStatus -> {
        });

//        mAdView = findViewById(R.id.adViewAddNote);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        Log.e(TAG, checkTheme+" В самом начале OnCreate");

        toolbar = findViewById(R.id.topAppBar_add_note_darck_buttns);
        toolbarDark = findViewById(R.id.topAppBar_add_note_dark);

        if(theme_app.equals("Dark")){
            setSupportActionBar(toolbarDark);
            toolbarDark.setVisibility(View.VISIBLE);
            toolbarDark.setNavigationIcon(R.drawable.ic_white_baseline_arrow_back_24);
            toolbarDark.setNavigationOnClickListener(view -> save());
        } else {
            setSupportActionBar(toolbar);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setNavigationOnClickListener(view -> save());
        }

        nameBox = findViewById(R.id.name_note);
        noteBox = findViewById(R.id.note_text);

        save_button = findViewById(R.id.fab_save_note);

        save_button.setOnClickListener(v -> save());

        boolean save_btn_activ = sharedPreferences.getBoolean("save_button_note", true);

        if(!save_btn_activ)
            save_button.setVisibility(View.GONE);

        Log.i(TAG, save_btn_activ + " активация кнопки");

        String name_user = sharedPreferences.getString("full_name", "empty_user_name");
        assert name_user != null;
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(AddNoteActivity.this, StarterActivity.class));

        if(theme_app.equals("Orange"))
            save_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab)));

        if(theme_app.equals("Dark"))
            save_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_color_fab)));

        if(theme_app.equals("Blue"))
            save_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));

        if(theme_app.equals("AquaBlue"))
            save_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blueaqua_color_fab)));

        if(theme_app.equals("Green"))
            save_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));

        if(theme_app.equals("Red"))
            save_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));

        if(theme_app.equals("Fiolet"))
            save_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));

        sqlHelper = new NotesDatabase(this);
        db = sqlHelper.getWritableDatabase();

        theme = findViewById(R.id.theme_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            notesId = extras.getLong("id");

        }
        // если 0, то добавление
        if (notesId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + NotesDatabase.TABLE + " where " +
                    NotesDatabase.COLUMN_ID + "=?", new String[]{String.valueOf(notesId)});
            userCursor.moveToFirst();

            nameBox.setText(userCursor.getString(1));
            noteBox.setText(userCursor.getString(2));
            checkTheme = userCursor.getString(3);
            Log.e(TAG, checkTheme+" Когда получил значение");

            if(checkTheme.equals("Default")){
                if(theme_app.equals("Dark"))
                    Log.i(TAG, "Ничего не делаем!");
                else {
                    theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
                    toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));
                }
            }

            if(checkTheme.equals("Red")){
                theme.setBackgroundColor(Color.parseColor("#ff9e9e"));
                toolbar.setBackgroundColor(Color.parseColor("#ff9e9e"));
            }

            if(checkTheme.equals("Light Yellow")){
                theme.setBackgroundColor(Color.parseColor("#ffeaa7"));
                toolbar.setBackgroundColor(Color.parseColor("#ffeaa7"));
            }

            if(checkTheme.equals("Yellow")){
                theme.setBackgroundColor(Color.parseColor("#fdcb6f"));
                toolbar.setBackgroundColor(Color.parseColor("#fdcb6f"));
            }

            if(checkTheme.equals("Green")){
                theme.setBackgroundColor(Color.parseColor("#a3ff9e"));
                toolbar.setBackgroundColor(Color.parseColor("#a3ff9e"));
            }

            if(checkTheme.equals("Blue")){
                theme.setBackgroundColor(Color.parseColor("#9eb8ff"));
                toolbar.setBackgroundColor(Color.parseColor("#9eb8ff"));
            }

            if(checkTheme.equals("Aqua blue")){
                theme.setBackgroundColor(Color.parseColor("#74cdfc"));
                toolbar.setBackgroundColor(Color.parseColor("#74cdfc"));
            }

            if(checkTheme.equals("fiolet")){
                theme.setBackgroundColor(Color.parseColor("#e9a6ff"));
                toolbar.setBackgroundColor(Color.parseColor("#e9a6ff"));
            }

            if(checkTheme.equals("pink")){
                theme.setBackgroundColor(Color.parseColor("#ff9cbe"));
                toolbar.setBackgroundColor(Color.parseColor("#ff9cbe"));
            }

            userCursor.close();
        }

    }

    public void save() {
        if(validateNameNote()) {
            ContentValues cv = new ContentValues();
            cv.put(NotesDatabase.COLUMN_NAME_NOTE, nameBox.getText().toString());
            cv.put(NotesDatabase.COLUMN_NOTE, noteBox.getText().toString());
            cv.put(NotesDatabase.COLUMN_THEME, checkTheme);

            if (notesId > 0)
                db.update(NotesDatabase.TABLE, cv, NotesDatabase.COLUMN_ID + "=" + notesId, null);
            else
                db.insert(NotesDatabase.TABLE, null, cv);

            goHome();
        }

    }

    public void delete(){
        db.delete(NotesDatabase.TABLE, "_id = ?", new String[]{String.valueOf(notesId)});
        goHome();
    }

    public void goHome(){
        db.close();

        Intent intent = new Intent(this, NotebookActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public boolean validateNameNote() {
        String check_name = nameBox.getText().toString().trim();
        String check_note = noteBox.getText().toString().trim();

        if(check_name.isEmpty() && check_note.isEmpty()) {
            String empty = "Empty boxes";
            Intent intent = new Intent(this, NotebookActivity.class);
            intent.putExtra("empty", empty);
            startActivity(intent);
            return false;
        } else {
            return true;
        }

    }

    public void showDialogTheme() {
        final Dialog dialog = new Dialog(AddNoteActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_theme);

        Button done = dialog.findViewById(R.id.button_ok);
        done.setOnClickListener(view -> dialog.dismiss());

        TextView text_default_theme = dialog.findViewById(R.id.text_default_theme);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String theme_app_check = sharedPreferences.getString("theme_app", "Fiolet");

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

        theme = findViewById(R.id.theme_view);
        Drawable image = getResources().getDrawable(R.drawable.dark_default);

        if(theme_app_check.equals("Dark")) {
            default_th.setImageDrawable(image);
            text_default_theme.setTextColor(Color.WHITE);
            done.setTextColor(Color.WHITE);
        }

        if(checkTheme.equals("Default")) {
            check_default.setVisibility(View.VISIBLE);
        }

        if(checkTheme.equals("Red"))
            check_red.setVisibility(View.VISIBLE);

        if(checkTheme.equals("Light Yellow"))
            chek_light_yellow.setVisibility(View.VISIBLE);

        if(checkTheme.equals("Yellow"))
            check_yellow.setVisibility(View.VISIBLE);

        if(checkTheme.equals("Green"))
            check_green.setVisibility(View.VISIBLE);

        if(checkTheme.equals("Blue"))
            check_blue.setVisibility(View.VISIBLE);

        if(checkTheme.equals("Aqua blue"))
            check_aqua_blue.setVisibility(View.VISIBLE);

        if(checkTheme.equals("fiolet"))
            check_fiolet.setVisibility(View.VISIBLE);

        if(checkTheme.equals("pink"))
            chek_pink.setVisibility(View.VISIBLE);


        default_th.setOnClickListener(view -> {
            Log.i(TAG, theme_app_check + " проверка в теме");
            if(theme_app_check.equals("Dark")) {
                recreate();
            } else {
                theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
                toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));
            }

            checkTheme = "Default";
            dialog.dismiss();
        });

        reed.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#ff9e9e"));
            toolbar.setBackgroundColor(Color.parseColor("#ff9e9e"));
            checkTheme = "Red";
            dialog.dismiss();
        });

        light_yello.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#ffeaa7"));
            toolbar.setBackgroundColor(Color.parseColor("#ffeaa7"));
            checkTheme = "Light Yellow";
            dialog.dismiss();
        });

        yello.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#fdcb6f"));
            toolbar.setBackgroundColor(Color.parseColor("#fdcb6f"));
            checkTheme = "Yellow";
            dialog.dismiss();
        });

        greeen.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#a3ff9e"));
            toolbar.setBackgroundColor(Color.parseColor("#a3ff9e"));
            checkTheme = "Green";
            dialog.dismiss();
        });

        bluue.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#9eb8ff"));
            toolbar.setBackgroundColor(Color.parseColor("#9eb8ff"));
            checkTheme = "Blue";
            dialog.dismiss();
        });

        aqqua_blue.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#74cdfc"));
            toolbar.setBackgroundColor(Color.parseColor("#74cdfc"));
            checkTheme = "Aqua blue";
            dialog.dismiss();
        });

        fiiolet.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#e9a6ff"));
            toolbar.setBackgroundColor(Color.parseColor("#e9a6ff"));
            checkTheme = "fiolet";
            dialog.dismiss();
        });

        piink.setOnClickListener(view -> {
            theme.setBackgroundColor(Color.parseColor("#ff9cbe"));
            toolbar.setBackgroundColor(Color.parseColor("#ff9cbe"));
            checkTheme = "pink";
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = sharedPreferences.getString("theme_app", "Fiolet");

        MenuInflater inflater = getMenuInflater();

        if(theme_app.equals("Dark"))
            inflater.inflate(R.menu.menu_top_note_only, menu);
        else
            inflater.inflate(R.menu.add_note_darck, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.in_top_note) {
            String name_top_note = nameBox.getText().toString();
            String top_note = noteBox.getText().toString();
            SharedPreferences sharedPreferences = this.getSharedPreferences("top", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(name_top_note.isEmpty() && top_note.isEmpty()) {
                Log.e(TAG, "Полностью пустая заметка");
                Toast.makeText(getApplicationContext(), getText(R.string.error_empty_top_note), Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Нормальная заметка");
                Toast.makeText(getApplicationContext(), getText(R.string.pinned_done_text), Toast.LENGTH_SHORT).show();

                editor.putString("note", top_note);
                editor.putString("name_note", name_top_note);
                editor.putString("theme_top_note", checkTheme);

                editor.apply();
            }

            if(name_top_note.isEmpty() && top_note.length() > 0) {
                Log.e(TAG, "Имя пустое а заметка нет");

                String nameEmpty = getString(R.string.noname_note);

                editor.putString("name_note", nameEmpty);
                editor.putString("note", top_note);
                editor.putString("theme_top_note", checkTheme);

                editor.apply();
            }

            if(name_top_note.length() > 0 && top_note.isEmpty()) {
                Log.e(TAG, "Заметка пустая а имя нет");

                String noteEmpty = getString(R.string.blank_note);

                editor.putString("name_note", name_top_note);
                editor.putString("note", noteEmpty);
                editor.putString("theme_top_note", checkTheme);

                editor.apply();
            }

        }

        if (item.getItemId() == R.id.nav_theme)
            showDialogTheme();

        if(item.getItemId() == R.id.del)
            delete();

        return super.onOptionsItemSelected(item);
    }


}