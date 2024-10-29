package com.example.salarycal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.salarycal.data.ReportItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    dailyReports: List<ReportItem>,
    monthlyTotal: Double,
    biWeeklyTotals: List<Double>,
    totalHours: Double
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports Summary") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Monthly Total Earnings: $${"%.2f".format(monthlyTotal)}", style = MaterialTheme.typography.headlineSmall)
            Text("Total Hours Worked: ${"%.2f".format(totalHours)} hrs", style = MaterialTheme.typography.bodyLarge)

            Text("Bi-Weekly Totals:", style = MaterialTheme.typography.titleMedium)
            biWeeklyTotals.forEachIndexed { index, total ->
                Text("Week ${index + 1}: $${"%.2f".format(total)}", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Daily Earnings:", style = MaterialTheme.typography.titleMedium)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                dailyReports.forEach { report ->
                    ReportCard(report)
                }
            }
        }
    }
}

@Composable
fun ReportCard(report: ReportItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text("Date: ${report.date}", style = MaterialTheme.typography.bodyMedium)
            Text("Earnings: $${"%.2f".format(report.totalEarnings)}", style = MaterialTheme.typography.bodyLarge)
            Text("Hours Worked: ${"%.2f".format(report.hoursWorked)} hrs", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
