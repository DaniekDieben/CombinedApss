package testing.gps_service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StageScheduleActivity extends AppCompatActivity {

    Button btn_go_back2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_schedule);
        btn_go_back2 = (Button) findViewById(R.id.go_back_2);

        btn_go_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StageScheduleActivity.this, StageActivity.class);
                startActivity(intent);
            }
        });
    }
}
