package com.example.bikestat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bikestat.WearableDeviceAPI.ApiOperations
import com.example.bikestat.ui.theme.BikeStatTheme
import io.realm.Realm

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BikeStatTheme {
                MainAppNavigationView()
            }
        }
        ApiOperations.getAPIRequest()
        Realm.init(this)
    }
}