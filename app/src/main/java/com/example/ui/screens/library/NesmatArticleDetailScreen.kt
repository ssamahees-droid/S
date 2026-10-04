package com.example.ui.screens.library

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NesmatArticleDetailScreen(
  articleId: Int,
  onSaveExerciseLocally: (String, String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val article = remember(articleId) {
    NesmatLibraryRepository.articles.find { it.id == articleId }
      ?: NesmatLibraryRepository.articles.first()
  }

  var personalReflectionNote by remember(articleId) { mutableStateOf("") }
  var exerciseSummaryText by remember(articleId) { mutableStateOf("") }
  var isExerciseCompleted by remember(articleId) { mutableStateOf(false) }

  UnifiedArticleTemplate(
    article = article,
    personalReflectionNote = personalReflectionNote,
    onPersonalReflectionChange = {
      personalReflectionNote = it
      isExerciseCompleted = false
    },
    isExerciseCompleted = isExerciseCompleted,
    onFinishExercise = {
      val combinedNote = buildString {
        appendLine("المقال: ${article.title}")
        if (exerciseSummaryText.isNotBlank()) {
          appendLine(exerciseSummaryText)
        }
        if (personalReflectionNote.isNotBlank()) {
          appendLine("ما كتبته لنفسي: $personalReflectionNote")
        }
        appendLine("خُد معك: ${article.takeWithYouSummary}")
      }.trimIndent()
      onSaveExerciseLocally("تمرين مكتبة نسمة حياة: ${article.title} 🌿", combinedNote)
      isExerciseCompleted = true
    },
    onBack = onBack,
    modifier = modifier
  ) {
    when (article.id) {
      1 -> ExerciseArticle1 { exerciseSummaryText = it }
      2 -> ExerciseArticle2 { exerciseSummaryText = it }
      3 -> ExerciseArticle3 { exerciseSummaryText = it }
      4 -> ExerciseArticle4 { exerciseSummaryText = it }
      5 -> ExerciseArticle5 { exerciseSummaryText = it }
      6 -> ExerciseArticle6 { exerciseSummaryText = it }
      7 -> ExerciseArticle7 { exerciseSummaryText = it }
      8 -> ExerciseArticle8 { exerciseSummaryText = it }
      9 -> ExerciseArticle9 { exerciseSummaryText = it }
      10 -> ExerciseArticle10 { exerciseSummaryText = it }
    }
  }
}

// =========================================================================
// المقال 1 — مش كل وجع بيبان
// =========================================================================
@Composable
private fun ExerciseArticle1(onUpdateSummary: (String) -> Unit) {
  var peopleSeeMe by remember { mutableStateOf("") }
  var peopleDontSee by remember { mutableStateOf("") }
  var drainingMost by remember { mutableStateOf("") }
  var reallyNeed by remember { mutableStateOf("") }

  fun sync() {
    onUpdateSummary(
      "الناس شايفاني: $peopleSeeMe\nلكن اللي مش شايفينه: $peopleDontSee\nأكتر حاجة مستنزفاني حاليًا: $drainingMost\nوالحاجة اللي محتاجها فعلًا: $reallyNeed"
    )
  }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    LabeledExerciseField(
      label = "الناس شايفاني:",
      value = peopleSeeMe,
      onValueChange = { peopleSeeMe = it; sync() },
      placeholder = "مثال: هادي، مبتسم، وبأنجز مهامي..."
    )
    LabeledExerciseField(
      label = "لكن اللي مش شايفينه:",
      value = peopleDontSee,
      onValueChange = { peopleDontSee = it; sync() },
      placeholder = "مثال: إني ببذل مجهود مضاعف عشان أبان متماسك..."
    )
    LabeledExerciseField(
      label = "أكتر حاجة مستنزفاني حاليًا:",
      value = drainingMost,
      onValueChange = { drainingMost = it; sync() },
      placeholder = "اكتب أكتر شيء يسحب طاقتك..."
    )
    LabeledExerciseField(
      label = "والحاجة اللي محتاجها فعلًا:",
      value = reallyNeed,
      onValueChange = { reallyNeed = it; sync() },
      placeholder = "مثال: راحة بدون لوم، أو حد يسمعني بهدوء..."
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(WarmBeige.copy(alpha = 0.45f))
        .padding(14.dp)
    ) {
      Text(
        text = "\"مش لازم وجعك يكون ظاهر عشان يستحق الاهتمام.\"",
        style = MaterialTheme.typography.bodyMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

// =========================================================================
// المقال 2 — أنا تعبان… بس مش عارف من إيه
// =========================================================================
@Composable
private fun ExerciseArticle2(onUpdateSummary: (String) -> Unit) {
  var myBody by remember { mutableStateOf("") }
  var myThoughts by remember { mutableStateOf("") }
  var myFeelings by remember { mutableStateOf("") }
  var myLife by remember { mutableStateOf("") }
  var mainDrainer by remember { mutableStateOf("") }

  fun sync() {
    onUpdateSummary(
      "جسمي: $myBody\nأفكاري: $myThoughts\nمشاعري: $myFeelings\nحياتي: $myLife\nأعتقد أن أكثر شيء يستنزفني هو: $mainDrainer"
    )
  }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    QuadExerciseBox(
      heading = "جسمي",
      subtitle = "ماذا أشعر جسديًا؟",
      value = myBody,
      onValueChange = { myBody = it; sync() }
    )
    QuadExerciseBox(
      heading = "أفكاري",
      subtitle = "ما أكثر فكرة تتكرر؟",
      value = myThoughts,
      onValueChange = { myThoughts = it; sync() }
    )
    QuadExerciseBox(
      heading = "مشاعري",
      subtitle = "ما الشعور الأقوى عندي الآن؟",
      value = myFeelings,
      onValueChange = { myFeelings = it; sync() }
    )
    QuadExerciseBox(
      heading = "حياتي",
      subtitle = "ما أكثر شيء يضغط عليّ حاليًا؟",
      value = myLife,
      onValueChange = { myLife = it; sync() }
    )

    Spacer(modifier = Modifier.height(4.dp))

    LabeledExerciseField(
      label = "أعتقد أن أكثر شيء يستنزفني هو:",
      value = mainDrainer,
      onValueChange = { mainDrainer = it; sync() },
      placeholder = "اكتب خلاصة ما لاحظته هنا..."
    )
  }
}

@Composable
private fun QuadExerciseBox(
  heading: String,
  subtitle: String,
  value: String,
  onValueChange: (String) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(CreamBackground)
      .padding(14.dp)
  ) {
    Text(
      text = heading,
      style = MaterialTheme.typography.titleMedium,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )
    Text(
      text = subtitle,
      style = MaterialTheme.typography.bodySmall,
      color = TextDark.copy(alpha = 0.75f)
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      placeholder = { Text(subtitle, fontSize = 13.sp) },
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = DarkGreen,
        unfocusedBorderColor = WarmBeige,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
      ),
      modifier = Modifier.fillMaxWidth()
    )
  }
}

// =========================================================================
// المقال 3 — مشاعري مش عدوي
// =========================================================================
@Composable
private fun ExerciseArticle3(onUpdateSummary: (String) -> Unit) {
  var iFeel by remember { mutableStateOf("") }
  var happenedAfter by remember { mutableStateOf("") }
  var affectedMost by remember { mutableStateOf("") }
  var needNowText by remember { mutableStateOf("") }
  var selectedOption by remember { mutableStateOf("فهم ما أشعر به") }

  val options = listOf(
    "فهم ما أشعر به",
    "تهدئة نفسي",
    "التحدث مع شخص",
    "التعامل مع المشكلة"
  )

  fun sync() {
    onUpdateSummary(
      "أنا أشعر بـ: $iFeel\nحدث هذا الشعور بعد: $happenedAfter\nأكثر شيء أثّر فيّ: $affectedMost\nالذي أحتاجه الآن: $needNowText\nاختياري الحالي: $selectedOption"
    )
  }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    LabeledExerciseField(
      label = "أنا أشعر بـ:",
      value = iFeel,
      onValueChange = { iFeel = it; sync() },
      placeholder = "سمِّ الشعور بهدوء..."
    )
    LabeledExerciseField(
      label = "حدث هذا الشعور بعد:",
      value = happenedAfter,
      onValueChange = { happenedAfter = it; sync() },
      placeholder = "الموقف أو اللحظة التي سبقت الشعور..."
    )
    LabeledExerciseField(
      label = "أعتقد أن أكثر شيء أثّر فيّ هو:",
      value = affectedMost,
      onValueChange = { affectedMost = it; sync() },
      placeholder = "الجزء الذي لمس قلبك في الموقف..."
    )
    LabeledExerciseField(
      label = "والذي أحتاجه الآن هو:",
      value = needNowText,
      onValueChange = { needNowText = it; sync() },
      placeholder = "ما الذي يريحك في هذه اللحظة؟"
    )

    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = "هل أحتاج الآن إلى:",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    options.forEach { option ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(if (selectedOption == option) SoftMint.copy(alpha = 0.45f) else CreamBackground)
          .clickable {
            selectedOption = option
            sync()
          }
          .padding(horizontal = 10.dp, vertical = 6.dp)
      ) {
        RadioButton(
          selected = selectedOption == option,
          onClick = {
            selectedOption = option
            sync()
          },
          colors = RadioButtonDefaults.colors(selectedColor = DarkGreen)
        )
        Text(
          text = option,
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark,
          fontWeight = if (selectedOption == option) FontWeight.Bold else FontWeight.Normal
        )
      }
    }
  }
}

// =========================================================================
// المقال 4 — الفكرة مش الحقيقة
// =========================================================================
@Composable
private fun ExerciseArticle4(onUpdateSummary: (String) -> Unit) {
  var step1Happened by remember { mutableStateOf("") }
  var step2ToldMyself by remember { mutableStateOf("") }
  var step3SelectedFeeling by remember { mutableStateOf("قلق") }
  var step3OtherFeeling by remember { mutableStateOf("") }
  var step4OtherExplanation by remember { mutableStateOf("") }
  var step5BalancedExplanation by remember { mutableStateOf("") }

  val feelingChoices = listOf("قلق", "حزن", "غضب", "إحراج", "ذنب", "حيرة", "خيار آخر")

  fun sync() {
    val feel = if (step3SelectedFeeling == "خيار آخر") step3OtherFeeling else step3SelectedFeeling
    onUpdateSummary(
      "1. ماذا حدث فعلًا: $step1Happened\n2. ماذا قلت لنفسي: $step2ToldMyself\n3. ماذا شعرت: $feel\n4. تفسير آخر ممكن: $step4OtherExplanation\n5. التفسير الأكثر توازنًا: $step5BalancedExplanation"
    )
  }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    LabeledExerciseField(
      label = "1. ماذا حدث فعلًا؟",
      value = step1Happened,
      onValueChange = { step1Happened = it; sync() },
      placeholder = "اكتب الحدث الواقعي فقط بدون تفسير..."
    )
    LabeledExerciseField(
      label = "2. ماذا قلت لنفسي عن الذي حدث؟",
      value = step2ToldMyself,
      onValueChange = { step2ToldMyself = it; sync() },
      placeholder = "الفكرة التلقائية التي قفزت لذهنك..."
    )

    Column {
      Text(
        text = "3. ماذا شعرت؟",
        style = MaterialTheme.typography.titleSmall,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        feelingChoices.forEach { choice ->
          val isSelected = step3SelectedFeeling == choice
          FilterChip(
            selected = isSelected,
            onClick = {
              step3SelectedFeeling = choice
              sync()
            },
            label = { Text(choice, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = DarkGreen,
              selectedLabelColor = Color.White,
              containerColor = CreamBackground
            )
          )
        }
      }

      if (step3SelectedFeeling == "خيار آخر") {
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = step3OtherFeeling,
          onValueChange = { step3OtherFeeling = it; sync() },
          placeholder = { Text("اكتب شعورك هنا...", fontSize = 13.sp) },
          shape = RoundedCornerShape(10.dp),
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

    LabeledExerciseField(
      label = "4. هل هناك تفسير آخر ممكن؟",
      value = step4OtherExplanation,
      onValueChange = { step4OtherExplanation = it; sync() },
      placeholder = "احتمال آخر غير الفكرة الأولى..."
    )
    LabeledExerciseField(
      label = "5. ما التفسير الأكثر توازنًا؟",
      value = step5BalancedExplanation,
      onValueChange = { step5BalancedExplanation = it; sync() },
      placeholder = "تفسير واقعي وهادئ يجمع الصورة..."
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(SoftMint.copy(alpha = 0.45f))
        .padding(14.dp)
    ) {
      Text(
        text = "«ما حدث شيء… وما قاله عقلك عن الذي حدث شيء آخر.»",
        style = MaterialTheme.typography.bodyMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

// =========================================================================
// المقال 5 — لما دماغي تفضل شغال
// =========================================================================
@Composable
private fun ExerciseArticle5(onUpdateSummary: (String) -> Unit) {
  var problemText by remember { mutableStateOf("") }
  var influenceOption by remember { mutableStateOf("نعم") } // "نعم", "جزئيًا", "لا"
  var followUpText by remember { mutableStateOf("") }

  val promptLabel = when (influenceOption) {
    "نعم" -> "أصغر خطوة أستطيع فعلها اليوم:"
    "جزئيًا" -> "ما الجزء الذي أستطيع التحكم فيه؟"
    else -> "ما الشيء الذي أستطيع فعله لنفسي رغم عدم قدرتي على تغيير الأمر؟"
  }

  fun sync() {
    onUpdateSummary(
      "المشكلة التي تشغلني: $problemText\nهل أستطيع التأثير فيها؟ $influenceOption\n$promptLabel $followUpText"
    )
  }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    LabeledExerciseField(
      label = "المشكلة التي تشغلني:",
      value = problemText,
      onValueChange = { problemText = it; sync() },
      placeholder = "اكتب الموضوع الذي يدور في ذهنك..."
    )

    Text(
      text = "هل أستطيع التأثير فيها؟",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    listOf("نعم", "جزئيًا", "لا").forEach { opt ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(if (influenceOption == opt) SoftMint.copy(alpha = 0.45f) else CreamBackground)
          .clickable {
            influenceOption = opt
            sync()
          }
          .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        RadioButton(
          selected = influenceOption == opt,
          onClick = {
            influenceOption = opt
            sync()
          },
          colors = RadioButtonDefaults.colors(selectedColor = DarkGreen)
        )
        Text(
          text = opt,
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark,
          fontWeight = if (influenceOption == opt) FontWeight.Bold else FontWeight.Normal
        )
      }
    }

    LabeledExerciseField(
      label = promptLabel,
      value = followUpText,
      onValueChange = { followUpText = it; sync() },
      placeholder = "اكتب خطوتك هنا..."
    )
  }
}

// =========================================================================
// المقال 6 — لما الطاقة تخلص قبل اليوم
// =========================================================================
@Composable
private fun ExerciseArticle6(onUpdateSummary: (String) -> Unit) {
  var energyLevel by remember { mutableIntStateOf(5) }
  var drain1 by remember { mutableStateOf("") }
  var drain2 by remember { mutableStateOf("") }
  var drain3 by remember { mutableStateOf("") }
  var recharge1 by remember { mutableStateOf("") }
  var recharge2 by remember { mutableStateOf("") }
  var recharge3 by remember { mutableStateOf("") }
  var reduceOne by remember { mutableStateOf("") }
  var addOne by remember { mutableStateOf("") }

  fun sync() {
    onUpdateSummary(
      "طاقتي اليوم: $energyLevel / 10\nأشياء تستنزفني: $drain1 ، $drain2 ، $drain3\nأشياء تعطيني طاقة: $recharge1 ، $recharge2 ، $recharge3\nشيء واحد سأخففه: $reduceOne\nشيء واحد سأضيفه: $addOne"
    )
  }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(
      text = "طاقتي اليوم: ($energyLevel / 10)",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    Scale1To10Selector(
      selectedValue = energyLevel,
      onSelect = {
        energyLevel = it
        sync()
      }
    )

    Text(
      text = "أشياء تستنزفني (3 خانات):",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )
    SmallTextInput(value = drain1, onValueChange = { drain1 = it; sync() }, placeholder = "١. شيء يستنزف طاقتي...")
    SmallTextInput(value = drain2, onValueChange = { drain2 = it; sync() }, placeholder = "٢. شيء ثانٍ يستنزفني...")
    SmallTextInput(value = drain3, onValueChange = { drain3 = it; sync() }, placeholder = "٣. شيء ثالث يستنزفني...")

    Text(
      text = "أشياء تعطيني بعض الطاقة أو الراحة (3 خانات):",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )
    SmallTextInput(value = recharge1, onValueChange = { recharge1 = it; sync() }, placeholder = "١. شيء يعطيني راحة...")
    SmallTextInput(value = recharge2, onValueChange = { recharge2 = it; sync() }, placeholder = "٢. شيء ثانٍ يريحني...")
    SmallTextInput(value = recharge3, onValueChange = { recharge3 = it; sync() }, placeholder = "٣. شيء ثالث يجدد طاقتي...")

    LabeledExerciseField(
      label = "شيء واحد سأخففه:",
      value = reduceOne,
      onValueChange = { reduceOne = it; sync() },
      placeholder = "اكتب حملًا واحدًا ستخففه اليوم..."
    )
    LabeledExerciseField(
      label = "شيء واحد سأضيفه:",
      value = addOne,
      onValueChange = { addOne = it; sync() },
      placeholder = "اكتب شيئًا لطيفًا ستضيفه ليومك..."
    )
  }
}

// =========================================================================
// المقال 7 — لا تنتظر أن تأتي الرغبة (مع مؤقت 10 دقائق)
// =========================================================================
@Composable
private fun ExerciseArticle7(onUpdateSummary: (String) -> Unit) {
  val activities = listOf(
    "مشي",
    "ترتيب جزء صغير",
    "قراءة",
    "مذاكرة",
    "مكالمة لشخص أحبه",
    "نشاط أحبه",
    "مهمة مؤجلة",
    "شيء آخر"
  )

  var selectedActivity by remember { mutableStateOf(activities.first()) }
  var customActivity by remember { mutableStateOf("") }
  var remainingSeconds by remember { mutableIntStateOf(600) } // 10 minutes = 600s
  var isTimerRunning by remember { mutableStateOf(false) }
  var timerFinishedOrTested by remember { mutableStateOf(false) }
  var energyBefore by remember { mutableIntStateOf(4) }
  var energyAfter by remember { mutableIntStateOf(7) }

  LaunchedEffect(isTimerRunning) {
    while (isTimerRunning && remainingSeconds > 0) {
      delay(1000L)
      remainingSeconds--
      if (remainingSeconds == 0) {
        isTimerRunning = false
        timerFinishedOrTested = true
      }
    }
  }

  fun sync() {
    val actName = if (selectedActivity == "شيء آخر" && customActivity.isNotBlank()) customActivity else selectedActivity
    onUpdateSummary(
      "النشاط المختار لمدة 10 دقائق: $actName\nطاقتي قبل النشاط: $energyBefore / 10\nطاقتي بعد النشاط: $energyAfter / 10"
    )
  }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(
      text = "اختر نشاطًا لمدة 10 دقائق فقط:",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    activities.forEach { act ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(if (selectedActivity == act) SoftMint.copy(alpha = 0.45f) else CreamBackground)
          .clickable {
            selectedActivity = act
            sync()
          }
          .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        RadioButton(
          selected = selectedActivity == act,
          onClick = {
            selectedActivity = act
            sync()
          },
          colors = RadioButtonDefaults.colors(selectedColor = DarkGreen)
        )
        Text(
          text = act,
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark,
          fontWeight = if (selectedActivity == act) FontWeight.Bold else FontWeight.Normal
        )
      }
    }

    if (selectedActivity == "شيء آخر") {
      SmallTextInput(
        value = customActivity,
        onValueChange = { customActivity = it; sync() },
        placeholder = "اكتب النشاط البسيط هنا..."
      )
    }

    // 10-Minute Interactive Timer Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = CreamBackground),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "⏱️ مؤقت الـ 10 دقائق",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        val mins = remainingSeconds / 60
        val secs = remainingSeconds % 60
        Text(
          text = String.format("%02d:%02d", mins, secs),
          style = MaterialTheme.typography.displaySmall,
          color = DarkGreen,
          fontWeight = FontWeight.ExtraBold,
          modifier = Modifier.testTag("ten_minute_timer_display")
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Button(
            onClick = { isTimerRunning = !isTimerRunning },
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("ten_minute_timer_start_pause")
          ) {
            Icon(
              imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isTimerRunning) "إيقاف مؤقت" else "ابدأ ١٠ دقائق")
          }

          OutlinedButton(
            onClick = {
              isTimerRunning = false
              remainingSeconds = 600
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("ten_minute_timer_reset")
          ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("إعادة", color = DarkGreen)
          }
        }
      }
    }

    // Energy Before & After (Always accessible so user can record anytime or after timer)
    Text(
      text = "طاقتي قبل النشاط: ($energyBefore / 10)",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )
    Scale1To10Selector(
      selectedValue = energyBefore,
      onSelect = { energyBefore = it; sync() }
    )

    Text(
      text = "طاقتي بعد النشاط: ($energyAfter / 10)",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )
    Scale1To10Selector(
      selectedValue = energyAfter,
      onSelect = { energyAfter = it; sync() }
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(SoftMint.copy(alpha = 0.45f))
        .padding(14.dp)
    ) {
      Text(
        text = "«مش لازم يكون عندك حماس عشان تبدأ. أحيانًا البداية الصغيرة هي اللي تفتح الباب.»",
        style = MaterialTheme.typography.bodyMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

// =========================================================================
// المقال 8 — أنا محتار… أبدأ منين؟
// =========================================================================
@Composable
private fun ExerciseArticle8(onUpdateSummary: (String) -> Unit) {
  val itemsText = remember { mutableStateListOf("", "", "", "", "") }
  // 0: 🟢 أستطيع أن أفعل شيئًا الآن, 1: 🟡 أستطيع التأثير فيه لكن ليس الآن, 2: ⚪ ليس تحت سيطرتي
  val itemsStatus = remember { mutableStateListOf(0, 1, 1, 2, 0) }
  var chosenGreenItem by remember { mutableStateOf("") }
  var smallest15MinStep by remember { mutableStateOf("") }

  val statusLabels = listOf(
    "🟢 أستطيع أن أفعل شيئًا الآن",
    "🟡 أستطيع التأثير فيه لكن ليس الآن",
    "⚪ ليس تحت سيطرتي"
  )

  fun sync() {
    val listFormatted = itemsText.mapIndexedNotNull { idx, txt ->
      if (txt.isNotBlank()) "${idx + 1}. $txt (${statusLabels[itemsStatus[idx]]})" else null
    }.joinToString("\n")
    onUpdateSummary(
      "الأشياء التي تشغلني:\n$listFormatted\nاختياري من 🟢: $chosenGreenItem\nأصغر خطوة خلال 15 دقيقة: $smallest15MinStep"
    )
  }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(
      text = "اكتب حتى 5 أشياء تشغلك الآن وصنّف كل واحدة منها:",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    for (index in 0 until 5) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(CreamBackground)
          .padding(12.dp)
      ) {
        SmallTextInput(
          value = itemsText[index],
          onValueChange = {
            itemsText[index] = it
            sync()
          },
          placeholder = "الشيء رقم ${index + 1} الذي يشغلني..."
        )
        Spacer(modifier = Modifier.height(6.dp))
        statusLabels.forEachIndexed { statusIdx, statusLabel ->
          val isSelected = itemsStatus[index] == statusIdx
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                itemsStatus[index] = statusIdx
                sync()
              }
              .padding(vertical = 2.dp)
          ) {
            RadioButton(
              selected = isSelected,
              onClick = {
                itemsStatus[index] = statusIdx
                sync()
              },
              colors = RadioButtonDefaults.colors(selectedColor = DarkGreen)
            )
            Text(
              text = statusLabel,
              style = MaterialTheme.typography.bodySmall,
              color = TextDark,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
          }
        }
      }
    }

    LabeledExerciseField(
      label = "اختر شيئًا واحدًا من 🟢:",
      value = chosenGreenItem,
      onValueChange = { chosenGreenItem = it; sync() },
      placeholder = "اكتب الشيء الذي اخترته من القائمة الخضراء..."
    )

    LabeledExerciseField(
      label = "أصغر خطوة أقدر أعملها خلال 15 دقيقة هي:",
      value = smallest15MinStep,
      onValueChange = { smallest15MinStep = it; sync() },
      placeholder = "خطوة صغيرة جدًا وواضحة خلال ١٥ دقيقة..."
    )
  }
}

// =========================================================================
// المقال 9 — محتاج مساعدة… ومكسوف أطلبها
// =========================================================================
@Composable
private fun ExerciseArticle9(onUpdateSummary: (String) -> Unit) {
  val defaultTemplate = "أنا بمر بفترة مش سهلة، ومش محتاج منك تحل مشكلتي. بس محتاج حد يسمعني شوية."
  var editableMessage by remember { mutableStateOf(defaultTemplate) }
  var isEditingEnabled by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(
      text = "رسالة جاهزة يمكنك تعديلها:",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.4f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = "\"$editableMessage\"",
        style = MaterialTheme.typography.bodyLarge,
        color = DarkGreen,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 25.sp,
        modifier = Modifier.padding(16.dp)
      )
    }

    OutlinedButton(
      onClick = { isEditingEnabled = !isEditingEnabled },
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.testTag("article9_edit_message_button")
    ) {
      Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Text("✏️ عدّل الرسالة", color = DarkGreen, fontWeight = FontWeight.Bold)
    }

    OutlinedTextField(
      value = editableMessage,
      onValueChange = {
        editableMessage = it
        onUpdateSummary("رسالتي لطلب المساندة: $it")
      },
      label = { Text("عدّل الرسالة بأسلوبك أو اكتب رسالتك الخاصة", fontSize = 12.sp) },
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = DarkGreen,
        unfocusedBorderColor = WarmBeige,
        focusedContainerColor = CreamBackground,
        unfocusedContainerColor = CreamBackground
      ),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("article9_message_input")
    )

    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "متى أفكر في مساعدة متخصصة؟",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "إذا كانت المعاناة مستمرة أو شديدة أو تؤثر على الدراسة أو العمل أو العلاقات أو القدرة على القيام بالمهام اليومية، فلا تتردد في التواصل مع مختص نفسي مناسب لمساندتك بمهنية وأمان.",
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark,
          lineHeight = 23.sp
        )
      }
    }
  }
}

// =========================================================================
// المقال 10 — إمتى أحتاج مساعدة متخصصة؟ (تقييم توعوي غير تشخيصي)
// =========================================================================
@Composable
private fun ExerciseArticle10(onUpdateSummary: (String) -> Unit) {
  val dimensions = listOf(
    "طاقتي",
    "اهتمامي بالأشياء",
    "تركيزي",
    "نومي",
    "قدرتي على القيام بمسؤولياتي",
    "تواصلي مع الناس"
  )

  val ratings = remember {
    mutableStateMapOf<String, Int>().apply {
      dimensions.forEach { put(it, 5) }
    }
  }
  var hasAnswered by remember { mutableStateOf(false) }

  fun sync() {
    hasAnswered = true
    val text = dimensions.joinToString("\n") { dim -> "$dim: ${ratings[dim] ?: 5} / 10" }
    onUpdateSummary("ملاحظتي الذاتية:\n$text")
  }

  Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Text(
      text = "تقييم توعوي بسيط للملاحظة الذاتية (وليس اختبار تشخيص):",
      style = MaterialTheme.typography.bodyMedium,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    dimensions.forEach { itemLabel ->
      val currentScore = ratings[itemLabel] ?: 5
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(CreamBackground)
          .padding(12.dp)
      ) {
        Text(
          text = "$itemLabel: ($currentScore / 10)",
          style = MaterialTheme.typography.titleSmall,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Scale1To10Selector(
          selectedValue = currentScore,
          onSelect = {
            ratings[itemLabel] = it
            sync()
          }
        )
      }
    }

    // Non-diagnostic mandatory message
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.5f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "هذه الإجابات لا تشخّص أي حالة نفسية. لكنها قد تساعدك على ملاحظة مدى تأثير ما تمر به على حياتك. إذا كانت المعاناة شديدة أو مستمرة أو تؤثر بوضوح في حياتك، ففكر في التحدث مع مختص.",
          style = MaterialTheme.typography.bodyMedium,
          color = DarkGreen,
          fontWeight = FontWeight.SemiBold,
          lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "قد تكون هذه المشاعر مرتبطة بالضغط أو الإرهاق أو عوامل أخرى. إذا استمرت أو أثرت في حياتك، تحدث مع مختص.",
          style = MaterialTheme.typography.labelMedium,
          color = TextDark.copy(alpha = 0.8f),
          lineHeight = 20.sp
        )
      }
    }
  }
}

// =========================================================================
// Shared UI Helpers for Exercises
// =========================================================================
@Composable
private fun LabeledExerciseField(
  label: String,
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = label,
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(6.dp))
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
private fun SmallTextInput(
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    placeholder = { Text(placeholder, fontSize = 13.sp) },
    singleLine = true,
    shape = RoundedCornerShape(10.dp),
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = DarkGreen,
      unfocusedBorderColor = WarmBeige,
      focusedContainerColor = Color.White,
      unfocusedContainerColor = Color.White
    ),
    modifier = Modifier.fillMaxWidth()
  )
}

@Composable
private fun Scale1To10Selector(
  selectedValue: Int,
  onSelect: (Int) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    for (num in 1..10) {
      val isSelected = selectedValue == num
      Box(
        modifier = Modifier
          .size(38.dp)
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
