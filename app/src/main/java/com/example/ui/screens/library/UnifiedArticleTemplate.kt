package com.example.ui.screens.library

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import java.util.Locale

/**
 * نموذج شاشة المقال الموحد (Unified Article Screen Template) لمكتبة نسمة حياة:
 * - ترويسة وعنوان المقال والوصف القصير
 * - قسم "اقرأ معي" (فقرات قصيرة ومريحة للعين)
 * - قسم "💡 خُد بالك من دي" (نقطة نفسية مهمة ومطمئنة)
 * - قسم "🧩 جرّب الآن" (التمرين التفاعلي الخاص بكل مقال)
 * - قسم "✍️ اكتب لنفسك" (مساحة حرة لتدوين إجابة أو خاطرة خاصة بالمستخدم)
 * - قسم "🌱 خُد معك" (خلاصة دافئة من سطر واحد)
 * - زر "✓ خلصت التمرين" (يحفظ المخرجات محليًا في جهاز المستخدم داخل "رحلتي")
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnifiedArticleTemplate(
  article: NesmatLibraryArticle,
  personalReflectionNote: String,
  onPersonalReflectionChange: (String) -> Unit,
  isExerciseCompleted: Boolean,
  onFinishExercise: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
  interactiveExerciseContent: @Composable () -> Unit
) {
  val scrollState = rememberScrollState()
  val context = LocalContext.current
  var isSpeaking by remember(article.id) { mutableStateOf(false) }
  var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }

  DisposableEffect(article.id) {
    var instance: TextToSpeech? = null
    instance = TextToSpeech(context) { status ->
      if (status == TextToSpeech.SUCCESS) {
        instance?.language = Locale("ar")
        instance?.setSpeechRate(0.92f)
      }
    }
    ttsEngine = instance
    onDispose {
      instance?.stop()
      instance?.shutdown()
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    TopAppBar(
      title = {
        Text(
          text = "🌿 مكتبة نسمة حياة",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("article_detail_back_button")
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

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
      // 1. عنوان المقال والوصف التمهيدي
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("unified_article_header_card")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(SageGreenPrimary.copy(alpha = 0.25f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = article.icon,
                contentDescription = article.title,
                tint = DarkGreen,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "المقال ${article.id} من ١٠",
                style = MaterialTheme.typography.labelMedium,
                color = SageGreenPrimary,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = article.shortDescription,
            style = MaterialTheme.typography.bodyMedium,
            color = TextDark.copy(alpha = 0.85f),
            lineHeight = 23.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedButton(
            onClick = {
              if (isSpeaking) {
                ttsEngine?.stop()
                isSpeaking = false
              } else {
                val fullSpeechText = buildString {
                  append("${article.title}. ")
                  article.paragraphs.forEach { p -> append("$p. ") }
                  append("خُد بالك من دي: ${article.takeCareTip}. ")
                  append("خُد معك: ${article.takeWithYouSummary}.")
                }
                ttsEngine?.speak(fullSpeechText, TextToSpeech.QUEUE_FLUSH, null, "article_tts_${article.id}")
                isSpeaking = true
              }
            },
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("article_tts_button")
          ) {
            Icon(
              imageVector = if (isSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isSpeaking) "إيقاف القراءة الصوتية" else "🔊 استمع للمقال بهدوء (بدون إنترنت)",
              color = DarkGreen,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
        }
      }

      // 2. قسم: اقرأ معي (3-4 فقرات قصيرة فقط ومريحة للعين)
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("unified_article_read_with_me_section")
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Text(
            text = "اقرأ معي",
            style = MaterialTheme.typography.titleLarge,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )

          article.paragraphs.forEachIndexed { idx, paragraph ->
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CreamBackground.copy(alpha = 0.75f))
                .padding(14.dp)
                .testTag("unified_article_paragraph_${idx + 1}")
            ) {
              Text(
                text = paragraph,
                style = MaterialTheme.typography.bodyLarge,
                color = TextDark,
                lineHeight = 26.sp
              )
            }
          }
        }
      }

      // 3. قسم: 💡 خُد بالك من دي
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.55f)),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("unified_article_take_care_section")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "💡 خُد بالك من دي",
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = article.takeCareTip,
            style = MaterialTheme.typography.bodyLarge,
            color = TextDark,
            lineHeight = 25.sp
          )
        }
      }

      // 4. قسم: 🧩 جرّب الآن (التمرين التفاعلي)
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.5.dp, SageGreenPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
          .testTag("unified_article_try_now_section")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "🧩 جرّب الآن",
            style = MaterialTheme.typography.titleLarge,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(14.dp))
          interactiveExerciseContent()
        }
      }

      // 5. قسم: ✍️ اكتب لنفسك
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("unified_article_write_to_self_section")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "✍️ اكتب لنفسك",
            style = MaterialTheme.typography.titleLarge,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "مكان كتابة إجابة المستخدم وخواطره الخاصة حول هذا المقال (تُحفظ محليًا على جهازك فقط):",
            style = MaterialTheme.typography.bodySmall,
            color = TextDark.copy(alpha = 0.75f)
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = personalReflectionNote,
            onValueChange = onPersonalReflectionChange,
            placeholder = { Text("اكتب إجابتك أو خواطرك لنفسك هنا...", fontSize = 14.sp) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = DarkGreen,
              unfocusedBorderColor = WarmBeige,
              focusedContainerColor = CreamBackground,
              unfocusedContainerColor = CreamBackground
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(110.dp)
              .testTag("article_write_to_self_input")
          )
        }
      }

      // 6. قسم: 🌱 خُد معك (خلاصة من سطر واحد)
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.45f)),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("unified_article_take_with_you_section")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "🌱 خُد معك",
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "\"${article.takeWithYouSummary}\"",
            style = MaterialTheme.typography.bodyLarge,
            color = DarkGreen,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 25.sp
          )
        }
      }

      // 7. زر: ✓ خلصت التمرين
      Button(
        onClick = onFinishExercise,
        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("article_finish_exercise_button")
      ) {
        Text(
          text = "✓ خلصت التمرين",
          style = MaterialTheme.typography.titleMedium,
          color = Color.White,
          fontWeight = FontWeight.Bold
        )
      }

      AnimatedVisibility(visible = isExerciseCompleted) {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.55f)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("unified_article_completed_banner")
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "أحسنت خطوتك الهادئة 🤍",
                style = MaterialTheme.typography.titleSmall,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "تم حفظ إجابتك محليًا على جهازك فقط داخل صفحة «رحلتي» دون إرسالها لأي جهة خارجية.",
                style = MaterialTheme.typography.bodySmall,
                color = TextDark
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}
