package testing.gps_service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Main4Activity extends AppCompatActivity {

    Button btn_go_back;
    Button btn_openinghours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        btn_go_back = (Button) findViewById(R.id.go_back);
        btn_openinghours = (Button) findViewById(R.id.opening_hours);
        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_openinghours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4Activity.this, PopUpTimetable.class);
                startActivity(intent);

            }

        });
    }
}
