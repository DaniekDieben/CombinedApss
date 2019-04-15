package testing.gps_service;

import android.Manifest;
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

                int latSquare = 0;
                int lonSquare = 0;
                String square;

                if (lon < startLon || lon > (startLon + 10 * addLon) || lat < startLat || lat > (startLat + 10 * addLat)) {
                    square = "None";
                } else {
                    // Loop latSquare
                    for (int j = 0; j < 10; j++) {
                        if (lat > startLat && lat < startLat + addLat) {
                            System.out.println("LatSquare: " + latSquare);
                            break;
                        }
                        startLat = startLat + addLat;
                        System.out.println(latSquare);
                        latSquare++;
                    }

                    // Loop lonSquare
                    for (int k = 0; k < 10; k++) {
                        if (lon > startLon && lon < startLon + addLon) {
                            System.out.println("lonSquare: " + lonSquare);
                            break;
                        }
                        startLon = startLon + addLon;
                        lonSquare++;

                    }
                    System.out.println("Lon= " + lon + " Lat= " + lat);

                    square = Integer.toString(latSquare) + lonSquare;
                }
                System.out.println("Send to Db");
                service.sendToDb(square);

                i.putExtra("coordinates", location.getLongitude() + " " + location.getLatitude() + " vak: " + square);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, listener);
    }

    public void sendToDb(String square) {
        // Create a new user with a first and last name
        Map<String, Object> value = new HashMap<>();
        value.put("Square", square);
        value.put("time", System.currentTimeMillis());

        db.collection("locations_15-4")
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
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }

}
