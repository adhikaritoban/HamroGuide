package com.example.sajak.hamroguide.news;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sajak.hamroguide.AboutUS;
import com.example.sajak.hamroguide.Agencies.AgenciesActivity;
import com.example.sajak.hamroguide.Disclaimer;
import com.example.sajak.hamroguide.Emergency.EmergencyActivity;
import com.example.sajak.hamroguide.LoginActivity;
import com.example.sajak.hamroguide.MainActivity;
import com.example.sajak.hamroguide.Places.PlacesActivity;
import com.example.sajak.hamroguide.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<NewsGetSet> arrayList = new ArrayList<>();
    NavigationView navigationView;
    DrawerLayout drawer;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        toolbar = findViewById(R.id.newsToolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BackgroundNews backgroundNews = new BackgroundNews(NewsActivity.this);
        backgroundNews.execute();

        recyclerView = findViewById(R.id.recyclerViewNews);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        navigationView = findViewById(R.id.navigationViewNews);

        CircleImageView userprofile = navigationView.getHeaderView(0).findViewById(R.id.facebookProfile);
        TextView username = navigationView.getHeaderView(0).findViewById(R.id.username);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String firstName = sharedPreferences.getString("firstName","");
        String lastName = sharedPreferences.getString("lastName","");
        String profileUrl = sharedPreferences.getString("profileImageUrl","");

        username.setText(firstName + " " + lastName);
        Glide.with(this).load(profileUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.hamroguidelogo)
                .error(R.mipmap.hamroguidelogo)
                .into(userprofile);

        navigationView.setItemIconTintList(null);
        navigationView.getMenu().findItem(R.id.travel_news_id).setChecked(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.travel_news_id:
                        drawer.closeDrawers();
                        break;
                    case R.id.emergency_id:
                        Intent Eintent=new Intent(NewsActivity.this, EmergencyActivity.class);
                        startActivity(Eintent);
                        drawer.closeDrawers();
                        finish();
                        break;

                    case R.id.agency_id:
                        Intent Aintent=new Intent(NewsActivity.this,AgenciesActivity.class);
                        startActivity(Aintent);
                        drawer.closeDrawers();
                        finish();
                        break;

                    case R.id.hamro_map_id:
                        startActivity(new Intent(NewsActivity.this, MainActivity.class));
                        drawer.closeDrawers();
                        finish();
                        break;

                    case R.id.place_id:
                        startActivity(new Intent(NewsActivity.this, PlacesActivity.class));
                        drawer.closeDrawers();
                        finish();
                        break;


                    case R.id.facebook_id:
                        Intent facebookBrowser= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sajak.khadka.5"));
                        startActivity(facebookBrowser);
                        drawer.closeDrawers();
                        break;

                    case R.id.gmail_id:
                        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                        /* Fill it with Data */
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"sity1c117014@islingtoncollege.edu.np"});
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

                        /* Send it off to the Activity-Chooser */
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                        break;

                    case R.id.share_id:
                        Intent shareIntent=new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        String shareBody = "https://www.facebook.com/sajak.khadka.5";
                        shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                        startActivity(Intent.createChooser(shareIntent,"Share Using"));
                        break;

                    case R.id.feedback_id:
                        final Intent feedbackIntent = new Intent(android.content.Intent.ACTION_SEND);

                        /* Fill it with Data */
                        feedbackIntent.setType("plain/text");
                        feedbackIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"sity1c117014@islingtoncollege.edu.np"});
                        feedbackIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                        feedbackIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

                        /* Send it off to the Activity-Chooser */
                        startActivity(Intent.createChooser(feedbackIntent, "Send mail..."));
                        break;

                    case R.id.logout_id:
                        logout();
                }

                return true;
            }
        });

        NewsDBHelper newsDBHelper = new NewsDBHelper(this);

        SQLiteDatabase sqLiteDatabase = newsDBHelper.getReadableDatabase();
        Cursor cursor = newsDBHelper.get_news_data(sqLiteDatabase);

        while (cursor.moveToNext()){
            NewsGetSet agenciesGetSet= new NewsGetSet(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
            arrayList.add(agenciesGetSet);
        }

        adapter = new NewsAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent aboutIntent = new Intent(NewsActivity.this, AboutUS.class);
                finish();
                startActivity(aboutIntent);
                break;

            case R.id.disclaimer:
                Intent disclaimerIntent = new Intent(NewsActivity.this, Disclaimer.class);
                startActivity(disclaimerIntent);
                finish();
                break;
        }
        return true;
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater=getMenuInflater();
//        inflater.inflate(R.menu.optionmenu,menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {

//        return true;
//    }

    private void logout() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (connectivityManager != null) {
            activeNetwork = connectivityManager.getActiveNetworkInfo();
        }
        if(activeNetwork !=null && activeNetwork.isConnected()) {
            //we are connected to a network
            new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK)
                    .setTitle("Logout")
                    .setMessage("Would you like to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (AccessToken.getCurrentAccessToken() == null) {
                                // already logged out
                            }
                            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
                                @Override
                                public void onCompleted(GraphResponse graphResponse) {
                                    LoginManager.getInstance().logOut();
                                    Intent intent = new Intent(NewsActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    NewsActivity.this.finish();
                                }
                            }).executeAsync();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // user doesn't want to quit
                        }
                    })
                    .show();
        }
        else {
            RelativeLayout relativeLayout_logout = findViewById(R.id.relativeLayout_home_logout);
            Toast.makeText(getApplicationContext(),"It seems that you are not connected to the internet. Make sure that your device is connected to internet", Toast.LENGTH_SHORT).show();
            final Snackbar snackbar = Snackbar.make(relativeLayout_logout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            retry(view);
                        }
                    });
            snackbar.setActionTextColor(Color.GREEN);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }
    private void retry(View view) {
        logout();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK)
                .setTitle("EXIT")
                .setMessage("Would you like to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // user doesn't want to quit
                    }
                })
                .show();
    }
}
