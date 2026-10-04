package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.components.NesmatAppShell
import com.example.features.light.LightActivitiesScreen
import com.example.lib.storage.DataExportHelper
import com.example.ui.navigation.Screen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.ai.CalmImageStudioScreen
import com.example.ui.screens.ai.GeminiChatScreen
import com.example.ui.screens.assessment.AssessmentScreen
import com.example.ui.screens.breathe.BreatheScreen
import com.example.ui.screens.breathe.GroundingExerciseScreen
import com.example.ui.screens.explore.ContentDetailScreen
import com.example.ui.screens.explore.ExploreScreen
import com.example.ui.screens.gwaya.GwayaHekayaScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.journal.GratitudeJournalScreen
import com.example.ui.screens.journey.JourneyScreen
import com.example.ui.screens.library.ContentCreatorScreen
import com.example.ui.screens.library.NesmatArticleDetailScreen
import com.example.ui.screens.library.NesmatLibraryScreen
import com.example.ui.screens.now.RightNowScreen
import com.example.ui.screens.now.RightNowStateDetailScreen
import com.example.ui.screens.oasis.SoundOasisScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.profile.AboutScreen
import com.example.ui.screens.profile.NotificationsScreen
import com.example.ui.screens.profile.PrivacyScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.profile.SavedContentScreen
import com.example.ui.screens.support.SupportScreen
import com.example.ui.screens.welcome.WelcomeScreen
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.NesmatTheme
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import com.example.ui.viewmodel.NesmatViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  private val viewModel: NesmatViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      NesmatTheme {
        // Enforce Arabic RTL Direction as specified in design specs (Page 2)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
          MainApp(viewModel = viewModel)
        }
      }
    }
  }
}

@Composable
fun MainApp(viewModel: NesmatViewModel) {
  val userPref by viewModel.userPreferences.collectAsState()
  val suggestedContent by viewModel.suggestedContent.collectAsState()
  val filteredContent by viewModel.filteredContent.collectAsState()
  val allAdminContent by viewModel.allAdminContent.collectAsState()
  val favoriteContent by viewModel.favoriteContent.collectAsState()
  val todayCheckin by viewModel.todayCheckin.collectAsState()
  val allCheckins by viewModel.allCheckins.collectAsState()
  val allNotes by viewModel.allNotes.collectAsState()
  val allAssessments by viewModel.allAssessments.collectAsState()
  val allSupportRequests by viewModel.allSupportRequests.collectAsState()
  val selectedContentId by viewModel.selectedContentId.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val notifications by viewModel.notifications.collectAsState()
  val chatMessages by viewModel.chatMessages.collectAsState()
  val isChatLoading by viewModel.isChatLoading.collectAsState()
  val isTranscribing by viewModel.isTranscribing.collectAsState()
  val generatedCalmImage by viewModel.generatedCalmImage.collectAsState()
  val isImageGenerating by viewModel.isImageGenerating.collectAsState()
  val mindfulMomentsCount by viewModel.mindfulMomentsCount.collectAsState()
  val todayHabits by viewModel.todayHabits.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val context = androidx.compose.ui.platform.LocalContext.current

  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route
  val currentScreen = remember(currentRoute) { Screen.fromRoute(currentRoute) }

  // Auto-advance if onboarding already completed in past session
  val hasCompletedOnboarding = userPref?.hasCompletedOnboarding == true
  LaunchedEffect(hasCompletedOnboarding) {
    if (hasCompletedOnboarding && currentScreen == Screen.Welcome) {
      navController.navigate(Screen.Home.route) {
        popUpTo(Screen.Welcome.route) { inclusive = true }
        launchSingleTop = true
      }
    }
  }

  fun navigateToTopLevel(target: Screen) {
    navController.navigate(target.route) {
      popUpTo(Screen.Home.route) {
        saveState = true
      }
      launchSingleTop = true
      restoreState = true
    }
  }

  fun navigateToScreen(target: Screen) {
    navController.navigate(target.route) {
      launchSingleTop = true
    }
  }

  NesmatAppShell(
    currentScreen = currentScreen,
    snackbarHostState = snackbarHostState,
    onNavigate = { targetScreen ->
      navigateToTopLevel(targetScreen)
    }
  ) {
    NavHost(
      navController = navController,
      startDestination = Screen.Welcome.route,
      enterTransition = { fadeIn() },
      exitTransition = { fadeOut() },
      popEnterTransition = { fadeIn() },
      popExitTransition = { fadeOut() }
    ) {
      composable(Screen.Welcome.route) {
        WelcomeScreen(
          onStartJourney = { navigateToScreen(Screen.Onboarding) },
          onAboutClick = { navigateToScreen(Screen.About) }
        )
      }

      composable(Screen.Onboarding.route) {
        OnboardingScreen(
          onComplete = { choices ->
            viewModel.saveOnboarding(choices)
            navController.navigate(Screen.Home.route) {
              popUpTo(Screen.Welcome.route) { inclusive = true }
              launchSingleTop = true
            }
            scope.launch {
              snackbarHostState.showSnackbar("أهلاً بك في مساحتك الآمنة 🌱")
            }
          }
        )
      }

      composable(Screen.Home.route) {
        HomeScreen(
          todayCheckin = todayCheckin,
          suggestedContent = suggestedContent,
          mindfulMomentsCount = mindfulMomentsCount,
          onRecordMood = { mood, note ->
            viewModel.recordDailyCheckin(mood, note)
            scope.launch {
              snackbarHostState.showSnackbar("تم تسجيل حالتك لليوم 🤍")
            }
          },
          onNavigateToExplore = { navigateToScreen(Screen.Explore) },
          onNavigateToLibrary = { navigateToTopLevel(Screen.NesmatLibrary) },
          onNavigateToRightNow = { navigateToScreen(Screen.RightNow) },
          onSaveNesmatToday = { title, content ->
            viewModel.addNote(title, content)
            scope.launch {
              snackbarHostState.showSnackbar("تم حفظ إجابتك في «نسمة اليوم» داخل رحلتي 🌱")
            }
          },
          onNavigateToSupport = { navigateToScreen(Screen.Support) },
          onNavigateToAssessment = { navigateToScreen(Screen.Assessment) },
          onNavigateToBreathe = { navigateToScreen(Screen.Breathe) },
          onNavigateToOasis = { navigateToScreen(Screen.SoundOasis) },
          onNavigateToGwaya = { navigateToScreen(Screen.GwayaHekaya) },
          onNavigateToLight = { navigateToTopLevel(Screen.LightActivities) },
          onNavigateToChat = { navigateToScreen(Screen.GeminiChat) },
          onNavigateToImageStudio = { navigateToScreen(Screen.ImageStudio) },
          onContentClick = { id ->
            viewModel.selectContent(id)
            navigateToScreen(Screen.ContentDetail)
          },
          onToggleFavorite = { id, currentFav ->
            viewModel.toggleFavorite(id, currentFav)
          },
          onWisdomCopied = {
            scope.launch {
              snackbarHostState.showSnackbar("تم نسخ الحكمة إلى الحافظة ✨")
            }
          },
          todayHabits = todayHabits,
          onToggleHabit = { id, comp -> viewModel.toggleHabit(id, comp) },
          onAddCustomHabit = { t, em -> viewModel.addCustomHabit(t, em) },
          onDeleteHabit = { id -> viewModel.deleteHabit(id) },
          onNavigateToGrounding = { navigateToScreen(Screen.Grounding) },
          onNavigateToGratitude = { navigateToScreen(Screen.GratitudeJournal) },
          onNavigateToContentCreator = { navigateToScreen(Screen.ContentCreator) }
        )
      }

      composable(Screen.Explore.route) {
        ExploreScreen(
          contentList = filteredContent,
          searchQuery = searchQuery,
          selectedCategory = selectedCategory,
          onSearchChange = { viewModel.updateSearchQuery(it) },
          onCategorySelect = { viewModel.selectCategory(it) },
          onContentClick = { id ->
            viewModel.selectContent(id)
            navigateToScreen(Screen.ContentDetail)
          },
          onToggleFavorite = { id, currentFav ->
            viewModel.toggleFavorite(id, currentFav)
          },
          onNavigateToLibrary = { navigateToTopLevel(Screen.NesmatLibrary) }
        )
      }

      composable(Screen.NesmatLibrary.route) {
        NesmatLibraryScreen(
          onOpenArticle = { articleId ->
            navController.navigate(Screen.NesmatArticleDetail.createRoute(articleId)) {
              launchSingleTop = true
            }
          },
          onSaveToJourney = { title, content ->
            viewModel.addNote(title, content)
            scope.launch {
              snackbarHostState.showSnackbar("تم حفظ التمرين محليًا في «رحلتي» 🌿")
            }
          },
          onOpenWorkshopsAndAudio = { navigateToScreen(Screen.Explore) },
          onNavigateToContentCreator = { navigateToScreen(Screen.ContentCreator) },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Home)
            }
          }
        )
      }

      composable(Screen.RightNow.route) {
        RightNowScreen(
          onSelectStateRoute = { stateId ->
            navController.navigate(Screen.RightNowStateDetail.createRoute(stateId)) {
              launchSingleTop = true
            }
          },
          onSaveToJourney = { title, content ->
            viewModel.addNote(title, content)
            scope.launch {
              snackbarHostState.showSnackbar("تم حفظ خطوتك محليًا في «رحلتي» 🤍")
            }
          },
          onOpenRelatedArticle = { articleId ->
            navController.navigate(Screen.NesmatArticleDetail.createRoute(articleId)) {
              launchSingleTop = true
            }
          },
          onNavigateToSupport = { navigateToScreen(Screen.Support) },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Home)
            }
          }
        )
      }

      composable(
        route = Screen.RightNowStateDetail.route,
        arguments = listOf(navArgument("stateId") { type = NavType.StringType })
      ) { backStackEntry ->
        val stateId = backStackEntry.arguments?.getString("stateId") ?: "suffocated"
        RightNowStateDetailScreen(
          stateId = stateId,
          onSaveToJourney = { title, content ->
            viewModel.addNote(title, content)
            scope.launch {
              snackbarHostState.showSnackbar("تم حفظ خطوتك محليًا في «رحلتي» 🤍")
            }
          },
          onOpenRelatedArticle = { articleId ->
            navController.navigate(Screen.NesmatArticleDetail.createRoute(articleId)) {
              launchSingleTop = true
            }
          },
          onNavigateToSupport = { navigateToScreen(Screen.Support) },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToScreen(Screen.RightNow)
            }
          }
        )
      }

      composable(
        route = Screen.NesmatArticleDetail.route,
        arguments = listOf(navArgument("articleId") { type = NavType.IntType })
      ) { backStackEntry ->
        val articleId = backStackEntry.arguments?.getInt("articleId") ?: 1
        NesmatArticleDetailScreen(
          articleId = articleId,
          onSaveExerciseLocally = { title, content ->
            viewModel.addNote(title, content)
            scope.launch {
              snackbarHostState.showSnackbar("تم حفظ التمرين محليًا في «رحلتي» 🤍")
            }
          },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.NesmatLibrary)
            }
          }
        )
      }

      composable(Screen.ContentDetail.route) {
        val selectedItem = filteredContent.find { it.id == selectedContentId }
          ?: allAdminContent.find { it.id == selectedContentId }
        ContentDetailScreen(
          item = selectedItem,
          audioPlayer = viewModel.audioPlayer,
          onBack = {
            viewModel.audioPlayer.stop()
            if (!navController.popBackStack()) {
              navigateToScreen(Screen.Explore)
            }
          },
          onToggleFavorite = { id, currentFav ->
            viewModel.toggleFavorite(id, currentFav)
          },
          onNavigateToSupport = { navigateToScreen(Screen.Support) },
          onSaveWorkshopNote = { title, content ->
            viewModel.addNote(title, content)
            scope.launch {
              snackbarHostState.showSnackbar("تم حفظ مخرج الورشة في «رحلتي» بنجاح 🌿")
            }
          }
        )
      }

      composable(Screen.Assessment.route) {
        AssessmentScreen(
          onSaveResult = { result, onDone ->
            viewModel.saveAssessment(result, onDone)
          },
          onNavigateToExplore = { navigateToScreen(Screen.Explore) },
          onNavigateToSupport = { navigateToScreen(Screen.Support) },
          onBackToHome = { navigateToTopLevel(Screen.Home) }
        )
      }

      composable(Screen.Breathe.route) {
        BreatheScreen(
          audioPlayer = viewModel.audioPlayer,
          onBack = {
            viewModel.audioPlayer.stop()
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Home)
            }
          }
        )
      }

      composable(Screen.LightActivities.route) {
        LightActivitiesScreen(
          onNavigateToBreathe = { navigateToScreen(Screen.Breathe) },
          onNavigateToGwayaHekaya = { navigateToScreen(Screen.GwayaHekaya) },
          onBack = { navigateToTopLevel(Screen.Home) }
        )
      }

      composable(Screen.GwayaHekaya.route) {
        GwayaHekayaScreen(
          onSaveToJourney = { title, content ->
            viewModel.addNote(title, content)
            scope.launch {
              snackbarHostState.showSnackbar("تم حفظ رحلة «جوايا حكاية» في دفتر رحلتك 🤍")
            }
          },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.LightActivities)
            }
          }
        )
      }

      composable(Screen.Support.route) {
        SupportScreen(
          requests = allSupportRequests,
          onSubmitRequest = { type, message, onDone ->
            viewModel.submitSupportRequest(type, message, onDone)
          },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Home)
            }
          }
        )
      }

      composable(Screen.Journey.route) {
        val daysCount = viewModel.getCheckinDaysThisWeek()
        JourneyScreen(
          checkins = allCheckins,
          notes = allNotes,
          assessments = allAssessments,
          daysCheckedInThisWeek = daysCount,
          onAddNote = { title, content ->
            viewModel.addNote(title, content)
            scope.launch {
              snackbarHostState.showSnackbar("تم حفظ الخاطرة بنجاح ✍️")
            }
          },
          onDeleteNote = { viewModel.deleteNote(it) },
          onDeleteCheckin = { viewModel.deleteCheckin(it) },
          onDeleteAssessment = { viewModel.deleteAssessment(it) },
          onOpenLibraryArticle = { articleId ->
            navController.navigate(Screen.NesmatArticleDetail.createRoute(articleId)) {
              launchSingleTop = true
            }
          }
        )
      }

      composable(Screen.Profile.route) {
        ProfileScreen(
          userPref = userPref,
          onNavigateToJourney = { navigateToTopLevel(Screen.Journey) },
          onNavigateToSupport = { navigateToScreen(Screen.Support) },
          onNavigateToSaved = { navigateToScreen(Screen.Saved) },
          onNavigateToNotifications = { navigateToScreen(Screen.Notifications) },
          onNavigateToPrivacy = { navigateToScreen(Screen.Privacy) },
          onNavigateToAbout = { navigateToScreen(Screen.About) },
          onNavigateToAdmin = { navigateToScreen(Screen.AdminDashboard) },
          onNavigateToChat = { navigateToScreen(Screen.GeminiChat) },
          onNavigateToImageStudio = { navigateToScreen(Screen.ImageStudio) },
          onExportData = {
            DataExportHelper.shareExportedData(
              context = context,
              checkins = allCheckins,
              notes = allNotes,
              assessments = allAssessments
            )
          },
          onClearAllData = {
            viewModel.eraseAllData {
              navController.navigate(Screen.Welcome.route) {
                popUpTo(0) { inclusive = true }
              }
              scope.launch {
                snackbarHostState.showSnackbar("تم مسح جميع بياناتك بنجاح")
              }
            }
          }
        )
      }

      composable(Screen.GeminiChat.route) {
        GeminiChatScreen(
          messages = chatMessages,
          isLoading = isChatLoading,
          isTranscribing = isTranscribing,
          audioRecorder = viewModel.audioRecorder,
          onSendMessage = { text, useSearch ->
            viewModel.sendChatMessage(text, useSearch)
          },
          onTranscribeAudio = { bytes, onDone ->
            viewModel.transcribeAudio(bytes, onDone)
          },
          onClearChat = { viewModel.clearChat() },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Home)
            }
          }
        )
      }

      composable(Screen.ImageStudio.route) {
        CalmImageStudioScreen(
          generatedImage = generatedCalmImage,
          isGenerating = isImageGenerating,
          onGenerateImage = { prompt, ref ->
            viewModel.generateCalmingImage(prompt, ref)
          },
          onClearImage = { viewModel.clearGeneratedImage() },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Home)
            }
          }
        )
      }

      composable(Screen.SoundOasis.route) {
        SoundOasisScreen(
          oasisPlayer = viewModel.soundOasisPlayer,
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Home)
            }
          }
        )
      }

      composable(Screen.Saved.route) {
        SavedContentScreen(
          favoriteItems = favoriteContent,
          onContentClick = { id ->
            viewModel.selectContent(id)
            navigateToScreen(Screen.ContentDetail)
          },
          onToggleFavorite = { id, currentFav ->
            viewModel.toggleFavorite(id, currentFav)
          },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Profile)
            }
          }
        )
      }

      composable(Screen.Notifications.route) {
        NotificationsScreen(
          notifications = notifications,
          gentleRemindersEnabled = userPref?.gentleReminderEnabled ?: true,
          onToggleGentleReminders = { viewModel.toggleGentleReminders(it) },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Profile)
            }
          }
        )
      }

      composable(Screen.Privacy.route) {
        PrivacyScreen(
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Profile)
            }
          }
        )
      }

      composable(Screen.About.route) {
        AboutScreen(
          onBack = {
            if (!navController.popBackStack()) {
              if (hasCompletedOnboarding) navigateToTopLevel(Screen.Profile)
              else navigateToScreen(Screen.Welcome)
            }
          }
        )
      }

      composable(Screen.AdminDashboard.route) {
        AdminDashboardScreen(
          userPref = userPref,
          allContent = allAdminContent,
          allRequests = allSupportRequests,
          onUpdateRole = { viewModel.updateUserRole(it) },
          onSaveContent = { item, onDone ->
            viewModel.saveContentItem(item, onDone)
          },
          onDeleteContent = { viewModel.deleteContentItem(it) },
          onUpdateRequestStatus = { id, newStatus, reply ->
            viewModel.updateSupportRequestStatus(id, newStatus, reply)
          },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Profile)
            }
          }
        )
      }

      composable(Screen.Grounding.route) {
        GroundingExerciseScreen(
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Home)
            }
          },
          onCompleteExercise = {
            viewModel.recordDailyCheckin("كويس", "أتممت تمرين التأريض الحسي 5-4-3-2-1 🌱")
            scope.launch {
              snackbarHostState.showSnackbar("أحسنت! تمت إضافة نقطة يقظة ذهنية لشجرتك 🌿")
            }
          }
        )
      }

      composable(Screen.GratitudeJournal.route) {
        val allNotes by viewModel.allNotes.collectAsState()
        GratitudeJournalScreen(
          notes = allNotes,
          onAddNote = { title, content, tag, moodEmoji ->
            viewModel.addGratitudeNote(title, content, tag, moodEmoji) {
              scope.launch {
                snackbarHostState.showSnackbar("تم حفظ الخاطرة في مفكرتك ✨")
              }
            }
          },
          onDeleteNote = { id -> viewModel.deleteNote(id) },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.Home)
            }
          }
        )
      }

      composable(Screen.ContentCreator.route) {
        ContentCreatorScreen(
          onPublish = { title, category, description, body, duration, author ->
            viewModel.publishNewArticle(title, category, description, body, duration, author) {
              scope.launch {
                snackbarHostState.showSnackbar("تم نشر المحتوى بنجاح في المكتبة 🎉")
              }
            }
          },
          onBack = {
            if (!navController.popBackStack()) {
              navigateToTopLevel(Screen.NesmatLibrary)
            }
          }
        )
      }
    }
  }
  }
