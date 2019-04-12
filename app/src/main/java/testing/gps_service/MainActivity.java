package testing.gps_service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

//    private TableLayout table;
    private Button[][] btnTag = new Button [10][10];
    private int[][] matrix = new int [10][10] ;
    TextView text;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    TextView text6;
    ImageView imgView1;
    ImageView imgView2;
    ImageView imgView3;
    ImageView imgView4;

    private Button btn_start, btn_stop;
    private BroadcastReceiver broadcastReceiver;

    private FirebaseFirestore db;
    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    System.out.println("\n" +intent.getExtras().get("coordinates"));


                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button) findViewById(R.id.start);
        btn_stop = (Button) findViewById(R.id.stop);

//        Typeface myCustomFont=Typeface.createFromAsset(getAssets(), "fonts/Adv.ttf");
//        textView.setTypeface(myCustomFont);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        if(!runtime_permissions())
            enable_buttons();

        imgView1 = (ImageView) findViewById(R.id.imageView);
        imgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
        imgView2 = (ImageView) findViewById(R.id.imageView2);
        imgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(intent);
            }
        });
        imgView3 = (ImageView) findViewById(R.id.imageView3);
        imgView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main4Activity.class);
                startActivity(intent);
            }
        });
        imgView4 = (ImageView) findViewById(R.id.imageView4);
        imgView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main5Activity.class);
                startActivity(intent);
            }
        });

        populateButtons();

        text = (TextView) findViewById(R.id.textView);
        Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_light.otf");
        text.setTypeface(myCustomFont);
        text3 = (TextView) findViewById(R.id.textView3);
        text3.setTypeface(myCustomFont);
        text4 = (TextView) findViewById(R.id.textView4);
        text4.setTypeface(myCustomFont);
        text5 = (TextView) findViewById(R.id.textView5);
        text5.setTypeface(myCustomFont);
        text6 = (TextView) findViewById(R.id.textView6);
        text6.setTypeface(myCustomFont);
        text2 = (TextView) findViewById(R.id.textView2);
        myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_medium.otf");
        text2.setTypeface(myCustomFont);

        matrix[1][5]=4;
        Map square = GPS_Service.get_info;
        ArrayList data_array = GPS_Service.all_data;
        System.out.println("HELLO");

        System.out.println("List in main"+ data_array);
//
//        for (int i = data_array.size(); i>0; i--){
//            String squareVal = data_array.get(i);
//
//            data_array.remove(i);
//        }



//        int squareValue1 = GPS_Service.squareValue1;
//        int squareValue2 = GPS_Service.squareValue2;

        int squareValue2=0;
        int squareValue1=0;



        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == squareValue1 && j == squareValue2) {
                    matrix[i][j]++;
                }
            }
        }


        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++) {
                if (matrix[i][j] < 5) {
                    //change color button
                    btnTag[i][j].setBackgroundColor(Color.parseColor("#8AD2FF"));

                } else if (matrix[i][j] < 10) {
                    //change color button
                    btnTag[i][j].setBackgroundColor(Color.parseColor("#8ABEFF"));

                } else if (matrix[i][j] < 15) {
                    //change color button
                    btnTag[i][j].setBackgroundColor(Color.parseColor("#8AAAFF"));

                } else {
                    //change color button
                    btnTag[i][j].setBackgroundColor(Color.parseColor("#8A96FF"));


                }
            }
        }



    }


    private void enable_buttons() {

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),GPS_Service.class);
                startService(i);
                System.out.println("Start");
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),GPS_Service.class);
                stopService(i);
                System.out.println("Stop");

            }
        });

    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
                runtime_permissions();
            }
        }
    }
    private void populateButtons() {
        TableLayout table= (TableLayout) findViewById((R.id.TabelForButtons));

        int k = 0;
        for (int i=0; i<10; i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
            matrix[i] = new int[10];
            for (int j=0; j<10;j++){
                Button bt = new Button(this);
                bt.setLayoutParams(new TableRow.LayoutParams(50, 80));
                matrix[i][j] = 0;
                btnTag[i][j] = bt;
                tableRow.addView(btnTag[i][j]);

            }
            table.addView(tableRow);
        }

    }

}
