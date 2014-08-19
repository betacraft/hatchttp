package com.rainingclouds.hatchttp.sampleapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.rainingclouds.hatchttp.TaskEventListener;
import com.rainingclouds.hatchttp.TaskMonitor;
import com.rainingclouds.hatchttp.exception.HatcHttpException;


public class MyActivity extends ActionBarActivity {
    LocationManager mLocationManager;
    Location mLatestLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        try {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    mLatestLocation = location;
                }

                @Override
                public void onStatusChanged(final String provider, final int status, final Bundle extras) {
                    //TODO handle it gracefully
                }

                @Override
                public void onProviderEnabled(final String provider) {
                    //TODO handle it gracefully
                }

                @Override
                public void onProviderDisabled(final String provider) {
                    //TODO handle it gracefully
                }
            });
            mLatestLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (mLatestLocation == null) {
                Log.d("asdasdasd", "GPS has no latest location so trying now NETWORK_PROVIDED");
                mLatestLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            new PlotMeTask(getApplicationContext(), new TaskMonitor(), mLatestLocation).execute(new TaskEventListener<String>() {
                @Override
                public void onTaskExecutionComplete(String response) {
                    Log.d("asdasd",response);
                }

                @Override
                public void onTaskExceptionEvent(HatcHttpException exception) {
                    Log.e("asdasd","asdasdasd sdfsdf",exception);
                }
            });
        }catch (Exception e){
            Log.d("asdasd","asdasd",e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
