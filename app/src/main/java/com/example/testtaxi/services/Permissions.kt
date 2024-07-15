package com.example.testtaxi.services

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService

class Permissions {
    private val ACCESS_COARSE_LOCATION_REQUEST_CODE = 1

    fun requestPermissions(activity: Activity) {
        when {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(activity, "Location permission granted", Toast.LENGTH_LONG).show()
                val serviceIntent = Intent(activity, MapForegroundService::class.java)
                activity.startForegroundService(serviceIntent)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                Toast.makeText(activity, "Location permission is needed", Toast.LENGTH_LONG).show()
                AlertDialog.Builder(activity)
                    .setTitle("Test Taxi Permission")
                    .setMessage("Нам необходимо разрешение вашей геопозиции для построения маршрута")
                    .setPositiveButton("OK") { dialog, _ ->
                        val  permission = Permissions()
                        dialog.dismiss()
                        permission.requestPermissions(activity)
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.INTERNET,
                                Manifest.permission.FOREGROUND_SERVICE,
                                Manifest.permission.FOREGROUND_SERVICE_LOCATION
                            ),
                            ACCESS_COARSE_LOCATION_REQUEST_CODE
                        )
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()

            }
            else -> {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.FOREGROUND_SERVICE_LOCATION
                    ),
                    ACCESS_COARSE_LOCATION_REQUEST_CODE
                )
            }
        }
    }
}
