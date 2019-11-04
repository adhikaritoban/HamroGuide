package com.example.sajak.hamroguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sajak.hamroguide.Agencies.BackgroundAgencies;
import com.example.sajak.hamroguide.Emergency.backgroundEmergency;
import com.example.sajak.hamroguide.Places.BackgroundPlaces;
import com.example.sajak.hamroguide.news.BackgroundNews;
import com.example.sajak.hamroguide.news.NewsActivity;
import com.facebook.AccessToken;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        BackgroundAgencies backgroundAgencies = new BackgroundAgencies(this);
        backgroundEmergency bacEmergency = new backgroundEmergency(this);
        BackgroundNews backgroundNews = new BackgroundNews(this);
        BackgroundPlaces backgroundPlaces = new BackgroundPlaces(this);


        backgroundAgencies.execute();
        bacEmergency.execute();
        backgroundNews.execute();
        backgroundPlaces.execute();

        final Intent loginintent=new Intent(this,LoginActivity.class);
        final Intent news=new Intent(this,NewsActivity.class);


        Thread timer=new Thread(){
            public void run(){
                try{
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    if (AccessToken.getCurrentAccessToken()!=null){
                        startActivity(news);
                        finish();
                    }
                    else {
                        startActivity(loginintent);
                        finish();
                    }
                }
            }
        };
        timer.start();
    }

}
