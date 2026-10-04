package com.example.ui.navigation

sealed class Screen(val route: String) {
  object Welcome : Screen("welcome")
  object Onboarding : Screen("onboarding")
  object Home : Screen("home")
  object Explore : Screen("explore")
  object ContentDetail : Screen("content_detail")
  object Assessment : Screen("assessment")
  object Breathe : Screen("breathe")
  object LightActivities : Screen("light_activities")
  object Journey : Screen("journey")
  object Support : Screen("support")
  object Profile : Screen("profile")
  object Saved : Screen("saved")
  object Notifications : Screen("notifications")
  object Privacy : Screen("privacy")
  object About : Screen("about")
  object AdminDashboard : Screen("admin_dashboard")
  object GeminiChat : Screen("gemini_chat")
  object ImageStudio : Screen("image_studio")
  object SoundOasis : Screen("sound_oasis")
  object GwayaHekaya : Screen("gwaya_hekaya")
  object NesmatLibrary : Screen("nesmat_library")
  object RightNow : Screen("right_now")
  object RightNowStateDetail : Screen("right_now/{stateId}") {
    const val BASE_ROUTE = "right_now"
    fun createRoute(stateId: String) = "$BASE_ROUTE/$stateId"
  }
  object NesmatArticleDetail : Screen("nesmat_article_detail/{articleId}") {
    const val BASE_ROUTE = "nesmat_article_detail"
    fun createRoute(articleId: Int) = "$BASE_ROUTE/$articleId"
  }
  object Grounding : Screen("grounding")
  object GratitudeJournal : Screen("gratitude_journal")
  object ContentCreator : Screen("content_creator")

  companion object {
    fun fromRoute(route: String?): Screen = when {
      route == Welcome.route -> Welcome
      route == Onboarding.route -> Onboarding
      route == Home.route -> Home
      route == Explore.route -> Explore
      route == ContentDetail.route -> ContentDetail
      route == Assessment.route -> Assessment
      route == Breathe.route -> Breathe
      route == LightActivities.route -> LightActivities
      route == Journey.route -> Journey
      route == Support.route -> Support
      route == Profile.route -> Profile
      route == Saved.route -> Saved
      route == Notifications.route -> Notifications
      route == Privacy.route -> Privacy
      route == About.route -> About
      route == AdminDashboard.route -> AdminDashboard
      route == GeminiChat.route -> GeminiChat
      route == ImageStudio.route -> ImageStudio
      route == SoundOasis.route -> SoundOasis
      route == GwayaHekaya.route -> GwayaHekaya
      route == NesmatLibrary.route -> NesmatLibrary
      route == RightNow.route -> RightNow
      route == Grounding.route -> Grounding
      route == GratitudeJournal.route -> GratitudeJournal
      route == ContentCreator.route -> ContentCreator
      route?.startsWith("right_now/") == true -> RightNowStateDetail
      route?.startsWith(NesmatArticleDetail.BASE_ROUTE) == true -> NesmatArticleDetail
      else -> Home
    }
  }
}
