package com.applikeysolutions.cosmocalendar.listeners;

import android.support.annotation.NonNull;

import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.view.CalendarView;

/**
 * The callback used to indicate a date has been selected or deselected
 */
public interface OnDateSelectedListener {

    /**
     * Called when a user clicks on a day.
     * There is no logic to prevent multiple calls for the same date and state.
     *
     * @param widget   the view associated with this listener
     * @param day     the date that was selected or unselected
     * @param selected true if the day is now selected, false otherwise
     */
    void onDateSelected(@NonNull CalendarView widget, @NonNull Day day, boolean selected);
}
