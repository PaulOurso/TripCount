package project.devmob.tripcount.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.ui.group.spending.AddSpendingActivity;

/**
 * Created by Tony on 02/09/2016.
 */
public class LocationHelper {

    private static LocationHelper locationHelper = null;

    private LocationHelper() {
        // Empty private constructor required
    }

    public static LocationHelper getInstance() {
        if (locationHelper == null)
            locationHelper = new LocationHelper();
        return locationHelper;
    }

    public void createRequest(final Activity act, AddSpendingActivity callback) {
        LocationManager locationManager = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(act,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constant.REQUEST_PERMISSIONS_GPS);
            return;
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, callback);
        else
            showActivateLocation(act);


        /*if (locationManager.getProviders(criteria, true).size() > 0)
            locationManager.requestSingleUpdate(Privid, callback, Looper.myLooper());
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    act);
            alertDialogBuilder
                    .setMessage(act.getString(R.string.alert_message_enable_gps))
                    .setCancelable(false)
                    .setPositiveButton(act.getString(R.string.alert_action_positive_enable_gps),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    act.startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton(act.getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }*/
    }

    public void showActivateLocation(final Activity act) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);
        alertDialogBuilder
                .setMessage(act.getString(R.string.alert_message_enable_gps))
                .setCancelable(false)
                .setPositiveButton(act.getString(R.string.alert_action_positive_enable_gps),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                act.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(act.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void removeRequest(AddSpendingActivity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(activity);
    }
}
