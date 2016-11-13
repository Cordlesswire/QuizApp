package com.udacity.android.quizapp;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geovani on 24/09/16.
 */

public class PermissionUtils {


    public static boolean validate(Activity activity, int requestCode, String... permissions) {
        List<String> permissionsList = new ArrayList<>();
        for (String permission : permissions) {
            boolean ok =
                         ContextCompat.checkSelfPermission(activity, permission)
                            ==
                         PackageManager.PERMISSION_GRANTED;
            if (!ok) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList.isEmpty()) {
            return true;
        }

        String[] newPermissions = new String[permissionsList.size()];
        permissionsList.toArray(newPermissions);

        ActivityCompat.requestPermissions(activity, newPermissions, 1);

        return false;
    }



}
