package in.rashtriyakaryadala.potholedetectionandlogging;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView x, y, z, location1, m1, result;
    EditText thres;

    double threshold = 30;

    Sensor accelerometer;
    SensorManager sm;

    LocationManager locationManager;
    LocationListener locationListener;

    DBHandler dbHandler;

    Location current;


    long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this,null,null,1);

        current = new Location("Dummy");

        current.setLatitude(13.00018011);
        current.setLongitude(77.57287016);


        x = (TextView) findViewById(R.id.x);
        y = (TextView) findViewById(R.id.y);
        z = (TextView) findViewById(R.id.z);

        thres = (EditText)findViewById(R.id.threshold);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                location1 = (TextView)findViewById(R.id.location1);
                location1.setText("Location:"+location.getLatitude()+" "+location.getLongitude());
                current = location;

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                },10);
            }
            return;
        }
        else{
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        x.setText("x= "+event.values[0]);
        y.setText("y= "+event.values[1]);
        z.setText("z= "+event.values[2]);


        //This is done to prevent multiple values above threshold being registered as multiple potholes
        long actualTime = event.timestamp;
        try{
            if(actualTime - lastUpdate > 100000000) {
                lastUpdate = actualTime;
                if (event.values[2] > threshold) {
                    Toast.makeText(getApplicationContext(),"Pothole Detected",Toast.LENGTH_SHORT).show();
                    dbHandler.addLocation(current);
                }
            }
        }
        catch (Exception e){
            lastUpdate = actualTime;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void goToMaps(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        //Create the bundle
        Log.i("Data", Double.toString(current.getLatitude()));
        intent.putExtra("long",current.getLongitude());
        intent.putExtra("lat",current.getLatitude());
        startActivity(intent);
    }
    public void saveLocation(View view){
        dbHandler.addLocation(current);
    }

    public void printDB(View view){
        result = (TextView)findViewById(R.id.result);
        result.setText(dbHandler.printDataBase());
    }

    public void deleteTable(View view){
        dbHandler.deleteTable();
    }

    public void setThreshold(View view){
        try{
            threshold = Double.parseDouble(thres.getText().toString());
        }
        catch (Exception e){
        }
    }


}