package gradient.notes.gradientnotesfree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class NotebookActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    FloatingActionButton add_note;

    NotesDatabase databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    ListView notesList;

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
        setContentView(R.layout.activity_notebook);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            String chek_empty = Objects.requireNonNull(arguments.get("empty")).toString();

            if(chek_empty.equals("Empty boxes")) {
                CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);
                Snackbar.make(coordinatorLayout, getString(R.string.empty_note_dont_save), Snackbar.LENGTH_SHORT)
                        .setAction("error", null).show();
            }
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adViewNotebook);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar = findViewById(R.id.toolbar_notebook);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotebookActivity.this, MainActivity.class));
            }
        });

        add_note = findViewById(R.id.add_note);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotebookActivity.this, AddNoteActivity.class));
            }
        });

        if(theme_app.equals("Orange"))
            add_note.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab))); //#f59619

        if(theme_app.equals("Blue"))
            add_note.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));

        if(theme_app.equals("Green"))
            add_note.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));

        if(theme_app.equals("Red"))
            add_note.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));

        if(theme_app.equals("Fiolet"))
            add_note.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));


        notesList = findViewById(R.id.notebook_list);

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        databaseHelper = new NotesDatabase(getApplicationContext());

        View empty = findViewById(R.id.empty_layout);
        notesList.setEmptyView(empty);
    }

    @Override
    public void onResume(){
        super.onResume();

        db = databaseHelper.getReadableDatabase();
        userCursor = db.rawQuery("select * from "+
                NotesDatabase.TABLE, null);

        String[] headers = new String[] {NotesDatabase.COLUMN_NAME_NOTE,
                NotesDatabase.COLUMN_NOTE};

        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, userCursor, headers, new int[] {android.R.id.text1, android.R.id.text2}, 0);
        notesList.setAdapter(userAdapter);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NotebookActivity.this, MainActivity.class));
    }
}