package com.example.salarycal

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    initialHourlyRate: Double = 100.0,
    initialOvertimeRate: Double = 150.0,
    initialStartTime: LocalTime = LocalTime.of(7, 0),
    initialWorkingHours: Int = 8,
    initialCurrency: String = "USD",
    onSave: (Double, Double, LocalTime, Int, String) -> Unit = { _, _, _, _, _ -> }
) {
    var hourlyRate by remember { mutableStateOf(TextFieldValue(initialHourlyRate.toString())) }
    var overtimeRate by remember { mutableStateOf(TextFieldValue(initialOvertimeRate.toString())) }
    var startTimeHour by remember { mutableStateOf(TextFieldValue(initialStartTime.hour.toString())) }
    var startTimeMinute by remember { mutableStateOf(TextFieldValue(initialStartTime.minute.toString())) }
    var workingHours by remember { mutableStateOf(TextFieldValue(initialWorkingHours.toString())) }
    var currency by remember { mutableStateOf(initialCurrency) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 24.dp))

        // Currency Selector
        Text("Currency", fontSize = 18.sp)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text("USD")
            Switch(
                checked = currency == "KES",
                onCheckedChange = { currency = if (it) "KES" else "USD" }
            )
            Text("KES")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hourly Rate Input
        OutlinedTextField(
            value = hourlyRate,
            onValueChange = { hourlyRate = it },
            label = { Text("Hourly Rate") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., 100.0") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Overtime Rate Input
        OutlinedTextField(
            value = overtimeRate,
            onValueChange = { overtimeRate = it },
            label = { Text("Overtime Rate") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., 150.0") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Start Time Input
        Text("Start Time (24-Hour Format)", fontSize = 18.sp)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = startTimeHour,
                onValueChange = { startTimeHour = it },
                label = { Text("Hour") },
                modifier = Modifier.width(100.dp),
                placeholder = { Text("07") }
            )
            OutlinedTextField(
                value = startTimeMinute,
                onValueChange = { startTimeMinute = it },
                label = { Text("Minute") },
                modifier = Modifier.width(100.dp),
                placeholder = { Text("00") }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Working Hours Input
        OutlinedTextField(
            value = workingHours,
            onValueChange = { workingHours = it },
            label = { Text("Working Hours") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., 8") }
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Save Button
        Button(onClick = {
            val parsedHourlyRate = hourlyRate.text.toDoubleOrNull() ?: initialHourlyRate
            val parsedOvertimeRate = overtimeRate.text.toDoubleOrNull() ?: initialOvertimeRate
            val parsedStartTimeHour = startTimeHour.text.toIntOrNull() ?: initialStartTime.hour
            val parsedStartTimeMinute = startTimeMinute.text.toIntOrNull() ?: initialStartTime.minute
            val parsedWorkingHours = workingHours.text.toIntOrNull() ?: initialWorkingHours

            onSave(
                parsedHourlyRate,
                parsedOvertimeRate,
                LocalTime.of(parsedStartTimeHour, parsedStartTimeMinute),
                parsedWorkingHours,
                currency
            )
        }) {
            Text("Save Settings")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
