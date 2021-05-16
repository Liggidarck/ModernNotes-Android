package com.george.modern_notes.passwords;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.george.modern_notes.R;
import com.george.modern_notes.common.StarterActivity;
import com.george.modern_notes.database.PasswordsDatabase;

import java.util.Objects;

public class AddPasswordActivity extends AppCompatActivity {

    TextInputEditText web_address_edit_text, password_edit_text, name_edit_text;
    TextInputLayout web_address_input_layout, password_text_layout, username_input_layout;
    FloatingActionButton save_fab;
    MaterialToolbar toolbar;
    View theme;

    String check_theme_password = "Default";

    PasswordsDatabase sql_helper;
    SQLiteDatabase db;
    Cursor user_cursor;
    long password_id = 0;

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
        setContentView(R.layout.activity_add_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        theme = findViewById(R.id.theme_pass);
        web_address_edit_text = findViewById(R.id.web_address);
        name_edit_text = findViewById(R.id.username);
        password_edit_text =findViewById(R.id.password);
        web_address_input_layout = findViewById(R.id.web_address_layout);
        password_text_layout = findViewById(R.id.password_layout);
        username_input_layout = findViewById(R.id.username_layout);
        save_fab = findViewById(R.id.add_password);
        toolbar = findViewById(R.id.toolbar_add_password);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> goHomePassword());

        save_fab.setOnClickListener(view -> savePassword());
        web_address_input_layout.setEndIconOnClickListener(v -> {
            if(validateWeb()) {
                String url = Objects.requireNonNull(web_address_input_layout.getEditText()).getText().toString();

                if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://"))
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                else {
                    String urlCheck = "http://" + url;
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlCheck)));
                }

            }

        });
        username_input_layout.setEndIconOnClickListener(v -> {
            if(validateUsername()) {
                username_input_layout.setError(null);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", Objects.requireNonNull(username_input_layout.getEditText()).getText().toString());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Snackbar.make(v, getString(R.string.username_copied), Snackbar.LENGTH_LONG).setAction("done", null).show();
            }
        });

        String name_user = preferences.getString(getString(R.string.root_full_name), "empty_user_name");
        assert name_user != null;
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(AddPasswordActivity.this, StarterActivity.class));

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            password_id = extras.getLong("id");

        sql_helper = new PasswordsDatabase(this);
        db = sql_helper.getWritableDatabase();

        if (password_id > 0) {
            // получаем элемент по id из бд
            user_cursor = db.rawQuery("select * from " + PasswordsDatabase.TABLE + " where " +
                    PasswordsDatabase.ID + "=?", new String[] {String.valueOf(password_id)} );
            user_cursor.moveToFirst();

            name_edit_text.setText(user_cursor.getString(1));
            password_edit_text.setText(user_cursor.getString(2));
            web_address_edit_text.setText(user_cursor.getString(3));
            check_theme_password = user_cursor.getString(4);

            user_cursor.close();
        }
        clearErrors();
    }

    void clearErrors() {
        Objects.requireNonNull(web_address_input_layout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                web_address_input_layout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Objects.requireNonNull(password_text_layout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password_text_layout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Objects.requireNonNull(username_input_layout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username_input_layout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void savePassword() {
        if(validatePassword() | validateWeb()) {
            ContentValues cv = new ContentValues();
            cv.put(PasswordsDatabase.WEB, Objects.requireNonNull(name_edit_text.getText()).toString());
            cv.put(PasswordsDatabase.PASSWORD, Objects.requireNonNull(password_edit_text.getText()).toString());
            cv.put(PasswordsDatabase.NAME_ACCOUNT, Objects.requireNonNull(web_address_edit_text.getText()).toString());
            cv.put(PasswordsDatabase.THEME_MODE_PASSWORDS, check_theme_password);

            if (password_id > 0)
                db.update(PasswordsDatabase.TABLE, cv, PasswordsDatabase.ID + "=" + password_id, null);
            else
                db.insert(PasswordsDatabase.TABLE, null, cv);
            goHomePassword();
        }
    }

    public void deletePassword(){
        db.delete(PasswordsDatabase.TABLE, "_id = ?", new String[]{String.valueOf(password_id)});
        goHomePassword();
    }

    private void goHomePassword(){
        db.close();
        Intent intent = new Intent(this, PasswordsListsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public boolean validateWeb() {
        String check = Objects.requireNonNull(web_address_input_layout.getEditText()).getText().toString().trim();

        if(check.isEmpty()){
            web_address_input_layout.setError(getString(R.string.error_empty_field));
            return false;
        } else {
            web_address_input_layout.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String check = Objects.requireNonNull(password_text_layout.getEditText()).getText().toString().trim();

        if(check.isEmpty()){
            password_text_layout.setError(getString(R.string.error_empty_field));
            return false;
        } else {
            password_text_layout.setError(null);
            return true;
        }

    }

    public boolean validateUsername() {
        String check = Objects.requireNonNull(username_input_layout.getEditText()).getText().toString().trim();

        if(check.isEmpty()){
            username_input_layout.setError(getString(R.string.error_empty_field));
            return false;
        } else {
            username_input_layout.setError(null);
            return true;
        }

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
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean confirmDelete = preferences.getBoolean(getString(R.string.root_delete_bool), true);

            if(confirmDelete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddPasswordActivity.this);
                builder.setTitle(getText(R.string.attention));
                builder.setMessage(getText(R.string.confirm_delete_password));

                builder.setPositiveButton(getString(android.R.string.ok), (dialog, id) -> {
                    deletePassword();
                    dialog.dismiss();
                });

                builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.dismiss());

                builder.show();
            } else
                deletePassword();
        }

        return super.onOptionsItemSelected(item);
    }
}