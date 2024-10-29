package com.example.salarycal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ReportItem(val date: String, val income: Double, val workingHours: Double)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(dailyReports: List<ReportItem>, monthlyTotal: Double, biWeeklyTotals: List<Double>, totalHours: Double) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Income Report",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Monthly summary card
        SummaryCard("Monthly Total Income", "$${"%.2f".format(monthlyTotal)}")

        // Total working hours
        SummaryCard("Total Working Hours", "${"%.2f".format(totalHours)} hours")

        // Bi-weekly totals
        biWeeklyTotals.forEachIndexed { index, total ->
            SummaryCard("Week ${index + 1} Total Income", "$${"%.2f".format(total)}")
        }

        // Daily earnings list
        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dailyReports.size) { index ->
                val report = dailyReports[index]
                ReportItemCard(report)
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, amount: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Text(text = amount, style = MaterialTheme.typography.headlineMedium, fontSize = 20.sp)
        }
    }
}

@Composable
fun ReportItemCard(report: ReportItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Date: ${report.date}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Income: $${"%.2f".format(report.income)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Working Hours: ${"%.2f".format(report.workingHours)} hours", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Preview function with sample data for the report screen
@Composable
fun ReportsScreenPreview() {
    val sampleReports = listOf(
        ReportItem("2024-10-01", 800.0, 9.0),
        ReportItem("2024-10-02", 850.0, 9.5),
        ReportItem("2024-10-03", 900.0, 10.0)
    )
    val monthlyTotal = 2500.0
    val biWeeklyTotals = listOf(1500.0, 1000.0)
    val totalHours = 70.0

    ReportsScreen(dailyReports = sampleReports, monthlyTotal = monthlyTotal, biWeeklyTotals = biWeeklyTotals, totalHours = totalHours)
}
