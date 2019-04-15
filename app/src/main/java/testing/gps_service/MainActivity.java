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
    private BroadcastReceiver broadcastReceiver;

    private FirebaseFirestore db;
    public ArrayList all_data;
    public Map get_info;
    public List list_with_values;
    private int x_square;
    private int y_square;

    final MainActivity service = this;



//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(broadcastReceiver == null){
//            broadcastReceiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    System.out.println("\n" +intent.getExtras().get("coordinates"));
//                }
//            };
//        }
//        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
//    }

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
        // layout main activity
        setContentView(R.layout.activity_main);

        // checks DB info every 10 sec
        final Handler handler = new Handler();
        // 10 sec
        Runnable r = new Runnable() {
            @Override
            public void run() {
                service.readFromDB();
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(r,10000);

        //start or stop the service
        btn_start = (Button) findViewById(R.id.start);
        btn_stop = (Button) findViewById(R.id.stop);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        //gps permission needed before enable buttons
        if(!runtime_permissions())
            enable_buttons();


        //images stages
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

        //Dynamic buttons
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
    public void readFromDB() {
        // Get data, order data "vak" (Poging 1 )
        all_data =  new ArrayList<>();
        CollectionReference locationsRef = db.collection("locations_8-4-3");
        locationsRef.whereGreaterThan("time", System.currentTimeMillis() - 60000)
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
                                all_data.add(String_info);
                                for (int i=0; i<all_data.size(); i++) {
                                    System.out.println(all_data.get(i));
                                    if (all_data.get(i).equals("None")) {
                                        System.out.println("Out of range");
                                        all_data.remove(all_data.get(i));
                                    }
                                }


                                for (int i=0; i<all_data.size();i++) {
                                    Object data = all_data.get(i);
                                    System.out.println("Object : " + data);
                                    String data_string = data.toString();
                                    String x_square_string = data_string.substring(0, 1);
                                    String y_square_string = data_string.substring(1, 2);
                                    System.out.println("Data substring" + x_square_string + "second: " + y_square_string);
                                    x_square = Integer.parseInt(x_square_string);
                                    y_square = Integer.parseInt(y_square_string);
//
                                }
                                System.out.println("Print value outside loop: " + x_square + "secondd" + y_square);



                                for (int i = 0; i < 10; i++) {
                                    for (int j = 0; j < 10; j++) {
                                        if (i == x_square && j == y_square) {
                                            matrix[i][j]++;
                                        }
                                    }
                                }

                                for (int i = 0; i < 10; i++) {
                                    for (int j = 0; j < 10; j++) {
                                        if (matrix[i][j] < 5) {
                                            //change color button
                                            btnTag[i][j].setBackgroundColor(getResources().getColor(R.color.Quiet));

                                        } else if (matrix[i][j] < 10) {
                                            //change color button
                                            btnTag[i][j].setBackgroundColor(getResources().getColor(R.color.Normal));

                                        } else if (matrix[i][j] < 15) {
                                            //change color button
                                            btnTag[i][j].setBackgroundColor(getResources().getColor(R.color.Crowded));

                                        } else {
                                            //change color button
                                            btnTag[i][j].setBackgroundColor(getResources().getColor(R.color.Very_crowded));
                                        }
                                    }
                                }

                                Log.d("Firebase dataset", document.getId() + " => " + document.getData());
                            }
                            System.out.println("List: " +all_data);

                        } else {
                            Log.w("Firebase", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
