package com.example.salarycal.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Settings(
    val startHour: Int = 7,
    val workingHours: Int = 8,
    val hourlyRate: Double = 100.0,
    val extraHourlyRate: Double = 150.0,
    val currency: String = "USD"
)

data class DailyRecord(
    val date: String = "",
    val totalEarnings: Double = 0.0,
    val hoursWorked: Double = 0.0
)

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun saveSettings(settings: Settings) {
        db.collection("settings").document("userSettings").set(settings).await()
    }

    suspend fun loadSettings(): Settings? {
        val doc = db.collection("settings").document("userSettings").get().await()
        return doc.toObject(Settings::class.java)
    }

    suspend fun addDailyRecord(record: DailyRecord) {
        db.collection("dailyRecords").add(record).await()
    }

    suspend fun getDailyRecords(): List<DailyRecord> {
        val docs = db.collection("dailyRecords").get().await()
        return docs.documents.mapNotNull { it.toObject(DailyRecord::class.java) }
    }
}
