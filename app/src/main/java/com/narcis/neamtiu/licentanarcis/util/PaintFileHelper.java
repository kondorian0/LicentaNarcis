package com.narcis.neamtiu.licentanarcis.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.narcis.neamtiu.licentanarcis.firestore.FirestoreManager;
import com.narcis.neamtiu.licentanarcis.models.Draw;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class PaintFileHelper extends View {

    //constant values
    public static int BRUSH_SIZE = 10;
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;

    //variables
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private String filename = "image_"+ System.currentTimeMillis() + ".jpg";
    private String mimeType = "image/jpeg";
    private String directory = Environment.DIRECTORY_PICTURES + "/CalendarNarcis";
    private Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private FileOutputStream fileOutputStream = null;
    private Uri imageUri = null;
    private String imageURL = "";

    private FirestoreManager firestoreManager = FirestoreManager.getInstance();

    private ArrayList<Draw> paths = new ArrayList<>();
    private ArrayList<Draw> undo = new ArrayList<>();

    public PaintFileHelper(Context context) {
        super(context, null);
    }

    public PaintFileHelper(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
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

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
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

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
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
        backgroundColor = DEFAULT_BG_COLOR;
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void saveImage() {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
            values.put(MediaStore.Images.Media.RELATIVE_PATH, directory);

            ContentResolver resolver = getContext().getContentResolver();
            imageUri = resolver.insert(mediaContentUri, values);

            fileOutputStream = (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(imageUri));

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Image not saved" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        firestoreManager.uploadImageToCloudStorage(this, filename, imageUri);
    }
    public void imageUploadSucces(String imageURL){
        this.imageURL = imageURL;
    }

    public String getImageURL(){
        return imageURL;
    }
}
