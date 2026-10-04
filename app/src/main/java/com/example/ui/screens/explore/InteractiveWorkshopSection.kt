package com.example.ui.screens.explore

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentItem
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.MoodDifficult
import com.example.ui.theme.MoodGreat
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

data class WorkshopFieldSpec(
  val key: String,
  val label: String,
  val placeholder: String,
  val quickSuggestions: List<String> = emptyList()
)

data class EmergencyScenario(
  val title: String,
  val contextText: String,
  val whatToDoNow: String,
  val whatNotToDo: String,
  val whenToSeekHelp: String
)

@Composable
fun InteractiveWorkshopSection(
  item: ContentItem,
  onSaveWorkshopNote: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var isSavedFeedbackVisible by remember(item.id) { mutableStateOf(false) }

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    modifier = modifier
      .fillMaxWidth()
      .border(1.5.dp, SageGreenPrimary.copy(alpha = 0.6f), RoundedCornerShape(22.dp))
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      // Header Badge
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
              .background(DarkGreen),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "مساحة التطبيق العملي للورشة ✍️",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "طبق خطوات الورشة الآن واحفظ مخرجك الشخصي في «رحلتي»",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.65f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      when {
        // Workshop 2: Emergency Room Simulation
        item.id == 2L || item.title.contains("غرفة الطوارئ النفسية") -> {
          EmergencyRoomSimulation(
            onSaveSummary = { title, content ->
              onSaveWorkshopNote(title, content)
              isSavedFeedbackVisible = true
            }
          )
        }

        // Workshop 3: Disagreement Without a Battle
        item.id == 3L || item.title.contains("خلاف بدون معركة") -> {
          DisagreementWithoutBattleTool(
            onSaveSummary = { title, content ->
              onSaveWorkshopNote(title, content)
              isSavedFeedbackVisible = true
            }
          )
        }

        // All Guided Builder Workshops (1, 4, 5, 6, 7, 8, 9, 10)
        else -> {
          val specs = remember(item.id, item.title) { getWorkshopSpecs(item) }
          val outcomeLabel = remember(item.id, item.title) { getWorkshopOutcomeTitle(item) }
          GuidedWorkshopBuilder(
            workshopTitle = item.title,
            outcomeTitle = outcomeLabel,
            fields = specs,
            onSaveSummary = { title, content ->
              onSaveWorkshopNote(title, content)
              isSavedFeedbackVisible = true
            }
          )
        }
      }

      AnimatedVisibility(visible = isSavedFeedbackVisible) {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.45f)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "تم حفظ الناتج العملي للورشة في دفتر «رحلتي» للرجوع إليه في أي وقت 🌿",
              style = MaterialTheme.typography.bodySmall,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
private fun EmergencyRoomSimulation(
  onSaveSummary: (String, String) -> Unit
) {
  val scenarios = remember {
    listOf(
      EmergencyScenario(
        title = "١. شخص تعرض لضغط شديد",
        contextText = "شخص أمامك يرتجف أو يتنفس بسرعة بعد خبر ضاغط أو أزمة مفاجئة في العمل أو الحياة.",
        whatToDoNow = "• التواجد الهادئ بجانبه وتأمين المكان.\n• توجيهه بلطف لتنفس بطيء (شهيق ٤ ثوانٍ وزفير أطول).\n• تقديم كوب ماء وسؤاله عن احتياجه اللحظي البسيط.",
        whatNotToDo = "• لا تقل له «اهدأ فوراً الموضوع بسيط».\n• لا تحاصره بأسئلة كثيرة عن التفاصيل الآن.\n• لا تصدر أحكاماً أو تلومه على انفعاله.",
        whenToSeekHelp = "إذا استمرت نوبة الهلع أو ظهر ألم جسدي حاد أو أفكار بإيذاء النفس أو عدم قدرة تامة على التواصل."
      ),
      EmergencyScenario(
        title = "٢. أم منهكة",
        contextText = "أم تشعر باستنزاف كامل، تبكي من تراكم المسؤوليات اليومية وتشعر بالذنب لأنها لم تعد تتحمل.",
        whatToDoNow = "• الاستماع المتعاطف دون مقاطعة وتأكيد أن تعبها طبيعي ومفهوم.\n• عرض مساعدة عملية محددة فوراً (مثل رعاية الأطفال لساعتين لتنام أو ترتاح).",
        whatNotToDo = "• لا تقارنها بأمهات أخريات.\n• لا تقل «كل الأمهات بيتعبوا، استحملي».\n• لا تزيد عليها قائمة النصائح والمهام.",
        whenToSeekHelp = "إذا صاحب الإنهاك فقدان تام للنوم أو الشهية، أو شعور مستمر باليأس وانطفاء الرغبة في الحياة لأسابيع."
      ),
      EmergencyScenario(
        title = "٣. طالب مقبل على امتحان",
        contextText = "طالب يشعر بشلل ذهني وخوف شديد من الفشل قبل الامتحان مع تسارع نبضات القلب ونسيان ما ذاكره.",
        whatToDoNow = "• طمأنته أن قيمته كإنسان أكبر من أي نتيجة.\n• مساعدته على تمرين التجذير الحسي وتقسيم المراجعة لـ ١٥ دقيقة فقط مع راحة.",
        whatNotToDo = "• لا تذكره بتضحيات الأسرة أو عواقب الرسوب الآن.\n• لا تقارنه بزملائه المتفوقين.",
        whenToSeekHelp = "إذا تحول قلق الامتحان إلى نوبات فزع متكررة تمنعه من دخول اللجان أو النوم لأيام متتالية."
      ),
      EmergencyScenario(
        title = "٤. شخص فقد شخصاً عزيزاً",
        contextText = "شخص يمر بصدمة الفقد والحزن الشديد بعد وفاة أو فراق إنسان غالٍ عليه.",
        whatToDoNow = "• الحضور الصامت الدافئ واحترام مساحته في البكاء أو الصمت.\n• المساندة في الأمور الحياتية اليومية دون إلحاح.",
        whatNotToDo = "• لا تقل «لا تبكِ، البكاء يعذبه» أو «تجاوز الأمر بسرعة».\n• لا تحاول فرض مراحل حزن ثابتة عليه.",
        whenToSeekHelp = "إذا طال الانعزال التام لشهور مع توقف كامل عن الأكل أو العمل، أو ظهور رغبة في اللحاق بالمتوفى."
      ),
      EmergencyScenario(
        title = "٥. شخص يعاني من أعراض نفسية مستمرة",
        contextText = "شخص يعاني منذ أسابيع من حزن عميق، أرق مستمر، انسحاب اجتماعي، أو قلق لا يهدأ.",
        whatToDoNow = "• الاستماع باحترام وتشجيعه برفق على أن طلب المساعدة المتخصصة قوة ووعي.\n• مساعدته في حجز موعد أو مرافقته إذا رغب.",
        whatNotToDo = "• لا تلعب دور الطبيب ولا تشخص حالته باسم مرض معين.\n• لا تقترح أدوية أو وصفات من تجربتك الخاصة.",
        whenToSeekHelp = "الآن وبشكل مباشر عبر التوجه لطبيب نفسي أو معالج نفسي معتمد لتقييم الحالة المهني."
      )
    )
  }

  var selectedIndex by remember { mutableIntStateOf(0) }
  val active = scenarios[selectedIndex]
  var userReflection by remember(selectedIndex) { mutableStateOf("") }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(
      text = "اختر السيناريو للتدرب على التصرف الصحيح (دون تشخيص أو لعب دور الطبيب):",
      style = MaterialTheme.typography.bodyMedium,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    scenarios.forEachIndexed { idx, sc ->
      val isSel = idx == selectedIndex
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isSel) DarkGreen else CreamBackground
        ),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { selectedIndex = idx }
      ) {
        Text(
          text = sc.title,
          color = if (isSel) Color.White else DarkGreen,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )
      }
    }

    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = CreamBackground),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "الموقف: ${active.contextText}",
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark,
          fontWeight = FontWeight.SemiBold
        )

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SoftMint.copy(alpha = 0.5f))
            .padding(12.dp)
        ) {
          Column {
            Text("✅ ماذا أفعل الآن؟", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(active.whatToDoNow, color = TextDark, fontSize = 12.sp, lineHeight = 20.sp)
          }
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MoodDifficult.copy(alpha = 0.25f))
            .padding(12.dp)
        ) {
          Column {
            Text("❌ ماذا لا أفعل؟", fontWeight = FontWeight.Bold, color = Color(0xFF8C3B32), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(active.whatNotToDo, color = TextDark, fontSize = 12.sp, lineHeight = 20.sp)
          }
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(WarmBeige.copy(alpha = 0.55f))
            .padding(12.dp)
        ) {
          Column {
            Text("🏥 متى أطلب مساعدة متخصصة؟", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(active.whenToSeekHelp, color = TextDark, fontSize = 12.sp, lineHeight = 20.sp)
          }
        }

        OutlinedTextField(
          value = userReflection,
          onValueChange = { userReflection = it },
          label = { Text("ملاحظتي التطبيقية أو كيف سأتصرف في هذا الموقف:", fontSize = 12.sp) },
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = WarmBeige,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
          ),
          modifier = Modifier.fillMaxWidth()
        )

        Button(
          onClick = {
            val summary = """
              السيناريو: ${active.title}
              ماذا أفعل الآن:
              ${active.whatToDoNow}
              ماذا لا أفعل:
              ${active.whatNotToDo}
              متى أطلب مساعدة:
              ${active.whenToSeekHelp}
              تطبيقي الشخصي: ${userReflection.ifBlank { "التصرف بهدوء ومساندة أولية دون تشخيص طبي." }}
            """.trimIndent()
            onSaveSummary("مخرج ورشة غرفة الطوارئ: ${active.title}", summary)
          },
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("حفظ بطاقة الطوارئ في رحلتي 🛟", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }
    }
  }
}

@Composable
private fun DisagreementWithoutBattleTool(
  onSaveSummary: (String, String) -> Unit
) {
  var problemTopic by remember { mutableStateOf("تأخر الطرف الآخر عن الموعد أو الانشغال بالهاتف وقت الحديث") }
  var myNeed by remember { mutableStateOf("أحتاج للشعور بالاهتمام وبأن وقتنا معاً له أولوية") }
  var myBoundary by remember { mutableStateOf("أحب أن نتفق على وقت محدد نضع فيه الهواتف جانباً") }
  var proposedAgreement by remember { mutableStateOf("تخصيص ٢٠ دقيقة يومياً للحديث الهادئ دون مقاطعات") }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(
      text = "طبق الطرق الثلاث على مشكلة أسرية أو زوجية واحدة لتخرج بصياغة «حوار صحي»:",
      style = MaterialTheme.typography.bodyMedium,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    OutlinedTextField(
      value = problemTopic,
      onValueChange = { problemTopic = it },
      label = { Text("١. المشكلة أو الموقف باختصار", fontSize = 12.sp) },
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = DarkGreen,
        unfocusedBorderColor = WarmBeige,
        focusedContainerColor = CreamBackground,
        unfocusedContainerColor = CreamBackground
      ),
      modifier = Modifier.fillMaxWidth()
    )

    // Compare 3 Communication Ways
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MoodDifficult.copy(alpha = 0.22f)),
        modifier = Modifier.weight(1f)
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text("❌ هجوم", fontWeight = FontWeight.Bold, color = Color(0xFF8C3B32), fontSize = 12.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "«إنت دايماً أناني ومبتفكرش غير في نفسك!»",
            fontSize = 11.sp,
            color = TextDark,
            lineHeight = 16.sp
          )
        }
      }
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f)),
        modifier = Modifier.weight(1f)
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text("❌ انسحاب", fontWeight = FontWeight.Bold, color = TextDark, fontSize = 12.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "«مفيش فايدة من الكلام أصلاً... سألتزم الصمت العقابي.»",
            fontSize = 11.sp,
            color = TextDark,
            lineHeight = 16.sp
          )
        }
      }
    }

    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.45f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text("✅ الحوار الصحي (التدريب العملي):", fontWeight = FontWeight.Bold, color = DarkGreen, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = myNeed,
          onValueChange = { myNeed = it },
          label = { Text("التعبير عن الاحتياج (أنا أشعر بـ... وأحتاج إلى...)", fontSize = 12.sp) },
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = WarmBeige,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
          ),
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = myBoundary,
          onValueChange = { myBoundary = it },
          label = { Text("وضع الحدود باحترام", fontSize = 12.sp) },
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = WarmBeige,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
          ),
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = proposedAgreement,
          onValueChange = { proposedAgreement = it },
          label = { Text("الوصول إلى اتفاق عملي في البيت", fontSize = 12.sp) },
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

    Button(
      onClick = {
        val text = """
          الموقف: $problemTopic
          ✅ رسالتي بالحوار الصحي:
          • احتياجي: $myNeed
          • حدودي: $myBoundary
          • اتفاقنا المقترح: $proposedAgreement
        """.trimIndent()
        onSaveSummary("ورشة خلاف بدون معركة 🤝", text)
      },
      colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.width(8.dp))
      Text("حفظ صياغة الحوار الصحي في رحلتي 🤍", fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
  }
}

@Composable
private fun GuidedWorkshopBuilder(
  workshopTitle: String,
  outcomeTitle: String,
  fields: List<WorkshopFieldSpec>,
  onSaveSummary: (String, String) -> Unit
) {
  val answers = remember(workshopTitle) {
    mutableStateMapOf<String, String>().apply {
      fields.forEach { spec ->
        put(spec.key, spec.quickSuggestions.firstOrNull() ?: "")
      }
    }
  }

  Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    fields.forEachIndexed { index, spec ->
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(CreamBackground)
          .padding(14.dp)
      ) {
        Text(
          text = "${index + 1}. ${spec.label}",
          style = MaterialTheme.typography.titleSmall,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )

        if (spec.quickSuggestions.isNotEmpty()) {
          Spacer(modifier = Modifier.height(6.dp))
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            spec.quickSuggestions.forEach { suggestion ->
              val currentVal = answers[spec.key] ?: ""
              val isSelected = currentVal.contains(suggestion)
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSelected) DarkGreen else Color.White)
                  .border(1.dp, if (isSelected) DarkGreen else WarmBeige, RoundedCornerShape(10.dp))
                  .clickable {
                    answers[spec.key] = suggestion
                  }
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = "✨ $suggestion",
                  fontSize = 11.sp,
                  color = if (isSelected) Color.White else TextDark
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = answers[spec.key] ?: "",
          onValueChange = { answers[spec.key] = it },
          placeholder = { Text(spec.placeholder, fontSize = 12.sp) },
          shape = RoundedCornerShape(12.dp),
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

    // Outcome Preview Box
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.35f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = "📋 الناتج النهائي: $outcomeTitle",
          style = MaterialTheme.typography.titleSmall,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        fields.forEach { spec ->
          val value = answers[spec.key]?.takeIf { it.isNotBlank() } ?: "—"
          Text(
            text = "• ${spec.label}: $value",
            style = MaterialTheme.typography.bodySmall,
            color = TextDark,
            lineHeight = 20.sp
          )
        }
      }
    }

    Button(
      onClick = {
        val formatted = buildString {
          appendLine("الناتج العملي: $outcomeTitle")
          appendLine("────────────────────")
          fields.forEach { spec ->
            appendLine("• ${spec.label}:")
            appendLine("  ${answers[spec.key]?.ifBlank { "لم يُحدد بعد" }}")
          }
        }.trimIndent()
        onSaveSummary(outcomeTitle, formatted)
      },
      colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.width(8.dp))
      Text("حفظ «$outcomeTitle» في رحلتي 🌿", fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
  }
}

private fun getWorkshopOutcomeTitle(item: ContentItem): String {
  return when {
    item.id == 1L || item.title.contains("صندوق أدواتي") -> "صندوق أدواتي النفسية الشخصي 🧰"
    item.id == 4L || item.title.contains("خطة إنقاذي") -> "خطة إنقاذي النفسية الشخصيّة 🛟"
    item.id == 5L || item.title.contains("أنا تعبت") -> "خريطة الاستنزاف الشخصية 🔋"
    item.id == 6L || item.title.contains("لما دماغي ما تسكتش") -> "خريطة تفكيك التفكير الزائد 🧠"
    item.id == 7L || item.title.contains("أنا مشاعري مش أعدائي") -> "مسار التعامل الواعي مع مشاعري 💛"
    item.id == 8L || item.title.contains("اتأذيت") -> "وثيقة التعافي وفهم أثر التجربة 🌱"
    item.id == 9L || item.title.contains("أقول «لا»") -> "دليل حدودي الشخصية بدون ذنب 🛡️"
    item.id == 10L || item.title.contains("خطة العودة لنفسي") -> "خطة الرعاية النفسية الشخصية (العودة لنفسي) 🕊️"
    else -> "مخرج الورشة التطبيقية 🌿"
  }
}

private fun getWorkshopSpecs(item: ContentItem): List<WorkshopFieldSpec> {
  return when {
    item.id == 1L || item.title.contains("صندوق أدواتي") -> listOf(
      WorkshopFieldSpec(
        key = "trigger",
        label = "تحديد ما يضغط عليّ الآن",
        placeholder = "اكتب أكثر ما يضغط عليك...",
        quickSuggestions = listOf("تراكم المهام والمسؤوليات اليومية", "التفكير في إرضاء الجميع على حساب راحتي")
      ),
      WorkshopFieldSpec(
        key = "coping_method",
        label = "اختيار طريقة مناسبة للتعامل",
        placeholder = "كيف ستتعامل مع هذا الضغط؟",
        quickSuggestions = listOf("تقسيم المهام لأولويات صغيرة وأخذ استراحة تنفس", "الاعتذار اللطيف عن أي التزام إضافي اليوم")
      ),
      WorkshopFieldSpec(
        key = "helping_list",
        label = "قائمتي الشخصية بالأشياء التي تساعدني",
        placeholder = "أشياء تعيد لك هدوءك...",
        quickSuggestions = listOf("المشي الهادئ ١٥ دقيقة، سماع صوت المطر، كوب شاي دافئ", "الكتابة في دفتري والابتعاد عن الشاشات لساعة")
      ),
      WorkshopFieldSpec(
        key = "hard_day_plan",
        label = "خطتي للتعامل مع يوم نفسي صعب",
        placeholder = "خطوتك الأولى في اليوم الصعب...",
        quickSuggestions = listOf("أخفف توقعاتي من نفسي للنصف وأكتفي بالضروريات وأنام مبكراً")
      )
    )

    item.id == 4L || item.title.contains("خطة إنقاذي") -> listOf(
      WorkshopFieldSpec(
        key = "early_signs",
        label = "علامات الإنذار المبكرة عندي",
        placeholder = "كيف تعرف أنك بدأت تتعب؟",
        quickSuggestions = listOf("اضطراب النوم، سرعة الانفعال، وشد مستمر في الكتفين", "الرغبة في الانعزال وتأجيل أبسط المهام")
      ),
      WorkshopFieldSpec(
        key = "stress_increases",
        label = "الأشياء التي تزيد ضغطي",
        placeholder = "ما الذي يضاعف توترك؟",
        quickSuggestions = listOf("قلة النوم، المقارنات على السوشيال ميديا، وكثرة الجدال")
      ),
      WorkshopFieldSpec(
        key = "what_helps",
        label = "الأشياء التي تساعدني على استعادة توازني",
        placeholder = "ما الذي يهدئك فعلاً؟",
        quickSuggestions = listOf("جلسة تنفس عميق، الصلاة والتأمل، أو الحديث مع شخص آمن")
      ),
      WorkshopFieldSpec(
        key = "safe_people",
        label = "الأشخاص الذين أستطيع اللجوء إليهم",
        placeholder = "اكتب اسماً أو صفة لشخص تثق به...",
        quickSuggestions = listOf("صديق مقرب يسمعني دون أحكام / فرد دافئ من العائلة")
      ),
      WorkshopFieldSpec(
        key = "when_specialist",
        label = "متى أحتاج إلى مختص؟",
        placeholder = "العلامة التي تخبرك بضرورة الاستشارة...",
        quickSuggestions = listOf("عندما يستمر الضيق لأكثر من أسبوعين ويعطل نومي وعملي")
      ),
      WorkshopFieldSpec(
        key = "where_to_go",
        label = "إلى أين أتوجه للحصول على المساعدة؟",
        placeholder = "الجهة أو المختص الموثوق...",
        quickSuggestions = listOf("فريق الدعم الأولي في نسمة الحياة أو عيادة طبيب/أخصائي نفسي معتمد")
      )
    )

    item.id == 5L || item.title.contains("أنا تعبت") -> listOf(
      WorkshopFieldSpec(
        key = "drain_sources",
        label = "مصادر استنزافي الحالية",
        placeholder = "أين تتسرب طاقتك؟",
        quickSuggestions = listOf("حمل هموم الآخرين، العمل دون فترات راحة، والقلق على المستقبل")
      ),
      WorkshopFieldSpec(
        key = "body_vs_mind",
        label = "التفرقة بين تعبي الجسدي وتعبي النفسي",
        placeholder = "ماذا يحتاج جسدك وماذا تحتاج روحك؟",
        quickSuggestions = listOf("تعبي نفسي لأنني أنام ولا أستيقظ مرتاحاً؛ أحتاج لأمان وتخفيف الأحمال")
      ),
      WorkshopFieldSpec(
        key = "collapse_signs",
        label = "العلامات التي تظهر عليّ عندما أبدأ في الانهيار",
        placeholder = "إشارات الإنهاك الشديد...",
        quickSuggestions = listOf("الشعور بالاختناق دون سبب واضح، وفقدان الطاقة حتى للكلام")
      ),
      WorkshopFieldSpec(
        key = "personal_drain_map",
        label = "خريطة الاستنزاف الشخصية (أول ثقب سأغلقه اليوم)",
        placeholder = "خطوة واحدة لحماية طاقتك...",
        quickSuggestions = listOf("إيقاف المهام غير العاجلة وتخصيص ٣٠ دقيقة راحة صافية لنفسي")
      )
    )

    item.id == 6L || item.title.contains("لما دماغي ما تسكتش") -> listOf(
      WorkshopFieldSpec(
        key = "event",
        label = "١. الحدث (ما الذي وقع فعلاً بدون تفسير؟)",
        placeholder = "اكتب الحدث المجرد...",
        quickSuggestions = listOf("تأخر الرد على رسالتي أو تغيّر مفاجئ في خطة العمل")
      ),
      WorkshopFieldSpec(
        key = "thought",
        label = "٢. الفكرة (ماذا قال لي عقلي عن الحدث؟)",
        placeholder = "الفكرة التلقائية...",
        quickSuggestions = listOf("«أكيد هناك مشكلة كبيرة أو أنني مقصر»")
      ),
      WorkshopFieldSpec(
        key = "feeling",
        label = "٣. الشعور الناتج",
        placeholder = "بماذا شعرت؟",
        quickSuggestions = listOf("قلق، توتر في الصدر، وانشغال ذهني مستمر")
      ),
      WorkshopFieldSpec(
        key = "action_step",
        label = "٤. إيقاف الاجترار وتحويله لخطوة عملية صغيرة",
        placeholder = "بدل التفكير الدائري، سأفعل...",
        quickSuggestions = listOf("الفكرة ليست حقيقة مؤكدة؛ سأقوم بخطوة واحدة واضحة الآن وأترك الباقي")
      )
    )

    item.id == 7L || item.title.contains("أنا مشاعري مش أعدائي") -> listOf(
      WorkshopFieldSpec(
        key = "i_feel",
        label = "١. أشعر (تسمية الشعور بصدق)",
        placeholder = "غضب، حزن، خوف، ذنب، أو خذلان...",
        quickSuggestions = listOf("أشعر بالحزن والخذلان من موقف قريب", "أشعر بالغضب نتيجة تخطي حدودي")
      ),
      WorkshopFieldSpec(
        key = "i_understand",
        label = "٢. أفهم (ما الرسالة أو الاحتياج خلف هذا الشعور؟)",
        placeholder = "ماذا يحاول هذا الشعور أن يحمي بداخلك؟",
        quickSuggestions = listOf("هذا الشعور يخبرني أنني أحتاج للتقدير والاحترام والأمان")
      ),
      WorkshopFieldSpec(
        key = "i_express",
        label = "٣. أعبّر (بدون كبت وبدون انفجار)",
        placeholder = "جملة هادئة وصادقة تعبر بها...",
        quickSuggestions = listOf("«لقد تضايقت من هذا التصرف وأحتاج أن نتحدث بهدوء»")
      ),
      WorkshopFieldSpec(
        key = "i_act",
        label = "٤. أتصرف (خطوة متزنة تحميني)",
        placeholder = "ما التصرف الصحي الآن؟",
        quickSuggestions = listOf("أعبر بوضوح أو آخذ مسافة هادئة حتى أستعيد اتزاني")
      )
    )

    item.id == 8L || item.title.contains("اتأذيت") -> listOf(
      WorkshopFieldSpec(
        key = "what_happened",
        label = "١. ما الذي حدث؟",
        placeholder = "وصف مختصر للتجربة...",
        quickSuggestions = listOf("تجربة خذلان أو علاقة مرهقة تركت في نفسي أثراً")
      ),
      WorkshopFieldSpec(
        key = "impact_on_me",
        label = "٢. ما الأثر الذي تركه فيّ؟",
        placeholder = "كيف أثرت التجربة على مشاعرك وثقتك؟",
        quickSuggestions = listOf("جعلتني أكثر حذراً وخوفاً من القرب أو الثقة السريعة")
      ),
      WorkshopFieldSpec(
        key = "still_carrying",
        label = "٣. ماذا ما زلت أحمله؟",
        placeholder = "اللوم أو الثقل المتبقي...",
        quickSuggestions = listOf("أحمل سؤال: لماذا حدث ذلك معي رغم نيتي الطيبة؟")
      ),
      WorkshopFieldSpec(
        key = "can_change_now",
        label = "٤. ما الذي أستطيع تغييره الآن؟",
        placeholder = "ما الذي يقع في دائرة سيطرتك اليوم؟",
        quickSuggestions = listOf("أستطيع رعاية نفسي، اختيار علاقاتي بوعي، ووضع حدود تحميني")
      ),
      WorkshopFieldSpec(
        key = "learn_to_live_with",
        label = "٥. ما الذي يجب أن أتعلم التعايش معه بسلام؟",
        placeholder = "تقبل أن الماضي انتهى...",
        quickSuggestions = listOf("أنني لا أملك تغيير الماضي، لكنني أملك ألا أعيش سجينه بعد اليوم")
      )
    )

    item.id == 9L || item.title.contains("أقول «لا»") -> listOf(
      WorkshopFieldSpec(
        key = "boundary_situation",
        label = "الموقف الذي أحتاج فيه لوضع حد",
        placeholder = "اختر أو اكتب الموقف...",
        quickSuggestions = listOf(
          "طلب لا أستطيع تلبيته حالياً",
          "شخص يتدخل في حياتي وقراراتي الخاصة",
          "شخص يضغط عليّ أو يستغل طيبتي"
        )
      ),
      WorkshopFieldSpec(
        key = "polite_refusal",
        label = "صياغة الرفض الهادئ (بدون تبرير طويل وبدون عدوان)",
        placeholder = "كيف ستقول «لا» باحترام؟",
        quickSuggestions = listOf("«أقدّر تواصلك، لكنني غير قادر على الالتزام بهذا الطلب حالياً»")
      ),
      WorkshopFieldSpec(
        key = "need_statement",
        label = "التعبير عن الاحتياج ووضع الحد بوضوح",
        placeholder = "الجملة التي تحمي مساحتك...",
        quickSuggestions = listOf("«هذا الأمر شخصي وأفضّل أن أتخذ قراري فيه بنفسي، شكراً لتفهمك»")
      ),
      WorkshopFieldSpec(
        key = "self_reminder",
        label = "تذكير لنفسي للتحرر من الشعور بالذنب",
        placeholder = "رسالة تطمئن بها قلبك...",
        quickSuggestions = listOf("حماية طاقتي ليست أنانية؛ قول «لا» لما يفوق طاقتي هو قول «نعم» لصحتي النفسية")
      )
    )

    // Workshop 10: Return to Myself Plan (7 steps)
    else -> listOf(
      WorkshopFieldSpec(
        key = "step1_drains",
        label = "١. ما الذي يستنزفني؟ ↓",
        placeholder = "المواقف أو العادات المستنزفة...",
        quickSuggestions = listOf("السهر الطويل، إهمال راحتي، وتحميل نفسي فوق طاقتها")
      ),
      WorkshopFieldSpec(
        key = "step2_warning",
        label = "٢. ما علامات تدهوري؟ ↓",
        placeholder = "العلامات المبكرة للتعب...",
        quickSuggestions = listOf("فقدان التركيز، ضيق التنفس، والانسحاب من الناس")
      ),
      WorkshopFieldSpec(
        key = "step3_helps",
        label = "٣. ما الذي يساعدني؟ ↓",
        placeholder = "أدواتي التي تعيد لي السكينة...",
        quickSuggestions = listOf("تمارين التنفس، الكتابة، الطبيعة، وتخفيف المهام")
      ),
      WorkshopFieldSpec(
        key = "step4_safe_people",
        label = "٤. من الأشخاص الآمنون حولي؟ ↓",
        placeholder = "دائرة الأمان الخاصة بك...",
        quickSuggestions = listOf("أسرتي الداعمة وصديقي المقرب الذي يسمعني بأمان")
      ),
      WorkshopFieldSpec(
        key = "step5_habits",
        label = "٥. ما العادات التي أحتاجها؟ ↓",
        placeholder = "عادة يومية صغيرة تحميك...",
        quickSuggestions = listOf("نوم منتظم، ١٠ دقائق صمت وتأمل يومياً، وقول «لا» عند الإرهاق")
      ),
      WorkshopFieldSpec(
        key = "step6_when_pro",
        label = "٦. متى أطلب مساعدة متخصصة؟ ↓",
        placeholder = "التوقيت الفاصل لطلب المختص...",
        quickSuggestions = listOf("حين أشعر أنني لا أستطيع تجاوز الضيق وحدي لأسبوعين متتاليين")
      ),
      WorkshopFieldSpec(
        key = "step7_where_to_go",
        label = "٧. إلى من أتوجه؟",
        placeholder = "وجهتك الآمنة للمساندة...",
        quickSuggestions = listOf("قسم «محتاج أتكلم» في نسمة الحياة أو أخصائي نفسي مرخص")
      )
    )
  }
}
