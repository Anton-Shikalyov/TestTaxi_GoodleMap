package com.example.testtaxi

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtaxi.databinding.ActivityMainBinding
import com.example.testtaxi.dto.JsonList
import com.example.testtaxi.services.MapForegroundService
import com.example.testtaxi.services.Permissions
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {
    private val ACCESS_COARSE_LOCATION_REQUEST_CODE = 1
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var httpData: List<JsonList> = emptyList()
    private val urlListJson = "https://jsonplaceholder.typicode.com/photos"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchJsonData()

        val permissions = Permissions()
        permissions.requestPermissions(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        /*  NAVIGATE BUTTONS
             *   I used my own buttons due to google navigation view
             *   doesn't allow additional settings for recycler view
         */

        binding.navView.findViewById<Button>(R.id.nav_google_maps).setOnClickListener {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_google_maps)
        }
        binding.navView.findViewById<Button>(R.id.nav_home).setOnClickListener {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_home)
        }
        binding.navView.findViewById<Button>(R.id.nav_gallery).setOnClickListener {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_gallery)
        }
        binding.navView.findViewById<Button>(R.id.nav_slideshow).setOnClickListener {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_slideshow)
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_google_maps
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        recyclerView = findViewById(R.id.list_view)
        adapter = MyAdapter(httpData) { item ->
            // Handle item click here,
            Toast.makeText(this, "Clicked on $item", Toast.LENGTH_SHORT).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    // Top left menu for starting and stopping foreground service
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val serviceIntent = Intent(this, MapForegroundService::class.java)
        return when (item.itemId) {
            R.id.action_start -> {
                startForegroundService(serviceIntent)
                true
            }
            R.id.action_stop -> {
                stopService(serviceIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_COARSE_LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                     grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    val permissions = Permissions()
                    permissions.requestPermissions(this)
                }
                return
            }
            else -> {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.alert_dialog_title))
                    .setMessage(getString(R.string.message_alert_dialog))
                    .setPositiveButton("OK") { dialog, _ ->
                        val  permission = Permissions()
                        permission.requestPermissions(this)
                        dialog.dismiss()
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", this.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        this.startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    // This function getting data from "urlListJson"
    private fun fetchJsonData() {
        val client = OkHttpClient()
        val request = Request.Builder().url(urlListJson)
            .build()
            client.newCall (request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val jsonData = it.string()
                // GSON library allow work with JSON in kotlin
                    val gson = Gson()
                    val photoType = object : TypeToken<List<JsonList>>() {}.type
                    val photos: List<JsonList> = gson.fromJson(jsonData, photoType)
                    httpData = photos
                }
            }
        })
    }
}