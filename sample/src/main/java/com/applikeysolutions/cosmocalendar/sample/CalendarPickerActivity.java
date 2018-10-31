package com.applikeysolutions.cosmocalendar.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.Toolbar;

import com.applikeysolutions.cosmocalendar.listeners.OnDateSelectedListener;
import com.applikeysolutions.cosmocalendar.listeners.OnRangeSelectedListener;
import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;

import java.util.Calendar;
import java.util.List;

public class CalendarPickerActivity extends AppCompatActivity implements OnDateSelectedListener, OnRangeSelectedListener {

    public static final int PRODUCT_TYPE_FLIGHT = 1;
    public static final int PRODUCT_TYPE_HOTEL = 2;
    public static final int PRODUCT_TYPE_TRAIN = 3;
    public static final int PRODUCT_TYPE_CAR = 4;
    public static final int PRODUCT_TYPE_EVENT = 5;
    public static final int MODE_VIEW_SINGLE = 15;
    public static final int TIME_DELAY_CLOSE = 500;
    public static final int TIME_DELAY_PRICE_SHOW = 500;

    public static final String KEY_CALENDAR_MODE = "calendar_mode";
    public static final String KEY_PRODUCT_TYPE = "product_type";
    public static final String KEY_CALENDAR_OPEN = "calendar_open";
    public static final String KEY_MIN_DATE = "min_date";
    public static final String KEY_MAX_DATE = "max_date";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_END_DATE = "end_date";
    public static final String KEY_RANGE_LIMIT = "range_limit";
    private static final String KEY_IS_RETURN = "is_return";
    private static final String KEY_ALLOWED_SAME_DAY = "allowedSameDay";
    private static final String KEY_FROM_RESCHEDULE = "from_reschedule";
    static final String KEY_SEARCH_FORM = "search_fom";
    private static final String FORMAT_MONTH = "MMM";
    public static String DATE_PATTERN_SHOW = "EEE, dd MMM yyyy";

    private CalendarView calendarView;
    private Toolbar toolbar;

    private Calendar mMinSelectedDate; // Calendar min date
    private Calendar mMaxSelectedDate; // Calendar max date

    private int mRangeLimit;
    private boolean mAllowedSameDay = true;
    private boolean hasOpenCalendar = false;

    private Calendar mStartDate; // user selected
    private Calendar mEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_calendar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Travel Date");
        initViews();
    }

    private void initViews() {
        calendarView = (CalendarView) findViewById(R.id.calendar_view);

        mMinSelectedDate = (Calendar) getIntent().getSerializableExtra(KEY_MIN_DATE);
        mMaxSelectedDate = (Calendar) getIntent().getSerializableExtra(KEY_MAX_DATE);
        calendarView.getSettingsManager().setMinSelectionDate(mMinSelectedDate);
        calendarView.getSettingsManager().setMaxSelectionDate(mMaxSelectedDate);

        calendarView.getSettingsManager().setCalendarOrientation(OrientationHelper.HORIZONTAL);

        calendarView.init(); // calendar begins



        String type = getIntent().getStringExtra(KEY_CALENDAR_MODE);
        switch (type) {
            case "range":
                calendarView.setSelectionType(SelectionType.RANGE);
                break;
            case "single":
            default:
                calendarView.setSelectionType(SelectionType.SINGLE);
        }

        mRangeLimit = getIntent().getIntExtra(KEY_RANGE_LIMIT, 0);
        mAllowedSameDay = getIntent().getBooleanExtra(KEY_ALLOWED_SAME_DAY, true);
        hasOpenCalendar = getIntent().getBooleanExtra(KEY_CALENDAR_OPEN, false);

        Calendar startDate = (Calendar) getIntent().getSerializableExtra(KEY_START_DATE);
        Calendar endDate = (Calendar) getIntent().getSerializableExtra(KEY_END_DATE);

        mStartDate = startDate;
        mEndDate = endDate;

        if(hasOpenCalendar && mEndDate != null) {
            //range
            //calendarView.selectRange(CalendarDay.from(mStartDate), CalendarDay.from(mEndDate));
        }

        if (hasOpenCalendar && startDate != null) {
            //single
            calendarView.setSelectedDate(startDate);
        }

        calendarView.setOnDateSelectedListener(this);
        calendarView.setOnRangeSelectedListener(this);
    }

    @Override
    public void onDateSelected(CalendarView widget, Day day, boolean selected) {
        doFinish(day.getCalendar(), null);
    }

    @Override
    public void onRangeSelected(CalendarView widget, List<Day> dates) {
        doFinish(dates.get(0).getCalendar(), dates.get(dates.size() - 1).getCalendar());
    }

    private void doFinish(final Calendar startDate, final Calendar endDate) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra(KEY_START_DATE, startDate);
                if (endDate != null)
                    intent.putExtra(KEY_END_DATE, endDate);
                intent.putExtra(KEY_CALENDAR_OPEN, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }, 500);
    }

    public static Intent getSinglePickIntent(Activity activity, Calendar minDate,
                                             Calendar maxDate, Calendar startDate, int productType, boolean hasOpenCalendar) {
        return getSinglePickIntent(activity, minDate, maxDate, startDate, productType, hasOpenCalendar, false);
    }


    public static Intent getSinglePickIntent(Activity activity, Calendar minDate,
                                             Calendar maxDate, Calendar startDate, int productType, boolean hasOpenCalendar, boolean isFromReschedule) {
        Intent intent = new Intent(activity, CalendarPickerActivity.class);
        intent.putExtra(KEY_CALENDAR_MODE, "single");
        intent.putExtra(KEY_MIN_DATE, minDate); // Calendar start
        intent.putExtra(KEY_MAX_DATE, maxDate); // Calendar end
        intent.putExtra(KEY_START_DATE, startDate);
        intent.putExtra(KEY_PRODUCT_TYPE, productType);
        intent.putExtra(KEY_CALENDAR_OPEN, hasOpenCalendar);
        intent.putExtra(KEY_FROM_RESCHEDULE, isFromReschedule);
        //intent.putExtra(KEY_SEARCH_FORM, searchForm);
        return intent;
    }

    public static Intent getRangePickIntent(Activity activity, Calendar minDate,
                                            Calendar maxDate, Calendar startDate, Calendar endDate,
                                            boolean isReturn, int productType, boolean hasOpenCalendar) {
        return getRangePickIntent(activity, minDate, maxDate, startDate, endDate, isReturn, productType, hasOpenCalendar, false);
    }

    public static Intent getRangePickIntent(Activity activity, Calendar minDate,
                                            Calendar maxDate, Calendar startDate, Calendar endDate,
                                            boolean isReturn, int productType, boolean hasOpenCalendar, boolean isFromReschedule) {
        Intent intent = new Intent(activity, CalendarPickerActivity.class);
        intent.putExtra(KEY_CALENDAR_MODE, "range");
        intent.putExtra(KEY_MIN_DATE, minDate);
        intent.putExtra(KEY_MAX_DATE, maxDate);
        intent.putExtra(KEY_START_DATE, startDate);
        intent.putExtra(KEY_END_DATE, endDate);
        intent.putExtra(KEY_IS_RETURN, isReturn);
        intent.putExtra(KEY_PRODUCT_TYPE, productType);
        intent.putExtra(KEY_CALENDAR_OPEN, hasOpenCalendar);
        intent.putExtra(KEY_FROM_RESCHEDULE, isFromReschedule);
        //intent.putExtra(KEY_SEARCH_FORM, searchForm);
        return intent;
    }
}

