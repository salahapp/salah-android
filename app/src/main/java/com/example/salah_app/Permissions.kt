package com.example.salah_app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener

class Permissions {
    companion object {

        fun checkPermissions(context: Context) = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        fun requestPermissions(context: Context, listener: PermissionListener) {
            Dexter.withContext(context)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(listener)
                .check()
        }
    }
}
