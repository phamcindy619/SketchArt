package com.inphamous.sketchart;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public class PlayActivity extends Activity implements OnClickListener {
    private DrawView drawView;      // Custom view
    // UI
    private ImageButton currPaint;
    private ImageButton drawButton;
    private ImageButton eraseButton;
    private ImageButton newButton;
    private ImageButton saveButton;

    // Brush sizes
    private float smallBrush;
    private float mediumBrush;
    private float largeBrush;

    // Debug
    private static final String LOG_TAG = PlayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        drawView = findViewById(R.id.draw);
        drawView.setBrushSize(mediumBrush);

        LinearLayout paintLayout = findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

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
            // Create dialog for brush size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_selector);

            // Small brush size has been clicked
            ImageButton smallButton = brushDialog.findViewById(R.id.small_brush);
            smallButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            // Medium brush size has been clicked
            ImageButton mediumButton = brushDialog.findViewById(R.id.medium_brush);
            mediumButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            // Large brush size has been clicked
            ImageButton largeButton = brushDialog.findViewById(R.id.large_brush);
            largeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        // Erase selected
        else if (view.getId() == R.id.erase_button) {
            final Dialog eraserDialog = new Dialog(this);
            eraserDialog.setTitle("Eraser size:");
            eraserDialog.setContentView(R.layout.brush_selector);

            // Small eraser size has been clicked
            ImageButton smallButton = eraserDialog.findViewById(R.id.small_brush);
            smallButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    eraserDialog.dismiss();
                }
            });

            // Medium eraser size has been clicked
            ImageButton mediumButton = eraserDialog.findViewById(R.id.medium_brush);
            mediumButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    eraserDialog.dismiss();
                }
            });

            // Large eraser size has been clicked
            ImageButton largeButton = eraserDialog.findViewById(R.id.large_brush);
            largeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    eraserDialog.dismiss();
                }
            });
            eraserDialog.show();
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
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Convert view into image
                    Bitmap bitmap = Bitmap.createBitmap(drawView.getWidth(), drawView.getHeight(), Bitmap.Config.ARGB_8888);
                    drawView.draw(new Canvas(bitmap));

                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File file = new File(path, UUID.randomUUID().toString() + ".jpg");
                    Log.d(LOG_TAG, "The path is: " + path);

                    // Save the image in SD card
                    try {
                        path.mkdirs();
                        Log.d(LOG_TAG, "Pictures directory found.");
                        OutputStream os = new FileOutputStream(file);
                        Log.d(LOG_TAG, "Created output stream.");
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        Log.d(LOG_TAG, "Bitmap is saved.");
                        os.close();
                        Toast savedToast = Toast.makeText(getApplicationContext(), "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    catch (Exception e) {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(), "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
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
    }
}
