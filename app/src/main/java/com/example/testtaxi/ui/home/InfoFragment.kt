package com.example.testtaxi.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testtaxi.R

class InfoFragment : Fragment() {

    private lateinit var infoFragmentViewModel: InfoFragmentViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_info, container, false)
        val latitudeTextView: TextView = root.findViewById(R.id.latitude)
        val longitudeTextView: TextView = root.findViewById(R.id.longitude)
        val speedTextView: TextView = root.findViewById(R.id.speed)

        infoFragmentViewModel = ViewModelProvider(this).get(InfoFragmentViewModel::class.java)

        infoFragmentViewModel.latitude.observe(viewLifecycleOwner) {
            latitudeTextView.text = it.toString()
        }

        infoFragmentViewModel.longitude.observe(viewLifecycleOwner) {
            longitudeTextView.text = it.toString()
        }

        infoFragmentViewModel.speed.observe(viewLifecycleOwner) {
            speedTextView.text = it.toString()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            infoFragmentViewModel.registerReceiver(it)
            Log.d("InfoFragment", "Receiver registered in onResume")
        }
    }

    override fun onPause() {
        super.onPause()
        context?.let {
            infoFragmentViewModel.unregisterReceiver(it)
            Log.d("InfoFragment", "Receiver unregistered in onPause")
        }
    }
}
