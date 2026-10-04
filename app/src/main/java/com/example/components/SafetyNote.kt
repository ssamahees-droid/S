package com.example.components

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.MoodDifficult
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

enum class SafetyNoteType {
  DISCLAIMER,
  EMERGENCY_SUPPORT,
  PRIVACY_ASSURANCE
}

private data class SafetyNoteConfig(
  val bgColor: Color,
  val borderColor: Color,
  val icon: ImageVector,
  val iconColor: Color,
  val title: String,
  val defaultText: String
)

@Composable
fun SafetyNote(
  type: SafetyNoteType = SafetyNoteType.DISCLAIMER,
  customMessage: String? = null,
  modifier: Modifier = Modifier
) {
  val config = when (type) {
    SafetyNoteType.DISCLAIMER -> SafetyNoteConfig(
      bgColor = WarmBeige.copy(alpha = 0.5f),
      borderColor = WarmBeige,
      icon = Icons.Default.Security,
      iconColor = DarkGreen,
      title = "تنبيه توعوي مهم",
      defaultText = "تطبيق «نسمة الحياة» مساحة توعوية ودعم ذاتي، ولا يقدم تشخيصاً طبياً أو علاجاً دوائياً، ولا يغني عن مراجعة الطبيب النفسي المختص."
    )
    SafetyNoteType.EMERGENCY_SUPPORT -> SafetyNoteConfig(
      bgColor = Color(0xFFFDECE8),
      borderColor = MoodDifficult,
      icon = Icons.Default.Warning,
      iconColor = MoodDifficult,
      title = "مسار الأمان والدعم الفوري 🤍",
      defaultText = "إذا كنت تمر بأوقات عصيبة أو تشعر برغبة في إيذاء نفسك، من فضلك اتصل فوراً بالخط الساخن للأمانة العامة للصحة النفسية: 16328 أو خدمات الطوارئ: 123. سلامتك وحياتك ثمينة."
    )
    SafetyNoteType.PRIVACY_ASSURANCE -> SafetyNoteConfig(
      bgColor = Color.White,
      borderColor = SageGreenPrimary.copy(alpha = 0.5f),
      icon = Icons.Default.Info,
      iconColor = SageGreenPrimary,
      title = "أمان وخصوصية تامة",
      defaultText = "جميع ملاحظاتك وتسجيلاتك تخزن محلياً على جهازك ولا تتم مشاركتها أو بيعها لأي طرف ثالث وفقاً لقانون حماية البيانات رقم 151."
    )
  }

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = config.bgColor),
    border = androidx.compose.foundation.BorderStroke(1.dp, config.borderColor),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      Icon(
        imageVector = config.icon,
        contentDescription = null,
        tint = config.iconColor,
        modifier = Modifier.size(22.dp)
      )

      Spacer(modifier = Modifier.width(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = config.title,
          style = MaterialTheme.typography.labelMedium,
          color = config.iconColor,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
          text = customMessage ?: config.defaultText,
          style = MaterialTheme.typography.bodySmall,
          color = TextDark.copy(alpha = 0.85f),
          lineHeight = 18.sp
        )
      }
    }
  }
}
