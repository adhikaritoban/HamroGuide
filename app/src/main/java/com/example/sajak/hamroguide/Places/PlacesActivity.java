package com.example.sajak.hamroguide.Places;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import com.example.sajak.hamroguide.R;
import com.example.sajak.hamroguide.news.NewsActivity;

import java.util.ArrayList;

public class PlacesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    Toolbar toolbar;
    RecyclerView recyclerView;
    PlacesAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PlacesGetSet> arrayList = new ArrayList<>();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        BackgroundPlaces backgroundPlaces = new BackgroundPlaces(this);
        backgroundPlaces.execute();

        toolbar = findViewById(R.id.apptoolbar);
        setActionBar(toolbar);

//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recyclerViewPlaces);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        PlacesDBHelper placesDBHelper= new PlacesDBHelper(this);
        SQLiteDatabase sqLiteDatabase = placesDBHelper.getReadableDatabase();
        Cursor cursor = placesDBHelper.get_places_data(sqLiteDatabase);

        while (cursor.moveToNext()){
            PlacesGetSet placesGetSet= new PlacesGetSet(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            arrayList.add(placesGetSet);

            Log.e("name", cursor.getString(1));
        }

        adapter = new PlacesAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.searchView);

        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlacesActivity.this, NewsActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        s = s.toLowerCase();
        ArrayList<PlacesGetSet> newList = new ArrayList<>();
        for (PlacesGetSet placesGetSet: arrayList){
            String name = placesGetSet.getPlace().toLowerCase();
            if (name.contains(s))
                newList.add(placesGetSet);
        }
        adapter.setFilter(newList);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PlacesActivity.this, NewsActivity.class));
        finish();
    }
}
