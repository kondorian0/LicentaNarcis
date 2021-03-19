package com.narcis.neamtiu.licentanarcis.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;


public class MyDotSpan implements LineBackgroundSpan
{
    public static final float DEFAULT_RADIUS = 4;

    public boolean drawNoteDot = false;
    public boolean drawEventDot = false;
    public boolean drawImageDot = false;
    public boolean drawAudioDot = false;

    public MyDotSpan()
    {

    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum)
    {
        if(drawNoteDot)
        {
            int oldColor = paint.getColor();
            paint.setColor(Color.BLACK);
            canvas.drawCircle((float) ((left + right) / 1.35), bottom + DEFAULT_RADIUS, DEFAULT_RADIUS, paint);
            paint.setColor(oldColor);
        }
        else if(drawEventDot)
        {
            int oldColor = paint.getColor();
            paint.setColor(Color.GREEN);
            canvas.drawCircle((float) ((left + right) / 1.7), bottom + DEFAULT_RADIUS, DEFAULT_RADIUS, paint);
            paint.setColor(oldColor);
        }
        else if (drawAudioDot)
        {
            int oldColor = paint.getColor();
            paint.setColor(Color.BLUE);
            canvas.drawCircle((float) ((left + right) / 2.4), bottom + DEFAULT_RADIUS, DEFAULT_RADIUS, paint);
            paint.setColor(oldColor);
        }
        else if (drawImageDot)
        {
            int oldColor = paint.getColor();
            paint.setColor(Color.RED);
            canvas.drawCircle ((left + right) / 4, bottom + DEFAULT_RADIUS, DEFAULT_RADIUS, paint);
            paint.setColor(oldColor);
        }
    }
}
