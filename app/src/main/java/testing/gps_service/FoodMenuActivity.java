package testing.gps_service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FoodMenuActivity extends AppCompatActivity {


    Button btn_go_back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        btn_go_back1 = (Button) findViewById(R.id.go_back_1);

        btn_go_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodMenuActivity.this, FoodActivity.class);
                startActivity(intent);
            }
        });

    }
}
