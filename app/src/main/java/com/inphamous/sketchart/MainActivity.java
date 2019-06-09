package com.inphamous.sketchart;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set screen orientation
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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
