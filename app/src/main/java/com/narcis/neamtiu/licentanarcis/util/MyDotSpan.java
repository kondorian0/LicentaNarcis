package com.narcis.neamtiu.licentanarcis.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;


public class MyDotSpan implements LineBackgroundSpan {

    public static final float DEFAULT_RADIUS = 5;

    public boolean drawNoteDot = false;
    public boolean drawImageDot = false;
    public boolean drawTextDot = false;
    public boolean drawAudioDot = false;

    public MyDotSpan() {
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {
        /*

            draw depending on drawNoteDot
            draw depending on drawImageDot
            draw depending on drawTextDot
            draw depending on drawAudioDot
         */
        paint.setColor(Color.BLACK);
        canvas.drawCircle((left + right) / 2, bottom + DEFAULT_RADIUS, DEFAULT_RADIUS, paint);
    }
}
