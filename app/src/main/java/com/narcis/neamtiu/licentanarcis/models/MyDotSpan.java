package com.narcis.neamtiu.licentanarcis.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class MyDotSpan implements LineBackgroundSpan {
    private final float DEFAULT_RADIUS = 5;
    private final float DEFAULT_STROKE = 6;
    private final float distance = 15;

    private final int blue = Color.rgb(66,164,245);
    private final int purple = Color.rgb(219, 44, 205);
    private final int green = Color.rgb(44, 219, 56);
    private final int yellow = Color.rgb(219, 181, 44);

    public boolean drawNoteDot = false;
    public boolean drawEventDot = false;
    public boolean drawImageDot = false;
    public boolean drawAudioDot = false;

    public MyDotSpan() { }

    private void drawWhiteBorderToDot(Canvas canvas, Paint paint, int bottom, float cx) {
        int color = paint.getColor();
        paint.setColor(Color.BLACK);
        canvas.drawCircle(cx, bottom + distance, DEFAULT_STROKE, paint);
        paint.setColor(color);
    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum) {
        if(drawNoteDot) {
            drawWhiteBorderToDot(canvas, paint, bottom, 69);
            int color = paint.getColor();
            paint.setColor(blue);
            canvas.drawCircle(69, bottom + distance, DEFAULT_RADIUS, paint);
            paint.setColor(color);
        } else if(drawEventDot) {
            drawWhiteBorderToDot(canvas, paint, bottom,85);
            int oldColor = paint.getColor();
            paint.setColor(purple);
            canvas.drawCircle(85, bottom + distance, DEFAULT_RADIUS, paint);
            paint.setColor(oldColor);
        } else if (drawAudioDot) {
            drawWhiteBorderToDot(canvas, paint, bottom,53);
            int oldColor = paint.getColor();
            paint.setColor(yellow);
            canvas.drawCircle(53, bottom + distance, DEFAULT_RADIUS, paint);
            paint.setColor(oldColor);
        } else if (drawImageDot) {
            drawWhiteBorderToDot(canvas, paint, bottom,101);
            int oldColor = paint.getColor();
            paint.setColor(green);
            canvas.drawCircle (101, bottom + distance, DEFAULT_RADIUS, paint);
            paint.setColor(oldColor);
        }
    }
}
