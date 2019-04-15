package testing.gps_service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    private FirebaseFirestore db;


    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }
    //allows to bind activities to the service; send requests and receive responses
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        final GPS_Service service = this;

        //Locationlistener receives notification when location has changed
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // get longitude and latitude and save as double.
                double lon = location.getLongitude();
                double lat = location.getLatitude();

                //create threshold values for main terrain, divide this into 10 squares in both x as y direction. So create 100 squares which matches the matrix in MainActivity
                double startLon = 4.3596;
                double endLon = 4.366;
                double addLon = (endLon-startLon)/10;

                double startLat = 51.9804;
                double endLat = 51.985;
                double addLat = (endLat-startLat)/10;

                int latSquare = 0;
                int lonSquare = 0;
                String square;

                //check if coordinates are in the set main terrain, if not square is none.
                if (lon < startLon || lon > (startLon + 10 * addLon) || lat < startLat || lat > (startLat + 10 * addLat)) {
                    square = "None";
                } else {
                    // For loop to see in which square the Latitude coordinates fit
                    for (int j = 0; j < 10; j++) {
                        if (lat > startLat && lat < startLat + addLat) {
                            System.out.println("LatSquare: " + latSquare);
                            break;
                        }
                        startLat = startLat + addLat;
                        latSquare++;
                    }

                    // For loop to see in which square the Longitude coordinates fit
                    for (int k = 0; k < 10; k++) {
                        if (lon > startLon && lon < startLon + addLon) {
                            System.out.println("lonSquare: " + lonSquare);
                            break;
                        }
                        startLon = startLon + addLon;
                        lonSquare++;

                    }
                    System.out.println("Lon= " + lon + " Lat= " + lat);

                    //combine squares of longitude and latitude to send to database, later used to index in the matrix
                    square = Integer.toString(latSquare) + lonSquare;
                }
                System.out.println("Send to Db");
                service.sendToDb(square);

//                i.putExtra("coordinates", location.getLongitude() + " " + location.getLatitude() + " vak: " + square);
//                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                // if location services are disabled on the phone, send user to settings through a intent to enable them
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        //locationManager provides access to system location services
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        //listen for updates every 5 sec
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);
    }

    public void sendToDb(String square) {
        Map<String, Object> value = new HashMap<>();
        value.put("Square", square);
        value.put("time", System.currentTimeMillis());

        db.collection("locations_test")
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

    @Override
    // when service is destroyed locationlistenere should stop listening, to avoid memory leak
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }

}
