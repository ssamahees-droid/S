package com.example.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

data class WisdomItem(
  val quote: String,
  val author: String,
  val microPractice: String
)

@Composable
fun DailyWisdomCapsule(
  onCopied: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var isFlipped by remember { mutableStateOf(false) }

  val wisdomList = remember {
    listOf(
      WisdomItem(
        quote = "ما فاتك لم يُخلق لك، وما خُلق لك لن يفوتك... فارمِ عن كاهلك ثِقل الندم واسترح.",
        author = "حكمة عربية أصيلة",
        microPractice = "ضع يدك على قلبك، خذ 3 أنفاس عميقة، وقل لنفسك: أنا في أمان وكل أمر مقدّر لي بخير."
      ),
      WisdomItem(
        quote = "الجرح هو المكان الذي يدخل منه النور إلى أعماقك.",
        author = "جلال الدين الرومي",
        microPractice = "تذكّر موقفاً صعباً مررت به سابقاً وكيف صقل صبرك وقوتك، ثم ابتسم شاكراً صمودك."
      ),
      WisdomItem(
        quote = "الهدوء ليس غياب العواصف من حولك، بل هو السكون التام الذي تسكن إليه في داخلك.",
        author = "نسمة حياة",
        microPractice = "أرخِ كتفيك الآن، وفك إطباق فكك، ودع لسانك يرتاح في قاع فمك لمدة دقيقة كاملة."
      ),
      WisdomItem(
        quote = "أعطِ لنفسك في كل يوم عذراً جديداً، وفسحة من التجاوز... فالكمال ليس من شأن البشر.",
        author = "أدب الاستبصار",
        microPractice = "اكتب كلمة واحدة لطيفة توجهها لنفسك اليوم كما لو كنت تخاطب أقرب وأعز أصدقائك."
      )
    )
  }

  var currentWisdomIndex by remember { mutableStateOf(0) }
  val currentWisdom = wisdomList[currentWisdomIndex]

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(WarmBeige.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.FormatQuote,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isFlipped) "تطبيق اليوم العملي 🌿" else "كبسولة الحكمة اليومية ✨",
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Wisdom", "${currentWisdom.quote} - ${currentWisdom.author}")
              clipboard.setPrimaryClip(clip)
              onCopied()
            },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "نسخ الحكمة",
              tint = DarkGreen,
              modifier = Modifier.size(16.dp)
            )
          }

          Spacer(modifier = Modifier.width(4.dp))

          IconButton(
            onClick = {
              currentWisdomIndex = (currentWisdomIndex + 1) % wisdomList.size
              isFlipped = false
            },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.SwapHoriz,
              contentDescription = "حكمة أخرى",
              tint = DarkGreen,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Card Content Box with Flip Switch
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(
            if (isFlipped) SageGreenPrimary.copy(alpha = 0.15f) else WarmBeige.copy(alpha = 0.35f)
          )
          .border(
            width = 1.dp,
            color = if (isFlipped) DarkGreen.copy(alpha = 0.3f) else WarmBeige,
            shape = RoundedCornerShape(16.dp)
          )
          .clickable { isFlipped = !isFlipped }
          .padding(16.dp)
      ) {
        if (!isFlipped) {
          Column {
            Text(
              text = "«${currentWisdom.quote}»",
              style = MaterialTheme.typography.bodyLarge,
              color = DarkGreen,
              textAlign = TextAlign.Center,
              fontWeight = FontWeight.Bold,
              lineHeight = 26.sp,
              modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "— ${currentWisdom.author}",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.7f),
              textAlign = TextAlign.End,
              modifier = Modifier.fillMaxWidth()
            )
          }
        } else {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = DarkGreen,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "دقيقة صفاء عملية:",
                style = MaterialTheme.typography.labelMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = currentWisdom.microPractice,
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark,
              lineHeight = 22.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = if (isFlipped) "انقر للعودة إلى نص الحكمة 🔄" else "المس البطاقة لمعرفة التطبيق العملي لليوم 💡",
        style = MaterialTheme.typography.labelSmall,
        color = TextDark.copy(alpha = 0.5f),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
