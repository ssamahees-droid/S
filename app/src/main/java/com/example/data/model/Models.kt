package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_items")
data class ContentItem(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val description: String,
  val body: String,
  val category: String,
  val contentType: String, // "article", "audio", "video", "exercise"
  val duration: String,
  val author: String = "فريق نسمة الحياة",
  val reviewer: String = "أخصائي نفسي معتمد",
  val reviewStatus: String = "Published", // Draft, Review, Approved, Published, Archived
  val publishedAt: Long = System.currentTimeMillis(),
  val isFavorite: Boolean = false,
  val isSuggested: Boolean = false,
  val audioToneType: String = "ambient_nature" // for built-in soothing sound simulation
)

@Entity(tableName = "daily_checkins")
data class DailyCheckin(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val date: String, // YYYY-MM-DD
  val moodValue: String, // "كويس", "مقبول", "مش عارف", "مش كويس", "متعب جداً"
  val timestamp: Long = System.currentTimeMillis(),
  val note: String? = null
)

@Entity(tableName = "personal_notes")
data class PersonalNote(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val content: String,
  val tag: String = "امتنان", // امتنان, عائلة, عمل, صحة, أمل, عام
  val moodEmoji: String = "🌸",
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "wellness_habits")
data class WellnessHabit(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val iconEmoji: String = "🌱",
  val isCompleted: Boolean = false,
  val date: String // YYYY-MM-DD
)

@Entity(tableName = "assessment_results")
data class AssessmentResult(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val assessmentType: String, // "التقييم المبدئي" or "فحص أعراض الاكتئاب (PHQ-9)"
  val score: Int,
  val maxScore: Int,
  val resultTitle: String,
  val resultText: String,
  val recommendation: String,
  val hasSafetyFlag: Boolean = false,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "support_requests")
data class SupportRequest(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val requestNumber: String, // e.g. "#1024"
  val requestType: String,
  val message: String,
  val status: String = "جديد", // جديد, تم الاستلام, قيد المراجعة, تم التواصل, تمت الإحالة, مغلق
  val adminReply: String? = null,
  val assignedTo: String? = null,
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_preferences")
data class UserPreference(
  @PrimaryKey val id: Int = 1,
  val hasCompletedOnboarding: Boolean = false,
  val onboardingChoices: String = "",
  val spiritualContentEnabled: Boolean = true,
  val gentleReminderEnabled: Boolean = true,
  val userRole: String = "مستخدم" // مستخدم, Super Admin, Content Manager, Support Supervisor, Support Member
)
