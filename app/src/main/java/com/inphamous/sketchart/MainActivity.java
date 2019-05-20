package com.inphamous.sketchart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchPlayActivity(View view) {
        Log.d(LOG_TAG, "Let's draw!");
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    public void launchGalleryActivity(View view) {
        Log.d(LOG_TAG, "Gallery");
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    public void launchSettingsActivity(View view) {
        Log.d(LOG_TAG, "Settings");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void launchCreditsActivity(View view) {
        Log.d(LOG_TAG, "Credits");
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }
}
