package com.example.ui.screens.library

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@Composable
fun RescuePlansContentList(
  onSaveToJourney: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var expandedPlanId by remember { mutableStateOf<String?>("distress") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 20.dp),
    contentPadding = PaddingValues(top = 14.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // مقدمة القسم
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.5.dp, SageGreenPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
          .testTag("rescue_plans_intro_card")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "🌿 خطط الإنقاذ — نسمة حياة",
            style = MaterialTheme.typography.titleLarge,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(10.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(SoftMint.copy(alpha = 0.45f))
              .padding(14.dp)
          ) {
            Text(
              text = "مش لازم تحل حياتك كلها دلوقتي.\nأحيانًا محتاج بس تعدّي الدقائق الصعبة بطريقة أهدى.",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold,
              lineHeight = 25.sp
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "خطط الإنقاذ مش علاج، ومش تشخيص.\nهي خطوات صغيرة تساعدك تتعامل مع اللحظة الحالية، وتعرف إيه الخطوة التالية.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextDark.copy(alpha = 0.85f),
            lineHeight = 23.sp
          )
        }
      }
    }

    // 1. 🫂 خطة وقت الضيق
    item {
      ExpandableRescuePlanContainer(
        id = "distress",
        emoji = "🫂",
        title = "خطة وقت الضيق",
        subtitle = "لما تحس إن كل حاجة كتير عليك",
        isExpanded = expandedPlanId == "distress",
        onToggle = { expandedPlanId = if (expandedPlanId == "distress") null else "distress" }
      ) {
        DistressPlanBody(onSaveToJourney = onSaveToJourney)
      }
    }

    // 2. 🌀 خطة الحيرة
    item {
      ExpandableRescuePlanContainer(
        id = "confusion",
        emoji = "🌀",
        title = "خطة الحيرة",
        subtitle = "لما كل حاجة تبان مهمة ومش عارف تبدأ منين",
        isExpanded = expandedPlanId == "confusion",
        onToggle = { expandedPlanId = if (expandedPlanId == "confusion") null else "confusion" }
      ) {
        ConfusionPlanBody(onSaveToJourney = onSaveToJourney)
      }
    }

    // 3. 😑 خطة الزهق
    item {
      ExpandableRescuePlanContainer(
        id = "boredom",
        emoji = "😑",
        title = "خطة الزهق",
        subtitle = "لما اليوم كله يبقى \"مفيش حاجة\"",
        isExpanded = expandedPlanId == "boredom",
        onToggle = { expandedPlanId = if (expandedPlanId == "boredom") null else "boredom" }
      ) {
        BoredomPlanBody(onSaveToJourney = onSaveToJourney)
      }
    }

    // 4. 🔥 خطة الانفعال
    item {
      ExpandableRescuePlanContainer(
        id = "anger",
        emoji = "🔥",
        title = "خطة الانفعال",
        subtitle = "لما تكون متعصب وممكن تقول أو تعمل حاجة تندم عليها",
        isExpanded = expandedPlanId == "anger",
        onToggle = { expandedPlanId = if (expandedPlanId == "anger") null else "anger" }
      ) {
        AngerPlanBody(onSaveToJourney = onSaveToJourney)
      }
    }

    // 5. 🧠 خطة التفكير الزائد
    item {
      ExpandableRescuePlanContainer(
        id = "overthinking",
        emoji = "🧠",
        title = "خطة التفكير الزائد",
        subtitle = "لما دماغك تفضل شغالة ومش عارفة تقفل",
        isExpanded = expandedPlanId == "overthinking",
        onToggle = { expandedPlanId = if (expandedPlanId == "overthinking") null else "overthinking" }
      ) {
        OverthinkingPlanBody(onSaveToJourney = onSaveToJourney)
      }
    }

    // 6. 🔋 خطة انخفاض الطاقة
    item {
      ExpandableRescuePlanContainer(
        id = "low_energy",
        emoji = "🔋",
        title = "خطة انخفاض الطاقة",
        subtitle = "لما تكون البطارية خلصانة قبل ما اليوم يخلص",
        isExpanded = expandedPlanId == "low_energy",
        onToggle = { expandedPlanId = if (expandedPlanId == "low_energy") null else "low_energy" }
      ) {
        LowEnergyPlanBody(onSaveToJourney = onSaveToJourney)
      }
    }

    // 7. 🚪 خطة العزلة
    item {
      ExpandableRescuePlanContainer(
        id = "isolation",
        emoji = "🚪",
        title = "خطة العزلة",
        subtitle = "لما تحس إنك مش عايز تشوف حد أو تتكلم مع حد",
        isExpanded = expandedPlanId == "isolation",
        onToggle = { expandedPlanId = if (expandedPlanId == "isolation") null else "isolation" }
      ) {
        IsolationPlanBody(onSaveToJourney = onSaveToJourney)
      }
    }

    // 8. 🌧️ خطة "مش قادر أعمل حاجة"
    item {
      ExpandableRescuePlanContainer(
        id = "cant_do_anything",
        emoji = "🌧️",
        title = "خطة \"مش قادر أعمل حاجة\"",
        subtitle = "لو وصلت لمرحلة إن حتى أبسط حاجة تقيلة",
        isExpanded = expandedPlanId == "cant_do_anything",
        onToggle = {
          expandedPlanId = if (expandedPlanId == "cant_do_anything") null else "cant_do_anything"
        }
      ) {
        CantDoAnythingPlanBody(onSaveToJourney = onSaveToJourney)
      }
    }

    // 🆘 متى لا تكفي خطة الإنقاذ؟ + ⚠️ لو في خطر فوري
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("rescue_when_not_enough_card")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "🆘 متى لا تكفي خطة الإنقاذ؟",
            style = MaterialTheme.typography.titleLarge,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "خطط الإنقاذ مخصصة للتعامل مع لحظة صعبة، وليست بديلًا عن التقييم أو العلاج المتخصص.",
            style = MaterialTheme.typography.bodyMedium,
            color = DarkGreen,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 23.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "لو الضيق أو الحزن أو فقدان الاهتمام أو القلق أو انخفاض الطاقة مستمر، أو بيؤثر بشكل واضح على النوم أو الدراسة أو العمل أو العلاقات أو قدرتك على القيام بمهامك، فالتواصل مع مختص نفسي قد يكون خطوة مناسبة.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextDark,
            lineHeight = 23.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(WarmBeige.copy(alpha = 0.6f))
              .padding(16.dp)
          ) {
            Column {
              Text(
                text = "⚠️ لو في خطر فوري",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF7B2D26),
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "إذا كنت تشعر أنك قد تؤذي نفسك أو شخصًا آخر، أو أنك غير قادر على الحفاظ على سلامتك الآن:\nلا تعتمد على التطبيق وحده.\nتواصل فورًا مع شخص تثق به، أو توجّه لأقرب قسم طوارئ/خدمة طوارئ محلية، ولا تبقَ وحدك مع الخطر.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF7B2D26),
                fontWeight = FontWeight.SemiBold,
                lineHeight = 23.sp
              )
            }
          }
        }
      }
    }

    // 🌱 رسالة ختام القسم
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.45f)),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("rescue_closing_message_card")
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "🌱 رسالة ختام القسم",
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "مش مطلوب منك تبقى كويس طول الوقت.\nومش مطلوب تحل كل حاجة النهارده.",
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 25.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "أحيانًا البداية تكون أبسط من كده:\nألاحظ.\nأهدأ خطوة.\nأطلب مساعدة لو محتاج.\nوأكمل من هنا.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextDark,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "نسمة حياة.. معك إلى الحياة.",
            style = MaterialTheme.typography.titleSmall,
            color = DarkGreen,
            fontWeight = FontWeight.ExtraBold
          )
        }
      }
    }
  }
}

@Composable
private fun ExpandableRescuePlanContainer(
  id: String,
  emoji: String,
  title: String,
  subtitle: String,
  isExpanded: Boolean,
  onToggle: () -> Unit,
  content: @Composable () -> Unit
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .border(
        width = if (isExpanded) 1.5.dp else 0.dp,
        color = if (isExpanded) SageGreenPrimary else Color.Transparent,
        shape = RoundedCornerShape(20.dp)
      )
      .testTag("rescue_plan_card_$id")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(WarmBeige.copy(alpha = 0.55f)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = emoji, fontSize = 22.sp)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "$emoji $title",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = subtitle,
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark.copy(alpha = 0.8f)
            )
          }
        }

        IconButton(onClick = onToggle) {
          Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = if (isExpanded) "طي" else "توسيع",
            tint = DarkGreen
          )
        }
      }

      AnimatedVisibility(visible = isExpanded) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
          content()
        }
      }
    }
  }
}

// ============================================================================
// 1. 🫂 خطة وقت الضيق
// ============================================================================
@Composable
private fun DistressPlanBody(onSaveToJourney: (String, String) -> Unit) {
  val feelings = listOf("مخنوق", "حزين", "متوتر", "مضغوط", "خايف", "مش عارف")
  val bodyActions = listOf(
    "اشرب مياه.",
    "اغسل وشك.",
    "اقعد في مكان أهدى.",
    "امشِ دقائق قليلة.",
    "غيّر وضع جسمك أو مكانك."
  )
  var selectedFeeling by remember { mutableStateOf("مضغوط") }
  var selectedBodyAction by remember { mutableStateOf(bodyActions.first()) }
  var mostPressingNow by remember { mutableStateOf("") }
  var smallStepNow by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  val readyMessage = "أنا مش في أحسن حال دلوقتي. ممكن تفضل/ي معايا شوية؟ مش محتاج حلول، بس محتاج حد موجود."

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    StepHeader("1. سمّي اللي حاصل (اختار أقرب وصف):")
    ChipRowSelector(
      options = feelings,
      selected = selectedFeeling,
      onSelect = { selectedFeeling = it }
    )

    StepHeader("2. قلّل المطلوب منك الآن:")
    QuoteHighlightBox("إيه الحاجة الوحيدة اللي لازم أتعامل معاها دلوقتي؟\nمش بكرة. مش الأسبوع ده. دلوقتي فقط.")

    StepHeader("3. ارجع لجسمك (اختار حاجة بسيطة):")
    ChipRowSelector(
      options = bodyActions,
      selected = selectedBodyAction,
      onSelect = { selectedBodyAction = it }
    )

    StepHeader("4. اتواصل (لو وجود شخص آمن ممكن يساعدك، ابعت):")
    CopyableMessageBox(message = readyMessage)

    StepHeader("5. بعد 10 دقائق اسأل:")
    QuoteHighlightBox("هل الضيق قلّ ولو 1%؟\nمش مطلوب تبقى كويس. المطلوب إنك ما تفضلش لوحدك مع الإحساس.")

    ExerciseSectionHeader()
    RescueInputField(
      label = "أكتر حاجة ضاغطة عليّ الآن:",
      value = mostPressingNow,
      onValueChange = { mostPressingNow = it; isSaved = false },
      placeholder = "اكتب أكتر حاجة ضاغطة عليك الآن..."
    )
    RescueInputField(
      label = "الخطوة الصغيرة اللي أقدر أعملها الآن:",
      value = smallStepNow,
      onValueChange = { smallStepNow = it; isSaved = false },
      placeholder = "اكتب الخطوة الصغيرة هنا..."
    )

    SaveRescueExerciseButton(
      isSaved = isSaved,
      onClick = {
        val summary = """
          اللي حاسس بيه: $selectedFeeling
          خطوة الرجوع للجسم: $selectedBodyAction
          أكتر حاجة ضاغطة عليّ الآن: $mostPressingNow
          الخطوة الصغيرة اللي أقدر أعملها الآن: $smallStepNow
        """.trimIndent()
        onSaveToJourney("🫂 خطة وقت الضيق", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// 2. 🌀 خطة الحيرة
// ============================================================================
@Composable
private fun ConfusionPlanBody(onSaveToJourney: (String, String) -> Unit) {
  var brainDump by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("🔵 أقدر أعمل فيه حاجة الآن.") }
  var mainConcernNow by remember { mutableStateOf("") }
  var smallestPossibleStep by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  val categories = listOf(
    "🔵 أقدر أعمل فيه حاجة الآن.",
    "🟡 أقدر أعمل فيه حاجة، لكن مش الآن.",
    "⚪ خارج سيطرتي حاليًا."
  )

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    StepHeader("1. اكتب كل اللي في دماغك (من غير ترتيب):")
    RescueInputField(
      label = "",
      value = brainDump,
      onValueChange = { brainDump = it; isSaved = false },
      placeholder = "اكتب كل الأفكار والمهام المتشابكة هنا من غير ترتيب..."
    )

    StepHeader("2. قسّمهم إلى:")
    categories.forEach { cat ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(if (selectedCategory == cat) SoftMint.copy(alpha = 0.45f) else CreamBackground)
          .clickable { selectedCategory = cat }
          .padding(horizontal = 10.dp, vertical = 6.dp)
      ) {
        RadioButton(
          selected = selectedCategory == cat,
          onClick = { selectedCategory = cat },
          colors = RadioButtonDefaults.colors(selectedColor = DarkGreen)
        )
        Text(text = cat, style = MaterialTheme.typography.bodyMedium, color = TextDark)
      }
    }

    StepHeader("3. اختار حاجة واحدة فقط من 🔵")
    StepHeader("4. صغّرها:")
    QuoteHighlightBox("بدل: \"أخلص الشغل.\"\nخليها: \"أفتح الملف وأكتب أول سطر.\"")
    StepHeader("5. ابدأ 15 دقيقة فقط. بعدها قرر: أكمل؟ أرتاح؟ أغيّر المهمة؟")

    ExerciseSectionHeader()
    RescueInputField(
      label = "أهم شيء يشغلني الآن:",
      value = mainConcernNow,
      onValueChange = { mainConcernNow = it; isSaved = false },
      placeholder = "اكتب الشيء الواحد الذي اخترته من 🔵..."
    )
    RescueInputField(
      label = "أصغر خطوة ممكنة فيه:",
      value = smallestPossibleStep,
      onValueChange = { smallestPossibleStep = it; isSaved = false },
      placeholder = "مثال: أفتح الملف وأكتب أول سطر..."
    )

    SaveRescueExerciseButton(
      isSaved = isSaved,
      onClick = {
        val summary = """
          كل اللي في دماغي: $brainDump
          أهم شيء يشغلني الآن: $mainConcernNow
          أصغر خطوة ممكنة فيه: $smallestPossibleStep
        """.trimIndent()
        onSaveToJourney("🌀 خطة الحيرة", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// 3. 😑 خطة الزهق
// ============================================================================
@Composable
private fun BoredomPlanBody(onSaveToJourney: (String, String) -> Unit) {
  val experiments = listOf(
    "🚶 امشِ 10 دقائق.",
    "🎵 اسمع موسيقى تحبها.",
    "📖 اقرأ صفحتين.",
    "🧹 رتّب مساحة صغيرة جدًا.",
    "☎️ كلم شخص ترتاح له.",
    "🎨 اعمل حاجة إبداعية.",
    "🌱 اخرج من المكان اللي قاعد فيه.",
    "🎯 جرّب حاجة جديدة لمدة 10 دقائق."
  )
  var selectedExperiment by remember { mutableStateOf(experiments.first()) }
  var energyBefore by remember { mutableIntStateOf(4) }
  var energyAfter by remember { mutableIntStateOf(6) }
  var noticedText by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(
      text = "الزهق مش معناه بالضرورة إنك محتاج ترفيه أكثر. أحيانًا يكون وراءه إرهاق، عزلة، تكرار، أو فقدان اهتمام.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextDark,
      lineHeight = 22.sp
    )

    StepHeader("اختار تجربة واحدة فقط:")
    experiments.forEach { exp ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(if (selectedExperiment == exp) SoftMint.copy(alpha = 0.45f) else CreamBackground)
          .clickable { selectedExperiment = exp }
          .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        RadioButton(
          selected = selectedExperiment == exp,
          onClick = { selectedExperiment = exp },
          colors = RadioButtonDefaults.colors(selectedColor = DarkGreen)
        )
        Text(text = exp, style = MaterialTheme.typography.bodyMedium, color = TextDark)
      }
    }

    QuoteHighlightBox("المهم: ما تستناش إنك \"تحس بالرغبة\" قبل ما تبدأ.")

    Text(
      text = "🧩 قبل وبعد",
      style = MaterialTheme.typography.titleMedium,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    Text(
      text = "طاقتي قبل التجربة: ($energyBefore / 10)",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )
    RescueScale1To10(selected = energyBefore, onSelect = { energyBefore = it; isSaved = false })

    Text(
      text = "طاقتي بعدها: ($energyAfter / 10)",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )
    RescueScale1To10(selected = energyAfter, onSelect = { energyAfter = it; isSaved = false })

    RescueInputField(
      label = "إيه اللي لاحظته؟",
      value = noticedText,
      onValueChange = { noticedText = it; isSaved = false },
      placeholder = "اكتب ما لاحظته بعد التجربة..."
    )

    SaveRescueExerciseButton(
      isSaved = isSaved,
      onClick = {
        val summary = """
          التجربة المختارة: $selectedExperiment
          طاقتي قبل التجربة: $energyBefore / 10
          طاقتي بعدها: $energyAfter / 10
          اللي لاحظته: $noticedText
        """.trimIndent()
        onSaveToJourney("😑 خطة الزهق", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// 4. 🔥 خطة الانفعال
// ============================================================================
@Composable
private fun AngerPlanBody(onSaveToJourney: (String, String) -> Unit) {
  val feelings = listOf("غضبان", "مجروح", "خايف", "متضايق", "محبط", "حاسس إنك مش مقدَّر")
  val lessHarmActions = listOf(
    "أؤجل الرد.",
    "أكتب اللي عايز أقوله.",
    "أطلب وقتًا.",
    "أتكلم لما أهدأ.",
    "أطلب مساعدة من شخص آمن."
  )
  var selectedFeeling by remember { mutableStateOf(feelings.first()) }
  var selectedAction by remember { mutableStateOf(lessHarmActions.first()) }
  var whatHappened by remember { mutableStateOf("") }
  var whatIFelt by remember { mutableStateOf("") }
  var whatINeeded by remember { mutableStateOf("") }
  var leastHarmNow by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    StepHeader("1. أوقف التصعيد: لو تقدر، ابعد دقائق عن الموقف.")
    StepHeader("2. لا تأخذ قرارًا كبيرًا وأنت في قمة الانفعال.")
    StepHeader("3. سمّي الشعور:")
    ChipRowSelector(
      options = feelings,
      selected = selectedFeeling,
      onSelect = { selectedFeeling = it }
    )

    StepHeader("4. اسأل نفسك:")
    QuoteHighlightBox("إيه اللي حصل فعلًا؟\nثم: إيه اللي أنا فهمته من اللي حصل؟\nالاثنين مش بالضرورة نفس الشيء.")

    StepHeader("5. اختار تصرفًا أقل ضررًا:")
    ChipRowSelector(
      options = lessHarmActions,
      selected = selectedAction,
      onSelect = {
        selectedAction = it
        leastHarmNow = it
      }
    )

    StepHeader("جملة جاهزة:")
    CopyableMessageBox("أنا متضايق جدًا دلوقتي، ومحتاج أهدأ قبل ما نكمل الكلام.")

    ExerciseSectionHeader()
    RescueInputField(
      label = "اللي حصل:",
      value = whatHappened,
      onValueChange = { whatHappened = it; isSaved = false },
      placeholder = "اكتب اللي حصل فعلاً..."
    )
    RescueInputField(
      label = "اللي حسيت به:",
      value = whatIFelt,
      onValueChange = { whatIFelt = it; isSaved = false },
      placeholder = "مثال: $selectedFeeling..."
    )
    RescueInputField(
      label = "اللي كنت محتاجه:",
      value = whatINeeded,
      onValueChange = { whatINeeded = it; isSaved = false },
      placeholder = "مثال: تقدير، احترام، أو وقت..."
    )
    RescueInputField(
      label = "التصرف الأقل ضررًا الآن:",
      value = leastHarmNow,
      onValueChange = { leastHarmNow = it; isSaved = false },
      placeholder = "مثال: $selectedAction"
    )

    SaveRescueExerciseButton(
      isSaved = isSaved,
      onClick = {
        val summary = """
          الشعور: $selectedFeeling
          اللي حصل: $whatHappened
          اللي حسيت به: $whatIFelt
          اللي كنت محتاجه: $whatINeeded
          التصرف الأقل ضررًا الآن: ${leastHarmNow.ifBlank { selectedAction }}
        """.trimIndent()
        onSaveToJourney("🔥 خطة الانفعال", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// 5. 🧠 خطة التفكير الزائد
// ============================================================================
@Composable
private fun OverthinkingPlanBody(onSaveToJourney: (String, String) -> Unit) {
  var thoughtInHead by remember { mutableStateOf("") }
  var postponedThought by remember { mutableStateOf("") }
  var canInfluence by remember { mutableStateOf("نعم") } // "نعم", "جزئيًا", "لا"
  var myStepIfYes by remember { mutableStateOf("") }
  var myNeedIfNo by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    QuoteHighlightBox("أول سؤال: هل عندي مشكلة أقدر أعمل فيها حاجة الآن؟\n• لو نعم: اكتب خطوة واحدة قابلة للتنفيذ.\n• لو لا: اكتب الفكرة في خانة: \"هرجع لها في وقت مناسب.\"")

    RescueInputField(
      label = "خانة \"هرجع لها في وقت مناسب\" (اختياري):",
      value = postponedThought,
      onValueChange = { postponedThought = it; isSaved = false },
      placeholder = "ضع الفكرة هنا لترتاح منها الآن..."
    )

    Text(
      text = "ثم انتقل لشيء حاضر أمامك الآن: مهمة بسيطة • مشي • ترتيب شيء • حديث مع شخص • نشاط يحتاج انتباهك.",
      style = MaterialTheme.typography.bodySmall,
      color = TextDark
    )

    QuoteHighlightBox("جرّب قاعدة:\nفكّر → اكتب → حدّد خطوة → ارجع للحياة.\nمش: فكّر → فكّر → فكّر → فكّر.")

    ExerciseSectionHeader()
    RescueInputField(
      label = "الفكرة التي تدور في رأسي:",
      value = thoughtInHead,
      onValueChange = { thoughtInHead = it; isSaved = false },
      placeholder = "اكتب الفكرة التي تشغل بالك..."
    )

    StepHeader("هل أستطيع التأثير فيها الآن؟")
    ChipRowSelector(
      options = listOf("نعم", "جزئيًا", "لا"),
      selected = canInfluence,
      onSelect = { canInfluence = it }
    )

    if (canInfluence == "نعم" || canInfluence == "جزئيًا") {
      RescueInputField(
        label = "لو نعم، خطوتي:",
        value = myStepIfYes,
        onValueChange = { myStepIfYes = it; isSaved = false },
        placeholder = "خطوة واحدة قابلة للتنفيذ الآن..."
      )
    } else {
      RescueInputField(
        label = "لو لا، ماذا أحتاج الآن بدل الاستمرار في التفكير؟",
        value = myNeedIfNo,
        onValueChange = { myNeedIfNo = it; isSaved = false },
        placeholder = "مثال: أقوم أتحرك أو أنشغل بنشاط حاضر أمامي..."
      )
    }

    SaveRescueExerciseButton(
      isSaved = isSaved,
      onClick = {
        val summary = """
          الفكرة التي تدور في رأسي: $thoughtInHead
          هرجع لها في وقت مناسب: $postponedThought
          هل أستطيع التأثير فيها الآن؟ $canInfluence
          خطوتي / ما أحتاجه الآن: ${if (canInfluence == "لا") myNeedIfNo else myStepIfYes}
        """.trimIndent()
        onSaveToJourney("🧠 خطة التفكير الزائد", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// 6. 🔋 خطة انخفاض الطاقة
// ============================================================================
@Composable
private fun LowEnergyPlanBody(onSaveToJourney: (String, String) -> Unit) {
  var currentEnergy by remember { mutableIntStateOf(3) }
  var drainedMe by remember { mutableStateOf("") }
  var comfortsMe by remember { mutableStateOf("") }
  var reduceToday by remember { mutableStateOf("") }
  var addToday by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  val dynamicAdvice = when (currentEnergy) {
    in 1..3 -> "طاقتك (1–3): اختار الحد الأدنى الضروري فقط.\nمثال: بدل \"أنظف البيت\" → \"أرتب المكان اللي هقعد فيه.\""
    in 4..6 -> "طاقتك (4–6): اختار مهمة واحدة أساسية + شيء صغير مريح."
    else -> "طاقتك (7–10): استغل الطاقة، لكن بدون تحميل نفسك كل شيء مرة واحدة."
  }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    QuoteHighlightBox("أولًا: انخفاض الطاقة مش معناه تلقائيًا إنك كسول.")

    StepHeader("قيّم طاقتك الآن من 1 إلى 10: ($currentEnergy / 10)")
    RescueScale1To10(selected = currentEnergy, onSelect = { currentEnergy = it; isSaved = false })

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(SoftMint.copy(alpha = 0.5f))
        .padding(14.dp)
    ) {
      Text(
        text = dynamicAdvice,
        style = MaterialTheme.typography.bodyMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold,
        lineHeight = 22.sp
      )
    }

    StepHeader("اسأل نفسك:")
    RescueInputField(
      label = "إيه اللي سحب طاقتي؟",
      value = drainedMe,
      onValueChange = { drainedMe = it; isSaved = false },
      placeholder = "اكتب ما استنزف طاقتك..."
    )
    RescueInputField(
      label = "إيه اللي ممكن يريحني أو يديني طاقة بسيطة؟",
      value = comfortsMe,
      onValueChange = { comfortsMe = it; isSaved = false },
      placeholder = "شيء بسيط يريحك الآن..."
    )
    RescueInputField(
      label = "حاجة واحدة هقللها اليوم:",
      value = reduceToday,
      onValueChange = { reduceToday = it; isSaved = false },
      placeholder = "حمل واحد ستخففه عن نفسك..."
    )
    RescueInputField(
      label = "حاجة واحدة هضيفها:",
      value = addToday,
      onValueChange = { addToday = it; isSaved = false },
      placeholder = "إضافة صغيرة لطيفة ليومك..."
    )

    SaveRescueExerciseButton(
      isSaved = isSaved,
      onClick = {
        val summary = """
          تقييم طاقتي: $currentEnergy / 10
          اللي سحب طاقتي: $drainedMe
          اللي يريحني: $comfortsMe
          حاجة واحدة هقللها اليوم: $reduceToday
          حاجة واحدة هضيفها: $addToday
        """.trimIndent()
        onSaveToJourney("🔋 خطة انخفاض الطاقة", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// 7. 🚪 خطة العزلة
// ============================================================================
@Composable
private fun IsolationPlanBody(onSaveToJourney: (String, String) -> Unit) {
  val levels = listOf(
    "🟢 مستوى 1 — رسالة قصيرة" to "أنا مش اجتماعي أوي النهارده، بس حبيت أطمن عليك.",
    "🟡 مستوى 2 — اطلب وجودًا بدون كلام" to "ممكن نقعد سوا شوية؟ مش محتاج أتكلم.",
    "🟠 مستوى 3 — احكي جزءًا بسيطًا" to "أنا الفترة دي مش أحسن حاجة ومحتاج حد يسمعني.",
    "🔴 مستوى 4 — أحتاج مساعدة" to "لو الإحساس مستمر أو بيأثر بوضوح على حياتك، فكّر في التواصل مع مختص نفسي أو شخص موثوق يساعدك في الوصول للمساعدة المناسبة."
  )

  var selectedLevelIdx by remember { mutableIntStateOf(0) }
  var safePerson by remember { mutableStateOf("") }
  var simplestMessage by remember { mutableStateOf(levels.first().second) }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(
      text = "أحيانًا نحتاج مساحة. لكن العزلة الطويلة ممكن تزود الإحساس بالثقل. مش لازم تعمل مكالمة طويلة.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextDark,
      lineHeight = 22.sp
    )

    StepHeader("اختار مستوى التواصل المناسب لك:")
    levels.forEachIndexed { idx, (levelTitle, levelMessage) ->
      val isSelected = selectedLevelIdx == idx
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isSelected) SoftMint.copy(alpha = 0.5f) else CreamBackground
        ),
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            selectedLevelIdx = idx
            if (idx < 3) simplestMessage = levelMessage
          }
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = levelTitle,
            style = MaterialTheme.typography.titleSmall,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "\"$levelMessage\"",
            style = MaterialTheme.typography.bodySmall,
            color = TextDark,
            lineHeight = 20.sp
          )
        }
      }
    }

    ExerciseSectionHeader()
    RescueInputField(
      label = "مين الشخص اللي أشعر معه بأمان نسبي؟",
      value = safePerson,
      onValueChange = { safePerson = it; isSaved = false },
      placeholder = "اكتب اسم الشخص الآمن هنا..."
    )
    RescueInputField(
      label = "أبسط رسالة أقدر أبعتها له:",
      value = simplestMessage,
      onValueChange = { simplestMessage = it; isSaved = false },
      placeholder = "اكتب أو عدّل الرسالة البسيطة هنا..."
    )

    CopyableMessageBox(simplestMessage)

    SaveRescueExerciseButton(
      isSaved = isSaved,
      onClick = {
        val summary = """
          مستوى التواصل المختار: ${levels[selectedLevelIdx].first}
          الشخص الآمن: $safePerson
          أبسط رسالة أقدر أبعتها له: $simplestMessage
        """.trimIndent()
        onSaveToJourney("🚪 خطة العزلة", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// 8. 🌧️ خطة "مش قادر أعمل حاجة"
// ============================================================================
@Composable
private fun CantDoAnythingPlanBody(onSaveToJourney: (String, String) -> Unit) {
  val microChoices = listOf(
    "أشرب مياه",
    "آكل حاجة",
    "أغير هدومي",
    "أتحرك من مكاني",
    "أفتح الشباك أو أخرج لمكان أهدى",
    "أستحم",
    "أتواصل مع شخص",
    "أطلب مساعدة"
  )

  var selectedChoice by remember { mutableStateOf(microChoices.first()) }
  var next5MinStep by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    QuoteHighlightBox("ما تحاولش تصلح كل شيء مرة واحدة. اختار واحدة فقط:")

    microChoices.forEach { choice ->
      val isChecked = selectedChoice == choice
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(if (isChecked) SoftMint.copy(alpha = 0.45f) else CreamBackground)
          .clickable { selectedChoice = choice }
          .padding(horizontal = 8.dp, vertical = 2.dp)
      ) {
        Checkbox(
          checked = isChecked,
          onCheckedChange = { selectedChoice = choice },
          colors = CheckboxDefaults.colors(checkedColor = DarkGreen)
        )
        Text(
          text = choice,
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark,
          fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal
        )
      }
    }

    StepHeader("ثم اسأل:")
    RescueInputField(
      label = "إيه أصغر خطوة ممكن أعملها خلال الخمس دقائق الجاية؟",
      value = next5MinStep,
      onValueChange = { next5MinStep = it; isSaved = false },
      placeholder = "مثال: أقوم أشرب كوب مياه الآن..."
    )

    SaveRescueExerciseButton(
      isSaved = isSaved,
      onClick = {
        val summary = """
          الخطوة الواحدة التي اخترتها: $selectedChoice
          أصغر خطوة خلال الخمس دقائق الجاية: ${next5MinStep.ifBlank { selectedChoice }}
        """.trimIndent()
        onSaveToJourney("🌧️ خطة مش قادر أعمل حاجة", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// Shared Components for Rescue Plans
// ============================================================================
@Composable
private fun StepHeader(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.titleSmall,
    color = DarkGreen,
    fontWeight = FontWeight.Bold
  )
}

@Composable
private fun ExerciseSectionHeader() {
  Spacer(modifier = Modifier.height(4.dp))
  Text(
    text = "🧩 تمرين سريع",
    style = MaterialTheme.typography.titleMedium,
    color = DarkGreen,
    fontWeight = FontWeight.Bold
  )
}

@Composable
private fun QuoteHighlightBox(text: String) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(CreamBackground)
      .border(1.dp, WarmBeige, RoundedCornerShape(12.dp))
      .padding(14.dp)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.bodyMedium,
      color = DarkGreen,
      fontWeight = FontWeight.SemiBold,
      lineHeight = 22.sp
    )
  }
}

@Composable
private fun CopyableMessageBox(message: String) {
  val context = LocalContext.current
  var copied by remember { mutableStateOf(false) }

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.4f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(
        text = "\"$message\"",
        style = MaterialTheme.typography.bodyMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold,
        lineHeight = 22.sp
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        OutlinedButton(
          onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            clipboard?.setPrimaryClip(ClipData.newPlainText("رسالة نسمة حياة", message))
            copied = true
          },
          shape = RoundedCornerShape(10.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
        ) {
          Icon(
            imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
            contentDescription = null,
            tint = DarkGreen,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (copied) "تم نسخ الرسالة" else "نسخ الرسالة",
            fontSize = 12.sp,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
private fun ChipRowSelector(
  options: List<String>,
  selected: String,
  onSelect: (String) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    options.forEach { option ->
      val isSel = selected == option
      FilterChip(
        selected = isSel,
        onClick = { onSelect(option) },
        label = { Text(option, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = DarkGreen,
          selectedLabelColor = Color.White,
          containerColor = CreamBackground
        )
      )
    }
  }
}

@Composable
private fun RescueInputField(
  label: String,
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    if (label.isNotBlank()) {
      Text(
        text = label,
        style = MaterialTheme.typography.titleSmall,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
    }
    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      placeholder = { Text(placeholder, fontSize = 13.sp) },
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = DarkGreen,
        unfocusedBorderColor = WarmBeige,
        focusedContainerColor = CreamBackground,
        unfocusedContainerColor = CreamBackground
      ),
      modifier = Modifier.fillMaxWidth()
    )
  }
}

@Composable
private fun RescueScale1To10(
  selected: Int,
  onSelect: (Int) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    for (num in 1..10) {
      val isSelected = selected == num
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(if (isSelected) DarkGreen else Color.White)
          .border(1.dp, if (isSelected) DarkGreen else WarmBeige, CircleShape)
          .clickable { onSelect(num) },
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = num.toString(),
          color = if (isSelected) Color.White else DarkGreen,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }
    }
  }
}

@Composable
private fun SaveRescueExerciseButton(
  isSaved: Boolean,
  onClick: () -> Unit
) {
  Column {
    Button(
      onClick = onClick,
      colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = if (isSaved) "✓ تم حفظ التمرين محليًا" else "✓ خلصت التمرين",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = Color.White
      )
    }

    if (isSaved) {
      Spacer(modifier = Modifier.height(6.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = null,
          tint = DarkGreen,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "تم حفظ خطوتك محليًا في جهازك داخل صفحة «رحلتي» 🌿",
          style = MaterialTheme.typography.labelSmall,
          color = DarkGreen
        )
      }
    }
  }
}
