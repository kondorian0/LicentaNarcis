package com.narcis.neamtiu.licentanarcis.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.narcis.neamtiu.licentanarcis.models.Draw;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class PaintFileHelper extends View {

    //variables
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private int currentColor;
    private int backgroundColor = Constants.DEFAULT_BG_COLOR;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private final Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private final ArrayList<Draw> paths = new ArrayList<>();
    private final ArrayList<Draw> undo = new ArrayList<>();

    public PaintFileHelper(Context context) {
        super(context, null);
    }

    public PaintFileHelper(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Constants.DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void initialise(WindowMetrics displayMetrics) {
        int height = displayMetrics.getBounds().height();
        int width = displayMetrics.getBounds().width();

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = Constants.DEFAULT_COLOR;
        strokeWidth = Constants.BRUSH_SIZE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);

        for (Draw draw : paths) {
            mPaint.setColor(draw.color);
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setMaskFilter(null);

            mCanvas.drawPath(draw.path, mPaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();

        Draw draw = new Draw(currentColor, strokeWidth, mPath);
        paths.add(draw);

        mPath.reset();
        mPath.moveTo(x, y);

        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mX);

        if (dx >= Constants.TOUCH_TOLERANCE || dy >= Constants.TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void clear() {
        backgroundColor = Constants.DEFAULT_BG_COLOR;
        paths.clear();
        invalidate();
    }

    public void undo() {
        if (paths.size() > 0) {
            undo.add(paths.remove(paths.size() - 1));
            invalidate();
        } else {
            Toast.makeText(getContext(), "Nimic pentru a anula", Toast.LENGTH_LONG).show();
        }
    }

    public void redo() {
        if (undo.size() > 0) {
            paths.add(undo.remove(undo.size() - 1));
            invalidate();
        } else {
            Toast.makeText(getContext(), "Nimic pentru a reface", Toast.LENGTH_LONG).show();
        }
    }

    public void setColor(int color) {
        currentColor = color;
    }

    public void compressImage(FileOutputStream fileOutputStream) {
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
    }
}
