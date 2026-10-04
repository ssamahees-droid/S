package com.example.ui.screens.gwaya

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

// Stage Definitions for "جوايا حكاية"
data class GwayaStage(
  val id: String,
  val stageIndex: Int,
  val label: String,
  val short: String,
  val eyebrow: String,
  val title: String,
  val description: String
)

data class ThoughtItem(
  val key: String,
  val kind: String,
  val title: String,
  val text: String,
  val icon: ImageVector,
  val color: Color
)

data class FeelingItem(
  val key: String,
  val label: String,
  val note: String,
  val color: Color,
  val emoji: String
)

data class BodySignal(
  val key: String,
  val label: String,
  val part: String,
  val icon: String
)

data class NeedItem(
  val key: String,
  val title: String,
  val text: String,
  val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GwayaHekayaScreen(
  onSaveToJourney: (String, String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var currentStageIndex by remember { mutableIntStateOf(0) } // 0 to 6 (6 is finish/journal)

  // User Selections
  var chosenSituation by remember { mutableStateOf("رسالة اتبعتت وماتردش عليها بقالها ساعات...") }
  var customSituation by remember { mutableStateOf("") }
  var selectedThoughtKey by remember { mutableStateOf<String?>("interpretation") }
  var selectedFeelingKey by remember { mutableStateOf<String?>("worry") }
  var selectedBodySignalKey by remember { mutableStateOf<String?>("chest") }
  var selectedNeedKey by remember { mutableStateOf<String?>("clarity") }
  var reframeAnswer by remember { mutableStateOf("") }

  val stages = remember {
    listOf(
      GwayaStage(
        id = "fact",
        stageIndex = 1,
        label = "الموقف",
        short = "حصل إيه؟",
        eyebrow = "المحطة ٠١ · نبدأ من الأرض",
        title = "نمسك اللي حصل… من غير ما نزوده",
        description = "في العالم اللي جواك، أول خيط دايمًا هو الحقيقة الصغيرة اللي نقدر نشوفها سوا."
      ),
      GwayaStage(
        id = "thought",
        stageIndex = 2,
        label = "الفكرة",
        short = "قال إيه؟",
        eyebrow = "المحطة ٠٢ · حديقة الأفكار",
        title = "الأفكار بتظهر بسرعة… بس مش كلها حقائق",
        description = "اختار فكرة واحدة لمستك. مش مطلوب تحكم عليها، بس نشوف نوعها بهدوء."
      ),
      GwayaStage(
        id = "feeling",
        stageIndex = 3,
        label = "الشعور",
        short = "حسيت بإيه؟",
        eyebrow = "المحطة ٠٣ · نغمة الشعور",
        title = "لو الشعور له لون وصوت، هيبقى إيه؟",
        description = "الشعور مش مشكلة لازم تختفي؛ هو رسالة بتحاول تقول لك حاجة."
      ),
      GwayaStage(
        id = "body",
        stageIndex = 4,
        label = "الجسم",
        short = "بان فين؟",
        eyebrow = "المحطة ٠٤ · إشارات الجسم",
        title = "الجسم كان بيحكي إيه؟",
        description = "قبل ما نسمي كل حاجة، نلاحظ: فين ظهرت الحكاية في جسمك؟"
      ),
      GwayaStage(
        id = "need",
        stageIndex = 5,
        label = "الاحتياج",
        short = "محتاج إيه؟",
        eyebrow = "المحطة ٠٥ · ما وراء الشعور",
        title = "وراء كل شعور… احتياج يستاهل يتسمع",
        description = "اختار الفانوس الأقرب لك دلوقتي. الاحتياج مش دلع، هو بوصلة."
      ),
      GwayaStage(
        id = "reframe",
        stageIndex = 6,
        label = "نظرة أوسع",
        short = "ممكن أشوف؟",
        eyebrow = "المحطة ٠٦ · نافذة جديدة",
        title = "نفتح شباكًا صغيرًا لاحتمال تاني",
        description = "مش لازم نكذّب إحساسنا؛ نجرب بس نوسّع الصورة سنة."
      ),
      GwayaStage(
        id = "finish",
        stageIndex = 7,
        label = "الدفتر",
        short = "دفتر الرحلة",
        eyebrow = "اكتملت الرحلة · خيط جديد في إيدك",
        title = "أنت بدأت تسمع الحكاية من جوّا",
        description = "مش لازم تخرج بإجابة نهائية. يكفي إنك بقيت قادر تلاحظ."
      )
    )
  }

  val activeStage = stages[currentStageIndex]

  val situationPresets = listOf(
    "💬 رسالة اتبعتت وماتردش عليها بقالها ساعات",
    "📚 امتحان أو تقييم قريب ودماغي مليانة سيناريوهات",
    "🌧️ خطة كنت معتمد عليها اتلغت أو ما مشيتش زي ما تمنيت",
    "👥 حد اتكلم بطريقة حسيتها غريبة أو باردة شوية"
  )

  val thoughts = listOf(
    ThoughtItem(
      key = "fact",
      kind = "حقيقة الموقف",
      title = "هو ما ردش لسه",
      text = "دي الواقعة المجردة اللي حصلت لحد دلوقتي بدون أي استنتاجات مسبقة.",
      icon = Icons.Default.MenuBook,
      color = SageGreenPrimary
    ),
    ThoughtItem(
      key = "interpretation",
      kind = "تفسير ذهني",
      title = "أكيد زعلان مني أو مطنشني",
      text = "تخمين سريع من دماغي لتفسير الموقف بناء على مخاوف سابقة.",
      icon = Icons.Default.CloudQueue,
      color = WarmBeige
    ),
    ThoughtItem(
      key = "fear",
      kind = "خوف أعمق",
      title = "هخسره أو هكون لوحدي ومحدش مهتم",
      text = "المخ بيكبّر الاحتمال ويفترض الأسوأ عشان يحميني من الصدمة.",
      icon = Icons.Default.Whatshot,
      color = MoodDifficult
    )
  )

  val feelings = listOf(
    FeelingItem(key = "worry", label = "قلق وترقب", note = "ترقب وعدم ارتياح في الصدر", color = WarmBeige, emoji = "🌊"),
    FeelingItem(key = "sad", label = "حزن خفيف", note = "إحساس بالمسافة أو الإحباط", color = Color(0xFFD6C7B2), emoji = "🌧️"),
    FeelingItem(key = "confused", label = "حيرة وتشتت", note = "تفكير رايح جاي وأسئلة كتير", color = SoftMint, emoji = "🌀"),
    FeelingItem(key = "disappointed", label = "خذلان أو عتاب", note = "كنت مستني حاجة مختلفة", color = MoodDifficult.copy(alpha = 0.6f), emoji = "🍂")
  )

  val bodySignals = listOf(
    BodySignal(key = "chest", label = "نَفَسي قصير وسريع شوية", part = "الصدر والرئتان", icon = "🫁"),
    BodySignal(key = "shoulders", label = "كتافي ورقبتي مشدودين", part = "الكتف والرقبة", icon = "🧍"),
    BodySignal(key = "stomach", label = "معدتي متكتفة ومقبوضة", part = "البطن والجهاز الهضمي", icon = "🫄"),
    BodySignal(key = "head", label = "دماغي بتزن وفيها ثقل", part = "الرأس والجبين", icon = "🧠")
  )

  val needs = listOf(
    NeedItem(key = "clarity", title = "وضوح 💡", text = "أحب أعرف إحنا واقفين فين بالضبط بدون ضباب.", icon = Icons.Default.Lightbulb),
    NeedItem(key = "safety", title = "أمان وثقة 🤍", text = "أحتاج أفتكر إن قيمتي ثابتة حتى لو الموقف متلخبط.", icon = Icons.Default.Favorite),
    NeedItem(key = "space", title = "مساحة ووقت 🕊️", text = "أدي لنفسي مسافة قبل ما أحكم أو أستنتج فوراً.", icon = Icons.Default.Spa),
    NeedItem(key = "step", title = "خطوة صغيرة 👣", text = "أبدأ بحركة واحدة بسيطة أقدر أعملها دلوقتي.", icon = Icons.Default.SelfImprovement)
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    // App Top Bar
    TopAppBar(
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(DarkGreen),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "ن",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "جوايا حكاية",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "نسمة الحياة · العالم اللي بيشرح نفسه بنفسه",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.65f)
            )
          }
        }
      },
      navigationIcon = {
        IconButton(
          onClick = {
            if (currentStageIndex > 0) currentStageIndex-- else onBack()
          },
          modifier = Modifier.testTag("gwaya_back_button")
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

    // Progress Bar (6 Stages)
    LinearProgressIndicator(
      progress = { (currentStageIndex + 1) / stages.size.toFloat() },
      color = DarkGreen,
      trackColor = WarmBeige.copy(alpha = 0.4f),
      modifier = Modifier
        .fillMaxWidth()
        .height(4.dp)
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Stage Header Card
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(SageGreenPrimary.copy(alpha = 0.25f))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = activeStage.eyebrow,
                  style = MaterialTheme.typography.labelSmall,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )
              }

              Text(
                text = "٠${currentStageIndex + 1} / ٠${stages.size}",
                style = MaterialTheme.typography.labelSmall,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = activeStage.title,
              style = MaterialTheme.typography.titleLarge,
              color = DarkGreen,
              fontWeight = FontWeight.Bold,
              lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = activeStage.description,
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark.copy(alpha = 0.75f),
              lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mascot Note
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(WarmBeige.copy(alpha = 0.35f))
                .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Text(text = "🌿", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "نسمة: أنا معاكي… ناخدها خطوة خطوة، مفيش إجابة صح أو غلط.",
                style = MaterialTheme.typography.labelMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }

      // Stage Specific Content
      when (currentStageIndex) {
        0 -> {
          // STAGE 1: FACT (الموقف)
          item {
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Text(
                  text = "اختر موقفاً مألوفاً، أو اكتب ما حدث معك:",
                  style = MaterialTheme.typography.titleSmall,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )

                situationPresets.forEach { preset ->
                  val isSelected = chosenSituation == preset
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(14.dp))
                      .background(if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.3f))
                      .clickable {
                        chosenSituation = preset
                        customSituation = ""
                      }
                      .padding(14.dp)
                  ) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Text(
                        text = preset,
                        color = if (isSelected) Color.White else TextDark,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        lineHeight = 20.sp,
                        modifier = Modifier.weight(1f)
                      )
                      if (isSelected) {
                        Icon(
                          imageVector = Icons.Default.Check,
                          contentDescription = null,
                          tint = Color.White,
                          modifier = Modifier.size(18.dp)
                        )
                      }
                    }
                  }
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                  value = customSituation,
                  onValueChange = {
                    customSituation = it
                    if (it.isNotBlank()) chosenSituation = it
                  },
                  placeholder = { Text("أو اكتب الموقف بكلماتك البسيطة...", fontSize = 13.sp) },
                  modifier = Modifier.fillMaxWidth(),
                  maxLines = 3,
                  shape = RoundedCornerShape(14.dp),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    unfocusedBorderColor = WarmBeige
                  )
                )
              }
            }
          }
        }

        1 -> {
          // STAGE 2: THOUGHT (الفكرة)
          items(thoughts) { thought ->
            val isSelected = selectedThoughtKey == thought.key
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isSelected) DarkGreen else Color.White
              ),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedThoughtKey = thought.key }
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = thought.icon,
                      contentDescription = null,
                      tint = if (isSelected) Color.White else DarkGreen,
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = thought.kind,
                      color = if (isSelected) SoftMint else DarkGreen,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                  if (isSelected) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = thought.title,
                  style = MaterialTheme.typography.titleMedium,
                  color = if (isSelected) Color.White else DarkGreen,
                  fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = thought.text,
                  style = MaterialTheme.typography.bodySmall,
                  color = if (isSelected) Color.White.copy(alpha = 0.85f) else TextDark.copy(alpha = 0.7f),
                  lineHeight = 18.sp
                )
              }
            }
          }
        }

        2 -> {
          // STAGE 3: FEELING (الشعور)
          item {
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Text(
                  text = "ما نغمة الشعور التي سكنتك في تلك اللحظة؟",
                  style = MaterialTheme.typography.titleSmall,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )

                feelings.forEach { feeling ->
                  val isSelected = selectedFeelingKey == feeling.key
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(14.dp))
                      .background(if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.35f))
                      .clickable { selectedFeelingKey = feeling.key }
                      .padding(14.dp)
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.SpaceBetween,
                      modifier = Modifier.fillMaxWidth()
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = feeling.emoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                          Text(
                            text = feeling.label,
                            color = if (isSelected) Color.White else DarkGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                          )
                          Text(
                            text = feeling.note,
                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else TextDark.copy(alpha = 0.65f),
                            fontSize = 11.sp
                          )
                        }
                      }
                      if (isSelected) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                      }
                    }
                  }
                }
              }
            }
          }
        }

        3 -> {
          // STAGE 4: BODY (الجسم)
          item {
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Text(
                  text = "أين سجل جسدك هذا الموقف؟",
                  style = MaterialTheme.typography.titleSmall,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )

                bodySignals.forEach { signal ->
                  val isSelected = selectedBodySignalKey == signal.key
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(14.dp))
                      .background(if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.35f))
                      .clickable { selectedBodySignalKey = signal.key }
                      .padding(14.dp)
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.SpaceBetween,
                      modifier = Modifier.fillMaxWidth()
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = signal.icon, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                          Text(
                            text = signal.label,
                            color = if (isSelected) Color.White else DarkGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                          )
                          Text(
                            text = "المنطقة: ${signal.part}",
                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else TextDark.copy(alpha = 0.65f),
                            fontSize = 11.sp
                          )
                        }
                      }
                      if (isSelected) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                      }
                    }
                  }
                }
              }
            }
          }
        }

        4 -> {
          // STAGE 5: NEED (الاحتياج)
          items(needs) { need ->
            val isSelected = selectedNeedKey == need.key
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isSelected) DarkGreen else Color.White
              ),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedNeedKey = need.key }
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White.copy(alpha = 0.2f) else WarmBeige.copy(alpha = 0.5f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = need.icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else DarkGreen,
                    modifier = Modifier.size(22.dp)
                  )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = need.title,
                    color = if (isSelected) Color.White else DarkGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = need.text,
                    color = if (isSelected) Color.White.copy(alpha = 0.85f) else TextDark.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                  )
                }
              }
            }
          }
        }

        5 -> {
          // STAGE 6: REFRAME (نظرة أوسع)
          item {
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
              ) {
                Text(
                  text = "مقارنة الصياغتين (تفكيك الحكاية)",
                  style = MaterialTheme.typography.titleMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )

                // Old Story
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MoodDifficult.copy(alpha = 0.15f))
                    .padding(14.dp)
                ) {
                  Column {
                    Text(
                      text = "القصة القديمة التلقائية ⚡",
                      style = MaterialTheme.typography.labelSmall,
                      color = DarkGreen,
                      fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = "«أكيد أنا مش مهم… أو عملت غلطة خلت الأمور تبوظ»",
                      style = MaterialTheme.typography.bodyMedium,
                      color = TextDark,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                }

                // New Reframe Story
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SageGreenPrimary.copy(alpha = 0.25f))
                    .padding(14.dp)
                ) {
                  Column {
                    Text(
                      text = "القصة الجديدة الأكثر اتساعاً ورأفة 🌱",
                      style = MaterialTheme.typography.labelSmall,
                      color = DarkGreen,
                      fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = "«أنا متضايق عشان الموقف ده مهم بالنسبة لي… ولسه في احتمالات كتير مفتوحة. أقدر آخد نَفَس وأستنى بوضوح دون أن أستنتج الأسوأ.»",
                      style = MaterialTheme.typography.bodyMedium,
                      color = DarkGreen,
                      fontWeight = FontWeight.Bold,
                      lineHeight = 22.sp
                    )
                  }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = "اكتب كلمة واحدة أو خطوة لطيفة تود إهداءها لنفسك الآن:",
                  style = MaterialTheme.typography.labelMedium,
                  color = TextDark.copy(alpha = 0.8f)
                )

                OutlinedTextField(
                  value = reframeAnswer,
                  onValueChange = { reframeAnswer = it },
                  placeholder = { Text("مثال: مهلة هدوء، فنجان شاي، كلام طيب...", fontSize = 13.sp) },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(14.dp),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    unfocusedBorderColor = WarmBeige
                  )
                )
              }
            }
          }
        }

        6 -> {
          // FINISH: JOURNAL SUMMARY (دفتر الرحلة)
          item {
            Card(
              shape = RoundedCornerShape(22.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = "📖✨", fontSize = 24.sp)
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "دفتر رحلة «جوايا حكاية»",
                      style = MaterialTheme.typography.titleMedium,
                      color = DarkGreen,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = "ملخص الاستبصار والتفكيك الذاتي",
                      style = MaterialTheme.typography.labelSmall,
                      color = TextDark.copy(alpha = 0.65f)
                    )
                  }
                }

                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(WarmBeige.copy(alpha = 0.35f))
                    .padding(16.dp)
                ) {
                  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row {
                      Text(text = "• الموقف الواقعي: ", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 12.sp)
                      Text(text = chosenSituation, color = TextDark, fontSize = 12.sp)
                    }
                    Row {
                      Text(text = "• نغمة الشعور: ", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 12.sp)
                      Text(text = feelings.find { it.key == selectedFeelingKey }?.label ?: "قلق", color = TextDark, fontSize = 12.sp)
                    }
                    Row {
                      Text(text = "• إشارة الجسد: ", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 12.sp)
                      Text(text = bodySignals.find { it.key == selectedBodySignalKey }?.label ?: "نفس قصير", color = TextDark, fontSize = 12.sp)
                    }
                    Row {
                      Text(text = "• الاحتياج الحقيقي: ", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 12.sp)
                      Text(text = needs.find { it.key == selectedNeedKey }?.title ?: "وضوح", color = TextDark, fontSize = 12.sp)
                    }
                    if (reframeAnswer.isNotBlank()) {
                      Row {
                        Text(text = "• رسالتي لنفسي: ", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 12.sp)
                        Text(text = reframeAnswer, color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                      }
                    }
                  }
                }

                Text(
                  text = "نسمة: مبروك وصولك لنهاية هذه الرحلة 🤍 لقد ألقيت ضوءاً دافئاً على ما كان مظلماً ومخيفاً.",
                  style = MaterialTheme.typography.bodySmall,
                  color = DarkGreen,
                  fontWeight = FontWeight.Medium,
                  textAlign = TextAlign.Center,
                  modifier = Modifier.fillMaxWidth()
                )

                Button(
                  onClick = {
                    val summaryText = """
                      الموقف: $chosenSituation
                      الشعور: ${feelings.find { it.key == selectedFeelingKey }?.label ?: ""}
                      الجسد: ${bodySignals.find { it.key == selectedBodySignalKey }?.label ?: ""}
                      الاحتياج: ${needs.find { it.key == selectedNeedKey }?.title ?: ""}
                      الرسالة: ${if (reframeAnswer.isNotBlank()) reframeAnswer else "أنا في سلام ومحاط بالرحمة"}
                    """.trimIndent()
                    onSaveToJourney("رحلة استبصار: جوايا حكاية 🌿", summaryText)
                    onBack()
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                  shape = RoundedCornerShape(14.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("حفظ في رحلتي 🤍", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      // Next / Previous Navigation Buttons
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (currentStageIndex > 0) {
            OutlinedButton(
              onClick = { currentStageIndex-- },
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("المحطة السابقة", color = DarkGreen, fontSize = 12.sp)
            }
          } else {
            Spacer(modifier = Modifier.width(8.dp))
          }

          if (currentStageIndex < stages.size - 1) {
            Button(
              onClick = { currentStageIndex++ },
              colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = if (currentStageIndex == stages.size - 2) "عرض دفتر الرحلة ✨" else "المحطة التالية ←",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}
