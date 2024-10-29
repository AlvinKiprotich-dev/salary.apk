package com.example.salarycal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.salarycal.data.FirebaseRepository
import com.example.salarycal.data.Settings
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

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
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

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Settings", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = startHour.toString(),
                onValueChange = { startHour = it.toIntOrNull() ?: 7 },
                label = { Text("Start Hour") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = workingHours.toString(),
                onValueChange = { workingHours = it.toIntOrNull() ?: 8 },
                label = { Text("Working Hours") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = hourlyRate.toString(),
                onValueChange = { hourlyRate = it.toDoubleOrNull() ?: 100.0 },
                label = { Text("Hourly Rate") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = extraHourlyRate.toString(),
                onValueChange = { extraHourlyRate = it.toDoubleOrNull() ?: 150.0 },
                label = { Text("Extra Hourly Rate") },
                modifier = Modifier.fillMaxWidth()
            )

            // Currency selection
            DropdownMenuBox(
                selectedCurrency = currency,
                onCurrencySelected = { currency = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                val settings = Settings(
                    startHour = startHour,
                    workingHours = workingHours,
                    hourlyRate = hourlyRate,
                    extraHourlyRate = extraHourlyRate,
                    currency = currency
                )

                coroutineScope.launch {
                    repository.saveSettings(settings)
                    snackbarHostState.showSnackbar("Settings saved successfully!")
                }
            }) {
                Text("Save Settings")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currencies = listOf("USD", "KES")

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCurrency,
            onValueChange = {},
            label = { Text("Currency") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            readOnly = true
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency) },
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}
