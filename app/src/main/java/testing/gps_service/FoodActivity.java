package testing.gps_service;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FoodActivity extends AppCompatActivity {

    Button btn_go_back;
    Button btn_openinghours;
    Button btn_menu_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_food);

        Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_light.otf");
        Typeface myCustomFontMedium= Typeface.createFromAsset(getAssets(),"fonts/futura_medium.otf");

        btn_go_back= (Button) findViewById(R.id.go_back);
        btn_go_back.setTypeface(myCustomFont);
        btn_openinghours = (Button) findViewById(R.id.opening_hours);
        btn_openinghours.setTypeface(myCustomFont);
        btn_menu_bar= (Button) findViewById(R.id.menu_bar);


        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_openinghours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodActivity.this, PopUpTimetable.class);
                startActivity(intent);

            }

        });

//        btn_menu_bar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FoodActivity.this, FoodMenuActivity.class);
//                startActivity(intent);
//
//            }
//
//        });
    }
}