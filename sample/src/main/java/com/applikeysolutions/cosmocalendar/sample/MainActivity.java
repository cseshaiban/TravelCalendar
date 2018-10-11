package com.applikeysolutions.cosmocalendar.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private Calendar mStartDate;
    private Calendar mEndDate;
    protected boolean hasOpenCalendar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.YEAR, 1);

        findViewById(R.id.single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(CalendarPickerActivity.getSinglePickIntent(MainActivity.this,
                        Calendar.getInstance(), maxDate, mStartDate,
                        CalendarPickerActivity.PRODUCT_TYPE_FLIGHT, hasOpenCalendar), 11);

            }
        });

        findViewById(R.id.range).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isReturn = false; // Opening from departure/return in search form
                startActivityForResult(CalendarPickerActivity.getRangePickIntent(MainActivity.this,
                        Calendar.getInstance(), maxDate, mStartDate, mEndDate, isReturn,
                        CalendarPickerActivity.PRODUCT_TYPE_FLIGHT, hasOpenCalendar), 11);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        if (requestCode == 11 && resultCode == RESULT_OK) {
            mStartDate = (Calendar) data.getSerializableExtra(CalendarPickerActivity.KEY_START_DATE);
            hasOpenCalendar = data.getBooleanExtra(CalendarPickerActivity.KEY_CALENDAR_OPEN, true);
            Toast.makeText(this, df.format(mStartDate.getTime()), Toast.LENGTH_SHORT).show();
        } else if (requestCode == 12 && resultCode == RESULT_OK) {
            mStartDate = (Calendar) data.getSerializableExtra(CalendarPickerActivity.KEY_START_DATE);
            mEndDate = (Calendar) data.getSerializableExtra(CalendarPickerActivity.KEY_END_DATE);
            hasOpenCalendar = data.getBooleanExtra(CalendarPickerActivity.KEY_CALENDAR_OPEN, true);
            Toast.makeText(this, df.format(mStartDate.getTime()) + " + " + df.format(mEndDate.getTime()), Toast.LENGTH_SHORT).show();
        }
    }

}
