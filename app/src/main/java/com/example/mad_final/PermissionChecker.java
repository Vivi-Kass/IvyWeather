/*
 * FILE :            MainActivity.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 11
 * DESCRIPTION :     Saves a long code by just having it stored here. Checks permissions and returns bools based on results. Also can ask for permissions
 */

package com.example.mad_final;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

public class PermissionChecker {

    //Check permissions
    static public boolean checkPermissions(Context ctx)
    {
        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //Request precise location
    static public void promptPrecise(Activity activity)
    {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(activity, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }
    }


    //Request coarse location permissions
    static public void promptCoarse(Activity activity)
    {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(activity, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
        }
    }

}
