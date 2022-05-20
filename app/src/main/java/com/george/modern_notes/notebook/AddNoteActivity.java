package com.george.modern_notes.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.george.modern_notes.R;
import com.george.modern_notes.common.StarterActivity;
import com.george.modern_notes.database.NotesDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity implements BottomSheetNotes.BottomSheetListener {

    MaterialToolbar toolbar;
    TextView date_text_view;
    EditText name_note_edit_text;
    EditText note_edit_text;
    FloatingActionButton save_button;
    ImageView more_notes;
    CardView card_more_bottom;
    CoordinatorLayout coordinator_layout;

    String check_theme = "Default";
    String DATE;

    NotesDatabase sql_helper;
    SQLiteDatabase db;
    Cursor user_cursor;
    long note_id = 0;

    private static final String TAG = "addNote";

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
        setContentView(R.layout.activity_add_note);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        card_more_bottom = findViewById(R.id.card_more_bottom);
        more_notes = findViewById(R.id.more_notes);
        date_text_view = findViewById(R.id.date_textView);
        name_note_edit_text = findViewById(R.id.name_note);
        note_edit_text = findViewById(R.id.note_text);
        save_button = findViewById(R.id.fab_save_note);
        coordinator_layout = findViewById(R.id.snackbar_layout_note);
        toolbar = findViewById(R.id.topAppBar_add_note);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> save());

        more_notes.setOnClickListener(v -> {
            BottomSheetNotes bottomSheetNotes = new BottomSheetNotes();

            Bundle bundle = new Bundle();
            bundle.putString("theme", check_theme);
            bottomSheetNotes.setArguments(bundle);

            bottomSheetNotes.show(getSupportFragmentManager(), "BottomSheetNotes");
        });
        save_button.setOnClickListener(v -> save());

        boolean save_btn_activate = preferences.getBoolean("save_button_note", true);
        if(!save_btn_activate)
            save_button.setVisibility(View.GONE);

        String name_user = preferences.getString(getString(R.string.root_full_name), "empty_user_name");
        assert name_user != null;
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(AddNoteActivity.this, StarterActivity.class));

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        DATE = getString(R.string.last_modified) + ":" + " " + dateText + " " + timeText;
        date_text_view.setText(DATE);

        sql_helper = new NotesDatabase(this);
        db = sql_helper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            note_id = extras.getLong("id");

        if (note_id > 0) {
            user_cursor = db.rawQuery("select * from " + NotesDatabase.TABLE + " where " +
                    NotesDatabase.COLUMN_ID + "=?", new String[]{String.valueOf(note_id)});
            user_cursor.moveToFirst();

            name_note_edit_text.setText(user_cursor.getString(1));
            note_edit_text.setText(user_cursor.getString(2));
            date_text_view.setText(user_cursor.getString(3));
            check_theme = user_cursor.getString(4);

            user_cursor.close();
        }

    }

    public void save() {
        if(validateNameNote()) {
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String dateText = dateFormat.format(currentDate);
            DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String timeText = timeFormat.format(currentDate);
            DATE = getString(R.string.last_modified) + ":" + " " + dateText + " " + timeText;

            ContentValues cv = new ContentValues();
            cv.put(NotesDatabase.COLUMN_NAME_NOTE, name_note_edit_text.getText().toString());
            cv.put(NotesDatabase.COLUMN_NOTE, note_edit_text.getText().toString());
            cv.put(NotesDatabase.COLUMN_DATE, DATE);
            cv.put(NotesDatabase.COLUMN_THEME, check_theme);

            if (note_id > 0)
                db.update(NotesDatabase.TABLE, cv, NotesDatabase.COLUMN_ID + "=" + note_id, null);
            else
                db.insert(NotesDatabase.TABLE, null, cv);

            goHome();
        }

    }

    public void delete(){
        db.delete(NotesDatabase.TABLE, "_id = ?", new String[]{String.valueOf(note_id)});
        goHome();
    }

    public void goHome(){
        db.close();

        Intent intent = new Intent(this, NotebookActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public boolean validateNameNote() {
        String check_name = name_note_edit_text.getText().toString().trim();
        String check_note = note_edit_text.getText().toString().trim();

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

    @Override
    public void onBackPressed() {
        save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top_note, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.in_top_note) {
            String name_top_note = name_note_edit_text.getText().toString();
            String top_note = note_edit_text.getText().toString();

            SharedPreferences sharedPreferences = this.getSharedPreferences("top", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(name_top_note.isEmpty() && top_note.isEmpty()) {
                Log.e(TAG, "Полностью пустая заметка");
                Snackbar.make(coordinator_layout, getText(R.string.error_empty_top_note), Snackbar.LENGTH_SHORT).setAction("done", null).show();
            } else {
                Log.e(TAG, "Нормальная заметка");
                Snackbar.make(coordinator_layout, getText(R.string.pinned_done_text), Snackbar.LENGTH_SHORT).setAction("done", null).show();

                editor.putString("note", top_note);
                editor.putString("name_note", name_top_note);
                editor.putString("theme_top_note", check_theme);

                editor.apply();
            }

            if(name_top_note.isEmpty() && top_note.length() > 0) {
                Log.e(TAG, "Имя пустое а заметка нет");
                Snackbar.make(coordinator_layout, getText(R.string.pinned_done_text), Snackbar.LENGTH_SHORT).setAction("done", null).show();

                String nameEmpty = getString(R.string.no_name_note);

                editor.putString("name_note", nameEmpty);
                editor.putString("note", top_note);
                editor.putString("theme_top_note", check_theme);

                editor.apply();
            }

            if(name_top_note.length() > 0 && top_note.isEmpty()) {
                Log.e(TAG, "Заметка пустая а имя нет");
                Snackbar.make(coordinator_layout, getText(R.string.pinned_done_text), Snackbar.LENGTH_SHORT).setAction("done", null).show();

                String noteEmpty = getString(R.string.blank_note);

                editor.putString("name_note", name_top_note);
                editor.putString("note", noteEmpty);
                editor.putString("theme_top_note", check_theme);

                editor.apply();
            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onButtonClicked(String button_clicked) {
        if(button_clicked.equals("Button delete clicked")) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean confirmDelete = preferences.getBoolean(getString(R.string.root_delete_bool), true);

            String checkNameNote = name_note_edit_text.getText().toString();
            String checkNote = note_edit_text.getText().toString();

            if(checkNameNote.isEmpty() & checkNote.isEmpty()) {
                Snackbar.make(coordinator_layout, "Пустая заметка не может быть удалена", Snackbar.LENGTH_SHORT).setAction("error", null).show();
            } else {

                if (confirmDelete) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
                    builder.setTitle(getText(R.string.attention));
                    builder.setMessage(getText(R.string.confirm_delete_note));

                    builder.setPositiveButton(getString(android.R.string.ok), (dialog, id) -> {
                        delete();
                        dialog.dismiss();
                    });

                    builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.dismiss());

                    builder.show();

                } else
                    delete();
            }

        }

        if(button_clicked.equals("Button copy clicked")){
            String copy = note_edit_text.getText().toString();
            if(copy.isEmpty()){
                Log.i(TAG, "empty_note_cant_copied");
                Snackbar.make(coordinator_layout, getText(R.string.empty_note_cant_copied), Snackbar.LENGTH_SHORT).setAction("done", null).show();
            } else {
                Log.i(TAG, "note_copied");
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", copy);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Snackbar.make(coordinator_layout, getText(R.string.note_copied), Snackbar.LENGTH_SHORT).setAction("done", null).show();
            }
        }

        if(button_clicked.equals("Button share clicked")) {
            String copy = note_edit_text.getText().toString();
            if (copy.isEmpty()) {
                Snackbar.make(coordinator_layout, getText(R.string.empty_note_cant_shared), Snackbar.LENGTH_SHORT).setAction("done", null).show();
            } else {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, copy + "");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        }

    }
}