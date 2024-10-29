package com.example.salarycal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.salarycal.data.FirebaseRepository
import com.example.salarycal.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val repository = FirebaseRepository()

    var startHour by remember { mutableStateOf(7) }
    var workingHours by remember { mutableStateOf(8) }
    var hourlyRate by remember { mutableStateOf(100.0) }
    var extraHourlyRate by remember { mutableStateOf(150.0) }
    var currency by remember { mutableStateOf("USD") }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val savedSettings = repository.loadSettings()
            savedSettings?.let {
                startHour = it.startHour
                workingHours = it.workingHours
                hourlyRate = it.hourlyRate
                extraHourlyRate = it.extraHourlyRate
                currency = it.currency
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(value = startHour.toString(), onValueChange = { startHour = it.toIntOrNull() ?: 7 }, label = { Text("Start Hour") })
        OutlinedTextField(value = workingHours.toString(), onValueChange = { workingHours = it.toIntOrNull() ?: 8 }, label = { Text("Working Hours") })
        OutlinedTextField(value = hourlyRate.toString(), onValueChange = { hourlyRate = it.toDoubleOrNull() ?: 100.0 }, label = { Text("Hourly Rate") })
        OutlinedTextField(value = extraHourlyRate.toString(), onValueChange = { extraHourlyRate = it.toDoubleOrNull() ?: 150.0 }, label = { Text("Extra Hourly Rate") })

        Button(onClick = {
            val settings = Settings(
                startHour = startHour,
                workingHours = workingHours,
                hourlyRate = hourlyRate,
                extraHourlyRate = extraHourlyRate,
                currency = currency
            )

            CoroutineScope(Dispatchers.IO).launch {
                repository.saveSettings(settings)
            }
        }) {
            Text("Save Settings")
        }
    }
}
