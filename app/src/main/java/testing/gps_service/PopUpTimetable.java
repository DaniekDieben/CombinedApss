package testing.gps_service;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PopUpTimetable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_timetable);

        Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_light.otf");
        Typeface myCustomFontMedium= Typeface.createFromAsset(getAssets(),"fonts/futura_medium.otf");


        TextView openingsHours= (TextView) findViewById(R.id.opening_hours_text);
        openingsHours.setTypeface(myCustomFont);
        TextView timesText= (TextView) findViewById(R.id.time_text);
        timesText.setTypeface(myCustomFont);
        TextView daysText= (TextView) findViewById(R.id.days_text);
        daysText.setTypeface(myCustomFont);



    }
}
