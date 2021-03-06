package com.inphamous.sketchart;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;

public class PlayActivity extends Activity implements OnClickListener {

    private DrawView drawView;      // Custom view
    // UI
    private ImageButton currPaint;
    private ImageButton drawButton;
    private ImageButton eraseButton;
    private ImageButton newButton;
    private ImageButton saveButton;
    private ImageButton smallButton;
    private ImageButton mediumButton;
    private ImageButton largeButton;

    // Art
    private ImageButton prevButton;
    private ImageButton nextButton;
    private ImageView artImage;
    private Integer[] imageIds = {R.drawable.bike, R.drawable.helicopter, R.drawable.car, R.drawable.rocket, R.drawable.truck,
                    R.drawable.palace, R.drawable.school, R.drawable.nature, R.drawable.cake, R.drawable.cupcake, R.drawable.ice_cream,
                    R.drawable.hamburger, R.drawable.pizza,R.drawable.hot_dog, R.drawable.cherry, R.drawable.pineapple,
                    R.drawable.watermelon, R.drawable.dolphin, R.drawable.german_shepard, R.drawable.octopus, R.drawable.pig,
                    R.drawable.puffin_bird, R.drawable.turtle, R.drawable.crab};
    private int currIndex = 0;

    // Brush sizes
    private float smallBrush;
    private float mediumBrush;
    private float largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Set screen orientation
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        drawView = findViewById(R.id.draw);
        drawView.setBrushSize(mediumBrush);

        // Set up initial brush size and color
        LinearLayout paintLayout = findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        drawView.setColor(currPaint.getTag().toString());
        drawView.setBrushSize(drawView.getLastBrushSize());
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallButton = findViewById(R.id.small_brush);
        smallButton.setOnClickListener(this);
        mediumButton = findViewById(R.id.medium_brush);
        mediumButton.setImageDrawable(getResources().getDrawable(R.drawable.medium_pressed));
        mediumButton.setOnClickListener(this);
        largeButton = findViewById(R.id.large_brush);
        largeButton.setOnClickListener(this);

        // Initialize brush sizes
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawButton = findViewById(R.id.draw_button);
        drawButton.setOnClickListener(this);

        eraseButton = findViewById(R.id.erase_button);
        eraseButton.setOnClickListener(this);

        newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);

        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        artImage = findViewById(R.id.artImage);

        // Display the first image upon opening
        artImage.setImageResource(imageIds[currIndex]);
    }

    public void paintClicked(View view) {
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        // Update color
        if (view != currPaint) {
            ImageButton imageView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton) view;
        }
    }

    @Override
    public void onClick(View view) {
        // Draw selected
        if (view.getId() == R.id.draw_button) {
            drawView.setErase(false);
        }
        // Erase selected
        else if (view.getId() == R.id.erase_button) {
            drawView.setErase(true);
        }
        // New selected
        else if (view.getId() == R.id.new_button) {
            // Confirm that user wants to start new drawing
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing? (You will lose the current drawing.)");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        // Save selected
        else if (view.getId() == R.id.save_button) {
            // Confirm that user wants to save their drawing
            final AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveBitmap();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
        // Get previous image
        else if (view.getId() == R.id.prev_button) {
            // Decrement index
            if (currIndex == 0)
                currIndex = imageIds.length - 1;
            else
                currIndex--;
            // Change image
            artImage.setImageResource(imageIds[currIndex]);
        }
        // Get next image
        else if (view.getId() == R.id.next_button) {
            // Increment index
            if (currIndex == imageIds.length - 1)
                currIndex = 0;
            else
                currIndex++;
            // Change image
            artImage.setImageResource(imageIds[currIndex]);
        }
        // Set small brush size
        else if (view.getId() == R.id.small_brush) {
            drawView.setBrushSize(smallBrush);
            drawView.setLastBrushSize(smallBrush);
            Log.d("hello", "Small pressed");
            smallButton.setImageDrawable(getResources().getDrawable(R.drawable.small_pressed));
            mediumButton.setImageDrawable(getResources().getDrawable(R.drawable.medium));
            largeButton.setImageDrawable(getResources().getDrawable(R.drawable.large));
        }
        // Set medium brush size
        else if (view.getId() == R.id.medium_brush) {
            drawView.setBrushSize(mediumBrush);
            drawView.setLastBrushSize(mediumBrush);
            Log.d("hello", "Med pressed");
            mediumButton.setImageDrawable(getResources().getDrawable(R.drawable.medium_pressed));
            smallButton.setImageDrawable(getResources().getDrawable(R.drawable.small));
            largeButton.setImageDrawable(getResources().getDrawable(R.drawable.large));
        }
        // Set large brush size
        else if (view.getId() == R.id.large_brush) {
            drawView.setBrushSize(largeBrush);
            drawView.setLastBrushSize(largeBrush);
            Log.d("hello", "Large pressed");
            largeButton.setImageDrawable(getResources().getDrawable(R.drawable.large_pressed));
            smallButton.setImageDrawable(getResources().getDrawable(R.drawable.small));
            mediumButton.setImageDrawable(getResources().getDrawable(R.drawable.medium));
        }
    }

    // Save drawing to Gallery
    private void saveBitmap() {
        // Get permission to access gallery
        ActivityCompat.requestPermissions(PlayActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        // Get the drawing
        Bitmap bitmap = Bitmap.createBitmap(drawView.getWidth(), drawView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        drawView.draw(canvas);
        // Save to gallery
        String imageSaved = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,UUID.randomUUID().toString() + ".jpg", "sketchart");
        // Image has been saved successfully
        if (imageSaved != null) {
            Toast savedToast = Toast.makeText(getApplicationContext(), "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
            savedToast.show();
        }
        // Image was not saved
        else {
            Toast unsavedToast = Toast.makeText(getApplicationContext(), "Oops! Drawing could not be saved.", Toast.LENGTH_SHORT);
            unsavedToast.show();
        }
    }
}
