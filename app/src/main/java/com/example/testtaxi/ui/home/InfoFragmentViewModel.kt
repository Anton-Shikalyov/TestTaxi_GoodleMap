package com.example.testtaxi.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log

class InfoFragmentViewModel : ViewModel() {
    private val _latitude = MutableLiveData<String>()
    val latitude: LiveData<String> = _latitude

    private val _longitude = MutableLiveData<String>()
    val longitude: LiveData<String> = _longitude

    private val _speed = MutableLiveData<String>()
    val speed: LiveData<String> = _speed

    private val locationUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val newLatitude = intent.getDoubleExtra("latitude", 0.0)
            val newLongitude = intent.getDoubleExtra("longitude", 0.0)
            val newSpped = intent.getDoubleExtra("speed", 0.0)

            _latitude.value = "Широта: " + newLatitude
            _longitude.value = "Долгота: " + newLongitude
            _speed.value = "Скорость: " + newSpped
        }
    }

    fun registerReceiver(context: Context) {
        val intentFilter = IntentFilter("LOCATION_UPDATE")
        context.registerReceiver(locationUpdateReceiver, intentFilter)
        Log.d("InfoFragmentViewModel", "Receiver registered")
    }

    fun unregisterReceiver(context: Context) {
        context.unregisterReceiver(locationUpdateReceiver)
        Log.d("InfoFragmentViewModel", "Receiver unregistered")
    }
}
