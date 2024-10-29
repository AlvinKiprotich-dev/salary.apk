package com.example.salarycal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ReportItem(val date: String, val totalEarnings: Double, val hoursWorked: Double)

@Composable
fun ReportsScreen(
    dailyReports: List<ReportItem>,
    monthlyTotal: Double,
    biWeeklyTotals: List<Double>,
    totalHours: Double
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Reports", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Total Monthly Earnings: $${"%.2f".format(monthlyTotal)}")
        Text("Total Hours Worked: ${"%.2f".format(totalHours)}")
        Text("Bi-Weekly Totals: ${biWeeklyTotals.joinToString { "$$it" }}")

        Spacer(modifier = Modifier.height(16.dp))
        dailyReports.forEach { report ->
            Text("${report.date}: $${report.totalEarnings} for ${report.hoursWorked} hours")
        }
    }
}
