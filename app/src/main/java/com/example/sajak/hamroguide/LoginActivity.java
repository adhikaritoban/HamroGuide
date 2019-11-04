package com.example.sajak.hamroguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajak.hamroguide.news.NewsActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class LoginActivity extends AppCompatActivity {

    private String firstName;
    private String lastName;
    private String profileImageUrl;

    CallbackManager callbackManager;
    LoginButton loginButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
    }

    public void loginClicked(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (connectivityManager != null) {
            activeNetwork = connectivityManager.getActiveNetworkInfo();
        }
        if (activeNetwork != null && activeNetwork.isConnected()) {
            loginButton = findViewById(R.id.loginButton);
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AccessToken accessToken = loginResult.getAccessToken();
                    accessTokenTracker = new AccessTokenTracker() {
                        @Override
                        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                        }
                    };
                    accessTokenTracker.startTracking();

                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                        }
                    };
                    profileTracker.startTracking();

                    Profile mProfile = Profile.getCurrentProfile();
                    if (mProfile!=null){
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        editor = sharedPreferences.edit();
                        firstName = mProfile.getFirstName();
                        lastName = mProfile.getLastName();
                        profileImageUrl = mProfile.getProfilePictureUri(150,150).toString();
                        editor.putString("firstName",firstName);
                        editor.putString("lastName",lastName);
                        editor.putString("profileImageUrl",profileImageUrl);
                        editor.apply();
                        RelativeLayout relativeLayout=findViewById(R.id.relativeLayout_login);
                        relativeLayout.setVisibility(View.INVISIBLE);
                        Intent success = new Intent(LoginActivity.this, NewsActivity.class);
                        startActivity(success);
                        finish();
                    }
                    else {
                        RelativeLayout relativeLayout=findViewById(R.id.relativeLayout_login);
                        relativeLayout.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(LoginActivity.this,NewsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(),"Login Cancelled",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getApplicationContext(),"Error Occurred in Login",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            RelativeLayout relativeLayout_Main = findViewById(R.id.relativeLayout_login);
            final Snackbar snackbar = Snackbar.make(relativeLayout_Main, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            retry(view);
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    private void retry(View view) {
        loginClicked(view);
    }
}
