package com.example.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

data class OnboardingOption(
  val id: Int,
  val text: String,
  val emoji: String
)

@Composable
fun OnboardingScreen(
  onComplete: (List<String>) -> Unit,
  modifier: Modifier = Modifier
) {
  val options = remember {
    listOf(
      OnboardingOption(1, "حاسس إني مضغوط", "🌧️"),
      OnboardingOption(2, "حاسس إني مش كويس ومش فاهم مالي", "☁️"),
      OnboardingOption(3, "محتاج حد يسمعني", "🌱"),
      OnboardingOption(4, "عايز أفهم نفسي أكتر", "🔍"),
      OnboardingOption(5, "عايز أساعد شخص قريب مني", "🤝"),
      OnboardingOption(6, "عايز أتعلم عن الصحة النفسية", "📚"),
      OnboardingOption(7, "مش عارف... بس محتاج حاجة تساعدني", "🕊️")
    )
  }

  val selectedOptions = remember { mutableStateListOf<String>() }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "خلينا نتعرف عليك",
          style = MaterialTheme.typography.headlineMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "إيه اللي جابك لنسمة الحياة النهارده؟",
          style = MaterialTheme.typography.titleLarge,
          color = TextDark,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "ممكن تختار أكتر من حاجة تصف شعورك أو هدفك.",
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark.copy(alpha = 0.75f)
        )

        Spacer(modifier = Modifier.height(18.dp))
      }

      items(options, key = { it.id }) { option ->
        val isSelected = selectedOptions.contains(option.text)

        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SageGreenPrimary.copy(alpha = 0.25f) else Color.White
          ),
          border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) DarkGreen else WarmBeige
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable {
              if (isSelected) {
                selectedOptions.remove(option.text)
              } else {
                selectedOptions.add(option.text)
              }
            }
            .testTag("onboarding_option_${option.id}")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = option.emoji,
              fontSize = 22.sp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
              text = option.text,
              style = MaterialTheme.typography.bodyLarge,
              color = TextDark,
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
              modifier = Modifier.weight(1f)
            )

            // Selection indicator circle
            Box(
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isSelected) DarkGreen else Color.Transparent)
                .border(
                  width = 2.dp,
                  color = if (isSelected) DarkGreen else WarmBeige,
                  shape = CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              if (isSelected) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "تم الاختيار",
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))

        // Privacy & Non-diagnostic disclaimer notice (Page 5)
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.6f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "هذه الاختيارات ليست تشخيصاً طبياً، وتُستخدم فقط لاقتراح محتوى وأدوات تناسب اهتماماتك. يمكنك تعديلها في أي وقت.",
              style = MaterialTheme.typography.labelMedium,
              color = TextDark.copy(alpha = 0.85f),
              lineHeight = 18.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = {
            onComplete(selectedOptions.toList())
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = DarkGreen,
            contentColor = CreamBackground
          ),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag("onboarding_continue_button")
        ) {
          Text(
            text = "متابعة",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
