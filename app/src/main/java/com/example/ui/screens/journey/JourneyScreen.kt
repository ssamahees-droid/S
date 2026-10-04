package com.example.ui.screens.journey

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssessmentResult
import com.example.data.model.DailyCheckin
import com.example.data.model.PersonalNote
import com.example.lib.storage.DataExportHelper
import com.example.ui.screens.library.NesmatLibraryRepository
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.MoodDifficult
import com.example.ui.theme.MoodGood
import com.example.ui.theme.MoodGreat
import com.example.ui.theme.MoodNeutral
import com.example.ui.theme.MoodTired
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JourneyScreen(
  checkins: List<DailyCheckin>,
  notes: List<PersonalNote>,
  assessments: List<AssessmentResult>,
  daysCheckedInThisWeek: Int,
  onAddNote: (String, String) -> Unit,
  onDeleteNote: (Long) -> Unit,
  onDeleteCheckin: (Long) -> Unit,
  onDeleteAssessment: (Long) -> Unit,
  onOpenLibraryArticle: (Int) -> Unit = {},
  modifier: Modifier = Modifier
) {
  var showAddNoteDialog by remember { mutableStateOf(false) }
  var newNoteTitle by remember { mutableStateOf("") }
  var newNoteContent by remember { mutableStateOf("") }
  var selectedFilter by remember { mutableStateOf("الكل") }

  val context = LocalContext.current
  val pinPrefs = remember { context.getSharedPreferences("nesmat_privacy_pin", Context.MODE_PRIVATE) }
  var savedPin by remember { mutableStateOf(pinPrefs.getString("user_pin", "") ?: "") }
  var isUnlocked by remember { mutableStateOf(savedPin.isEmpty()) }
  var enteredPin by remember { mutableStateOf("") }
  var pinError by remember { mutableStateOf(false) }
  var showSetPinDialog by remember { mutableStateOf(false) }
  var newPinInput by remember { mutableStateOf("") }

  // حساب تقدم المستخدم في تمارين مقالات مكتبة نسمة حياة العشرة بشكل بصري بسيط بدون تشخيص
  val libraryArticles = remember { NesmatLibraryRepository.articles }
  val completedArticleIds = remember(notes) {
    libraryArticles.filter { article ->
      notes.any { note ->
        note.title.contains(article.title) || note.content.contains("المقال: ${article.title}")
      }
    }.map { it.id }.toSet()
  }

  val completedCount = completedArticleIds.size
  val totalArticlesCount = libraryArticles.size
  val progressFraction = if (totalArticlesCount > 0) {
    completedCount.toFloat() / totalArticlesCount.toFloat()
  } else 0f

  val filteredNotes = remember(notes, selectedFilter) {
    when (selectedFilter) {
      "تمارين المكتبة" -> notes.filter { it.title.contains("تمرين مكتبة نسمة حياة") || it.title.contains("📘") }
      "أنا دلوقتي" -> notes.filter { it.title.contains("أنا دلوقتي") }
      "خطط الإنقاذ" -> notes.filter { it.title.contains("خطة إنقاذ") || it.title.contains("خطة نسمة الحياة") }
      "خواطري" -> notes.filter {
        !it.title.contains("تمرين مكتبة نسمة حياة") &&
          !it.title.contains("أنا دلوقتي") &&
          !it.title.contains("خطة إنقاذ")
      }
      else -> notes
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    // Top Bar — صفحة "حياتي" (المرجع المركزي لتمارين المستخدم ورحلته)
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "حياتي ورحلتي 🌱",
          style = MaterialTheme.typography.headlineMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.testTag("my_life_header_title")
        )

        OutlinedButton(
          onClick = { showSetPinDialog = true },
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.testTag("my_life_privacy_lock_button")
        ) {
          Icon(
            imageVector = if (savedPin.isNotEmpty()) Icons.Default.Lock else Icons.Default.LockOpen,
            contentDescription = null,
            tint = DarkGreen,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (savedPin.isNotEmpty()) "تعديل القفل" else "قفل الخصوصية",
            color = DarkGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "مرجعك المركزي لكل التمارين والخطوات الهادئة التي قمت بها لنفسك — خطوة صغيرة كل يوم تصنع فرقًا.",
        style = MaterialTheme.typography.bodyMedium,
        color = TextDark.copy(alpha = 0.8f)
      )
    }

    if (!isUnlocked && savedPin.isNotEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "صفحة «حياتي» محمية برمز الخصوصية 🔒",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "أدخل الرمز السري المكون من ٤ أرقام لعرض تمارينك وخواطرك المحفوظة محليًا:",
              style = MaterialTheme.typography.bodySmall,
              color = TextDark.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
              value = enteredPin,
              onValueChange = {
                if (it.length <= 4) {
                  enteredPin = it
                  pinError = false
                  if (it == savedPin) {
                    isUnlocked = true
                  }
                }
              },
              placeholder = { Text("••••") },
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            )
            if (pinError) {
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "الرمز غير صحيح، حاول مرة أخرى",
                color = Color(0xFFB3261E),
                fontSize = 12.sp
              )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
              onClick = {
                if (enteredPin == savedPin) {
                  isUnlocked = true
                } else {
                  pinError = true
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("فتح صفحتي", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
      return@Column
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      contentPadding = PaddingValues(bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // بطاقة تصدير «خطة نسمة الحياة الخاصة بي» كبطاقة جاهزة للمشاركة مع المختص
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.5f)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("my_life_export_specialist_card")
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "📄 بطاقة متابعة جاهزة للمختص أو لنفسك",
                style = MaterialTheme.typography.titleSmall,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = "استخرج ملخصًا منسقًا يجمع خطتك الشخصية، سجل النوم، وتمارينك لتحتفظ به أو تعرضه على المختص النفسي.",
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.85f)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
              onClick = {
                DataExportHelper.shareSpecialistSummaryReport(
                  context = context,
                  checkins = checkins,
                  notes = notes,
                  completedLibraryCount = completedCount,
                  totalLibraryCount = totalArticlesCount
                )
              },
              colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
              shape = RoundedCornerShape(12.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("تصدير الملخص", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
      // بطاقة التقدم البصري البسيط في تمارين المكتبة العشرة (بدون تشخيص أو تقييم طبي)
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, SageGreenPrimary.copy(alpha = 0.5f), RoundedCornerShape(22.dp))
            .testTag("my_life_library_progress_card")
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "🌿 تقدمي في تمارين مكتبة نسمة حياة",
                  style = MaterialTheme.typography.titleMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "أكملت $completedCount من أصل $totalArticlesCount تمارين في المكتبة",
                  style = MaterialTheme.typography.bodyMedium,
                  color = TextDark.copy(alpha = 0.85f),
                  fontWeight = FontWeight.SemiBold
                )
              }
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(CircleShape)
                  .background(SoftMint.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "$completedCount/$totalArticlesCount",
                  style = MaterialTheme.typography.titleSmall,
                  color = DarkGreen,
                  fontWeight = FontWeight.ExtraBold
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
              progress = { progressFraction },
              color = DarkGreen,
              trackColor = SoftMint.copy(alpha = 0.45f),
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.height(14.dp))

            // شبكة مبسطة للمقالات العشرة وحالة إتمام تمرين كل مقال
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              libraryArticles.forEach { article ->
                val isDone = completedArticleIds.contains(article.id)
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDone) SoftMint.copy(alpha = 0.45f) else CreamBackground)
                    .clickable { onOpenLibraryArticle(article.id) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("my_life_article_progress_${article.id}")
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                  ) {
                    Icon(
                      imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                      contentDescription = if (isDone) "مكتمل" else "غير مكتمل بعد",
                      tint = if (isDone) DarkGreen else TextDark.copy(alpha = 0.4f),
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = "${article.id}. ${article.title}",
                      style = MaterialTheme.typography.bodyMedium,
                      color = DarkGreen,
                      fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal
                    )
                  }
                  Text(
                    text = if (isDone) "مكتمل ✓" else "ابدأ التمرين ←",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDone) DarkGreen else SageGreenPrimary,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "💡 هذا المؤشر البصري لتشجيعك على التدرج وتوثيق خطواتك الهادئة فقط، وليس تقييمًا أو تشخيصًا طبيًا.",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.65f),
              lineHeight = 18.sp
            )
          }
        }
      }
      // Descriptive Statistics Card (Page 19)
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(SageGreenPrimary.copy(alpha = 0.25f)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "$daysCheckedInThisWeek",
                style = MaterialTheme.typography.headlineMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "«سجلت حالتك $daysCheckedInThisWeek أيام هذا الأسبوع»",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "إحصائيات وصفية تشجعك على الاستمرار في الوعي وملاحظة نفسك.",
                style = MaterialTheme.typography.labelSmall,
                color = TextDark.copy(alpha = 0.65f)
              )
            }
          }
        }
      }

      // Daily Check-ins History (Page 18)
      item {
        Text(
          text = "سجل الأيام والحالة اليومية",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      }

      if (checkins.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "لم تسجل حالتك بعد اليوم. توجه للصفحة الرئيسية لتسجيل شعورك.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark.copy(alpha = 0.7f),
              modifier = Modifier.padding(16.dp)
            )
          }
        }
      } else {
        items(checkins.take(7), key = { it.id }) { checkin ->
          val (emoji, moodColor) = when (checkin.moodValue) {
            "كويس" -> "😊" to MoodGreat
            "مقبول" -> "😐" to MoodGood
            "مش عارف" -> "🫥" to MoodNeutral
            "مش كويس" -> "😔" to MoodDifficult
            else -> "😫" to MoodTired
          }

          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(moodColor.copy(alpha = 0.25f)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(emoji, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Text(
                    text = checkin.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextDark.copy(alpha = 0.6f)
                  )
                  Text(
                    text = checkin.moodValue,
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  if (!checkin.note.isNullOrBlank()) {
                    Text(
                      text = "ملاحظة: ${checkin.note}",
                      style = MaterialTheme.typography.bodySmall,
                      color = TextDark.copy(alpha = 0.8f)
                    )
                  }
                }
              }

              IconButton(
                onClick = { onDeleteCheckin(checkin.id) },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "حذف التسجيل",
                  tint = TextDark.copy(alpha = 0.4f),
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }

      // Central Completed Exercises & Personal Notes Section
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "سجل التمارين المكتملة وملاحظاتي ✍️",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )

            Button(
              onClick = { showAddNoteDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              modifier = Modifier.testTag("add_personal_note_button")
            ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("خاطرة جديدة", fontSize = 12.sp)
            }
          }

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf("الكل", "تمارين المكتبة", "أنا دلوقتي", "خطط الإنقاذ", "خواطري").forEach { filter ->
              FilterChip(
                selected = selectedFilter == filter,
                onClick = { selectedFilter = filter },
                label = { Text(filter, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = DarkGreen,
                  selectedLabelColor = Color.White
                )
              )
            }
          }
        }
      }

      if (filteredNotes.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "لا توجد تمارين أو ملاحظات مسجلة في هذا التصنيف بعد. أي تمرين تكمله في «مكتبة نسمة حياة» أو «أنا دلوقتي...» سيُحفظ هنا تلقائيًا.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark.copy(alpha = 0.7f),
              modifier = Modifier.padding(16.dp)
            )
          }
        }
      } else {
        items(filteredNotes, key = { it.id }) { note ->
          val dateStr = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(note.timestamp))
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = note.title,
                  style = MaterialTheme.typography.titleMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )
                IconButton(
                  onClick = { onDeleteNote(note.id) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "حذف الخاطرة",
                    tint = TextDark.copy(alpha = 0.4f),
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark,
                lineHeight = 22.sp
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = dateStr,
                style = MaterialTheme.typography.labelSmall,
                color = TextDark.copy(alpha = 0.5f)
              )
            }
          }
        }
      }

      // Past Assessments (Page 18)
      item {
        Text(
          text = "التقييمات السابقة",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      }

      if (assessments.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "لم تقم بأي تقييم بعد. يمكنك تجربة التقييم المبدئي من الصفحة الرئيسية.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark.copy(alpha = 0.7f),
              modifier = Modifier.padding(16.dp)
            )
          }
        }
      } else {
        items(assessments, key = { it.id }) { ass ->
          val dateStr = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(ass.timestamp))
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = ass.assessmentType,
                  style = MaterialTheme.typography.titleMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = dateStr,
                  style = MaterialTheme.typography.labelSmall,
                  color = TextDark.copy(alpha = 0.5f)
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "النتيجة: ${ass.resultTitle} (الدرجة: ${ass.score} من ${ass.maxScore})",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkGreen,
                fontWeight = FontWeight.SemiBold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = ass.recommendation,
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.8f)
              )
              Spacer(modifier = Modifier.height(6.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "لا يتم وصف المستخدم طبياً بناءً على البيانات.",
                  style = MaterialTheme.typography.labelSmall,
                  color = TextDark.copy(alpha = 0.5f)
                )
                IconButton(
                  onClick = { onDeleteAssessment(ass.id) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "حذف التقييم",
                    tint = TextDark.copy(alpha = 0.4f),
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // Add Note Dialog
  if (showAddNoteDialog) {
    AlertDialog(
      onDismissRequest = { showAddNoteDialog = false },
      title = {
        Text("خاطرة جديدة ✍️", color = DarkGreen, fontWeight = FontWeight.Bold)
      },
      text = {
        Column {
          OutlinedTextField(
            value = newNoteTitle,
            onValueChange = { newNoteTitle = it },
            label = { Text("العنوان") },
            placeholder = { Text("مثال: لحظة امتنان اليوم...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = DarkGreen,
              unfocusedBorderColor = WarmBeige
            )
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = newNoteContent,
            onValueChange = { newNoteContent = it },
            label = { Text("نص الخاطرة") },
            placeholder = { Text("ما الذي خطر ببالك أو شعرت به اليوم؟") },
            modifier = Modifier
              .fillMaxWidth()
              .height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = DarkGreen,
              unfocusedBorderColor = WarmBeige
            )
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newNoteContent.isNotBlank()) {
              onAddNote(newNoteTitle, newNoteContent)
              newNoteTitle = ""
              newNoteContent = ""
              showAddNoteDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
        ) {
          Text("حفظ الخاطرة")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddNoteDialog = false }) {
          Text("إلغاء", color = TextDark)
        }
      }
    )
  }

  if (showSetPinDialog) {
    AlertDialog(
      onDismissRequest = { showSetPinDialog = false },
      title = {
        Text("🔒 قفل الخصوصية المحلي لصفحة «حياتي»", color = DarkGreen, fontWeight = FontWeight.Bold)
      },
      text = {
        Column {
          Text(
            text = "حدد رمزًا سريًا من ٤ أرقام لحماية تمارينك وخواطرك على جهازك (أو اتركه فارغًا لإلغاء القفل):",
            style = MaterialTheme.typography.bodySmall,
            color = TextDark
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = newPinInput,
            onValueChange = { if (it.length <= 4) newPinInput = it },
            placeholder = { Text("مثال: 1234") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            pinPrefs.edit().putString("user_pin", newPinInput.trim()).apply()
            savedPin = newPinInput.trim()
            isUnlocked = true
            newPinInput = ""
            showSetPinDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
        ) {
          Text(if (newPinInput.isBlank()) "إلغاء القفل" else "حفظ الرمز")
        }
      },
      dismissButton = {
        TextButton(onClick = { showSetPinDialog = false }) {
          Text("إغلاق", color = TextDark)
        }
      }
    )
  }
}
