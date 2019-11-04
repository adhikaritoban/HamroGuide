package com.example.sajak.hamroguide.Agencies;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.example.sajak.hamroguide.R;
import com.example.sajak.hamroguide.news.NewsActivity;

import java.util.ArrayList;

public class AgenciesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    Toolbar toolbar;
    RecyclerView recyclerView;
    AgenciesAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<AgenciesGetSet> arrayList = new ArrayList<>();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agencies);

        BackgroundAgencies backgroundAgencies = new BackgroundAgencies(AgenciesActivity.this);
        backgroundAgencies.execute();

        toolbar = findViewById(R.id.apptoolbar);
        setActionBar(toolbar);

//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recyclerViewAgencies);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        AgenciesDBHolder agenciesDBHolder= new AgenciesDBHolder(this);
        SQLiteDatabase sqLiteDatabase = agenciesDBHolder.getReadableDatabase();
        Cursor cursor = agenciesDBHolder.get_agencies_data(sqLiteDatabase);

        while (cursor.moveToNext()){
            AgenciesGetSet agenciesGetSet= new AgenciesGetSet(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            arrayList.add(agenciesGetSet);
        }

        adapter = new AgenciesAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.searchView);

        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AgenciesActivity.this, NewsActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s = s.toLowerCase();
        ArrayList<AgenciesGetSet> newList = new ArrayList<>();
        for (AgenciesGetSet agenciesGetSet: arrayList){
            String name = agenciesGetSet.getName().toLowerCase();
            if (name.contains(s))
                newList.add(agenciesGetSet);
        }
        adapter.setFilter(newList);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AgenciesActivity.this, NewsActivity.class));
        finish();
    }
}
