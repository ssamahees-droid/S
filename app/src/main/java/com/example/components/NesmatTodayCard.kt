package com.example.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.library.NesmatLibraryRepository
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import kotlin.random.Random

@Composable
fun NesmatTodayCard(
  onSaveResponseLocally: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val prompts = remember { NesmatLibraryRepository.dailyPrompts }
  var currentIndex by remember { mutableIntStateOf(Random.nextInt(prompts.size)) }
  var userResponse by remember(currentIndex) { mutableStateOf("") }
  var isSaved by remember(currentIndex) { mutableStateOf(false) }

  val activePrompt = prompts[currentIndex]

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .border(1.5.dp, SageGreenPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
      .testTag("home_nesmat_today_card")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(SoftMint.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Spa,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "🌱 نسمة اليوم",
              style = MaterialTheme.typography.titleLarge,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "رسالة قصيرة + مهمة صغيرة ليومك",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.65f)
            )
          }
        }

        IconButton(
          onClick = {
            currentIndex = (currentIndex + 1) % prompts.size
          },
          modifier = Modifier.testTag("nesmat_today_next_button")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "نسمة أخرى",
            tint = DarkGreen
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(CreamBackground)
          .padding(14.dp)
      ) {
        Column {
          Text(
            text = "اليوم:",
            style = MaterialTheme.typography.labelMedium,
            color = SageGreenPrimary,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "\"${activePrompt.message}\"",
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold,
            lineHeight = 25.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = userResponse,
        onValueChange = {
          userResponse = it
          isSaved = false
        },
        placeholder = { Text(activePrompt.inputPlaceholder, fontSize = 13.sp) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = DarkGreen,
          unfocusedBorderColor = WarmBeige,
          focusedContainerColor = CreamBackground,
          unfocusedContainerColor = CreamBackground
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("nesmat_today_input")
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        Button(
          onClick = {
            val content = buildString {
              appendLine("نسمة اليوم: ${activePrompt.message}")
              if (userResponse.isNotBlank()) {
                appendLine("إجابتي: $userResponse")
              } else {
                appendLine("تم تأمل نسمة اليوم بهدوء.")
              }
            }.trimIndent()
            onSaveResponseLocally("🌱 نسمة اليوم", content)
            isSaved = true
          },
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("nesmat_today_save_button")
        ) {
          Text(
            text = if (isSaved) "✓ تم الحفظ" else "اكتبها واحفظها 🤍",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      }

      AnimatedVisibility(visible = isSaved) {
        Row(
          modifier = Modifier.padding(top = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = DarkGreen,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "حُفظت خطوتك محليًا في «رحلتي» 🌿",
            style = MaterialTheme.typography.labelSmall,
            color = DarkGreen
          )
        }
      }
    }
  }
}
