package com.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SupportAgent
import com.example.components.DailyWisdomCapsule
import com.example.components.LivingTranquilityTree
import com.example.components.NesmatTodayCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentItem
import com.example.data.model.DailyCheckin
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

data class MoodOption(
  val label: String,
  val emoji: String,
  val color: Color
)

@Composable
fun HomeScreen(
  todayCheckin: DailyCheckin?,
  suggestedContent: List<ContentItem>,
  mindfulMomentsCount: Int = 1,
  onRecordMood: (String, String?) -> Unit,
  onNavigateToExplore: () -> Unit,
  onNavigateToLibrary: () -> Unit = {},
  onNavigateToRightNow: () -> Unit = {},
  onSaveNesmatToday: (String, String) -> Unit = { _, _ -> },
  onNavigateToSupport: () -> Unit,
  onNavigateToAssessment: () -> Unit,
  onNavigateToBreathe: () -> Unit,
  onNavigateToOasis: () -> Unit,
  onNavigateToGwaya: () -> Unit = {},
  onNavigateToLight: () -> Unit,
  onNavigateToChat: () -> Unit,
  onNavigateToImageStudio: () -> Unit,
  onContentClick: (Long) -> Unit,
  onToggleFavorite: (Long, Boolean) -> Unit,
  onWisdomCopied: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val moods = remember {
    listOf(
      MoodOption("كويس", "😊", MoodGreat),
      MoodOption("مقبول", "😐", MoodGood),
      MoodOption("مش عارف", "🫥", MoodNeutral),
      MoodOption("مش كويس", "😔", MoodDifficult),
      MoodOption("متعب جداً", "😫", MoodTired)
    )
  }

  var showNoteField by remember { mutableStateOf(false) }
  var checkinNote by remember { mutableStateOf("") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
      .padding(horizontal = 20.dp),
    contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp)
  ) {
    // Header Greeting
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "أهلاً بيك في نسمة الحياة 🌿",
            style = MaterialTheme.typography.headlineMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "خد لحظة مع نفسك...",
            style = MaterialTheme.typography.bodyLarge,
            color = TextDark.copy(alpha = 0.8f)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }

    // Creative Feature: Living Tranquility Tree
    item {
      LivingTranquilityTree(
        mindfulMomentsCount = mindfulMomentsCount,
        modifier = Modifier.padding(bottom = 20.dp)
      )
    }

    // Daily Check-in Card (Page 6-7)
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "إنت عامل إيه النهارده؟",
              style = MaterialTheme.typography.titleLarge,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )

            if (todayCheckin != null) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(SageGreenPrimary.copy(alpha = 0.2f))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = DarkGreen,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "تم التسجيل اليوم",
                    style = MaterialTheme.typography.labelMedium,
                    color = DarkGreen,
                    fontWeight = FontWeight.Medium
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Mood Emojis Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            moods.forEach { mood ->
              val isSelected = todayCheckin?.moodValue == mood.label

              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                  .clickable {
                    onRecordMood(mood.label, todayCheckin?.note)
                    showNoteField = true
                  }
                  .padding(4.dp)
                  .testTag("mood_${mood.label}")
              ) {
                Box(
                  modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                      if (isSelected) mood.color.copy(alpha = 0.35f)
                      else WarmBeige.copy(alpha = 0.4f)
                    )
                    .border(
                      width = if (isSelected) 2.dp else 1.dp,
                      color = if (isSelected) DarkGreen else Color.Transparent,
                      shape = CircleShape
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = mood.emoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = mood.label,
                  style = MaterialTheme.typography.labelMedium,
                  color = if (isSelected) DarkGreen else TextDark,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              }
            }
          }

          // Optional Note Expansion
          if (showNoteField || (todayCheckin != null && !todayCheckin.note.isNullOrBlank())) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
              value = checkinNote.ifEmpty { todayCheckin?.note ?: "" },
              onValueChange = { checkinNote = it },
              placeholder = { Text("أضف ملاحظة قصيرة عن يومك (اختياري)...", fontSize = 13.sp) },
              modifier = Modifier.fillMaxWidth(),
              maxLines = 2,
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkGreen,
                unfocusedBorderColor = WarmBeige
              )
            )

            if (checkinNote.isNotBlank() && checkinNote != todayCheckin?.note) {
              Spacer(modifier = Modifier.height(8.dp))
              Button(
                onClick = {
                  val currentMood = todayCheckin?.moodValue ?: "كويس"
                  onRecordMood(currentMood, checkinNote)
                  showNoteField = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.End)
              ) {
                Text("حفظ الملاحظة", fontSize = 12.sp)
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Non-diagnostic legal note
          Text(
            text = "هذا الـ Check-in: ليس تشخيصاً. يمكن تسجيله اختيارياً لمتابعة رحلتك في صفحة «رحلتي».",
            style = MaterialTheme.typography.labelMedium,
            color = TextDark.copy(alpha = 0.6f),
            lineHeight = 16.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }

    // المرحلة الجديدة: بطاقة رئيسية واضحة "🌿 أنا دلوقتي..."
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToRightNow() }
          .padding(bottom = 20.dp)
          .border(2.dp, SageGreenPrimary, RoundedCornerShape(22.dp))
          .testTag("home_right_now_card")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "🌿 أنا دلوقتي...",
            style = MaterialTheme.typography.headlineSmall,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "\"مش لازم تحل كل حاجة دلوقتي... خلينا نبدأ من اللي حاصل معاك الآن.\"",
            style = MaterialTheme.typography.bodyLarge,
            color = TextDark,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 25.sp
          )
          Spacer(modifier = Modifier.height(14.dp))
          Button(
            onClick = onNavigateToRightNow,
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("home_right_now_start_button")
          ) {
            Text(
              text = "ابدأ",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }
        }
      }
    }

    // Section 6: 🌱 نسمة اليوم
    item {
      NesmatTodayCard(
        onSaveResponseLocally = onSaveNesmatToday,
        modifier = Modifier.padding(bottom = 20.dp)
      )
    }

    // كبسولة «🌙 قبل النوم بـ 5 دقائق» (تفريغ الوسادة)
    item {
      BeforeSleepCapsuleCard(
        onSaveCapsuleLocally = onSaveNesmatToday,
        onNavigateToBreathe = onNavigateToBreathe,
        modifier = Modifier.padding(bottom = 20.dp)
      )
    }

    // Section 1: 🌿 مكتبة نسمة حياة (المقالات العشرة + التمارين + خطط الإنقاذ)
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToLibrary() }
          .padding(bottom = 20.dp)
          .border(1.5.dp, DarkGreen.copy(alpha = 0.25f), RoundedCornerShape(22.dp))
          .testTag("home_nesmat_library_card")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(SageGreenPrimary.copy(alpha = 0.28f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.MenuBook,
                  contentDescription = null,
                  tint = DarkGreen,
                  modifier = Modifier.size(24.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "🌿 مكتبة نسمة حياة",
                  style = MaterialTheme.typography.titleLarge,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "١٠ مقالات وتمارين + 🌿 خطط الإنقاذ + 📘 الكتيب وخطتي",
                  style = MaterialTheme.typography.labelMedium,
                  color = SageGreenPrimary,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "\"كثيرون يعيشون الحياة... وقليلون يستمتعون بها\" — نحو مجتمع يهتم بالصحة النفسية كما يهتم بالصحة الجسدية.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextDark.copy(alpha = 0.85f),
            lineHeight = 22.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = onNavigateToLibrary,
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("home_open_nesmat_library_button")
          ) {
            Text(
              text = "افتح مكتبة نسمة حياة وخطط الإنقاذ ←",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
        }
      }
    }

    // Creative Feature: Daily Wisdom Capsule
    item {
      DailyWisdomCapsule(
        onCopied = onWisdomCopied,
        modifier = Modifier.padding(bottom = 20.dp)
      )
    }

    // Creative Feature: Sound Oasis Spotlight Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToOasis() }
          .padding(bottom = 24.dp)
          .testTag("home_sound_oasis_card")
      ) {
        Row(
          modifier = Modifier.padding(18.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(SageGreenPrimary.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(26.dp)
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "واحة السكينة الصوتية 🌿",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(DarkGreen)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "جديد",
                  color = Color.White,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = "اخلط أصوات المطر، والأمواج، ونسيم الزيتون للتركيز والنوم",
              style = MaterialTheme.typography.bodySmall,
              color = TextDark.copy(alpha = 0.75f),
              lineHeight = 18.sp
            )
          }

          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = DarkGreen,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }

    // Featured Hero: جوايا حكاية — العالم اللي بيشرح نفسه بنفسه
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToGwaya() }
          .padding(bottom = 24.dp)
          .testTag("home_gwaya_card")
      ) {
        Column(
          modifier = Modifier.padding(20.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "ن",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "جوايا حكاية 📖",
                  style = MaterialTheme.typography.titleMedium,
                  color = Color.White,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "العالم اللي بيشرح نفسه بنفسه",
                  style = MaterialTheme.typography.labelSmall,
                  color = SoftMint
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(SageGreenPrimary)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "مساحة آمنة للفضول",
                color = DarkGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "رحلة ذاتية عبر ٦ محطات هادئة لتفكيك ما يدور في داخلك: الموقف الحقيقي، الفكرة التلقائية، نغمة الشعور، إشارة الجسد، والاحتياج الأصيل، وصولاً لصياغة أكثر رأفة.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.85f),
            lineHeight = 20.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "نسمة: أنا معاكي… ناخدها واحدة واحدة 🌿",
              style = MaterialTheme.typography.labelSmall,
              color = SoftMint.copy(alpha = 0.9f)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "ابدأ الرحلة",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }

    // 4 Primary Sections Grid (Page 7)
    item {
      Text(
        text = "الأقسام الرئيسية",
        style = MaterialTheme.typography.titleLarge,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(12.dp))

      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ActionTile(
            title = "أفهم نفسي",
            subtitle = "مكتبة المحتوى والتوعية",
            icon = Icons.Default.MenuBook,
            color = SageGreenPrimary,
            modifier = Modifier.weight(1f),
            testTag = "home_explore_tile",
            onClick = onNavigateToExplore
          )
          ActionTile(
            title = "محتاج أتكلم",
            subtitle = "طلب الدعم والتوجيه",
            icon = Icons.Default.SupportAgent,
            color = WarmBeige,
            modifier = Modifier.weight(1f),
            testTag = "home_support_tile",
            onClick = onNavigateToSupport
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ActionTile(
            title = "قيّم حالتك",
            subtitle = "استكشاف وفحص مطمئن",
            icon = Icons.Default.Assignment,
            color = WarmBeige,
            modifier = Modifier.weight(1f),
            testTag = "home_assessment_tile",
            onClick = onNavigateToAssessment
          )
          ActionTile(
            title = "خد نفس",
            subtitle = "تمارين التهدئة والتنفس",
            icon = Icons.Default.Air,
            color = SageGreenPrimary,
            modifier = Modifier.weight(1f),
            testTag = "home_breathe_tile",
            onClick = onNavigateToBreathe
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ActionTile(
            title = "خفة ولعب 🕊️",
            subtitle = "إطلاق الهموم، التجذير، والامتنان",
            icon = Icons.Default.Gamepad,
            color = SoftMint,
            modifier = Modifier.weight(1f),
            testTag = "home_light_tile",
            onClick = onNavigateToLight
          )
          ActionTile(
            title = "واحة الأصوات 🌿",
            subtitle = "مطر، أمواج، نسيم الزيتون، و 432Hz",
            icon = Icons.Default.GraphicEq,
            color = WarmBeige,
            modifier = Modifier.weight(1f),
            testTag = "home_oasis_tile",
            onClick = onNavigateToOasis
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }

    // Gemini AI Features Section
    item {
      Text(
        text = "أدوات الذكاء الاصطناعي المساندة ✨",
        style = MaterialTheme.typography.titleLarge,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(10.dp))

      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToChat() }
          .testTag("home_ai_chat_card")
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(DarkGreen),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Spa,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "رفيق نسمة الحياة الذكي 🌿",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "محادثة هادئة، بحث Google الموثوق، وإدخال صوتي",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.7f)
            )
          }

          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = DarkGreen
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToImageStudio() }
          .testTag("home_ai_image_card")
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(SageGreenPrimary.copy(alpha = 0.35f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "استوديو السكينة البصرية 🎨",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "صمم لوحات تأملية وطبيعية بـ Gemini 3.1 Flash Image",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.7f)
            )
          }

          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = DarkGreen
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))
    }

    // Suggested Content (Page 7: ممكن يهمك)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "ممكن يهمك 🌱",
            style = MaterialTheme.typography.titleLarge,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "محتوى مقترح بناءً على اهتماماتك بدون استنتاجات تشخيصية",
            style = MaterialTheme.typography.labelMedium,
            color = TextDark.copy(alpha = 0.7f)
          )
        }

        IconButton(onClick = onNavigateToExplore) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "عرض المزيد من المحتوى",
            tint = DarkGreen
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
    }

    if (suggestedContent.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "«لسه بنجهز محتوى جديد ليك 🌱»",
            style = MaterialTheme.typography.bodyMedium,
            color = TextDark.copy(alpha = 0.8f),
            modifier = Modifier.padding(20.dp)
          )
        }
      }
    } else {
      items(suggestedContent, key = { it.id }) { item ->
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onContentClick(item.id) }
            .testTag("suggested_item_${item.id}")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SageGreenPrimary.copy(alpha = 0.25f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = item.category,
                    style = MaterialTheme.typography.labelMedium,
                    color = DarkGreen,
                    fontWeight = FontWeight.SemiBold
                  )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "• ${item.duration}",
                  style = MaterialTheme.typography.labelMedium,
                  color = TextDark.copy(alpha = 0.6f)
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

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
                maxLines = 2
              )
            }

            IconButton(
              onClick = { onToggleFavorite(item.id, item.isFavorite) }
            ) {
              Icon(
                imageVector = if (item.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = if (item.isFavorite) "إزالة من المحفوظات" else "حفظ في المحفوظات",
                tint = if (item.isFavorite) DarkGreen else TextDark.copy(alpha = 0.4f)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun ActionTile(
  title: String,
  subtitle: String,
  icon: ImageVector,
  color: Color,
  modifier: Modifier = Modifier,
  testTag: String,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier
      .clickable { onClick() }
      .testTag(testTag)
  ) {
    Column(
      modifier = Modifier.padding(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = DarkGreen,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelMedium,
        color = TextDark.copy(alpha = 0.7f),
        lineHeight = 16.sp
      )
    }
  }
}

@Composable
private fun BeforeSleepCapsuleCard(
  onSaveCapsuleLocally: (String, String) -> Unit,
  onNavigateToBreathe: () -> Unit,
  modifier: Modifier = Modifier
) {
  var expanded by remember { mutableStateOf(false) }
  var tomorrowDrawerThought by remember { mutableStateOf("") }
  var peacefulThingToday by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .border(1.dp, SageGreenPrimary.copy(alpha = 0.4f), RoundedCornerShape(22.dp))
      .testTag("home_before_sleep_capsule_card")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { expanded = !expanded },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(SoftMint.copy(alpha = 0.55f)),
            contentAlignment = Alignment.Center
          ) {
            Text("🌙", fontSize = 22.sp)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "كبسولة «قبل النوم بـ ٥ دقائق»",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "فرّغ وسادتك من التفكير قبل النوم واحفظها في درج الغد",
              style = MaterialTheme.typography.labelMedium,
              color = TextDark.copy(alpha = 0.75f)
            )
          }
        }
        TextButton(onClick = { expanded = !expanded }) {
          Text(
            text = if (expanded) "طي" else "افتح",
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
        }
      }

      AnimatedVisibility(visible = expanded) {
        Column(
          modifier = Modifier.padding(top = 12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text(
            text = "١. درج الغد: ما الفكرة التي يمكنك تركها الآن ومتابعتها غدًا؟",
            style = MaterialTheme.typography.labelLarge,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          OutlinedTextField(
            value = tomorrowDrawerThought,
            onValueChange = { tomorrowDrawerThought = it; isSaved = false },
            placeholder = { Text("اكتب الفكرة هنا لتخرج من رأسك قبل النوم...", fontSize = 13.sp) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = DarkGreen,
              unfocusedBorderColor = WarmBeige,
              focusedContainerColor = CreamBackground,
              unfocusedContainerColor = CreamBackground
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Text(
            text = "٢. شيء واحد صغير مرّ بسلام أو لطف اليوم:",
            style = MaterialTheme.typography.labelLarge,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          OutlinedTextField(
            value = peacefulThingToday,
            onValueChange = { peacefulThingToday = it; isSaved = false },
            placeholder = { Text("مثال: كوب شاي دافئ، محادثة لطيفة، أو إنهاء مهمة...", fontSize = 13.sp) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = DarkGreen,
              unfocusedBorderColor = WarmBeige,
              focusedContainerColor = CreamBackground,
              unfocusedContainerColor = CreamBackground
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                val summary = """
                  • في درج الغد (فكرة تركتها للغد): $tomorrowDrawerThought
                  • شيء مرّ بسلام اليوم: $peacefulThingToday
                """.trimIndent()
                onSaveCapsuleLocally("🌙 كبسولة قبل النوم (تفريغ الوسادة)", summary)
                isSaved = true
              },
              colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = if (isSaved) "✓ تم الحفظ في حياتي" else "حفظ تفريغ الوسادة",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }

            OutlinedButton(
              onClick = onNavigateToBreathe,
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("تنفس دقيقتين 🌬️", fontSize = 12.sp, color = DarkGreen, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
