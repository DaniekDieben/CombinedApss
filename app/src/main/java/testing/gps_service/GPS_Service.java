package testing.gps_service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    private FirebaseFirestore db;
    public static Map get_info;
    public static ArrayList all_data;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        final GPS_Service service = this;

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_update");

                double lon = location.getLongitude();
                double lat = location.getLatitude();

                double startLon = 4.374030;
                double endLon = 4.377568;
                double addLon = (endLon-startLon)/10;

                double startLat = 52.002;
                double endLat = 52.003344;
                double addLat = (endLat-startLat)/10;

                int xVak = 0;
                int yVak = 0;
                String vak;

                if (lon < startLon || lon > (startLon + 10*addLon) || lat < startLat || lat > (startLat + 10*addLat)){
                    vak = "None";
                }
                else {
                    // Loop y vakken
                    for (int j=0; j<10; j++){
                        if (lat > startLat && lat < startLat + addLat){
                            System.out.println("yVak: " +yVak);
                            break;}
                        startLat = startLat + addLat;
                        System.out.println(yVak);
                        yVak ++;
                    }

                    // Loop x vakken
                    for (int k=0; k<10; k++){
                        if (lon > startLon && lon < startLon + addLon){
                            System.out.println("xVak: " +xVak);
                            break;}
                        startLon = startLon + addLon;
                        xVak ++;

                    }
                    System.out.println("Lon= "+lon+ " Lat= "+lat);
                    System.out.println("xVak end= "+xVak);
                    System.out.println("yVak end= " +yVak);

                    vak = Integer.toString(xVak)+yVak;
                }

                System.out.println("Send to Db");
                service.sendToDb(vak);

                System.out.println("Read from Db");
                service.readFromDB(vak);

                i.putExtra("coordinates",location.getLongitude()+" "+location.getLatitude()+" vak: "+ vak);
                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,listener);

    }

    public void sendToDb(String vak) {
        // Create a new user with a first and last name
        Map<String, Object> value = new HashMap<>();
        value.put("Square", vak);
        value.put("time", System.currentTimeMillis());

        db.collection("locations_8-4-3")
                .add(value)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase", "Error adding document", e);
                    }
                });
    }

    public void readFromDB(String vak) {
        // Get data, order data "vak" (Poging 1 )
        Log.d("In read from", "In read from");
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
                                System.out.println("output eind midden" +get_info);
                                all_data.add(get_info);
                                Log.d("Firebase dataset", document.getId() + " => " + document.getData());
                            }
                            System.out.println("List: " +all_data);
                        } else {
                            Log.w("Firebase", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}
