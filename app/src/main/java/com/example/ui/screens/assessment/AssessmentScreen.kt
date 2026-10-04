package com.example.ui.screens.assessment

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.AssessmentResult
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.MoodDifficult
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

data class AssessmentQuestion(
  val id: Int,
  val text: String,
  val isSafetyCritical: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentScreen(
  onSaveResult: (AssessmentResult, () -> Unit) -> Unit,
  onNavigateToExplore: () -> Unit,
  onNavigateToSupport: () -> Unit,
  onBackToHome: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTool by remember { mutableStateOf<String?>(null) } // null = selection, "initial", "phq9"
  var currentQuestionIndex by remember { mutableIntStateOf(0) }
  val answers = remember { mutableStateMapOf<Int, Int>() }
  var assessmentCompleted by remember { mutableStateOf(false) }
  var completedResult by remember { mutableStateOf<AssessmentResult?>(null) }

  // Initial Assessment Questions (8 key general indicators from page 13)
  val initialQuestions = remember {
    listOf(
      AssessmentQuestion(1, "كيف تصف استقرار مزاجك العام خلال الأسبوع الماضي؟"),
      AssessmentQuestion(2, "هل تشعر بتوفر الطاقة الكافية لبدء يومك وأداء مهامك المعتادة؟"),
      AssessmentQuestion(3, "هل ما زلت تجد متعة واهتماماً في الأنشطة التي كنت تحبها؟"),
      AssessmentQuestion(4, "كيف تقيّم جودة نومك وسهولة الاستغراق فيه والاستيقاظ منتعشاً؟"),
      AssessmentQuestion(5, "هل تجد صعوبة في التركيز أو تسارعاً في التفكير وتشتت الذهن؟"),
      AssessmentQuestion(6, "ما مدى إحساسك بوطأة الضغوط اليومية وصعوبة التعامل معها؟"),
      AssessmentQuestion(7, "كيف تشعر تجاه علاقاتك وتواصلك مع المقربين منك مؤخراً؟"),
      AssessmentQuestion(8, "هل تشعر بقدرتك على تلبية مسؤولياتك اليومية دون إرهاق مفرط؟")
    )
  }

  // PHQ-9 Standard Questions (Page 14, 31)
  val phq9Questions = remember {
    listOf(
      AssessmentQuestion(1, "قلة الاهتمام أو المتعة في ممارسة الأشياء؟"),
      AssessmentQuestion(2, "الشعور بالإحباط أو الاكتئاب أو اليأس؟"),
      AssessmentQuestion(3, "صعوبة في النوم أو الاستيقاظ المتكرر، أو الإفراط في النوم؟"),
      AssessmentQuestion(4, "الشعور بالتعب أو قلة الطاقة؟"),
      AssessmentQuestion(5, "ضعف الشهية أو الإفراط في تناول الطعام؟"),
      AssessmentQuestion(6, "الشعور بالسوء تجاه نفسك أو أنك خذلت نفسك أو عائلتك؟"),
      AssessmentQuestion(7, "صعوبة في التركيز على أشياء مثل القراءة أو مشاهدة التلفاز؟"),
      AssessmentQuestion(8, "بطء في الحركة أو الكلام لدرجة لاحظها الآخرون، أو العكس (تململ واضطراب حركي)؟"),
      AssessmentQuestion(9, "أفكار بأنه من الأفضل لو كنت ميتاً أو الرغبة في إيذاء نفسك؟", isSafetyCritical = true)
    )
  }

  val activeQuestions = if (selectedTool == "initial") initialQuestions else phq9Questions

  BackHandler {
    if (assessmentCompleted) {
      assessmentCompleted = false
      selectedTool = null
    } else if (selectedTool != null) {
      selectedTool = null
      currentQuestionIndex = 0
      answers.clear()
    } else {
      onBackToHome()
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
          text = if (selectedTool == null) "قيّم حالتك" else if (selectedTool == "initial") "التقييم المبدئي" else "فحص أعراض الاكتئاب",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = {
            if (assessmentCompleted) {
              assessmentCompleted = false
              selectedTool = null
            } else if (selectedTool != null) {
              selectedTool = null
              currentQuestionIndex = 0
              answers.clear()
            } else {
              onBackToHome()
            }
          },
          modifier = Modifier.testTag("assessment_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "رجوع",
            tint = DarkGreen
          )
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
    )

    if (selectedTool == null) {
      // Screen 5 Selection Screen (Page 12)
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 100.dp)
      ) {
        item {
          Text(
            text = "خد خطوة لفهم نفسك 🌱",
            style = MaterialTheme.typography.headlineMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "اختار التقييم المناسب ليك:",
            style = MaterialTheme.typography.titleMedium,
            color = TextDark.copy(alpha = 0.8f)
          )
          Spacer(modifier = Modifier.height(20.dp))
        }

        // Option 1: التقييم المبدئي (Page 12-13)
        item {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                selectedTool = "initial"
                currentQuestionIndex = 0
                answers.clear()
              }
              .testTag("select_initial_assessment")
          ) {
            Column(modifier = Modifier.padding(20.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SageGreenPrimary.copy(alpha = 0.3f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = DarkGreen,
                    modifier = Modifier.size(24.dp)
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = "التقييم المبدئي 🌱",
                    style = MaterialTheme.typography.titleLarge,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "نسمة الحياة للاستكشاف الذاتي",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextDark.copy(alpha = 0.6f)
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = "أسئلة قصيرة تساعدك تلاحظ حالتك النفسية ومؤشراتك الحيوية بشكل عام (المزاج، النوم، التركيز، الضغط...).",
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark,
                lineHeight = 22.sp
              )

              Spacer(modifier = Modifier.height(12.dp))

              // Mandatory Note (Page 12)
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(WarmBeige.copy(alpha = 0.5f))
                  .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = null,
                  tint = DarkGreen,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "ملاحظة: هذا التقييم ليس تشخيصاً طبياً.",
                  style = MaterialTheme.typography.labelSmall,
                  color = TextDark,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
        }

        // Option 2: فحص أعراض الاكتئاب (Page 12-14)
        item {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                selectedTool = "phq9"
                currentQuestionIndex = 0
                answers.clear()
              }
              .testTag("select_phq9_assessment")
          ) {
            Column(modifier = Modifier.padding(20.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(WarmBeige),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = null,
                    tint = DarkGreen,
                    modifier = Modifier.size(24.dp)
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = "فحص أعراض الاكتئاب 🌸",
                    style = MaterialTheme.typography.titleLarge,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "استبيان معتمد (PHQ-9)",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextDark.copy(alpha = 0.6f)
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = "استبيان معتمد عالمياً لفحص وتتبع أعراض الاكتئاب وشدتها خلال الأسبوعين الماضيين بموضوعية.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark,
                lineHeight = 22.sp
              )

              Spacer(modifier = Modifier.height(12.dp))

              // Mandatory Note (Page 12)
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(WarmBeige.copy(alpha = 0.5f))
                  .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = null,
                  tint = DarkGreen,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "ملاحظة: الفحص ليس تشخيصاً، والنتيجة تحتاج إلى تفسير ولا تغني عن التقييم المتخصص.",
                  style = MaterialTheme.typography.labelSmall,
                  color = TextDark,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }
      }
    } else if (!assessmentCompleted) {
      // Active Quiz Step
      val question = activeQuestions[currentQuestionIndex]
      val progress = (currentQuestionIndex + 1).toFloat() / activeQuestions.size

      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp, vertical = 12.dp)
      ) {
        // Progress Indicator
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "السؤال ${currentQuestionIndex + 1} من ${activeQuestions.size}",
            style = MaterialTheme.typography.labelMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = TextDark.copy(alpha = 0.6f)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = DarkGreen,
          trackColor = WarmBeige
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Question Card
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Text(
              text = question.text,
              style = MaterialTheme.typography.titleLarge,
              color = DarkGreen,
              fontWeight = FontWeight.SemiBold,
              lineHeight = 30.sp
            )

            if (question.isSafetyCritical) {
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "نحن نهتم لأمرك وسلامتك فوق كل شيء. إجابتك تساعدنا في تقديم الدعم المناسب.",
                style = MaterialTheme.typography.labelSmall,
                color = MoodDifficult
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Answer Options
        val options = if (selectedTool == "initial") {
          listOf(
            0 to "بشكل ممتاز / مطمئن تماماً",
            1 to "جيد إلى حد ما مع بعض التفاوت",
            2 to "أشعر بصعوبة ملحوظة أغلب الأوقات",
            3 to "صعب ومتعب للغاية ومستمر"
          )
        } else {
          listOf(
            0 to "أبداً (لم يحدث)",
            1 to "في عدة أيام",
            2 to "أكثر من نصف الأيام",
            3 to "كل يوم تقريباً"
          )
        }

        options.forEach { (score, label) ->
          val isSelected = answers[question.id] == score
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isSelected) SageGreenPrimary.copy(alpha = 0.25f) else Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(
              width = if (isSelected) 2.dp else 1.dp,
              color = if (isSelected) DarkGreen else WarmBeige
            ),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 5.dp)
              .clickable {
                answers[question.id] = score
              }
              .testTag("assessment_option_${question.id}_$score")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(22.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) DarkGreen else Color.Transparent)
                  .border(2.dp, if (isSelected) DarkGreen else WarmBeige, CircleShape)
              )
              Spacer(modifier = Modifier.width(14.dp))
              Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = TextDark,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
              )
            }
          }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next / Finish Button
        Button(
          onClick = {
            if (currentQuestionIndex < activeQuestions.size - 1) {
              currentQuestionIndex++
            } else {
              // Calculate result
              val totalScore = answers.values.sum()
              val maxPossible = activeQuestions.size * 3
              val isPhq9 = (selectedTool == "phq9")
              val hasSafetyFlag = isPhq9 && (answers[9] ?: 0) > 0

              val (title, text, recommendation) = if (isPhq9) {
                when {
                  totalScore <= 4 -> Triple(
                    "أعراض طفيفة أو مطمئنة",
                    "تشير درجات الفحص إلى مستوى منخفض جداً من الأعراض، وهو مؤشر مطمئن يعكس توازناً عاماً.",
                    "استمر في رعاية نفسك وعاداتك اليومية واستكشاف محتوى الراحة والسكينة."
                  )
                  totalScore <= 9 -> Triple(
                    "أعراض خفيفة قد تستحق المتابعة",
                    "إجاباتك تشير إلى وجود بعض الأعراض الخفيفة التي قد تظهر استجابة لضغوط معتادة.",
                    "يمكنك استكشاف تقنيات تنظيم النوم وتمارين التهدئة والتنفس في قسم «خد نفس»."
                  )
                  totalScore <= 14 -> Triple(
                    "أعراض متوسطة تستحق الاهتمام",
                    "إجاباتك تشير إلى وجود بعض الأمور التي قد تستحق مزيداً من الانتباه والاعتناء بمشاعرك وطاقتك.",
                    "ننصحك بمشاركة ما تشعر به مع شخص موثوق أو طلب توجيه من فريق الدعم المتخصص."
                  )
                  else -> Triple(
                    "أعراض واضحة ومستمرة",
                    "إجاباتك تشير إلى ضغوط نفسية ملحوظة تؤثر على يومك ونشاطك.",
                    "نوصيك بشدة بعدم التردد في استشارة طبيب أو معالج نفسي متخصص للحصول على مساندة مهنية."
                  )
                }
              } else {
                if (totalScore <= 6) {
                  Triple(
                    "مؤشرات مستقرة ومتزنة 🌱",
                    "إجاباتك تعكس حالة جيدة من التوازن النفسي والقدرة على إدارة متطلبات اليوم.",
                    "يمكنك مواصلة تعزيز وعيك الذاتي من خلال مقالات تطوير الذات وبناء الحدود."
                  )
                } else if (totalScore <= 14) {
                  Triple(
                    "أمور تستحق الانتباه والرعاية 🌿",
                    "إجاباتك تشير إلى وجود بعض الأمور التي قد تستحق مزيداً من الانتباه. من الطبيعي أن نمر بفترات إرهاق.",
                    "يمكنك استكشاف المحتوى المناسب في مكتبة «أفهم نفسي» أو التفكير في طلب دعم متخصص إذا استمر الأمر."
                  )
                } else {
                  Triple(
                    "حاجة إلى التباطؤ والدعم 🕊️",
                    "تشير إجاباتك إلى أنك تتحمل عبئاً كبيراً في الوقت الراهن وتواجه استنزافاً في طاقتك.",
                    "أنت لست وحدك؛ تواصل مع فريق نسمة الحياة عبر قسم «محتاج أتكلم» أو راجع أخصائياً موثوقاً."
                  )
                }
              }

              val result = AssessmentResult(
                assessmentType = if (isPhq9) "فحص أعراض الاكتئاب (PHQ-9)" else "التقييم المبدئي",
                score = totalScore,
                maxScore = maxPossible,
                resultTitle = title,
                resultText = text,
                recommendation = recommendation,
                hasSafetyFlag = hasSafetyFlag,
                timestamp = System.currentTimeMillis()
              )

              onSaveResult(result) {
                completedResult = result
                assessmentCompleted = true
              }
            }
          },
          enabled = answers.containsKey(question.id),
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag("assessment_next_button")
        ) {
          Text(
            text = if (currentQuestionIndex < activeQuestions.size - 1) "التالي ←" else "عرض النتيجة 🌱",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    } else {
      // Result Screen (Page 13, 14, 31)
      val result = completedResult
      if (result != null) {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          contentPadding = PaddingValues(top = 10.dp, bottom = 100.dp)
        ) {
          item {
            Card(
              shape = RoundedCornerShape(22.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Box(
                  modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(SageGreenPrimary.copy(alpha = 0.25f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = DarkGreen,
                    modifier = Modifier.size(36.dp)
                  )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                  text = result.resultTitle,
                  style = MaterialTheme.typography.headlineMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold,
                  textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = "الدرجة المحسوبة: ${result.score} من ${result.maxScore}",
                  style = MaterialTheme.typography.labelMedium,
                  color = TextDark.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                  text = result.resultText,
                  style = MaterialTheme.typography.bodyLarge,
                  color = TextDark,
                  textAlign = TextAlign.Center,
                  lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                  shape = RoundedCornerShape(12.dp),
                  colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                      text = "الخطوة المقترحة:",
                      style = MaterialTheme.typography.labelMedium,
                      color = DarkGreen,
                      fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = result.recommendation,
                      style = MaterialTheme.typography.bodyMedium,
                      color = TextDark
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))
          }

          // Safety Flag Banner (Mandatory Protocol from Page 14, 31)
          if (result.hasSafetyFlag) {
            item {
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFDECE8)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MoodDifficult),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(18.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Warning,
                      contentDescription = null,
                      tint = MoodDifficult,
                      modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "مسار الأمان والرعاية الفورية 🤍",
                      style = MaterialTheme.typography.titleMedium,
                      color = MoodDifficult,
                      fontWeight = FontWeight.Bold
                    )
                  }

                  Spacer(modifier = Modifier.height(8.dp))

                  Text(
                    text = "سلامتك وحياتك ثمينة جداً. إذا كنت تمر بأفكار صعبة أو تشعر برغبة في إيذاء نفسك، من فضلك تواصل فوراً مع خدمات الطوارئ أو خطوط المساندة المجانية المتخصصة:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDark,
                    lineHeight = 22.sp
                  )

                  Spacer(modifier = Modifier.height(10.dp))

                  Text(
                    text = "• الخط الساخن للأمانة العامة للصحة النفسية: 16328 / 08008880700\n• خط نجدة الطوارئ: 122 أو 123",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )

                  Spacer(modifier = Modifier.height(8.dp))

                  Text(
                    text = "تنبيه: تطبيق نسمة الحياة لا يمثل خدمة طوارئ طبية أو إسعافاً فورياً.",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextDark.copy(alpha = 0.7f)
                  )
                }
              }

              Spacer(modifier = Modifier.height(16.dp))
            }
          }

          // Action navigation buttons
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = onNavigateToExplore,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                  .weight(1f)
                  .height(50.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("استكشف المحتوى", fontSize = 13.sp)
                }
              }

              Button(
                onClick = onNavigateToSupport,
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                  .weight(1f)
                  .height(50.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(imageVector = Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("محتاج أتكلم", fontSize = 13.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
              onClick = {
                assessmentCompleted = false
                selectedTool = null
              },
              colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
            ) {
              Text("العودة لاختيار تقييم آخر", color = DarkGreen, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
