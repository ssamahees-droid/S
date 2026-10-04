package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentItem
import com.example.ui.screens.explore.ContentCard
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import com.example.ui.viewmodel.UiNotification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedContentScreen(
  favoriteItems: List<ContentItem>,
  onContentClick: (Long) -> Unit,
  onToggleFavorite: (Long, Boolean) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    TopAppBar(
      title = {
        Text(
          text = "المحفوظات (${favoriteItems.size})",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("saved_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "الرجوع",
            tint = DarkGreen
          )
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      if (favoriteItems.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                tint = SageGreenPrimary,
                modifier = Modifier.size(36.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "لا توجد عناصر محفوظة بعد",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "يمكنك حفظ المقالات والتمارين المفضلة بالضغط على أيقونة الإشارة المرجعية.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark.copy(alpha = 0.7f)
              )
            }
          }
        }
      } else {
        items(favoriteItems, key = { it.id }) { item ->
          ContentCard(
            item = item,
            onClick = { onContentClick(item.id) },
            onToggleFavorite = { onToggleFavorite(item.id, item.isFavorite) }
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
  notifications: List<UiNotification>,
  gentleRemindersEnabled: Boolean,
  onToggleGentleReminders: (Boolean) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    TopAppBar(
      title = {
        Text(
          text = "الإشعارات والتذكيرات",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("notifications_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "الرجوع",
            tint = DarkGreen
          )
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Toggle card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "التذكيرات اللطيفة",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "تذكيرات هادئة وداعمة دون أي لغة ضغط أو لوم (اختيارية تماماً)",
                style = MaterialTheme.typography.labelSmall,
                color = TextDark.copy(alpha = 0.7f)
              )
            }

            Switch(
              checked = gentleRemindersEnabled,
              onCheckedChange = onToggleGentleReminders,
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = DarkGreen,
                uncheckedThumbColor = WarmBeige,
                uncheckedTrackColor = Color.LightGray
              )
            )
          }
        }
      }

      items(notifications, key = { it.id }) { notif ->
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(SageGreenPrimary.copy(alpha = 0.25f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = null,
                tint = DarkGreen,
                modifier = Modifier.size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = notif.title,
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = notif.message,
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark,
                lineHeight = 20.sp
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = notif.time,
                style = MaterialTheme.typography.labelSmall,
                color = TextDark.copy(alpha = 0.5f)
              )
            }
          }
        }
      }
    }
  }
}
