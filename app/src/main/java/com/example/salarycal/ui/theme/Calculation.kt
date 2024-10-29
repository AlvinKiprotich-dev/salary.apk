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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    var isWorking by remember { mutableStateOf(true) }
    var isPaused by remember { mutableStateOf(false) }
    var pauseAccumulatedTime by remember { mutableStateOf(0L) }
    var pauseStartTime by remember { mutableStateOf<LocalTime?>(null) }

    LaunchedEffect(isWorking) {
        while (isWorking) {
            if (!isPaused) {
                delay(1000L)
                currentTime = LocalTime.now()

                val effectiveStartTime = startTime.plusSeconds(pauseAccumulatedTime)
                val workedMinutes = effectiveStartTime.until(currentTime, ChronoUnit.MINUTES).toDouble()
                val regularMinutes = workedMinutes.coerceAtMost(8.0 * 60)
                val extraMinutes = (workedMinutes - 8.0 * 60).coerceAtLeast(0.0)
                totalEarned = (regularMinutes / 60 * hourlyRate) + (extraMinutes / 60 * extraHourlyRate)
            } else {
                pauseStartTime?.let {
                    pauseAccumulatedTime += ChronoUnit.SECONDS.between(it, LocalTime.now())
                    pauseStartTime = null
                }
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
            "Real-Time Salary Calculator",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("Hourly Rate: $$hourlyRate", fontSize = 18.sp)
        Text("Extra Hourly Rate: $$extraHourlyRate", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Start Time: $startTime", fontSize = 18.sp)
        Text("Current Time: $currentTime", fontSize = 18.sp)
        Text(
            "Total Earned: $${"%.2f".format(totalEarned)}",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    isPaused = !isPaused
                    pauseStartTime = if (isPaused) LocalTime.now() else pauseStartTime
                },
                colors = ButtonDefaults.buttonColors(
                    //backgroundColor = if (isPaused) Color.Green else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (isPaused) "Resume" else "Pause")
            }

            Button(
                onClick = {
                    isWorking = false
                },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Stop")
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
