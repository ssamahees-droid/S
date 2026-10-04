package com.example.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.TextDark

@Composable
fun EmptyState(
  emoji: String = "🌱",
  title: String,
  message: String,
  actionButtonLabel: String? = null,
  onActionClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = emoji,
        fontSize = 40.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = TextDark.copy(alpha = 0.7f),
        textAlign = TextAlign.Center,
        lineHeight = 22.sp
      )

      if (actionButtonLabel != null && onActionClick != null) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = onActionClick,
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(text = actionButtonLabel, fontSize = 13.sp)
        }
      }
    }
  }
}
