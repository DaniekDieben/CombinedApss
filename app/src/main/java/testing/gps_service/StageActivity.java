package testing.gps_service;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StageActivity extends AppCompatActivity {

    Button btn_go_back;
    Button btn_openinghours;
    Button btn_stage_schedule;
//    Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_medium.otf");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stage);


        Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_light.otf");
        Typeface myCustomFontMedium= Typeface.createFromAsset(getAssets(),"fonts/futura_medium.otf");

        btn_go_back= (Button) findViewById(R.id.go_back);
        btn_go_back.setTypeface(myCustomFont);
        btn_openinghours = (Button) findViewById(R.id.opening_hours);
        btn_openinghours.setTypeface(myCustomFont);
        btn_stage_schedule = (Button)findViewById(R.id.stage_schedule);

        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StageActivity.this, MainActivity.class);
                startActivity(intent);


            }
        });
        btn_openinghours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StageActivity.this, PopUpTimetable.class);
                startActivity(intent);

            }

        });

        btn_stage_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StageActivity.this, StageScheduleActivity.class);
                startActivity(intent);

            }

        });
    }


}





