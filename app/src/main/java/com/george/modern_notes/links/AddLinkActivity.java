package com.george.modern_notes.links;

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
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.george.modern_notes.R;
import com.george.modern_notes.common.StarterActivity;
import com.george.modern_notes.database.LinksDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddLinkActivity extends AppCompatActivity implements BottomSheetLinks.BottomSheetListener {

    TextInputEditText name_link_edit_text, link_edit_text;
    TextInputLayout name_link_input_layout, link_input_layout;
    EditText link_note_input_layout;

    MaterialToolbar toolbar;
    FloatingActionButton save_link;
    String check_theme_link = "Default";

    private static final String TAG = "addLink";

    LinksDatabase sql_helper;
    SQLiteDatabase db;
    Cursor user_cursor;
    long link_id =0;

    TextView date_text_view;
    String DATE;

    CardView card_more_bottom;
    ImageView more_links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences shared_preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = shared_preferences.getString(getString(R.string.root_theme_app), getString(R.string.root_theme_violet));

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
        setContentView(R.layout.activity_add_link);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        save_link = findViewById(R.id.save_link_fab);
        date_text_view = findViewById(R.id.date_textView_link);
        card_more_bottom = findViewById(R.id.card_more_bottom_link);
        more_links = findViewById(R.id.more_links);
        toolbar = findViewById(R.id.topAppBar_add_link);
        name_link_edit_text = findViewById(R.id.name_link_edit_text);
        link_edit_text = findViewById(R.id.link_edit_text);
        link_note_input_layout = findViewById(R.id.note_link);
        name_link_input_layout = findViewById(R.id.link_name_text_layout);
        link_input_layout = findViewById(R.id.link_edit_text_layout);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> goHomeLink());

        more_links.setOnClickListener(v -> {
            BottomSheetLinks bottom_sheet_notes = new BottomSheetLinks();

            Bundle bundle = new Bundle();
            bundle.putString("theme", check_theme_link);
            bottom_sheet_notes.setArguments(bundle);

            bottom_sheet_notes.show(getSupportFragmentManager(), "BottomSheetNotes");
        });

        save_link.setOnClickListener(view -> saveLink());

        link_input_layout.setEndIconOnClickListener(view -> {
            if(validateLink()) {
                String url = Objects.requireNonNull(link_input_layout.getEditText()).getText().toString();

                if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://") )
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                else {
                    String urlCheck = "http://" + url;
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlCheck)));
                }

            }

        });

        String name_user = shared_preferences.getString(getString(R.string.root_full_name), "empty_user_name");
        assert name_user != null;
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(AddLinkActivity.this, StarterActivity.class));

        Date current_date = new Date();
        DateFormat date_format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String date_text = date_format.format(current_date);
        DateFormat time_format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time_text = time_format.format(current_date);
        DATE = getString(R.string.last_modified) + ":" + " " + date_text + " " + time_text;
        date_text_view.setText(DATE);

        sql_helper = new LinksDatabase(this);
        db = sql_helper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            link_id = extras.getLong("id");
        }

        if (link_id > 0) {
            user_cursor = db.rawQuery("select * from " + LinksDatabase.TABLE + " where " +
                    LinksDatabase.COLUMN_ID + "=?", new String[]{String.valueOf(link_id)});
            user_cursor.moveToFirst();

            name_link_edit_text.setText(user_cursor.getString(1));
            link_edit_text.setText(user_cursor.getString(2));
            link_note_input_layout.setText(user_cursor.getString(3));
            String date = user_cursor.getString(4);
            check_theme_link = user_cursor.getString(5);

            Log.i(TAG, "checkThemeLink - " + check_theme_link);
            Log.i(TAG, "date" + date);

            user_cursor.close();
        }


        Objects.requireNonNull(link_input_layout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                link_input_layout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public boolean validateLink() {
        String check = Objects.requireNonNull(link_input_layout.getEditText()).getText().toString().trim();

        if(check.isEmpty()){
            link_input_layout.setError(getString(R.string.error_empty_field));
            return false;
        } else {
            link_input_layout.setError(null);
            return true;
        }

    }

    public boolean validateNameLink() {
        String check = Objects.requireNonNull(name_link_input_layout.getEditText()).getText().toString();
        return !check.isEmpty();
    }

    public void saveLink() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        DATE = getString(R.string.last_modified) + ":" + " " + dateText + " " + timeText;


        boolean data = validateLink();
        Log.i(TAG, "DATA - " + data);

        if(!validateLink()) {
            Log.i(TAG, "!validate");
        } else if (!validateNameLink()) {

            Objects.requireNonNull(name_link_input_layout.getEditText()).setText(getString(R.string.no_name_link));

            ContentValues cv = new ContentValues();
            cv.put(LinksDatabase.COLUMN_NAME_LINK, Objects.requireNonNull(name_link_edit_text.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LNK, Objects.requireNonNull(link_edit_text.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LINK_NOTE, link_note_input_layout.getText().toString());
            cv.put(LinksDatabase.COLUMN_LINK_DATE, DATE);
            cv.put(LinksDatabase.COLUMN_THEME_MODE_LINK, check_theme_link);

            if (link_id > 0) {
                db.update(LinksDatabase.TABLE, cv, LinksDatabase.COLUMN_ID + "=" + link_id, null);
            } else {
                db.insert(LinksDatabase.TABLE, null, cv);
            }
            goHomeLink();

        } else {

            ContentValues cv = new ContentValues();
            cv.put(LinksDatabase.COLUMN_NAME_LINK, Objects.requireNonNull(name_link_edit_text.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LNK, Objects.requireNonNull(link_edit_text.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LINK_NOTE, link_note_input_layout.getText().toString());
            cv.put(LinksDatabase.COLUMN_LINK_DATE, DATE);
            cv.put(LinksDatabase.COLUMN_THEME_MODE_LINK, check_theme_link);

            if (link_id > 0) {
                db.update(LinksDatabase.TABLE, cv, LinksDatabase.COLUMN_ID + "=" + link_id, null);
            } else {
                db.insert(LinksDatabase.TABLE, null, cv);
            }
            goHomeLink();
        }

    }

    public void deleteLink(){
        db.delete(LinksDatabase.TABLE, "_id = ?", new String[]{String.valueOf(link_id)});
        goHomeLink();
    }

    private void goHomeLink(){
        db.close();

        Intent intent = new Intent(this, ListOfLinksActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onButtonClicked(String button_clicked) {
        Log.i(TAG, "" + button_clicked);
        CoordinatorLayout Layout = findViewById(R.id.snackbar_layout_links);

        if(button_clicked.equals("Button delete clicked")) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean confirmDelete = preferences.getBoolean(getString(R.string.root_delete_bool), true);

            String checkLink = Objects.requireNonNull(link_input_layout.getEditText()).getText().toString();
            String checkNameLink = Objects.requireNonNull(name_link_input_layout.getEditText()).getText().toString();
            String NoteLink = link_note_input_layout.getText().toString();

            if(checkLink.isEmpty() & checkNameLink.isEmpty() & NoteLink.isEmpty()) {
                Snackbar.make(Layout, "Пустая ссылка не может быть удалена", Snackbar.LENGTH_SHORT).setAction("error", null).show();
            } else {
                if (confirmDelete) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddLinkActivity.this);
                    builder.setTitle(getString(R.string.attention));
                    builder.setMessage(getString(R.string.confirm_delete_link));

                    builder.setPositiveButton(getString(android.R.string.ok), (dialog, id) -> {
                        deleteLink();
                        dialog.dismiss();
                    });

                    builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.dismiss());

                    builder.show();

                } else
                    deleteLink();
            }
        }

        if(button_clicked.equals("Button copy clicked")) {
            String copy = Objects.requireNonNull(link_edit_text.getText()).toString();
            if(copy.isEmpty()) {
                Snackbar.make(Layout, getText(R.string.empty_link_cant_copied), Snackbar.LENGTH_SHORT)
                        .setAction("done", null).show();
            } else {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", copy);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Snackbar.make(Layout, getText(R.string.link_copied), Snackbar.LENGTH_SHORT)
                        .setAction("done", null).show();
            }
        }

        if(button_clicked.equals("Button share clicked")) {
            String copy = Objects.requireNonNull(link_edit_text.getText()).toString();
            if (copy.isEmpty()) {
                Snackbar.make(Layout, getText(R.string.empty_link_cant_shared), Snackbar.LENGTH_SHORT).setAction("done", null).show();
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