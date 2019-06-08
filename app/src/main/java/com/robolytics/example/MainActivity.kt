package com.robolytics.example

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.sertook.robolytics.RobolyticsConst
import com.sertook.robolytics.RobolyticsEvent
import com.sertook.robolytics.RobolyticsTracker
import com.sertook.robolytics.annotations.RobolyticsData
import com.sertook.robolytics.annotations.RobolyticsTrack

class MainActivity : AppCompatActivity() {

    var currentScreen = Screen("Home")

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                setScreen(Screen(getString(R.string.title_home)))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                setScreen(Screen(getString(R.string.title_dashboard)))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                setScreen(Screen(getString(R.string.title_notifications)))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        textMessage.setOnClickListener {
            Log.i("MainActivity", "button clicked")
            RobolyticsTracker.trackValue("custom-key", applicationContext.packageName)
        }
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }


    @RobolyticsTrack(type = RobolyticsConst.TYPE_ACTION)
    fun setScreen(screen: Screen) {
        Log.i("setScreen", "This method will generate an event")
        currentScreen = screen
        textMessage.text = screen.name
    }


    data class Screen(@RobolyticsData val name: String)
}
