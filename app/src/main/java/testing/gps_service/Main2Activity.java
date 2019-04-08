package testing.gps_service;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    Button btn_back;
    Button btn_schedule;

//    Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_medium.otf");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btn_back= (Button) findViewById(R.id.go_back);
//        btn_back.setTypeface(myCustomFont);
        btn_schedule= (Button) findViewById(R.id.Artists_schedule);
//        btn_schedule.setTypeface(myCustomFont);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
//        btn_schedule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
