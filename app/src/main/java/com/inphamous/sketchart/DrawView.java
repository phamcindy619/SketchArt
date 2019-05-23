package com.inphamous.sketchart;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

public class DrawView extends View {
    private Path drawPath;      // Drawing path
    private Paint drawPaint;    // Drawings and Canvas paint
    private Paint canvasPaint;
    private int paintColor = 0x000000;   // Initial color
    private int backgroundColor = 0xFFFFFF; // Background canvas color
    private Canvas drawCanvas;           // Canvas
    private Bitmap canvasBitmap;         // Canvas Bitmap
    private boolean erase = false;       // Flag for erasing
    private float brushSize;             // Current brush size
    private float lastBrushSize;         // Last brush size used

    // Default constructor
    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setUpDraw();
    }

    private void setUpDraw() {
        // Set up brush sizes
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;

        // Set up the drawing area
        drawPath = new Path();
        drawPaint = new Paint();

        // Set the initial color
        drawPaint.setColor(paintColor);

        // Set initial path
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        // Instantiate canvas Paint
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        drawCanvas.drawColor(backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Get position of user's touch
        float touchX = event.getX();
        float touchY = event.getY();

        // Draw the path
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    // Change brush color
    public void setColor(String newColor) {
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    // Switch from brush to eraser
    public void setErase(boolean isErase) {
        erase = isErase;
        if (erase) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
    }

    // Change brush size
    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize) {
        lastBrushSize = lastSize;
    }

    public float getLastBrushSize() {
        return lastBrushSize;
    }
}
