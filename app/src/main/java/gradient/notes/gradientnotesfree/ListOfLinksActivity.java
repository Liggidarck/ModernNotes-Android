package gradient.notes.gradientnotesfree;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListOfLinksActivity extends AppCompatActivity {

    FloatingActionButton fab_add_link;

    ListView userList;
    LinksDatabase databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

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
        setContentView(R.layout.activity_list_of_links);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adViewLinks);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        MaterialToolbar toolbar = findViewById(R.id.toolbar_list_of_links);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfLinksActivity.this, MainActivity.class));
            }
        });

        fab_add_link = findViewById(R.id.add_link);
        fab_add_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfLinksActivity.this, AddLinkActivity.class));
            }
        });

        if(theme_app.equals("Orange"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color_fab))); //#f59619

        if(theme_app.equals("Blue"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_color_fab)));

        if(theme_app.equals("Green"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_color_fab)));

        if(theme_app.equals("Red"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_color_fab)));

        if(theme_app.equals("Fiolet"))
            fab_add_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fiolet_color_fab)));


        userList = findViewById(R.id.list_of_links);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddLinkActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        View empty = findViewById(R.id.empty_link_layout);
        userList.setEmptyView(empty);

        databaseHelper = new LinksDatabase(getApplicationContext());


    }

    @Override
    public void onResume(){
        super.onResume();

        db = databaseHelper.getReadableDatabase();
        userCursor = db.rawQuery("select * from "+
                LinksDatabase.TABLE, null);

        String[] headers = new String[] {LinksDatabase.COLUMN_NAME_LINK,
                LinksDatabase.COLUMN_LNK};

        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, userCursor,
                headers, new int[] {android.R.id.text1, android.R.id.text2}, 0);
        userList.setAdapter(userAdapter);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }


}