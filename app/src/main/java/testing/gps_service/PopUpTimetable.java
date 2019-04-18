package testing.gps_service;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PopUpTimetable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_timetable);

        Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_light.otf");



        TextView openingsHours= (TextView) findViewById(R.id.opening_hours_text);
        openingsHours.setTypeface(myCustomFont);
        TextView timesText= (TextView) findViewById(R.id.time_text);
        timesText.setTypeface(myCustomFont);
        TextView daysText= (TextView) findViewById(R.id.days_text);
        daysText.setTypeface(myCustomFont);



    }
}
