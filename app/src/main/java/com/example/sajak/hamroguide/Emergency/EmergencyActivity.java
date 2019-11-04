package com.example.sajak.hamroguide.Emergency;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toolbar;

import com.example.sajak.hamroguide.R;
import com.example.sajak.hamroguide.news.NewsActivity;

import java.util.ArrayList;

public class EmergencyActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    Toolbar toolbar;
    RecyclerView recyclerView;
    EmergencyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<EmergencyGetSet> arrayList = new ArrayList<>();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        backgroundEmergency background = new backgroundEmergency(EmergencyActivity.this);
        background.execute();

        toolbar = findViewById(R.id.apptoolbar);
        setActionBar(toolbar);

        getActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recyclerViewEmergency);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        EmergencyDBHelper emergencyDBHelper = new EmergencyDBHelper(this);
        SQLiteDatabase sqLiteDatabase = emergencyDBHelper.getReadableDatabase();
        Cursor cursor = emergencyDBHelper.get_emergency_Data(sqLiteDatabase);

        while (cursor.moveToNext()){
            EmergencyGetSet emergencyGetSet = new EmergencyGetSet(cursor.getInt(0),
                    cursor.getString(1));
            arrayList.add(emergencyGetSet);
        }

        adapter = new EmergencyAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.searchView);

        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmergencyActivity.this, NewsActivity.class));
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
        ArrayList<EmergencyGetSet> newList = new ArrayList<>();
        for (EmergencyGetSet emergencyGetSet: arrayList){
            String name = emergencyGetSet.getName().toLowerCase();
            if (name.contains(s))
                newList.add(emergencyGetSet);
        }
        adapter.setFilter(newList);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EmergencyActivity.this, NewsActivity.class));
        finish();
    }
}
