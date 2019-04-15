package testing.gps_service;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    Button btn_back;
    Button btn_openinghours;
//    Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_medium.otf");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn_back= (Button) findViewById(R.id.go_back);
//        btn_back.setTypeface(myCustomFont);
        btn_openinghours= (Button) findViewById(R.id.opening_hours);
//        btn_schedule.setTypeface(myCustomFont);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);


            }
        });
        btn_openinghours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, PopUpTimetable.class);
                startActivity(intent);

            }

        });
    }


}





