package com.example.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.navigation.Screen
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.NesmatTheme

@Composable
fun NesmatAppShell(
  currentScreen: Screen,
  snackbarHostState: SnackbarHostState,
  onNavigate: (Screen) -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val isBottomNavVisible = currentScreen in listOf(
    Screen.Home,
    Screen.NesmatLibrary,
    Screen.Explore,
    Screen.LightActivities,
    Screen.Journey,
    Screen.Profile
  )

  NesmatTheme {
    // RTL Arabic Layout Direction
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
      Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
          if (isBottomNavVisible) {
            NesmatBottomNav(
              currentScreen = currentScreen,
              onNavigate = onNavigate
            )
          }
        },
        containerColor = CreamBackground,
        modifier = modifier.fillMaxSize()
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          content()
        }
      }
    }
  }
}
