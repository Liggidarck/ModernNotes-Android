package com.gradient.free;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AddLinkActivity extends AppCompatActivity {

    TextInputEditText nameLinkBox, linkBox;
    TextInputLayout name_link_layoutBOX, link_layputBOX;
    EditText link_noteBOX;

    MaterialToolbar toolbar;

    View theme;
    String checkThemeLink = "Default";

    LinksDatabase sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long linkId =0;
    private AdView mAdView;


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
        setContentView(R.layout.activity_add_link);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adViewAddLink);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar = findViewById(R.id.topAppBar_add_link);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome_link();
            }
        });

        nameLinkBox = findViewById(R.id.name_link_edit_text);
        linkBox = findViewById(R.id.link_edit_text);
        link_noteBOX = findViewById(R.id.note_link);

        name_link_layoutBOX = findViewById(R.id.link_name_text_layout);
        link_layputBOX = findViewById(R.id.link_edit_text_layout);

        String name_user = sharedPreferences.getString("full_name", "empty_user_name");

        assert name_user != null;
        if(name_user.equals("empty_user_name"))
            startActivity(new Intent(AddLinkActivity.this, StarterActivity.class));

        Objects.requireNonNull(link_layputBOX.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                link_layputBOX.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        link_layputBOX.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateLink()) {
                    return;
                } else {
                    String url = Objects.requireNonNull(link_layputBOX.getEditText()).getText().toString();

                    if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://") )
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    else {
                        String urlCheck = "http://" + url;
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlCheck)));
                    }

                }

            }
        });

        sqlHelper = new LinksDatabase(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            linkId = extras.getLong("id");
        }

        theme = findViewById(R.id.theme_link);

        FloatingActionButton delButton = findViewById(R.id.del_link_fab);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_link();
            }
        });

        if (linkId > 0) {
            userCursor = db.rawQuery("select * from " + LinksDatabase.TABLE + " where " +
                    LinksDatabase.COLUMN_ID + "=?", new String[]{String.valueOf(linkId)});
            userCursor.moveToFirst();

            nameLinkBox.setText(userCursor.getString(1));
            linkBox.setText(userCursor.getString(2));
            link_noteBOX.setText(userCursor.getString(3));

            checkThemeLink = userCursor.getString(4);

            if(checkThemeLink.equals("Default")){
                theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
                toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));
            }

            if(checkThemeLink.equals("Darcker")){
                theme.setBackgroundColor(Color.parseColor("#303030"));
                toolbar.setBackgroundColor(Color.parseColor("#303030"));
            }

            if(checkThemeLink.equals("Red")){
                theme.setBackgroundColor(Color.parseColor("#ff9e9e"));
                toolbar.setBackgroundColor(Color.parseColor("#ff9e9e"));
            }

            if(checkThemeLink.equals("Light Yellow")){
                theme.setBackgroundColor(Color.parseColor("#ffeaa7"));
                toolbar.setBackgroundColor(Color.parseColor("#ffeaa7"));
            }

            if(checkThemeLink.equals("Yellow")){
                theme.setBackgroundColor(Color.parseColor("#fdcb6f"));
                toolbar.setBackgroundColor(Color.parseColor("#fdcb6f"));
            }

            if(checkThemeLink.equals("Green")){
                theme.setBackgroundColor(Color.parseColor("#a3ff9e"));
                toolbar.setBackgroundColor(Color.parseColor("#a3ff9e"));
            }

            if(checkThemeLink.equals("Blue")){
                theme.setBackgroundColor(Color.parseColor("#9eb8ff"));
                toolbar.setBackgroundColor(Color.parseColor("#9eb8ff"));
            }

            if(checkThemeLink.equals("Aqua blue")){
                theme.setBackgroundColor(Color.parseColor("#74cdfc"));
                toolbar.setBackgroundColor(Color.parseColor("#74cdfc"));
            }

            if(checkThemeLink.equals("fiolet")){
                theme.setBackgroundColor(Color.parseColor("#e9a6ff"));
                toolbar.setBackgroundColor(Color.parseColor("#e9a6ff"));
            }

            if(checkThemeLink.equals("pink")){
                theme.setBackgroundColor(Color.parseColor("#ff9cbe"));
                toolbar.setBackgroundColor(Color.parseColor("#ff9cbe"));
            }



            userCursor.close();
        } else
            delButton.setVisibility(View.GONE);


        FloatingActionButton saveLink = findViewById(R.id.save_link_fab);
        saveLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_link();
            }
        });

        if(theme_app.equals("Orange")) {
            saveLink.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab))); //#f59619
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab)));
        }

        if(theme_app.equals("Blue")) {
            saveLink.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));
        }

        if(theme_app.equals("Green")) {
            saveLink.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));
        }

        if(theme_app.equals("Red")) {
            saveLink.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));
        }

        if(theme_app.equals("Fiolet")) {
            saveLink.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));
            delButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));
        }

    }

    public boolean validateLink() {
        String check = Objects.requireNonNull(link_layputBOX.getEditText()).getText().toString().trim();

        if(check.isEmpty()){
            link_layputBOX.setError(getString(R.string.error_empty_field));
            return false;
        } else {
            link_layputBOX.setError(null);
            return true;
        }

    }

    public void save_link() {
        if(!validateLink()) {
            return;
        } else if (Objects.requireNonNull(name_link_layoutBOX.getEditText()).getText().toString().equals("")) {

            name_link_layoutBOX.getEditText().setText(getString(R.string.noname_link));

            ContentValues cv = new ContentValues();
            cv.put(LinksDatabase.COLUMN_NAME_LINK, Objects.requireNonNull(nameLinkBox.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LNK, Objects.requireNonNull(linkBox.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LINK_NOTE, link_noteBOX.getText().toString());
            cv.put(LinksDatabase.COLUMN_THEME_MODE_LINK, checkThemeLink);

            if (linkId > 0) {
                db.update(LinksDatabase.TABLE, cv, LinksDatabase.COLUMN_ID + "=" + String.valueOf(linkId), null);
            } else {
                db.insert(LinksDatabase.TABLE, null, cv);
            }
            goHome_link();

        } else {

            ContentValues cv = new ContentValues();
            cv.put(LinksDatabase.COLUMN_NAME_LINK, Objects.requireNonNull(nameLinkBox.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LNK, Objects.requireNonNull(linkBox.getText()).toString());
            cv.put(LinksDatabase.COLUMN_LINK_NOTE, link_noteBOX.getText().toString());
            cv.put(LinksDatabase.COLUMN_THEME_MODE_LINK, checkThemeLink);

            if (linkId > 0) {
                db.update(LinksDatabase.TABLE, cv, LinksDatabase.COLUMN_ID + "=" + String.valueOf(linkId), null);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_theme, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.edit_theme)
            showDialogEditThemeLink();


        return super.onOptionsItemSelected(item);
    }

    public void showDialogEditThemeLink() {
        final Dialog dialog = new Dialog(AddLinkActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_theme);

        Button done = dialog.findViewById(R.id.button_ok);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView default_th, darker, reed, light_yello, yello, greeen, bluue, aqqua_blue, fiiolet, piink;
        ImageView check_default, check_darcker, check_red, check_yellow, chek_light_yellow, check_green, check_blue, check_aqua_blue, check_fiolet, chek_pink;

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

        theme = findViewById(R.id.theme_link);

        if(checkThemeLink.equals("Default"))
            check_default.setVisibility(View.VISIBLE);

        if(checkThemeLink.equals("Red"))
            check_red.setVisibility(View.VISIBLE);

        if(checkThemeLink.equals("Light Yellow"))
            chek_light_yellow.setVisibility(View.VISIBLE);

        if(checkThemeLink.equals("Yellow"))
            check_yellow.setVisibility(View.VISIBLE);

        if(checkThemeLink.equals("Green"))
            check_green.setVisibility(View.VISIBLE);

        if(checkThemeLink.equals("Blue"))
            check_blue.setVisibility(View.VISIBLE);

        if(checkThemeLink.equals("Aqua blue"))
            check_aqua_blue.setVisibility(View.VISIBLE);

        if(checkThemeLink.equals("fiolet"))
            check_fiolet.setVisibility(View.VISIBLE);

        if(checkThemeLink.equals("pink"))
            chek_pink.setVisibility(View.VISIBLE);


        default_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#FAFAFA"));
                toolbar.setBackgroundColor(Color.parseColor("#FAFAFA"));
                checkThemeLink = "Default";
                dialog.dismiss();
            }
        });

        reed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#ff9e9e"));
                toolbar.setBackgroundColor(Color.parseColor("#ff9e9e"));
                checkThemeLink = "Red";
                dialog.dismiss();
            }
        });

        light_yello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#ffeaa7"));
                toolbar.setBackgroundColor(Color.parseColor("#ffeaa7"));
                checkThemeLink = "Light Yellow";
                dialog.dismiss();
            }
        });

        yello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#fdcb6f"));
                toolbar.setBackgroundColor(Color.parseColor("#fdcb6f"));
                checkThemeLink = "Yellow";
                dialog.dismiss();
            }
        });

        greeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#a3ff9e"));
                toolbar.setBackgroundColor(Color.parseColor("#a3ff9e"));
                checkThemeLink = "Green";
                dialog.dismiss();
            }
        });

        bluue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#9eb8ff"));
                toolbar.setBackgroundColor(Color.parseColor("#9eb8ff"));
                checkThemeLink = "Blue";
                dialog.dismiss();
            }
        });

        aqqua_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#74cdfc"));
                toolbar.setBackgroundColor(Color.parseColor("#74cdfc"));
                checkThemeLink = "Aqua blue";
                dialog.dismiss();
            }
        });

        fiiolet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#e9a6ff"));
                toolbar.setBackgroundColor(Color.parseColor("#e9a6ff"));
                checkThemeLink = "fiolet";
                dialog.dismiss();
            }
        });

        piink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme.setBackgroundColor(Color.parseColor("#ff9cbe"));
                toolbar.setBackgroundColor(Color.parseColor("#ff9cbe"));
                checkThemeLink = "pink";
                dialog.dismiss();
            }
        });



        dialog.show();
    }

}