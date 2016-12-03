package firespace.com.polutiondetector;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

/**
 * Created by Brian on 12/3/2016.
 */

public class LocationStuff implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    public Double[] coordinates;
    private Context context;

    final int locationPermission = 9999;

    public LocationStuff(Context context) {

        System.out.println("in initialization");

        this.context = context;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            System.out.println("m googleapiclient was null");
        }
        mGoogleApiClient.connect();

    }

    public Double[] getCurrentCoordinates() {



        return coordinates;
    }


    @Override
    public void onConnected(Bundle connectionHint) {

        System.out.println("Connected to Google Api Client");

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    locationPermission);
            return;
        } else {
            System.out.println("Location permission possessed.");
        }



        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            Double[] coords = {lastLocation.getLatitude(), lastLocation.getLongitude()};
            coordinates = coords;
            System.out.println("Coords (in locationSTuff): " + coords[0] + ", " + coords[1]);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Connection failed: " + connectionResult.getErrorMessage());
        System.out.println(connectionResult.getErrorCode());
    }


    //see if close to the saved location; range in seconds
    public boolean withinRangeOfCurrentLocation(Double[]originalCoords, int range) {
        GoogleMatrixRequest request = new GoogleMatrixRequest(context);
        try {
            int duration = request.getRouteDuration(originalCoords, getCurrentCoordinates());
            System.out.println("Final calculated duration = " + duration);
            if (duration <= range) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
