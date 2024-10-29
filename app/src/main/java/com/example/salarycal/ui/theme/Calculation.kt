package com.example.salarycal

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SalaryCalculatorScreen() {
    val repository = FirebaseRepository()

    var hourlyRate by remember { mutableStateOf(100.0) }
    var extraHourlyRate by remember { mutableStateOf(150.0) }
    var startTime by remember { mutableStateOf(LocalTime.of(7, 0)) }
    var totalEarned by remember { mutableStateOf(0.0) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    var isWorking by remember { mutableStateOf(true) }

    LaunchedEffect(isWorking) {
        while (isWorking) {
            delay(1000L)
            currentTime = LocalTime.now()
            val workedHours = startTime.until(currentTime, ChronoUnit.HOURS).toDouble()
            val regularHours = workedHours.coerceAtMost(8.0)
            val extraHours = (workedHours - 8.0).coerceAtLeast(0.0)
            totalEarned = (regularHours * hourlyRate) + (extraHours * extraHourlyRate)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Real-Time Salary Calculator", style = MaterialTheme.typography.headlineSmall)
        Text("Total Earned: $${"%.2f".format(totalEarned)}")

        Button(onClick = {
            isWorking = false
            val record = DailyRecord(
                date = LocalDate.now().toString(),
                totalEarnings = totalEarned,
                hoursWorked = startTime.until(currentTime, ChronoUnit.HOURS).toDouble()
            )

            CoroutineScope(Dispatchers.IO).launch {
                repository.addDailyRecord(record)
            }
        }) {
            Text("Save Daily Record")
        }
    }
}
