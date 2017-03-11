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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView x, y, z, location1, m1;
    float x_val;
    int count = 0;

    Sensor accelerometer;
    SensorManager sm;

    LocationManager locationManager;
    LocationListener locationListener;

    DBHandler dbHandler;

    Location current;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this,null,null,1);


        m1 = (TextView)findViewById(R.id.message);




        x = (TextView) findViewById(R.id.x);
        y = (TextView) findViewById(R.id.y);
        z = (TextView) findViewById(R.id.z);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        Button map = (Button) findViewById(R.id.map);

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
        if (event.values[2] > 30 )
        {
            count++;
            m1.setText("Pothole Count = "+ Integer.toString(count));
        }

        x.setText("x= "+event.values[0]);
        x_val = event.values[0];
        y.setText("y= "+event.values[1]);
        z.setText("z= "+event.values[2]);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void goToMaps(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        //Create the bundle
        intent.putExtra("long",current.getLongitude());
        intent.putExtra("lat",current.getLatitude());
        startActivity(intent);
    }
    public void saveLocation(View view){
    }


}

