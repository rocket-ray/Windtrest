package com.example.app_team35

import android.location.Location
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.test.core.app.ActivityScenario
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Test that updating the user location triggers the LiveData observer callback
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class UserLocationTest {
    @Test
    fun userLocation() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        activityScenario.moveToState(Lifecycle.State.RESUMED).onActivity{activity -> run {
            // Test onMapReady callback is not triggered in the unit test, so we set it up manually
            activity.requestLocationUpdates();

            // Register test observer
            activity.locationViewModel.getLocationLiveData().observe(activity, Observer {
                assertEquals(it.longitude, 5.0);
                assertEquals(it.latitude, 5.0);
            })

            // Trigger observer update. This simulates the locationCallback calling setLocationData()
            var mock = Location("");
            mock.latitude = 5.0;
            mock.longitude = 5.0;
            activity.locationViewModel.getLocationLiveData().setLocationData(mock);
        }}
    }
}