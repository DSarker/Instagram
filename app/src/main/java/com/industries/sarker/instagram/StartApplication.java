package com.industries.sarker.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class StartApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(String.valueOf(R.string.parse_app_id))
                .clientKey(null)
                .server(String.valueOf(R.string.parse_client_key))
                .build()
        );

//        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
