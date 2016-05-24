package com.industries.sarker.instagram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserFeed extends AppCompatActivity {

    private ArrayList<Bitmap> images;
    private ArrayAdapter arrayAdapter;
    private LinearLayout userFeed;
//    private ListView userFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        userFeed = (LinearLayout) findViewById(R.id.userFeed);
//        userFeed = (ListView) findViewById(R.id.userFeed);

        images = new ArrayList<>();

        String activeUser = getIntent().getStringExtra("USER");

        Log.v("**", activeUser);

        setTitle(activeUser + "'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Images");

        query.whereEqualTo("username", activeUser);
        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        for (ParseObject object : objects) {
                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        ImageView imageView = new ImageView(UserFeed.this);

                                        imageView.setImageBitmap(image);

                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));

                                        userFeed.addView(imageView);
                                    }
                                }
                            });

                        }
                    }
                }
            }
        });

        arrayAdapter = new ArrayAdapter(UserFeed.this, R.layout.activity_user_feed, images);

    }
}
