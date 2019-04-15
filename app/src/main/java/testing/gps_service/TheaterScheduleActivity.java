package testing.gps_service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TheaterScheduleActivity extends AppCompatActivity {

    Button btn_go_back3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_schedule);
        btn_go_back3 = (Button)findViewById(R.id.go_back_3) ;


        btn_go_back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TheaterScheduleActivity.this, TheaterActivity.class);
                startActivity(intent);
            }
        });
    }
}
