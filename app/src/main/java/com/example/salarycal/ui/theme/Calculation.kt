package com.example.salarycal

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.salarycal.data.DailyRecord
import com.example.salarycal.data.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.max

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryCalculatorScreen() {
    val repository = FirebaseRepository()
    val snackbarHostState = remember { SnackbarHostState() }

    var hourlyRate by remember { mutableStateOf(100.0) }
    var extraHourlyRate by remember { mutableStateOf(150.0) }
    var startTime by remember { mutableStateOf(LocalTime.of(7, 0)) }
    var totalEarned by remember { mutableStateOf(0.0) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    var isWorking by remember { mutableStateOf(true) }
    var countdown by remember { mutableStateOf(8 * 60 * 60) } // 8 hours in seconds

    LaunchedEffect(isWorking) {
        while (isWorking) {
            delay(1000L)
            currentTime = LocalTime.now()

            // Calculate total worked hours
            val workedSeconds = startTime.until(currentTime, ChronoUnit.SECONDS)
            val workedHours = workedSeconds / 3600.0
            val regularHours = workedHours.coerceAtMost(8.0)
            val extraHours = max(0.0, workedHours - 8.0)

            // Calculate earnings based on regular and extra hours
            totalEarned = (regularHours * hourlyRate) + (extraHours * extraHourlyRate)

            // Update countdown timer for the 8-hour work period
            countdown = max(0, 8 * 3600 - workedSeconds.toInt())
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Salary Calculator") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Real-Time Salary Calculator", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // Display countdown
            val hours = countdown / 3600
            val minutes = (countdown % 3600) / 60
            val seconds = countdown % 60
            Text("Time Left: %02d:%02d:%02d".format(hours, minutes, seconds))

            Spacer(modifier = Modifier.height(16.dp))
            Text("Total Earned: $${"%.2f".format(totalEarned)}")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isWorking = !isWorking
                    CoroutineScope(Dispatchers.IO).launch {
                        snackbarHostState.showSnackbar(if (isWorking) "Resumed work" else "Paused work")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (isWorking) "Pause" else "Resume")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isWorking = false
                    val record = DailyRecord(
                        date = LocalDate.now().toString(),
                        totalEarnings = totalEarned,
                        hoursWorked = startTime.until(currentTime, ChronoUnit.HOURS).toDouble()
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        repository.addDailyRecord(record)
                        snackbarHostState.showSnackbar("Record saved successfully")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Daily Record")
            }
        }
    }
}
