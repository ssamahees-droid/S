package com.example.data.repository

import com.example.data.local.NesmatDatabase
import com.example.data.model.AssessmentResult
import com.example.data.model.ContentItem
import com.example.data.model.DailyCheckin
import com.example.data.model.PersonalNote
import com.example.data.model.SupportRequest
import com.example.data.model.UserPreference
import com.example.data.model.WellnessHabit
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class NesmatRepository(private val database: NesmatDatabase) {
  private val contentDao = database.contentDao()
  private val checkinDao = database.checkinDao()
  private val noteDao = database.noteDao()
  private val assessmentDao = database.assessmentDao()
  private val supportDao = database.supportDao()
  private val userPrefDao = database.userPrefDao()
  private val habitDao = database.habitDao()

  // Content
  val publishedContent: Flow<List<ContentItem>> = contentDao.getPublishedContent()
  val allAdminContent: Flow<List<ContentItem>> = contentDao.getAllContentForAdmin()
  val favoriteContent: Flow<List<ContentItem>> = contentDao.getFavoriteContent()
  val suggestedContent: Flow<List<ContentItem>> = contentDao.getSuggestedContent()

  fun getContentById(id: Long): Flow<ContentItem?> = contentDao.getContentById(id)
  fun searchContent(query: String): Flow<List<ContentItem>> = contentDao.searchContent(query)

  suspend fun toggleFavorite(id: Long, isFav: Boolean) {
    contentDao.updateFavorite(id, isFav)
  }

  suspend fun saveContentItem(item: ContentItem): Long {
    return if (item.id == 0L) {
      contentDao.insert(item)
    } else {
      contentDao.update(item)
      item.id
    }
  }

  suspend fun deleteContentItem(item: ContentItem) {
    contentDao.delete(item)
  }

  // Daily Check-ins
  val allCheckins: Flow<List<DailyCheckin>> = checkinDao.getAllCheckins()

  fun getTodayCheckin(): Flow<DailyCheckin?> {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return checkinDao.getCheckinByDate(today)
  }

  suspend fun recordCheckin(moodValue: String, note: String? = null) {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val checkin = DailyCheckin(
      date = today,
      moodValue = moodValue,
      note = note?.takeIf { it.isNotBlank() },
      timestamp = System.currentTimeMillis()
    )
    checkinDao.insertCheckin(checkin)
  }

  suspend fun deleteCheckin(id: Long) {
    checkinDao.deleteCheckin(id)
  }

  // Personal Notes
  val allNotes: Flow<List<PersonalNote>> = noteDao.getAllNotes()

  suspend fun addNote(
    title: String,
    content: String,
    tag: String = "امتنان",
    moodEmoji: String = "🌸"
  ) {
    noteDao.insertNote(
      PersonalNote(
        title = title.ifBlank { "خاطرة جديدة" },
        content = content,
        tag = tag,
        moodEmoji = moodEmoji,
        timestamp = System.currentTimeMillis()
      )
    )
  }

  suspend fun deleteNote(id: Long) {
    noteDao.deleteNote(id)
  }

  // Assessments
  val allAssessments: Flow<List<AssessmentResult>> = assessmentDao.getAllAssessments()

  suspend fun saveAssessmentResult(result: AssessmentResult): Long {
    return assessmentDao.insertAssessment(result)
  }

  suspend fun deleteAssessment(id: Long) {
    assessmentDao.deleteAssessment(id)
  }

  // Support Requests
  val allSupportRequests: Flow<List<SupportRequest>> = supportDao.getAllRequests()

  fun getSupportRequestById(id: Long): Flow<SupportRequest?> = supportDao.getRequestById(id)

  suspend fun submitSupportRequest(requestType: String, message: String): SupportRequest {
    val reqNum = "#" + (1000 + Random.nextInt(9000))
    val request = SupportRequest(
      requestNumber = reqNum,
      requestType = requestType,
      message = message,
      status = "جديد",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )
    val id = supportDao.insertRequest(request)
    return request.copy(id = id)
  }

  suspend fun updateSupportStatus(id: Long, newStatus: String, reply: String? = null) {
    val existing = supportDao.getAllRequests() // or fetch directly
    // Let's create an update
    supportDao.updateRequest(
      SupportRequest(
        id = id,
        requestNumber = "#$id",
        requestType = "طلب دعم",
        message = "",
        status = newStatus,
        adminReply = reply,
        updatedAt = System.currentTimeMillis()
      )
    )
  }

  suspend fun updateSupportRequest(request: SupportRequest) {
    supportDao.updateRequest(request.copy(updatedAt = System.currentTimeMillis()))
  }

  // User Preferences
  val userPreferences: Flow<UserPreference?> = userPrefDao.getPreferences()

  suspend fun completeOnboarding(choices: List<String>) {
    val pref = UserPreference(
      id = 1,
      hasCompletedOnboarding = true,
      onboardingChoices = choices.joinToString(","),
      spiritualContentEnabled = true,
      gentleReminderEnabled = true,
      userRole = "مستخدم"
    )
    userPrefDao.savePreferences(pref)
  }

  suspend fun updatePreferences(pref: UserPreference) {
    userPrefDao.savePreferences(pref)
  }

  // Wellness Habits
  fun getTodayHabits(): Flow<List<WellnessHabit>> {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return habitDao.getHabitsForDate(today)
  }

  suspend fun ensureDefaultHabitsForToday() {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val count = habitDao.getHabitsCountForDate(today)
    if (count == 0) {
      val defaultHabits = listOf(
        WellnessHabit(title = "٥ دقائق تنفس هادئ وعميق", iconEmoji = "🌬️", date = today),
        WellnessHabit(title = "شرب كوب ماء بوعي وتأمل", iconEmoji = "💧", date = today),
        WellnessHabit(title = "تدوين سطر امتنان واحد", iconEmoji = "✨", date = today),
        WellnessHabit(title = "مشي خفيف أو حركة ١٠ دقائق", iconEmoji = "🚶", date = today),
        WellnessHabit(title = "لحظة صمت وتصفية الذهن", iconEmoji = "🧘", date = today)
      )
      habitDao.insertAll(defaultHabits)
    }
  }

  suspend fun toggleHabit(id: Long, completed: Boolean) {
    habitDao.toggleHabit(id, completed)
  }

  suspend fun addCustomHabit(title: String, iconEmoji: String = "🌱") {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    habitDao.insertHabit(
      WellnessHabit(
        title = title,
        iconEmoji = iconEmoji,
        isCompleted = false,
        date = today
      )
    )
  }

  suspend fun deleteHabit(id: Long) {
    habitDao.deleteHabit(id)
  }

  // Privacy & Data Erasure (Egyptian Law 151 / Play Health Guidelines)
  suspend fun eraseAllUserData() {
    checkinDao.clearAll()
    noteDao.clearAll()
    assessmentDao.clearAll()
    supportDao.clearAll()
    userPrefDao.clearAll()
    habitDao.clearAll()
  }
}
