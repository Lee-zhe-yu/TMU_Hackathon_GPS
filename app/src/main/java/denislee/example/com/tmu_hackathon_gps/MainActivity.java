package denislee.example.com.tmu_hackathon_gps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public int heartBeat;
    public String location = null;

    private boolean getService = false;        //是否已開啟定位服務
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testLocationProvider();        //檢查定位服務
        Button showButton = (Button) findViewById(R.id.mapButton);
        showButton.setOnClickListener(show);

        sendToServer();

        if(heartBeat>150||heartBeat<50){
            Uri call=Uri.parse("tel:123");
            Intent phoneCall=new Intent(Intent.ACTION_VIEW,call);
            startActivity(phoneCall);

        }
        else{


        }
    }
     private void testLocationProvider() {

        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            getService = true;
            locationServiceInitial();
        } else {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;

    private void locationServiceInitial() {
        lms = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        bestProvider = lms.getBestProvider(criteria, true);
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
        Location location = lms.getLastKnownLocation(bestProvider);
        getLocation(location);
    }

    private void getLocation(Location location) {
        if (location != null) {
            TextView longitude_txt = (TextView) findViewById(R.id.longitude);
            TextView latitude_txt = (TextView) findViewById(R.id.latitude);

            Double longitude = location.getLongitude();
            Double latitude = location.getLatitude();

            longitude_txt.setText(String.valueOf(longitude));
            latitude_txt.setText(String.valueOf(latitude));

        } else {
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener show = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView longitude_txt = (TextView) findViewById(R.id.longitude);
            TextView latitude_txt = (TextView) findViewById(R.id.latitude);
            String longitude = String.valueOf(Double.parseDouble(longitude_txt.getText().toString()));
            String latitude = String.valueOf(Double.parseDouble(latitude_txt.getText().toString()));
            Toast.makeText(MainActivity.this, "https://www.google.com.tw/maps/@" + longitude+"," + latitude, Toast.LENGTH_LONG).show();
            location = "https://www.google.com.tw/maps/@" + longitude+"," + latitude;
        }
    };
    @Override
    public void onLocationChanged(Location location) {

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
}
