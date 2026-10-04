package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.ChatMessage
import com.example.data.api.GeminiApiService
import com.example.data.local.NesmatDatabase
import com.example.data.model.AssessmentResult
import com.example.data.model.ContentItem
import com.example.data.model.DailyCheckin
import com.example.data.model.PersonalNote
import com.example.data.model.SupportRequest
import com.example.data.model.UserPreference
import com.example.data.model.WellnessHabit
import com.example.data.repository.NesmatRepository
import com.example.util.AmbientAudioPlayer
import com.example.util.AudioRecorderHelper
import com.example.util.SoundOasisPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class UiNotification(
  val id: Long = System.currentTimeMillis(),
  val title: String,
  val message: String,
  val time: String = "الآن"
)

class NesmatViewModel(application: Application) : AndroidViewModel(application) {
  private val database = NesmatDatabase.getDatabase(application, viewModelScope)
  val repository = NesmatRepository(database)
  val audioPlayer = AmbientAudioPlayer(viewModelScope)
  val soundOasisPlayer = SoundOasisPlayer(viewModelScope)
  val geminiService = GeminiApiService()
  val audioRecorder = AudioRecorderHelper(application)

  val todayHabits: StateFlow<List<WellnessHabit>> = repository.getTodayHabits().stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  init {
    viewModelScope.launch {
      repository.ensureDefaultHabitsForToday()
    }
  }

  val mindfulMomentsCount: StateFlow<Int> = combine(
    repository.allCheckins,
    repository.allNotes,
    repository.allAssessments,
    todayHabits
  ) { checkins: List<DailyCheckin>, notes: List<PersonalNote>, assessments: List<AssessmentResult>, habits: List<WellnessHabit> ->
    val completedHabits = habits.count { it.isCompleted }
    (checkins.size + notes.size + assessments.size + completedHabits).coerceAtLeast(1)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

  // Gemini AI Chat State (gemini-3.5-flash with Search Grounding)
  private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
    listOf(
      ChatMessage(
        role = "model",
        text = "أهلاً بك 🌿 أنا «رفيق نسمة الحياة»، مساحتك الهادئة للاستماع والتأمل الذاتي بدون أحكام. كيف تشعر اليوم، أو ما الذي تود الحديث عنه؟"
      )
    )
  )
  val chatMessages = _chatMessages.asStateFlow()

  private val _isChatLoading = MutableStateFlow(false)
  val isChatLoading = _isChatLoading.asStateFlow()

  // Gemini AI Calming Image State (gemini-3.1-flash-image-preview)
  private val _generatedCalmImage = MutableStateFlow<Bitmap?>(null)
  val generatedCalmImage = _generatedCalmImage.asStateFlow()

  private val _isImageGenerating = MutableStateFlow(false)
  val isImageGenerating = _isImageGenerating.asStateFlow()

  // Audio Transcription State (gemini-3.5-transcribe)
  private val _isTranscribing = MutableStateFlow(false)
  val isTranscribing = _isTranscribing.asStateFlow()

  private val _transcribedText = MutableStateFlow<String?>(null)
  val transcribedText = _transcribedText.asStateFlow()

  // User Preferences
  val userPreferences: StateFlow<UserPreference?> = repository.userPreferences.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = null
  )

  // Content
  val publishedContent: StateFlow<List<ContentItem>> = repository.publishedContent.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val allAdminContent: StateFlow<List<ContentItem>> = repository.allAdminContent.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val favoriteContent: StateFlow<List<ContentItem>> = repository.favoriteContent.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val suggestedContent: StateFlow<List<ContentItem>> = repository.suggestedContent.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // Search & Filter
  private val _searchQuery = MutableStateFlow("")
  val searchQuery = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("الكل")
  val selectedCategory = _selectedCategory.asStateFlow()

  val filteredContent: StateFlow<List<ContentItem>> = combine(
    publishedContent,
    searchQuery,
    selectedCategory
  ) { items, query, category ->
    items.filter { item ->
      val matchesCategory = (category == "الكل" || item.category == category)
      val matchesQuery = query.isBlank() ||
        item.title.contains(query, ignoreCase = true) ||
        item.description.contains(query, ignoreCase = true) ||
        item.body.contains(query, ignoreCase = true)
      matchesCategory && matchesQuery
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // Checkins
  val todayCheckin: StateFlow<DailyCheckin?> = repository.getTodayCheckin().stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = null
  )

  val allCheckins: StateFlow<List<DailyCheckin>> = repository.allCheckins.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // Notes
  val allNotes: StateFlow<List<PersonalNote>> = repository.allNotes.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // Assessments
  val allAssessments: StateFlow<List<AssessmentResult>> = repository.allAssessments.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // Support Requests
  val allSupportRequests: StateFlow<List<SupportRequest>> = repository.allSupportRequests.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // Selected Content for Detail view
  private val _selectedContentId = MutableStateFlow<Long?>(null)
  val selectedContentId = _selectedContentId.asStateFlow()

  // In-app notifications
  private val _notifications = MutableStateFlow(
    listOf(
      UiNotification(
        title = "مرحباً بك في نسمة الحياة 🌿",
        message = "مساحتك الآمنة للتوعية وفهم الذات بهدوء ودفء.",
        time = "اليوم"
      ),
      UiNotification(
        title = "تذكير لطيف 🌱",
        message = "حابب تاخد دقيقتين لنفسك النهارده مع تمرين تنفس هادئ؟",
        time = "أمس"
      )
    )
  )
  val notifications = _notifications.asStateFlow()

  // Onboarding actions
  fun saveOnboarding(selectedChoices: List<String>) {
    viewModelScope.launch {
      repository.completeOnboarding(selectedChoices)
    }
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun selectCategory(category: String) {
    _selectedCategory.value = category
  }

  fun selectContent(id: Long?) {
    _selectedContentId.value = id
  }

  fun toggleFavorite(id: Long, currentFav: Boolean) {
    viewModelScope.launch {
      repository.toggleFavorite(id, !currentFav)
    }
  }

  fun recordDailyCheckin(moodValue: String, note: String? = null) {
    viewModelScope.launch {
      repository.recordCheckin(moodValue, note)
    }
  }

  fun deleteCheckin(id: Long) {
    viewModelScope.launch {
      repository.deleteCheckin(id)
    }
  }

  fun addNote(title: String, content: String) {
    viewModelScope.launch {
      repository.addNote(title, content)
    }
  }

  fun addGratitudeNote(
    title: String,
    content: String,
    tag: String = "امتنان",
    moodEmoji: String = "🌸",
    onComplete: (() -> Unit)? = null
  ) {
    viewModelScope.launch {
      repository.addNote(title, content, tag, moodEmoji)
      onComplete?.invoke()
    }
  }

  fun toggleHabit(id: Long, completed: Boolean) {
    viewModelScope.launch {
      repository.toggleHabit(id, completed)
    }
  }

  fun addCustomHabit(title: String, iconEmoji: String = "🌱") {
    viewModelScope.launch {
      repository.addCustomHabit(title, iconEmoji)
    }
  }

  fun deleteHabit(id: Long) {
    viewModelScope.launch {
      repository.deleteHabit(id)
    }
  }

  fun publishNewArticle(
    title: String,
    category: String,
    description: String,
    body: String,
    duration: String,
    author: String = "فريق نسمة الحياة",
    reviewer: String = "أخصائي نفسي معتمد",
    contentType: String = "article",
    onComplete: () -> Unit
  ) {
    viewModelScope.launch {
      val newItem = ContentItem(
        title = title,
        description = description,
        body = body,
        category = category,
        contentType = contentType,
        duration = duration,
        author = author,
        reviewer = reviewer,
        reviewStatus = "Published",
        publishedAt = System.currentTimeMillis(),
        isFavorite = false,
        isSuggested = true
      )
      repository.saveContentItem(newItem)
      onComplete()
    }
  }

  fun deleteNote(id: Long) {
    viewModelScope.launch {
      repository.deleteNote(id)
    }
  }

  fun submitSupportRequest(requestType: String, message: String, onDone: (SupportRequest) -> Unit) {
    viewModelScope.launch {
      val req = repository.submitSupportRequest(requestType, message)
      onDone(req)
    }
  }

  fun updateSupportRequestStatus(id: Long, newStatus: String, adminReply: String? = null) {
    viewModelScope.launch {
      val req = allSupportRequests.value.find { it.id == id }
      if (req != null) {
        val updated = req.copy(
          status = newStatus,
          adminReply = adminReply ?: req.adminReply,
          updatedAt = System.currentTimeMillis()
        )
        repository.updateSupportRequest(updated)
      }
    }
  }

  fun saveAssessment(result: AssessmentResult, onComplete: () -> Unit) {
    viewModelScope.launch {
      repository.saveAssessmentResult(result)
      onComplete()
    }
  }

  fun deleteAssessment(id: Long) {
    viewModelScope.launch {
      repository.deleteAssessment(id)
    }
  }

  fun saveContentItem(item: ContentItem, onComplete: () -> Unit) {
    viewModelScope.launch {
      repository.saveContentItem(item)
      onComplete()
    }
  }

  fun deleteContentItem(item: ContentItem) {
    viewModelScope.launch {
      repository.deleteContentItem(item)
    }
  }

  fun updateUserRole(role: String) {
    viewModelScope.launch {
      val curr = userPreferences.value ?: UserPreference()
      repository.updatePreferences(curr.copy(userRole = role))
    }
  }

  fun toggleSpiritualContent(enabled: Boolean) {
    viewModelScope.launch {
      val curr = userPreferences.value ?: UserPreference()
      repository.updatePreferences(curr.copy(spiritualContentEnabled = enabled))
    }
  }

  fun toggleGentleReminders(enabled: Boolean) {
    viewModelScope.launch {
      val curr = userPreferences.value ?: UserPreference()
      repository.updatePreferences(curr.copy(gentleReminderEnabled = enabled))
    }
  }

  fun eraseAllData(onComplete: () -> Unit) {
    viewModelScope.launch {
      repository.eraseAllUserData()
      onComplete()
    }
  }

  // Calculate descriptive checkin count for this week (Page 19: "سجلت حالتك 5 أيام هذا الأسبوع")
  fun getCheckinDaysThisWeek(): Int {
    val checkins = allCheckins.value
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    val startOfWeekMillis = calendar.timeInMillis
    return checkins.filter { it.timestamp >= startOfWeekMillis }.map { it.date }.distinct().size
  }

  // AI Chat Actions
  fun sendChatMessage(userText: String, useSearchGrounding: Boolean = true) {
    if (userText.isBlank()) return
    val userMsg = ChatMessage(role = "user", text = userText)
    val currentHistory = _chatMessages.value
    _chatMessages.value = currentHistory + userMsg
    _isChatLoading.value = true

    viewModelScope.launch {
      val result = geminiService.sendChatMessage(
        history = currentHistory,
        newMessage = userText,
        useSearchGrounding = useSearchGrounding
      )

      val modelMsg = ChatMessage(
        role = "model",
        text = result.text,
        isGroundedWithSearch = result.searchSources.isNotEmpty(),
        searchSources = result.searchSources
      )
      _chatMessages.value = _chatMessages.value + modelMsg
      _isChatLoading.value = false
    }
  }

  fun clearChat() {
    _chatMessages.value = listOf(
      ChatMessage(
        role = "model",
        text = "أهلاً بك مجدداً 🌿 أنا معك للاستماع وتقديم مساحة للتأمل والهدوء."
      )
    )
  }

  // AI Image Studio Actions (gemini-3.1-flash-image-preview)
  fun generateCalmingImage(prompt: String, referenceBitmap: Bitmap? = null) {
    if (prompt.isBlank()) return
    _isImageGenerating.value = true
    viewModelScope.launch {
      val bitmap = geminiService.generateCalmingImage(prompt, referenceBitmap)
      _generatedCalmImage.value = bitmap
      _isImageGenerating.value = false
    }
  }

  fun clearGeneratedImage() {
    _generatedCalmImage.value = null
  }

  // Audio Transcription Actions (gemini-3.5-transcribe)
  fun transcribeAudio(audioBytes: ByteArray, onDone: (String) -> Unit) {
    _isTranscribing.value = true
    viewModelScope.launch {
      val text = geminiService.transcribeAudio(audioBytes)
      _transcribedText.value = text
      _isTranscribing.value = false
      onDone(text)
    }
  }

  fun clearTranscribedText() {
    _transcribedText.value = null
  }

  override fun onCleared() {
    super.onCleared()
    audioPlayer.stop()
    soundOasisPlayer.stop()
    audioRecorder.cancelRecording()
  }
}
