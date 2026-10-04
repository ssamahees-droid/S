package com.example.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WellnessHabit
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@Composable
fun DailyHabitsCard(
  habits: List<WellnessHabit>,
  onToggleHabit: (id: Long, completed: Boolean) -> Unit,
  onAddCustomHabit: (title: String, emoji: String) -> Unit,
  onDeleteHabit: (id: Long) -> Unit,
  modifier: Modifier = Modifier
) {
  var showAddDialog by remember { mutableStateOf(false) }
  var newHabitTitle by remember { mutableStateOf("") }
  var selectedEmoji by remember { mutableStateOf("🌱") }

  val completedCount = habits.count { it.isCompleted }
  val totalCount = habits.size
  val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
  val animatedProgress by animateFloatAsState(targetValue = progress, label = "habitsProgress")

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("daily_habits_card")
  ) {
    Column(
      modifier = Modifier.padding(18.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(SoftMint.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Eco,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "عادات التعافي اليومية 🌱",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = DarkGreen
            )
            Text(
              text = "$completedCount من $totalCount عادات مكتملة اليوم",
              style = MaterialTheme.typography.bodySmall,
              color = TextDark.copy(alpha = 0.65f)
            )
          }
        }

        IconButton(
          onClick = { showAddDialog = true },
          modifier = Modifier.testTag("add_habit_button")
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "إضافة عادة جديدة",
            tint = DarkGreen
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Progress bar
      LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = SageGreenPrimary,
        trackColor = WarmBeige.copy(alpha = 0.5f),
        strokeCap = StrokeCap.Round
      )

      if (progress >= 1.0f && totalCount > 0) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SoftMint.copy(alpha = 0.45f))
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = DarkGreen,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "رائع جداً! أتممت جميع عادات الطمأنينة لليوم، شجرتك تزدهر بنضارة ✨",
            style = MaterialTheme.typography.bodySmall,
            color = DarkGreen,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Habits list
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        habits.forEach { habit ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(
                if (habit.isCompleted) SoftMint.copy(alpha = 0.25f)
                else WarmBeige.copy(alpha = 0.25f)
              )
              .clickable { onToggleHabit(habit.id, !habit.isCompleted) }
              .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Checkbox(
              checked = habit.isCompleted,
              onCheckedChange = { onToggleHabit(habit.id, it) },
              colors = CheckboxDefaults.colors(
                checkedColor = DarkGreen,
                checkmarkColor = Color.White,
                uncheckedColor = TextDark.copy(alpha = 0.4f)
              ),
              modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
              text = habit.iconEmoji,
              fontSize = 18.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
              text = habit.title,
              style = MaterialTheme.typography.bodyMedium,
              color = if (habit.isCompleted) DarkGreen else TextDark,
              textDecoration = if (habit.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
              fontWeight = if (habit.isCompleted) FontWeight.Normal else FontWeight.Medium,
              modifier = Modifier.weight(1f)
            )

            // Option to delete custom habit if created
            if (habit.id > 5) {
              IconButton(
                onClick = { onDeleteHabit(habit.id) },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.DeleteOutline,
                  contentDescription = "حذف العادة",
                  tint = TextDark.copy(alpha = 0.35f),
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }
  }

  // Add Habit Dialog
  if (showAddDialog) {
    val emojis = listOf("🌱", "💧", "🌬️", "✨", "🧘", "🚶", "📖", "☀️", "🍵")
    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = {
        Text(
          text = "إضافة عادة سكينة جديدة 🌱",
          fontWeight = FontWeight.Bold,
          color = DarkGreen
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "اختر رمزاً واكتب عادة يومية بسيطة تساعدك على التوازن:",
            fontSize = 13.sp,
            color = TextDark.copy(alpha = 0.7f)
          )

          // Emoji selector
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            emojis.forEach { emoji ->
              val isSelected = selectedEmoji == emoji
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) DarkGreen else Color.Transparent)
                  .clickable { selectedEmoji = emoji },
                contentAlignment = Alignment.Center
              ) {
                Text(text = emoji, fontSize = 16.sp)
              }
            }
          }

          OutlinedTextField(
            value = newHabitTitle,
            onValueChange = { newHabitTitle = it },
            label = { Text("اسم العادة (مثال: شرب كوب يانسون دافئ)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        TextButton(
          onClick = {
            if (newHabitTitle.isNotBlank()) {
              onAddCustomHabit(newHabitTitle.trim(), selectedEmoji)
              newHabitTitle = ""
              showAddDialog = false
            }
          }
        ) {
          Text("إضافة", fontWeight = FontWeight.Bold, color = DarkGreen)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddDialog = false }) {
          Text("إلغاء", color = TextDark.copy(alpha = 0.6f))
        }
      }
    )
  }
}
