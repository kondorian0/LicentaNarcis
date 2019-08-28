package com.narcis.neamtiu.licentanarcis.util;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class EventDecorator implements DayViewDecorator{

    private CalendarDay mDay;

    public boolean decorateNoteDot = false;
    public boolean decorateEventDot = false;
    public boolean decorateImageDot = false;
    public boolean decorateAudioDot = false;

    public EventDecorator(CalendarDay day) {
        this.mDay = day;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(mDay);
    }

    @Override
    public void decorate(DayViewFacade view) {

        MyDotSpan dotSpan = new MyDotSpan();

        dotSpan.drawNoteDot = this.decorateNoteDot;
        dotSpan.drawEventDot = this.decorateEventDot;
        dotSpan.drawImageDot = this.decorateImageDot;
        dotSpan.drawAudioDot = this.decorateAudioDot;

        view.addSpan(dotSpan);

    }
}