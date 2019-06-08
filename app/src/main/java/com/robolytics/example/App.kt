package com.robolytics.example

import android.app.Application
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sertook.robolytics.Robolytics
import java.util.concurrent.TimeUnit


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        Robolytics.debug(true)

        Robolytics.listenEvents { event ->
            Log.i("Robolytics", "event created : ${event.name}")
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event.name)
            event.values.forEach {
                bundle.putString(it.key, it.value)
            }
            firebaseAnalytics.logEvent(event.type, bundle)
        }


        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.fetch(TimeUnit.HOURS.toSeconds(24))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("Robolytics", "Fetch config succeeded")
                    firebaseRemoteConfig.activate()
                        .addOnCompleteListener {
                            Robolytics.updateRules(firebaseRemoteConfig.getString("ROBOLYTICS"))
                        }
                } else {
                    Log.i("Robolytics", "Fetch config failed")
                }
            }

    }

}