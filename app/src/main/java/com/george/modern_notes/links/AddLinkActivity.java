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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

public class    AddLinkActivity extends AppCompatActivity implements BottomSheetLinks.BottomSheetListener {

    TextInputEditText nameLinkEditText, linkEditText;
    TextInputLayout name_link_input_layout, link_input_layout;
    EditText link_note_input_layout;

    MaterialToolbar toolbar;
    FloatingActionButton saveLink;
    View theme;
    String checkThemeLink = "Default";

    private static final String TAG = "addLink";

    LinksDatabase sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long linkId =0;

    TextView dateView;
    String dateFull;

    CardView card_more_bottom;
    ImageView more_links;

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
        setContentView(R.layout.activity_add_link);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        saveLink = findViewById(R.id.save_link_fab);
        dateView = findViewById(R.id.date_textView_link);
        card_more_bottom = findViewById(R.id.card_more_bottom_link);
        more_links = findViewById(R.id.more_links);
        toolbar = findViewById(R.id.topAppBar_add_link);
        nameLinkEditText = findViewById(R.id.name_link_edit_text);
        linkEditText = findViewById(R.id.link_edit_text);
        link_note_input_layout = findViewById(R.id.note_link);
        name_link_input_layout = findViewById(R.id.link_name_text_layout);
        link_input_layout = findViewById(R.id.link_edit_text_layout);
        theme = findViewById(R.id.theme_link);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> goHome_link());

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        dateFull = getString(R.string.last_modified) + ":" + " " + dateText + " " + timeText;
        dateView.setText(dateFull);

        more_links.setOnClickListener(v -> {
            BottomSheetLinks bottomSheetNotes = new BottomSheetLinks();

            Bundle bundle = new Bundle();
            bundle.putString("theme", checkThemeLink );
            bottomSheetNotes.setArguments(bundle);

            bottomSheetNotes.show(getSupportFragmentManager(), "BottomSheetNotes");
        });

        saveLink.setOnClickListener(view -> save_link());

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

        String name_user = sharedPreferences.getString(getString(R.string.root_full_name), "empty_user_name");

        assert name_user != null;
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(AddLinkActivity.this, StarterActivity.class));

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

        sqlHelper = new LinksDatabase(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            linkId = extras.getLong("id");
        }

        if (linkId > 0) {
            userCursor = db.rawQuery("select * from " + LinksDatabase.TABLE + " where " +
                    LinksDatabase.COLUMN_ID + "=?", new String[]{String.valueOf(linkId)});
            userCursor.moveToFirst();

            nameLinkEditText.setText(userCursor.getString(1));
            linkEditText.setText(userCursor.getString(2));
            link_note_input_layout.setText(userCursor.getString(3));
            String date = userCursor.getString(4);
            checkThemeLink = userCursor.getString(5);

            Log.i(TAG, "checkThemeLink - " + checkThemeLink);
            Log.i(TAG, "date" + date);

            switch (checkThemeLink){

                case ("Default"):
                    theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
                    toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#FAFAFA"));
                    break;

                case("Red"):
                    theme.setBackgroundColor(Color.parseColor("#FF8C8C"));
                    toolbar.setBackgroundColor(Color.parseColor("#FF8C8C"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#FF8C8C"));
                    break;

                case ("Orange"):
                    theme.setBackgroundColor(Color.parseColor("#FFB661"));
                    toolbar.setBackgroundColor(Color.parseColor("#FFB661"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#FFB661"));
                    break;

                case ("Yellow"):
                    theme.setBackgroundColor(Color.parseColor("#FFD850"));
                    toolbar.setBackgroundColor(Color.parseColor("#FFD850"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#FFD850"));
                    break;

                case ("Green"):
                    theme.setBackgroundColor(Color.parseColor("#7AE471"));
                    toolbar.setBackgroundColor(Color.parseColor("#7AE471"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#7AE471"));
                    break;

                case("Light Green"):
                    theme.setBackgroundColor(Color.parseColor("#56E0C7"));
                    toolbar.setBackgroundColor(Color.parseColor("#56E0C7"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#56E0C7"));
                    break;

                case("Light Blue"):
                    theme.setBackgroundColor(Color.parseColor("#6CD3FF"));
                    toolbar.setBackgroundColor(Color.parseColor("#6CD3FF"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#6CD3FF"));

                case("Blue"):
                    theme.setBackgroundColor(Color.parseColor("#819CFF"));
                    toolbar.setBackgroundColor(Color.parseColor("#819CFF"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#819CFF"));
                    break;

                case ("violet"):
                    theme.setBackgroundColor(Color.parseColor("#DD8BFA"));
                    toolbar.setBackgroundColor(Color.parseColor("#DD8BFA"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#DD8BFA"));
                    break;

                case("Pink"):
                    theme.setBackgroundColor(Color.parseColor("#FF6CA1"));
                    toolbar.setBackgroundColor(Color.parseColor("#FF6CA1"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#FF6CA1"));
                    break;

                case("Gray"):
                    theme.setBackgroundColor(Color.parseColor("#C4C4C4"));
                    toolbar.setBackgroundColor(Color.parseColor("#C4C4C4"));
                    card_more_bottom.setCardBackgroundColor(Color.parseColor("#C4C4C4"));
                    break;
            }

            userCursor.close();
        }

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

    public void save_link() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        dateFull = getString(R.string.last_modified) + ":" + " " + dateText + " " + timeText;


        boolean data = validateLink();
        Log.i(TAG, "DATA - " + data);

        if(!validateLink()) {
            Log.i(TAG, "!validate");
        } else if (!validateNameLink()) {

            Objects.requireNonNull(name_link_input_layout.getEditText()).setText(getString(R.string.noname_link));

            ContentValues cv = new ContentValues();
            cv.put(LinksDatabase.COLUMN_NAME_LINK, Objects.requireNonNull(nameLinkEditText.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LNK, Objects.requireNonNull(linkEditText.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LINK_NOTE, link_note_input_layout.getText().toString());
            cv.put(LinksDatabase.COLUMN_LINK_DATE, dateFull);
            cv.put(LinksDatabase.COLUMN_THEME_MODE_LINK, checkThemeLink);

            if (linkId > 0) {
                db.update(LinksDatabase.TABLE, cv, LinksDatabase.COLUMN_ID + "=" + linkId, null);
            } else {
                db.insert(LinksDatabase.TABLE, null, cv);
            }
            goHome_link();

        } else {

            ContentValues cv = new ContentValues();
            cv.put(LinksDatabase.COLUMN_NAME_LINK, Objects.requireNonNull(nameLinkEditText.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LNK, Objects.requireNonNull(linkEditText.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LINK_NOTE, link_note_input_layout.getText().toString());
            cv.put(LinksDatabase.COLUMN_LINK_DATE, dateFull);
            cv.put(LinksDatabase.COLUMN_THEME_MODE_LINK, checkThemeLink);

            if (linkId > 0) {
                db.update(LinksDatabase.TABLE, cv, LinksDatabase.COLUMN_ID + "=" + linkId, null);
            } else {
                db.insert(LinksDatabase.TABLE, null, cv);
            }
            goHome_link();
        }

    }

    public void delete_link(){
        db.delete(LinksDatabase.TABLE, "_id = ?", new String[]{String.valueOf(linkId)});
        goHome_link();
    }

    private void goHome_link(){
        db.close();

        Intent intent = new Intent(this, ListOfLinksActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onButtonClicked(String button_clicked) {
        Log.i(TAG, "" + button_clicked);
        CoordinatorLayout
                Layout = findViewById(R.id.snak_layout_links);

        if(button_clicked.equals("Button delete clicked")) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean confirmDelete = preferences.getBoolean(getString(R.string.root_delete_bool), true);

            if(confirmDelete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddLinkActivity.this);
                builder.setTitle(getString(R.string.attention));
                builder.setMessage(getString(R.string.confirm_delete_link));

                builder.setPositiveButton(getString(android.R.string.ok), (dialog, id) -> {
                    delete_link();
                    dialog.dismiss();
                });

                builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.dismiss());

                builder.show();

            } else
                delete_link();

        }

        if(button_clicked.equals("Button copy clicked")){
            String copy = Objects.requireNonNull(linkEditText.getText()).toString();
            if(copy.isEmpty()){
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
            String copy = Objects.requireNonNull(linkEditText.getText()).toString();
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

        switch (button_clicked) {

            case("Default"):
                checkThemeLink = "Default";
                theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
                toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#FAFAFA"));
                break;

            case ("Red"):
                checkThemeLink = "Red";
                theme.setBackgroundColor(Color.parseColor("#FF8C8C"));
                toolbar.setBackgroundColor(Color.parseColor("#FF8C8C"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#FF8C8C"));
                break;

            case ("Orange"):
                checkThemeLink = "Orange";
                theme.setBackgroundColor(Color.parseColor("#FFB661"));
                toolbar.setBackgroundColor(Color.parseColor("#FFB661"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#FFB661"));
                break;

            case("Yellow"):
                checkThemeLink = "Yellow";
                theme.setBackgroundColor(Color.parseColor("#FFD850"));
                toolbar.setBackgroundColor(Color.parseColor("#FFD850"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#FFD850"));
                break;

            case("Green"):
                checkThemeLink = "Green";
                theme.setBackgroundColor(Color.parseColor("#7AE471"));
                toolbar.setBackgroundColor(Color.parseColor("#7AE471"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#7AE471"));
                break;

            case ("Light Green"):
                checkThemeLink = "Light Green";
                theme.setBackgroundColor(Color.parseColor("#56E0C7"));
                toolbar.setBackgroundColor(Color.parseColor("#56E0C7"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#56E0C7"));
                break;

            case("Light Blue"):
                checkThemeLink = "Light Blue";
                theme.setBackgroundColor(Color.parseColor("#6CD3FF"));
                toolbar.setBackgroundColor(Color.parseColor("#6CD3FF"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#6CD3FF"));
                break;

            case ("Blue"):
                checkThemeLink = "Blue";
                theme.setBackgroundColor(Color.parseColor("#819CFF"));
                toolbar.setBackgroundColor(Color.parseColor("#819CFF"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#819CFF"));
                break;

            case("violet"):
                checkThemeLink = "violet";
                theme.setBackgroundColor(Color.parseColor("#DD8BFA"));
                toolbar.setBackgroundColor(Color.parseColor("#DD8BFA"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#DD8BFA"));
                break;

            case("Pink"):
                checkThemeLink = "Pink";
                theme.setBackgroundColor(Color.parseColor("#FF6CA1"));
                toolbar.setBackgroundColor(Color.parseColor("#FF6CA1"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#FF6CA1"));
                break;

            case("Gray"):
                checkThemeLink = "Gray";
                theme.setBackgroundColor(Color.parseColor("#C4C4C4"));
                toolbar.setBackgroundColor(Color.parseColor("#C4C4C4"));
                card_more_bottom.setCardBackgroundColor(Color.parseColor("#C4C4C4"));
                break;
        }

    }
}