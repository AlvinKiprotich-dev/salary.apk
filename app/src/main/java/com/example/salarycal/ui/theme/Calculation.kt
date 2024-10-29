package com.example.salarycal

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SalaryCalculatorScreen() {
    var hourlyRate by remember { mutableStateOf(100.0) }
    var extraHourlyRate by remember { mutableStateOf(hourlyRate * 1.5) }
    var startTime by remember { mutableStateOf(LocalTime.of(7, 0)) }
    var totalEarned by remember { mutableStateOf(0.0) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    var isWorking by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }

    // Coroutine to update time and calculate earnings
    LaunchedEffect(isWorking) {
        if (isWorking) {
            while (isWorking) {
                delay(1000L)
                currentTime = LocalTime.now()
                elapsedTime = startTime.until(currentTime, ChronoUnit.SECONDS)

                val workedHours = elapsedTime / 3600.0 // Convert seconds to hours
                val regularHours = workedHours.coerceAtMost(8.0)
                val extraHours = (workedHours - 8.0).coerceAtLeast(0.0)
                totalEarned = (regularHours * hourlyRate) + (extraHours * extraHourlyRate)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Salary Calculator",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Display rates
        Text("Hourly Rate: $$hourlyRate", style = MaterialTheme.typography.bodyLarge)
        Text("Overtime Rate: $$extraHourlyRate", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(20.dp))

        // Display working status and calculated earnings
        Text("Start Time: $startTime", style = MaterialTheme.typography.bodyMedium)
        Text("Current Time: $currentTime", style = MaterialTheme.typography.bodyMedium)
        Text("Total Earned: $${"%.2f".format(totalEarned)}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(20.dp))

        // Control buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { isWorking = !isWorking },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isWorking) "Pause" else "Start")
            }

            Button(
                onClick = {
                    isWorking = false
                    totalEarned = 0.0
                    elapsedTime = 0L
                    startTime = LocalTime.now()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Stop & Reset")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SalaryCalculatorScreenPreview() {
    SalaryCalculatorScreen()
}
