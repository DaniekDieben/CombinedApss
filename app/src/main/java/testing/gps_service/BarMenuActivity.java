package testing.gps_service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BarMenuActivity extends AppCompatActivity {

    Button btn_go_back4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_menu);
        btn_go_back4 = (Button)findViewById(R.id.go_back_4) ;

        btn_go_back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarMenuActivity.this, BarActivity.class);
                startActivity(intent);
            }
        });
    }
}
