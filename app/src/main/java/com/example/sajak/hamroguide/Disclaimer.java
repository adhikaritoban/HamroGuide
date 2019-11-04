package com.example.sajak.hamroguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.example.sajak.hamroguide.news.NewsActivity;

public class Disclaimer extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        toolbar = findViewById(R.id.optiontoolbar);
        setActionBar(toolbar);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Disclaimer.this, NewsActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Disclaimer.this, NewsActivity.class));
        finish();
    }
}
