package com.gtappdevelopers.wallpaperapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getVideoList();
    }

    public void newIntent(View view) {
        Intent i =new Intent(MainActivity.this,HomeScreenActivity.class);
        startActivity(i);
    }

    private void getVideoList() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA,MediaStore.Video.VideoColumns.DURATION};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        ArrayList<String> pathArrList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                pathArrList.add(cursor.getString(0));
                Log.e("TAG","VIDEO LINK IS "+cursor.getString(0)+
                        "\n"+cursor.getString(1));

            }
            cursor.close();
        }
//        Log.e("all path",pathArrList.toString());
    }

}