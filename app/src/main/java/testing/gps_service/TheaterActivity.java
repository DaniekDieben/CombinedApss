package testing.gps_service;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TheaterActivity extends AppCompatActivity {

    Button btn_go_back;
    Button btn_openinghours;
    Button btn_theater_schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_theater);

        Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_light.otf");
        Typeface myCustomFontMedium= Typeface.createFromAsset(getAssets(),"fonts/futura_medium.otf");

        btn_go_back= (Button) findViewById(R.id.go_back);
        btn_go_back.setTypeface(myCustomFont);
        btn_openinghours = (Button) findViewById(R.id.opening_hours);
        btn_openinghours.setTypeface(myCustomFont);
        btn_theater_schedule = (Button)findViewById(R.id.theater_schedule);


        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TheaterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_openinghours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TheaterActivity.this, PopUpTimetable.class);
                startActivity(intent);

            }

        });

        btn_theater_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TheaterActivity.this, TheaterScheduleActivity.class);
                startActivity(intent);

            }

        });
    }
}
