package com.example.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.Gamepad
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.Screen
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.TextDark

data class BottomNavItem(
  val screen: Screen,
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String
)

@Composable
fun NesmatBottomNav(
  currentScreen: Screen,
  onNavigate: (Screen) -> Unit,
  modifier: Modifier = Modifier
) {
  val items = listOf(
    BottomNavItem(
      screen = Screen.Home,
      label = "الرئيسية",
      selectedIcon = Icons.Filled.Home,
      unselectedIcon = Icons.Outlined.Home,
      testTag = "nav_item_home"
    ),
    BottomNavItem(
      screen = Screen.NesmatLibrary,
      label = "المكتبة",
      selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
      unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
      testTag = "nav_item_library"
    ),
    BottomNavItem(
      screen = Screen.LightActivities,
      label = "خفة ولعب",
      selectedIcon = Icons.Filled.Gamepad,
      unselectedIcon = Icons.Outlined.Gamepad,
      testTag = "nav_item_light"
    ),
    BottomNavItem(
      screen = Screen.Journey,
      label = "حياتي",
      selectedIcon = Icons.Filled.Spa,
      unselectedIcon = Icons.Outlined.Spa,
      testTag = "nav_item_journey"
    ),
    BottomNavItem(
      screen = Screen.Profile,
      label = "حسابي",
      selectedIcon = Icons.Filled.Person,
      unselectedIcon = Icons.Outlined.Person,
      testTag = "nav_item_account"
    )
  )

  NavigationBar(
    containerColor = Color.White,
    tonalElevation = 6.dp,
    modifier = modifier
  ) {
    items.forEach { item ->
      val isSelected = currentScreen == item.screen
      NavigationBarItem(
        selected = isSelected,
        onClick = { onNavigate(item.screen) },
        icon = {
          Icon(
            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.label,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = item.label,
            fontSize = 11.sp
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = Color.White,
          selectedTextColor = DarkGreen,
          indicatorColor = DarkGreen,
          unselectedIconColor = TextDark.copy(alpha = 0.6f),
          unselectedTextColor = TextDark.copy(alpha = 0.6f)
        ),
        modifier = Modifier.testTag(item.testTag)
      )
    }
  }
}
