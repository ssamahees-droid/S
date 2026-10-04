package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@Composable
fun NesmatContentCard(
  item: ContentItem,
  onClick: () -> Unit,
  onToggleFavorite: () -> Unit,
  modifier: Modifier = Modifier
) {
  val typeIcon = when (item.contentType) {
    "audio" -> Icons.Default.Headphones
    "video" -> Icons.Default.PlayCircle
    "exercise" -> Icons.Default.FitnessCenter
    else -> Icons.AutoMirrored.Filled.Article
  }

  val typeName = when (item.contentType) {
    "audio" -> "صوت"
    "video" -> "فيديو"
    "exercise" -> "تمرين"
    else -> "مقال"
  }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("content_card_${item.id}")
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Category Badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(SageGreenPrimary.copy(alpha = 0.25f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = item.category,
              style = MaterialTheme.typography.labelMedium,
              color = DarkGreen,
              fontWeight = FontWeight.SemiBold
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Content type pill
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(WarmBeige.copy(alpha = 0.6f))
              .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = typeIcon,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = typeName,
              style = MaterialTheme.typography.labelSmall,
              color = DarkGreen
            )
          }

          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "• ${item.duration}",
            style = MaterialTheme.typography.labelSmall,
            color = TextDark.copy(alpha = 0.6f)
          )
        }

        IconButton(
          onClick = onToggleFavorite,
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = if (item.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = if (item.isFavorite) "محفوظ" else "حفظ",
            tint = if (item.isFavorite) DarkGreen else TextDark.copy(alpha = 0.4f)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = item.title,
        style = MaterialTheme.typography.titleMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = item.description,
        style = MaterialTheme.typography.bodyMedium,
        color = TextDark.copy(alpha = 0.8f),
        lineHeight = 22.sp,
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "مراجعة: ${item.reviewer}",
          style = MaterialTheme.typography.labelSmall,
          color = TextDark.copy(alpha = 0.55f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "اقرأ المزيد",
            style = MaterialTheme.typography.labelMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = DarkGreen,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}
