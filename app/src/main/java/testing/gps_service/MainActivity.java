package testing.gps_service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
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
//    private BroadcastReceiver broadcastReceiver;

    private FirebaseFirestore db;
    public ArrayList all_data;
    public Map get_info;
    public List list_with_values;
    private int x_square;
    private int y_square;

    final MainActivity service = this;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // layout main activity
        setContentView(R.layout.activity_main);

        //start or stop the service
        btn_start = (Button) findViewById(R.id.start);
        btn_stop = (Button) findViewById(R.id.stop);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        // checks DB info every 10 sec
        final Handler handler = new Handler();
        // 10 sec
        Runnable r = new Runnable() {
            @Override
            public void run() {
                service.readFromDB();
                handler.postDelayed(this, 5000);

            }
        };
        handler.postDelayed(r,5000);

        //if permission is already given or not needed (checked via runtime_permissions()), then enable buttons
        if(!runtime_permissions())
            enable_buttons();

        //Dynamic buttons populated
        populateButtons();

        //clickable images festival activities with intent to their own activity class
        imgView1 = (ImageView) findViewById(R.id.stage_image);
        imgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StageActivity.class);
                startActivity(intent);
            }
        });
        imgView2 = (ImageView) findViewById(R.id.food_image);
        imgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodActivity.class);
                startActivity(intent);
            }
        });
        imgView3 = (ImageView) findViewById(R.id.theater_image);
        imgView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TheaterActivity.class);
                startActivity(intent);
            }
        });
        imgView4 = (ImageView) findViewById(R.id.bar_image);
        imgView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarActivity.class);
                startActivity(intent);
            }
        });


        Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/futura_light.otf");
        Typeface myCustomFontMedium= Typeface.createFromAsset(getAssets(),"fonts/futura_medium.otf");

        text = (TextView) findViewById(R.id.textView);
        text.setTypeface(myCustomFont);
        text2 = (TextView) findViewById(R.id.textView2);
        text2.setTypeface(myCustomFontMedium);
        text3 = (TextView) findViewById(R.id.textView3);
        text3.setTypeface(myCustomFont);
        text4 = (TextView) findViewById(R.id.textView4);
        text4.setTypeface(myCustomFont);
        text5 = (TextView) findViewById(R.id.textView5);
        text5.setTypeface(myCustomFont);
        text6 = (TextView) findViewById(R.id.textView6);
        text6.setTypeface(myCustomFont);

    }

    private void enable_buttons() {

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new intent pointing to gps service, starting service
                Intent i =new Intent(getApplicationContext(),GPS_Service.class);
                startService(i);
                System.out.println("Start");
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new intent pointing to gps service, stopping service
                Intent i = new Intent(getApplicationContext(),GPS_Service.class);
                stopService(i);
                System.out.println("Stop");

            }
        });

    }

    private boolean runtime_permissions() {
        //To make app working for android 23 and higher, we need user permission. So if higher then 23 and permission not granted yet, ask for permission
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){



            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            //requestcode for permission checking should be unique: 100

            return true;
        }
        //if we dont need to ask for permission
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //check is requestcode is same as set request code in runtime_permissions() and both permissions are granted. If true, enable buttons
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
         // create button matrix of 10 by 10 with right color and size dynamically to make sure we can access them easily through a loop
        for (int i=0; i<10; i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
            matrix[i] = new int[10];
            for (int j=0; j<10;j++){
                Button bt = new Button(this);
                bt.setLayoutParams(new TableRow.LayoutParams(50, 80));
                bt.setBackgroundColor(getResources().getColor(R.color.Quiet));
                matrix[i][j] = 0;
                btnTag[i][j] = bt;
                tableRow.addView(btnTag[i][j]);
            }
            table.addView(tableRow);
        }

    }
    public void readFromDB() {
        // make sure that the old values are removed from the matrix
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j]=0;
            }
        }
        // Get data, filter by time (only last 10 sec) and order by square
        all_data =  new ArrayList<>();
        CollectionReference locationsRef = db.collection("locations_test");
        locationsRef.whereGreaterThan("time", System.currentTimeMillis() - 5000)
                .orderBy("time")
                .orderBy("Square")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                get_info= document.getData();
                                String String_info=document.getString("Square");
                                //add all data to the arraylist
                                all_data.add(String_info);
                                for (int i=0; i<all_data.size(); i++) {
                                    // filter all "None" out of the list
                                    if (all_data.get(i).equals("None")) {
                                        // location is out of range and not usefull, so delete from Arraylist
                                        all_data.remove(all_data.get(i));
                                    }
                                }

                                processData();
                                Log.d("Firebase dataset", document.getId() + " => " + document.getData());
                            }
                            System.out.println("List: " +all_data);

                        } else {
                            Log.w("Firebase", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void processData(){
        if (all_data.size() != 0){
            for (int i=0; i<all_data.size();i++) {
                // convert data into separate substrings, one for each square (square goes from 0-9 so each can only be 1 element long.
                Object data = all_data.get(i);
                String data_string = data.toString();
                String x_square_string = data_string.substring(0, 1);
                String y_square_string = data_string.substring(1, 2);
                //convert substring to integer to be able to use for loops
                x_square = Integer.parseInt(x_square_string);
                y_square = Integer.parseInt(y_square_string);
        }



            //double for loop to match the two square int value to the matrix index. If match add 1 to the matrix
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (i == x_square && j == y_square) {
                        matrix[i][j]++;
                    }
                }
            }
            System.out.println(matrix[0][0]);
            //check all values of the whole matrix, if value above certain threshold, change color.
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (matrix[i][j] < 1) {
                        //change color button
                        btnTag[i][j].setBackgroundColor(getResources().getColor(R.color.Quiet));

                    } else if (matrix[i][j] <2 ) {
                        //change color button
                        btnTag[i][j].setBackgroundColor(getResources().getColor(R.color.Normal));

                    } else if (matrix[i][j] <3) {
                        //change color button
                        btnTag[i][j].setBackgroundColor(getResources().getColor(R.color.Crowded));

                    } else {
                        //change color button
                        btnTag[i][j].setBackgroundColor(getResources().getColor(R.color.Very_crowded));
                    }
                }
            }
        }
    }
}
