package com.inphamous.sketchart;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public class PlayActivity extends Activity implements OnClickListener {
    private DrawView drawView;
    // UI
    private ImageButton currPaint;
    private ImageButton drawButton;
    private ImageButton eraseButton;
    // Brush sizes
    private float smallBrush;
    private float mediumBrush;
    private float largeBrush;

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
    }

    public void paintClicked(View view) {
        drawView.setErase(false);
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

            brushDialog.show();
            // Small brush size has been clicked
            ImageButton smallButton = brushDialog.findViewById(R.id.small_brush);
            smallButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
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
                    brushDialog.dismiss();
                }
            });

            drawView.setErase(false);
        }
        // Erase selected
        else if (view.getId() == R.id.erase_button) {
            drawView.setErase(true);
        }
    }
}
