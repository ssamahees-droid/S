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
import androidx.compose.material.icons.filled.BookmarkAdded
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

/**
 * كتيب "دليل مبسط للصحة النفسية" — برنامج "نسمة الحياة"
 * "كثيرون يعيشون الحياة... وقليلون يستمتعون بها"
 * نحو مجتمع يهتم بالصحة النفسية كما يهتم بالصحة الجسدية
 *
 * يشمل جميع صفحات الكتيب الـ 15 + المحطات التفاعلية + بطاقة "خطة نسمة الحياة الخاصة بي"
 */
@Composable
fun MentalHealthBookletSection(
  onSaveToJourney: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var expandedPageId by remember { mutableIntStateOf(15) } // Default open: Personal Plan or Page 1

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 20.dp),
    contentPadding = PaddingValues(top = 14.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // غلاف الكتيب ومقدمته (صفحة 1 و 2 من الـ PDF)
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("booklet_cover_card")
      ) {
        Column(
          modifier = Modifier.padding(22.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(50))
              .background(SageGreenPrimary.copy(alpha = 0.25f))
              .padding(horizontal = 14.dp, vertical = 6.dp)
          ) {
            Text(
              text = "📘 كتيب برنامج «نسمة الحياة» التفاعلي",
              style = MaterialTheme.typography.labelMedium,
              color = SoftMint,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "\"كثيرون يعيشون الحياة... وقليلون يستمتعون بها.\"",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "نحو مجتمع يهتم بالصحة النفسية كما يهتم بالصحة الجسدية",
            style = MaterialTheme.typography.bodyMedium,
            color = SoftMint,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(modifier = Modifier.height(14.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color.White.copy(alpha = 0.12f))
              .padding(14.dp)
          ) {
            Text(
              text = "دليل مبسط للصحة النفسية: كتيب صغير يقرأه الشخص العادي المضغوط أو المتأذي نفسيًا، فيساعده على فهم ما يحدث له، واكتشاف العلامات المبكرة، ومعرفة ما يستطيع فعله، ومتى يحتاج إلى مختص.",
              style = MaterialTheme.typography.bodySmall,
              color = CreamBackground,
              textAlign = TextAlign.Center,
              lineHeight = 21.sp
            )
          }
        }
      }
    }

    // الصفحة الخامسة عشرة (مميزة في الأعلى للوصول السريع): خطة نسمة الحياة الخاصة بي
    item {
      ExpandableBookletPageCard(
        pageNumber = 15,
        badge = "بطاقتي الشخصية التفاعلية",
        title = "خطة نسمة الحياة الخاصة بي",
        subtitle = "صفحتان تملؤهما بنفسك لتكون بوصلتك الشخصية عند التعب",
        isExpanded = expandedPageId == 15,
        onToggle = { expandedPageId = if (expandedPageId == 15) 0 else 15 }
      ) {
        BookletPage15MyPersonalPlan(onSaveToJourney = onSaveToJourney)
      }
    }

    // الصفحة الأولى | مقدمة: هل أنت بخير فعلًا؟
    item {
      ExpandableBookletPageCard(
        pageNumber = 1,
        badge = "الصفحة الأولى | مقدمة",
        title = "هل أنت بخير فعلًا؟",
        subtitle = "هذا الألم حقيقي، حتى لو لم يستطع أحد رؤيته",
        isExpanded = expandedPageId == 1,
        onToggle = { expandedPageId = if (expandedPageId == 1) 0 else 1 }
      ) {
        BookletPage1Intro()
      }
    }

    // الصفحة الثانية | نفسي تعبانة... بس مالي؟
    item {
      ExpandableBookletPageCard(
        pageNumber = 2,
        badge = "الصفحة الثانية",
        title = "نفسي تعبانة... بس مالي؟",
        subtitle = "العلامات العشر المبكرة + سؤال: ما الشيء الذي تغير فيّ مؤخرًا؟",
        isExpanded = expandedPageId == 2,
        onToggle = { expandedPageId = if (expandedPageId == 2) 0 else 2 }
      ) {
        BookletPage2Signs(onSaveToJourney = onSaveToJourney)
      }
    }

    // الصفحة الثالثة | الضغط النفسي
    item {
      ExpandableBookletPageCard(
        pageNumber = 3,
        badge = "الصفحة الثالثة",
        title = "الضغط النفسي: متى يكون طبيعيًا؟",
        subtitle = "إيه اللي بيستنزفني باستمرار؟ + تمرين الأشياء الثلاثة",
        isExpanded = expandedPageId == 3,
        onToggle = { expandedPageId = if (expandedPageId == 3) 0 else 3 }
      ) {
        BookletPage3Stress(onSaveToJourney = onSaveToJourney)
      }
    }

    // الصفحة الرابعة | عندما تفقد الأشياء طعمها
    item {
      ExpandableBookletPageCard(
        pageNumber = 4,
        badge = "الصفحة الرابعة",
        title = "عندما تفقد الأشياء طعمها",
        subtitle = "فقدان الاهتمام أو المتعة ومتى تستحق هذه الإشارة الانتباه",
        isExpanded = expandedPageId == 4,
        onToggle = { expandedPageId = if (expandedPageId == 4) 0 else 4 }
      ) {
        BookletPage4LossOfPleasure(onSaveToJourney = onSaveToJourney)
      }
    }

    // الصفحة الخامسة | مشاعرك ليست عدوك
    item {
      ExpandableBookletPageCard(
        pageNumber = 5,
        badge = "الصفحة الخامسة",
        title = "مشاعرك ليست عدوك",
        subtitle = "تمرين «سمِّ شعورك»: ماذا حدث؟ ماذا شعرت؟ ماذا كنت تحتاج؟",
        isExpanded = expandedPageId == 5,
        onToggle = { expandedPageId = if (expandedPageId == 5) 0 else 5 }
      ) {
        BookletPage5Emotions(onSaveToJourney = onSaveToJourney)
      }
    }

    // الصفحة السادسة | لما دماغك ما تسكتش
    item {
      ExpandableBookletPageCard(
        pageNumber = 6,
        badge = "الصفحة السادسة",
        title = "لما دماغك ما تسكتش",
        subtitle = "متى يتحول التفكير من حل المشكلة إلى استنزافك؟",
        isExpanded = expandedPageId == 6,
        onToggle = { expandedPageId = if (expandedPageId == 6) 0 else 6 }
      ) {
        BookletPage6Overthinking(onSaveToJourney = onSaveToJourney)
      }
    }

    // الصفحة السابعة | عندما يصبح النوم مشكلة
    item {
      ExpandableBookletPageCard(
        pageNumber = 7,
        badge = "الصفحة السابعة | أداة جديدة",
        title = "عندما يصبح النوم مشكلة",
        subtitle = "سجل النوم الأسبوعي التفاعلي واكتشاف الأنماط المتكررة",
        isExpanded = expandedPageId == 7,
        onToggle = { expandedPageId = if (expandedPageId == 7) 0 else 7 }
      ) {
        BookletPage7SleepTracker(onSaveToJourney = onSaveToJourney)
      }
    }

    // الصفحة الثامنة | أنت لست آلة
    item {
      ExpandableBookletPageCard(
        pageNumber = 8,
        badge = "الصفحة الثامنة",
        title = "أنت لست آلة — تمرين «ميزانية الطاقة»",
        subtitle = "٥ أشياء تستنزف طاقتك + ٣ أشياء تستعيدها + شيء تقلله هذا الأسبوع",
        isExpanded = expandedPageId == 8,
        onToggle = { expandedPageId = if (expandedPageId == 8) 0 else 8 }
      ) {
        BookletPage8EnergyBudget(onSaveToJourney = onSaveToJourney)
      }
    }

    // الصفحة التاسعة | الحدود ليست قسوة
    item {
      ExpandableBookletPageCard(
        pageNumber = 9,
        badge = "الصفحة التاسعة | أداة جديدة",
        title = "الحدود ليست قسوة",
        subtitle = "ما الذي أقبله ولا أقبله؟ + صياغة جمل الحدود الهادئة",
        isExpanded = expandedPageId == 9,
        onToggle = { expandedPageId = if (expandedPageId == 9) 0 else 9 }
      ) {
        BookletPage9Boundaries(onSaveToJourney = onSaveToJourney)
      }
    }

    // الصفحة العاشرة | عندما يؤذينا شخص آخر
    item {
      ExpandableBookletPageCard(
        pageNumber = 10,
        badge = "الصفحة العاشرة | أداة جديدة",
        title = "عندما يؤذينا شخص آخر",
        subtitle = "النظر بصدق لمصدر الأذى وكيف تحمي نفسك بأمان",
        isExpanded = expandedPageId == 10,
        onToggle = { expandedPageId = if (expandedPageId == 10) 0 else 10 }
      ) {
        BookletPage10HarmfulRelationships(onSaveToJourney = onSaveToJourney)
      }
    }

    // الصفحات 11 - 14 | الوعي الصحي وطلب المساعدة والفرق بين المختصين
    item {
      ExpandableBookletPageCard(
        pageNumber = 11,
        badge = "الصفحات ١١ إلى ١٤ | دليلك للمساعدة",
        title = "لا تعالج نفسك من الإنترنت • أخصائي أم طبيب نفسي؟ • متى تكون الحالة طارئة؟",
        subtitle = "تصحيح المفاهيم الخاطئة حول العلاج النفسي والفرق بين الأدوار بدون وصمة",
        isExpanded = expandedPageId == 11,
        onToggle = { expandedPageId = if (expandedPageId == 11) 0 else 11 }
      ) {
        BookletPages11To14Guide()
      }
    }

    // الصفحة الأخيرة | رسالة إليك
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.5f)),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.5.dp, DarkGreen.copy(alpha = 0.2f), RoundedCornerShape(22.dp))
          .testTag("booklet_final_message_card")
      ) {
        Column(
          modifier = Modifier.padding(22.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "💌 الصفحة الأخيرة | رسالة إليك",
            style = MaterialTheme.typography.titleLarge,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "ربما قضيت وقتًا طويلًا وأنت تقول:\n\"أنا لازم أستحمل\".\nوربما أخفيت تعبك لأنك تخاف أن يراك الآخرون ضعيفًا.\nوربما اعتدت أن تسأل عن الجميع، ولم تسأل نفسك منذ وقت طويل:\n\"وأنا... عاملة إيه؟ / وأنا... عامل إيه؟\"",
            style = MaterialTheme.typography.bodyLarge,
            color = TextDark,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "توقف قليلًا. اسأل نفسك.\nوإذا اكتشفت أنك تحتاج إلى مساعدة، فلا تجعل الخوف أو الخجل يمنعانك منها.\nلا تنتظر حتى ينهار كل شيء حتى تبدأ في الاهتمام بنفسك.\nفطلب المساعدة ليس هزيمة.\nأحيانًا... تكون أول خطوة في طريق العودة إلى الحياة.",
            style = MaterialTheme.typography.bodyMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 25.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(DarkGreen)
              .padding(horizontal = 18.dp, vertical = 8.dp)
          ) {
            Text(
              text = "نسمة الحياة — كثيرون يعيشون الحياة... ولكن قليلون يستمتعون بها.",
              style = MaterialTheme.typography.labelMedium,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ExpandableBookletPageCard(
  pageNumber: Int,
  badge: String,
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
      .testTag("booklet_page_card_$pageNumber")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = badge,
            style = MaterialTheme.typography.labelSmall,
            color = SageGreenPrimary,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = TextDark.copy(alpha = 0.78f)
          )
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
        Column(modifier = Modifier.padding(top = 14.dp)) {
          content()
        }
      }
    }
  }
}

// ============================================================================
// الصفحة 15: خطة نسمة الحياة الخاصة بي (صفحتان يملؤهما القارئ بنفسه)
// ============================================================================
@Composable
private fun BookletPage15MyPersonalPlan(onSaveToJourney: (String, String) -> Unit) {
  var earlySigns by remember { mutableStateOf("") }
  var mostDraining by remember { mutableStateOf("") }
  var thingsThatHelp by remember { mutableStateOf("") }
  var safePerson by remember { mutableStateOf("") }
  var specialistOrEntity by remember { mutableStateOf("") }
  var stepWhenOverwhelmed by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    BookletHighlightNote(
      "املأ هذه الخطة بهدوء واحتفظ بها في دفتر «رحلتي» لتعود إليها في أي وقت تشعر فيه بالإرهاق أو التشتت:"
    )

    BookletTextField(
      label = "١. عندما أبدأ في التدهور أو التعب، ألاحظ أنني (العلامات المبكرة):",
      value = earlySigns,
      onValueChange = { earlySigns = it; isSaved = false },
      placeholder = "مثال: أسهر كثيرًا، أنعزل عن الناس، أفقد الشهية أو أتوتر بسرعة..."
    )

    BookletTextField(
      label = "٢. أكثر الأشياء التي تستنزفني:",
      value = mostDraining,
      onValueChange = { mostDraining = it; isSaved = false },
      placeholder = "مثال: ضغط العمل المتواصل، إرضاء الجميع، السهر على الهاتف..."
    )

    BookletTextField(
      label = "٣. الأشياء التي تساعدني عادةً:",
      value = thingsThatHelp,
      onValueChange = { thingsThatHelp = it; isSaved = false },
      placeholder = "مثال: المشي ٢٠ دقيقة، النوم المبكر، الكتابة، الحديث مع صديق..."
    )

    BookletTextField(
      label = "٤. الشخص الذي أستطيع التحدث معه أو اللجوء إليه:",
      value = safePerson,
      onValueChange = { safePerson = it; isSaved = false },
      placeholder = "اكتب اسم شخص واحد تشعر معه بالأمان..."
    )

    BookletTextField(
      label = "٥. المختص أو الجهة التي يمكنني طلب المساعدة منها:",
      value = specialistOrEntity,
      onValueChange = { specialistOrEntity = it; isSaved = false },
      placeholder = "مثال: عيادة نفسية، أخصائي نفسي، أو خط مساندة..."
    )

    BookletTextField(
      label = "٦. الخطوة التي سأقوم بها إذا شعرت أنني لم أعد قادرًا على التعامل وحدي:",
      value = stepWhenOverwhelmed,
      onValueChange = { stepWhenOverwhelmed = it; isSaved = false },
      placeholder = "مثال: سأحجز موعد تقييم مع مختص وأخبر شخصًا قريبًا مني..."
    )

    BookletSaveButton(
      text = "حفظ «خطة نسمة الحياة الخاصة بي» في رحلتي",
      isSaved = isSaved,
      onClick = {
        val summary = """
          • عندما أبدأ في التدهور ألاحظ أنني: $earlySigns
          • أكثر الأشياء التي تستنزفني: $mostDraining
          • الأشياء التي تساعدني عادةً: $thingsThatHelp
          • الشخص الذي أستطيع التحدث معه: $safePerson
          • المختص أو الجهة لطلب المساعدة: $specialistOrEntity
          • خطوتي إذا لم أعد قادرًا على التعامل وحدي: $stepWhenOverwhelmed
        """.trimIndent()
        onSaveToJourney("🌿 خطة نسمة الحياة الخاصة بي", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// الصفحة 1: مقدمة | هل أنت بخير فعلًا؟
// ============================================================================
@Composable
private fun BookletPage1Intro() {
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = "قد تكون حياتك تبدو جيدة من الخارج.\nتعمل، وتدرس، وتتزوج، وتربي أبناءك، وتنجح في عملك، وتضحك مع من حولك...\nومع ذلك، قد تستيقظ يومًا وتشعر أنك متعب من الداخل. لا تعرف لماذا.",
      style = MaterialTheme.typography.bodyLarge,
      color = TextDark,
      lineHeight = 25.sp
    )
    Text(
      text = "لا يوجد حدث كبير يبرر حزنك، ولا مشكلة واضحة تستطيع أن تشير إليها وتقول: \"هذا هو السبب\".\nومع ذلك... تشعر بالضيق، تفقد رغبتك في الأشياء التي كنت تحبها، تكثر أفكارك، تقل طاقتك، وتصبح أبسط الأمور ثقيلة عليك. وقد تشعر أحيانًا أنك موجود في حياتك... لكنك لا تستمتع بها.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextDark,
      lineHeight = 24.sp
    )
    BookletHighlightNote(
      "هذا الألم حقيقي، حتى لو لم يستطع أحد رؤيته.\nوالصحة النفسية ليست فقط غياب المرض النفسي؛ بل هي جزء أساسي من الصحة والرفاه والقدرة على التعامل مع ضغوط الحياة اليومية.\nهذا الكتيب ليس للتشخيص، بل دعوة لأن تتوقف قليلًا... وتنظر إلى نفسك. وفي كل الحالات... لا تستهن بما تشعر به."
    )
  }
}

// ============================================================================
// الصفحة 2: نفسي تعبانة... بس مالي؟
// ============================================================================
@Composable
private fun BookletPage2Signs(onSaveToJourney: (String, String) -> Unit) {
  val signs = listOf(
    "عصبية أكثر من المعتاد",
    "فقدان الاستمتاع بالأشياء",
    "صعوبة في النوم أو النوم أكثر من المعتاد",
    "تعب مستمر",
    "صعوبة في التركيز",
    "رغبة في الابتعاد عن الناس",
    "إحساس دائم بالضغط",
    "كثرة التفكير",
    "تغيرات في الشهية",
    "شعور بالحزن أو الفراغ"
  )
  val selectedSigns = remember { mutableStateListOf<String>() }
  var changedRecently by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = "أحيانًا لا يبدأ التعب النفسي في صورة واضحة. لا يأتي ومعه ورقة تقول: \"أنا قلق\" أو \"أنا مكتئب\" أو \"أنا منهك\". قد يظهر في صورة (حدد ما تلاحظه مؤخرًا):",
      style = MaterialTheme.typography.bodyMedium,
      color = TextDark,
      lineHeight = 23.sp
    )

    signs.forEach { sign ->
      val checked = selectedSigns.contains(sign)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(if (checked) SoftMint.copy(alpha = 0.45f) else CreamBackground)
          .clickable {
            if (checked) selectedSigns.remove(sign) else selectedSigns.add(sign)
            isSaved = false
          }
          .padding(horizontal = 8.dp, vertical = 2.dp)
      ) {
        Checkbox(
          checked = checked,
          onCheckedChange = {
            if (checked) selectedSigns.remove(sign) else selectedSigns.add(sign)
            isSaved = false
          },
          colors = CheckboxDefaults.colors(checkedColor = DarkGreen)
        )
        Text(text = sign, style = MaterialTheme.typography.bodyMedium, color = TextDark)
      }
    }

    BookletHighlightNote(
      "وجود عرض واحد لا يعني بالضرورة وجود اضطراب نفسي. المهم هو الصورة كاملة: ما شدة الأعراض؟ وكم استمرت؟ وهل بدأت تؤثر في حياتك وعملك وعلاقاتك؟"
    )

    BookletTextField(
      label = "✏️ توقف واسأل نفسك: ما الشيء الذي تغير فيّ مؤخرًا ولم أنتبه إليه؟",
      value = changedRecently,
      onValueChange = { changedRecently = it; isSaved = false },
      placeholder = "اكتب ما لاحظت تغيره فيك مؤخرًا..."
    )

    BookletSaveButton(
      text = "حفظ ملاحظتي في رحلتي",
      isSaved = isSaved,
      onClick = {
        val summary = """
          العلامات التي لاحظتها: ${selectedSigns.joinToString("، ").ifBlank { "لم أحدد" }}
          ما الشيء الذي تغير فيّ مؤخرًا ولم أنتبه إليه؟ $changedRecently
        """.trimIndent()
        onSaveToJourney("📘 نفسي تعبانة... بس مالي؟", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// الصفحة 3: الضغط النفسي
// ============================================================================
@Composable
private fun BookletPage3Stress(onSaveToJourney: (String, String) -> Unit) {
  var drain1 by remember { mutableStateOf("") }
  var action1 by remember { mutableStateOf("أستطيع تقليله") }
  var drain2 by remember { mutableStateOf("") }
  var action2 by remember { mutableStateOf("أستطيع تغييره") }
  var drain3 by remember { mutableStateOf("") }
  var action3 by remember { mutableStateOf("أحتاج إلى مساعدة في التعامل معه") }
  var isSaved by remember { mutableStateOf(false) }

  val options = listOf("أستطيع تغييره", "أستطيع تقليله", "أحتاج إلى مساعدة في التعامل معه")

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = "كلنا نتعرض للضغط... لكن لسنا جميعًا نتأثر بالطريقة نفسها.\nالمشكلة تبدأ عندما يصبح الضغط مستمرًا أو شديدًا لدرجة يؤثر فيها على قدرتنا على ممارسة حياتنا بصورة طبيعية.\nلذلك لا تسأل نفسك فقط: \"إيه اللي مضايقني؟\" اسأل أيضًا: \"إيه اللي بيستنزفني باستمرار؟\"",
      style = MaterialTheme.typography.bodyMedium,
      color = TextDark,
      lineHeight = 23.sp
    )

    Text(
      text = "🧩 تمرين صغير: اكتب أكثر ثلاثة أشياء تستنزفك حاليًا، وأمام كل واحد اسأل نفسك:",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    DrainItemWithAction("١. الاستنزاف الأول:", drain1, { drain1 = it; isSaved = false }, action1, { action1 = it; isSaved = false }, options)
    DrainItemWithAction("٢. الاستنزاف الثاني:", drain2, { drain2 = it; isSaved = false }, action2, { action2 = it; isSaved = false }, options)
    DrainItemWithAction("٣. الاستنزاف الثالث:", drain3, { drain3 = it; isSaved = false }, action3, { action3 = it; isSaved = false }, options)

    BookletSaveButton(
      text = "حفظ تمرين الضغط النفسي",
      isSaved = isSaved,
      onClick = {
        val summary = """
          1) $drain1 ← ($action1)
          2) $drain2 ← ($action2)
          3) $drain3 ← ($action3)
        """.trimIndent()
        onSaveToJourney("📘 تمرين الضغط النفسي والاستنزاف", summary)
        isSaved = true
      }
    )
  }
}

@Composable
private fun DrainItemWithAction(
  label: String,
  text: String,
  onTextChange: (String) -> Unit,
  selectedAction: String,
  onActionSelect: (String) -> Unit,
  options: List<String>
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(CreamBackground)
      .padding(12.dp)
  ) {
    BookletTextField(label = label, value = text, onValueChange = onTextChange, placeholder = "اكتب الشيء الذي يستنزفك...")
    Spacer(modifier = Modifier.height(6.dp))
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      options.forEach { opt ->
        FilterChip(
          selected = selectedAction == opt,
          onClick = { onActionSelect(opt) },
          label = { Text(opt, fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = DarkGreen,
            selectedLabelColor = Color.White
          )
        )
      }
    }
  }
}

// ============================================================================
// الصفحة 4: عندما تفقد الأشياء طعمها
// ============================================================================
@Composable
private fun BookletPage4LossOfPleasure(onSaveToJourney: (String, String) -> Unit) {
  var stillEnjoy by remember { mutableStateOf("أحيانًا") }
  var affectsLife by remember { mutableStateOf("لا") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = "من العلامات التي تستحق الانتباه أن يبدأ الإنسان في فقدان الاهتمام أو المتعة بأشياء كان يستمتع بها سابقًا.\nقد يقول: \"مش فارق معايا\"، \"مش عايز أخرج\"، \"حتى الحاجات اللي كنت بحبها مبقتش تفرق\".\nهذا ليس مجرد \"دلع\" أو \"كسل\" بالضرورة.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextDark,
      lineHeight = 23.sp
    )

    BookletHighlightNote(
      "لا تشخّص نفسك من خلال هذا الكتيب. المعلومة تساعدك على الانتباه، وليست بديلًا عن التقييم المتخصص."
    )

    Text(
      text = "اسأل نفسك: هل ما زلت أستمتع بالأشياء التي كنت أستمتع بها؟",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      listOf("نعم", "أحيانًا", "لا").forEach { opt ->
        FilterChip(
          selected = stillEnjoy == opt,
          onClick = { stillEnjoy = opt; isSaved = false },
          label = { Text(opt) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = DarkGreen,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Text(
      text = "هل هذا التغير مستمر ويؤثر في حياتي؟",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      listOf("نعم", "لا").forEach { opt ->
        FilterChip(
          selected = affectsLife == opt,
          onClick = { affectsLife = opt; isSaved = false },
          label = { Text(opt) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = DarkGreen,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    if (stillEnjoy == "لا" || affectsLife == "نعم") {
      BookletHighlightNote("إذا كانت الإجابة مقلقة بالنسبة لك، فهذه إشارة تستحق ألا تتجاهلها، والتحدث مع مختص خطوة واعية ومطمئنة.")
    }

    BookletSaveButton(
      text = "حفظ إجابتي في رحلتي",
      isSaved = isSaved,
      onClick = {
        onSaveToJourney(
          "📘 عندما تفقد الأشياء طعمها",
          "هل ما زلت أستمتع بالأشياء؟ $stillEnjoy\nهل هذا التغير مستمر ويؤثر في حياتي؟ $affectsLife"
        )
        isSaved = true
      }
    )
  }
}

// ============================================================================
// الصفحة 5: مشاعرك ليست عدوك — تمرين «سمِّ شعورك»
// ============================================================================
@Composable
private fun BookletPage5Emotions(onSaveToJourney: (String, String) -> Unit) {
  var whatHappened by remember { mutableStateOf("") }
  var whatFelt by remember { mutableStateOf("") }
  var whatNeeded by remember { mutableStateOf("") }
  var howActed by remember { mutableStateOf("") }
  var betterNextTime by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    BookletHighlightNote(
      "الغضب ليس دائمًا مشكلة. والحزن ليس ضعفًا. والخوف ليس جبنًا.\nالمشاعر تخبرنا بشيء، لكن الشعور لا يجب أن يكون هو الذي يتخذ القرار بدلًا منا.\nجرّب: أنا غاضب ← ماذا حدث؟ ← ماذا أشعر؟ ← ماذا أحتاج؟ ← ما التصرف الأفضل الآن؟"
    )

    Text(
      text = "🧩 تمرين «سمِّ شعورك» — اكتب موقفًا أزعجك مؤخرًا:",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    BookletTextField("ماذا حدث؟", whatHappened, { whatHappened = it; isSaved = false }, "اكتب الموقف باختصار...")
    BookletTextField("ماذا شعرت؟", whatFelt, { whatFelt = it; isSaved = false }, "مثال: غضب، حزن، خوف، خذلان...")
    BookletTextField("ماذا كنت تحتاج وقتها؟", whatNeeded, { whatNeeded = it; isSaved = false }, "مثال: احترام، أمان، وضوح، راحة...")
    BookletTextField("كيف تصرفت؟", howActed, { howActed = it; isSaved = false }, "كيف كانت ردة فعلك وقتها؟")
    BookletTextField("لو تكرر الموقف، ماذا يمكنك أن تفعل بطريقة أفضل؟", betterNextTime, { betterNextTime = it; isSaved = false }, "خطوة أهدأ لو تكرر الموقف...")

    BookletSaveButton(
      text = "حفظ تمرين «سمِّ شعورك»",
      isSaved = isSaved,
      onClick = {
        val summary = """
          ماذا حدث؟ $whatHappened
          ماذا شعرت؟ $whatFelt
          ماذا كنت تحتاج وقتها؟ $whatNeeded
          كيف تصرفت؟ $howActed
          لو تكرر الموقف سأفعل: $betterNextTime
        """.trimIndent()
        onSaveToJourney("📘 تمرين سمِّ شعورك", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// الصفحة 6: لما دماغك ما تسكتش
// ============================================================================
@Composable
private fun BookletPage6Overthinking(onSaveToJourney: (String, String) -> Unit) {
  var problem by remember { mutableStateOf("") }
  var canActNow by remember { mutableStateOf("نعم") }
  var smallStep by remember { mutableStateOf("") }
  var whatCanControlInstead by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = "هل حدث أن تنتهي من يومك، وتضع رأسك على الوسادة، ثم يبدأ عقلك في تشغيل كل شيء من جديد؟\nالتفكير يساعدنا على حل المشكلات، لكن التفكير المستمر دون الوصول إلى حل يمكن أن يتحول إلى دائرة تستنزفنا.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextDark,
      lineHeight = 23.sp
    )

    BookletTextField("ما المشكلة التي تدور في رأسك؟", problem, { problem = it; isSaved = false }, "اكتب الفكرة أو المشكلة...")

    Text("هل أستطيع فعل شيء حيالها الآن؟", style = MaterialTheme.typography.titleSmall, color = DarkGreen, fontWeight = FontWeight.Bold)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      listOf("نعم", "لا").forEach { opt ->
        FilterChip(
          selected = canActNow == opt,
          onClick = { canActNow = opt; isSaved = false },
          label = { Text(opt) },
          colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DarkGreen, selectedLabelColor = Color.White)
        )
      }
    }

    if (canActNow == "نعم") {
      BookletTextField("إذا كانت نعم: ما الخطوة الصغيرة التي أستطيع القيام بها؟", smallStep, { smallStep = it; isSaved = false }, "اكتب خطوة صغيرة واحدة...")
    } else {
      BookletTextField("إذا كانت لا: ما الذي أستطيع التحكم فيه بدلًا منها؟", whatCanControlInstead, { whatCanControlInstead = it; isSaved = false }, "اكتب شيئًا واحدًا في يدك الآن...")
    }

    BookletHighlightNote("الفكرة ليست أن تمنع عقلك من التفكير، وإنما أن تتعلم متى يتحول التفكير من حل المشكلة إلى استنزافك.")

    BookletSaveButton(
      text = "حفظ التمرين في رحلتي",
      isSaved = isSaved,
      onClick = {
        val summary = """
          المشكلة: $problem
          هل أستطيع فعل شيء الآن؟ $canActNow
          الخطوة / ما أتحكم فيه: ${if (canActNow == "نعم") smallStep else whatCanControlInstead}
        """.trimIndent()
        onSaveToJourney("📘 لما دماغك ما تسكتش", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// الصفحة 7: عندما يصبح النوم مشكلة (سجل النوم الأسبوعي التفاعلي)
// ============================================================================
@Composable
private fun BookletPage7SleepTracker(onSaveToJourney: (String, String) -> Unit) {
  var sleepTime by remember { mutableStateOf("") }
  var wakeTime by remember { mutableStateOf("") }
  var wakeCount by remember { mutableStateOf("") }
  var phoneBeforeSleep by remember { mutableStateOf("نعم") }
  var stressLevel by remember { mutableIntStateOf(5) }
  var noticedPattern by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = "النوم ليس مجرد وقت يتوقف فيه الجسم عن العمل. إنه جزء أساسي من صحتنا الجسدية والنفسية.\nلذلك لا تتعامل مع النوم باعتباره أمرًا ثانويًا. جرّب تسجيل نمط نومك:",
      style = MaterialTheme.typography.bodyMedium,
      color = TextDark,
      lineHeight = 23.sp
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Box(modifier = Modifier.weight(1f)) {
        BookletTextField("موعد نومك:", sleepTime, { sleepTime = it; isSaved = false }, "مثال: ١٢:٣٠ ص")
      }
      Box(modifier = Modifier.weight(1f)) {
        BookletTextField("موعد استيقاظك:", wakeTime, { wakeTime = it; isSaved = false }, "مثال: ٧:٣٠ ص")
      }
    }

    BookletTextField("عدد مرات الاستيقاظ أثناء الليل:", wakeCount, { wakeCount = it; isSaved = false }, "مثال: مرتان")

    Text("استخدام الهاتف قبل النوم:", style = MaterialTheme.typography.titleSmall, color = DarkGreen, fontWeight = FontWeight.Bold)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      listOf("نعم بكثرة", "قليلًا", "لا").forEach { opt ->
        FilterChip(
          selected = phoneBeforeSleep == opt,
          onClick = { phoneBeforeSleep = opt; isSaved = false },
          label = { Text(opt) },
          colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DarkGreen, selectedLabelColor = Color.White)
        )
      }
    }

    Text("مستوى التوتر في ذلك اليوم: ($stressLevel / 10)", style = MaterialTheme.typography.titleSmall, color = DarkGreen, fontWeight = FontWeight.Bold)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      for (n in 1..10) {
        val sel = stressLevel == n
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(if (sel) DarkGreen else CreamBackground)
            .clickable { stressLevel = n; isSaved = false },
          contentAlignment = Alignment.Center
        ) {
          Text("$n", color = if (sel) Color.White else DarkGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
      }
    }

    BookletTextField("اسأل نفسك: هل هناك نمط يتكرر؟", noticedPattern, { noticedPattern = it; isSaved = false }, "مثال: كلما سهرت على الهاتف زاد قلقي وتقطع نومي...")

    BookletHighlightNote("إذا استمرت مشكلات النوم أو أصبحت تؤثر بوضوح على حياتك، فالأفضل مناقشتها مع مختص بدل محاولة علاجها عشوائيًا.")

    BookletSaveButton(
      text = "حفظ سجل النوم في رحلتي",
      isSaved = isSaved,
      onClick = {
        val summary = """
          موعد النوم: $sleepTime | موعد الاستيقاظ: $wakeTime
          مرات الاستيقاظ: $wakeCount | الهاتف قبل النوم: $phoneBeforeSleep
          مستوى التوتر: $stressLevel / 10
          النمط المتكرر الذي لاحظته: $noticedPattern
        """.trimIndent()
        onSaveToJourney("🌙 سجل النوم والملاحظة", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// الصفحة 8: أنت لست آلة — تمرين «ميزانية الطاقة»
// ============================================================================
@Composable
private fun BookletPage8EnergyBudget(onSaveToJourney: (String, String) -> Unit) {
  val drains = remember { mutableStateListOf("", "", "", "", "") }
  val restores = remember { mutableStateListOf("", "", "") }
  var reduceThisWeek by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    BookletHighlightNote(
      "أحيانًا نعيش وكأن المطلوب منا أن نكون متاحين طوال الوقت: نعمل، نساعد، نرد، ننجز، نلبي احتياجات الآخرين... ثم نتساءل: \"أنا ليه خلصت؟\"\nلأن الإنسان له طاقة محدودة. العناية بالنفس ليست أن تهرب من مسؤولياتك، وإنما أن تتعلم إدارة طاقتك قبل أن تصل إلى مرحلة الانهيار."
    )

    Text("🧩 تمرين «ميزانية الطاقة» — اكتب خمسة أشياء تستنزف طاقتك:", style = MaterialTheme.typography.titleSmall, color = DarkGreen, fontWeight = FontWeight.Bold)
    for (i in 0 until 5) {
      BookletTextField(
        label = "",
        value = drains[i],
        onValueChange = { drains[i] = it; isSaved = false },
        placeholder = "${i + 1}. شيء يستنزف طاقتي..."
      )
    }

    Text("ثم اكتب ثلاثة أشياء تساعدك على استعادة بعض طاقتك:", style = MaterialTheme.typography.titleSmall, color = DarkGreen, fontWeight = FontWeight.Bold)
    for (i in 0 until 3) {
      BookletTextField(
        label = "",
        value = restores[i],
        onValueChange = { restores[i] = it; isSaved = false },
        placeholder = "${i + 1}. شيء يستعيد طاقتي..."
      )
    }

    BookletTextField(
      label = "السؤال المهم: ما الشيء الذي يمكنني تقليله هذا الأسبوع؟",
      value = reduceThisWeek,
      onValueChange = { reduceThisWeek = it; isSaved = false },
      placeholder = "اكتب شيئًا واحدًا ستخففه هذا الأسبوع..."
    )

    BookletSaveButton(
      text = "حفظ «ميزانية الطاقة» في رحلتي",
      isSaved = isSaved,
      onClick = {
        val summary = """
          أشياء تستنزف طاقتي: ${drains.filter { it.isNotBlank() }.joinToString("، ")}
          أشياء تستعيد طاقتي: ${restores.filter { it.isNotBlank() }.joinToString("، ")}
          ما سأقلله هذا الأسبوع: $reduceThisWeek
        """.trimIndent()
        onSaveToJourney("🔋 تمرين ميزانية الطاقة", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// الصفحة 9: الحدود ليست قسوة + مكتبة جمل الحدود الجاهزة للنسخ حسب الموقف
// ============================================================================
@Composable
private fun BookletPage9Boundaries(onSaveToJourney: (String, String) -> Unit) {
  var whatIAccept by remember { mutableStateOf("") }
  var whatIDontAccept by remember { mutableStateOf("") }
  var selectedSituationTab by remember { mutableStateOf("في العمل") }
  var customBoundarySentence by remember {
    mutableStateOf("أنا لا أستطيع القيام بهذا الآن، وأحتاج إلى بعض الوقت.")
  }
  var isSaved by remember { mutableStateOf(false) }
  val context = LocalContext.current
  var copied by remember { mutableStateOf(false) }

  val readyBoundaryTemplates = remember {
    mapOf(
      "في العمل" to listOf(
        "أنا حاليًا مركز في مهام ثانية عاجلة، أقدر أبدأ في الطلب ده بكرة الصبح.",
        "محتاج أراجع جدولي الأول وأرد عليك بوقت التسليم الواقعي.",
        "مش هقدر أرد على رسائل الشغل بعد انتهاء مواعيد العمل، هتابعها أول ما أبدأ بكرة."
      ),
      "مع العائلة" to listOf(
        "أنا بحبكم وبحترم رأيكم، بس الموضوع ده خاص بيّ ومحتاج آخد قراري فيه بهدوء.",
        "أنا مرهق جدًا النهارده ومحتاج أقعد لوحدي شوية عشان أرتاح، نتكلم بكرة بهدوء.",
        "أنا لا أستطيع القيام بهذا المشوار الآن، وأحتاج إلى الراحة."
      ),
      "مع الأصدقاء" to listOf(
        "مقدرش أخرج النهارده لأن طاقتي خلصانة، خلينا نرتب يوم تاني قريب.",
        "محتاج أفكر وأرد عليك بخصوص الموضوع ده.",
        "أنا مش مستعد أتكلم في تفاصيل الموضوع ده دلوقتي، شكرًا لتفهمك."
      ),
      "عند الإرهاق" to listOf(
        "أنا محتاج أهدأ شوية قبل ما نكمل الكلام عشان منخسرش بعض.",
        "طاقتي اليوم لا تسمح بأي التزام إضافي، هعتذر منك المرة دي.",
        "محتاج أفكر وأرد عليك لما أكون أهدى."
      )
    )
  }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = "قد تؤذي نفسك وأنت تحاول إرضاء الجميع: توافق وأنت لا تريد، تتحمل أكثر مما تستطيع، تسكت حتى لا تغضب أحدًا.\nوضع الحدود لا يعني أن تصبح قاسيًا؛ يعني أن تعرف: ما الذي أقبله؟ وما الذي لا أقبله؟ وما الذي أستطيع تحمله؟",
      style = MaterialTheme.typography.bodyMedium,
      color = TextDark,
      lineHeight = 23.sp
    )

    BookletHighlightNote(
      "جرّب هذه الصيغة:\nبدل: \"أنت دائمًا بتضغط عليّ!\"\nقل: \"أنا لا أستطيع القيام بهذا الآن، وأحتاج إلى...\"\nوبدل الموافقة التلقائية، أعطِ نفسك حق قول: \"محتاج أفكر وأرد عليك\"."
    )

    // 🧩 بطاقات مواقف وحدود جاهزة للنسخ بنقرة واحدة
    Text(
      text = "🧩 مكتبة جمل الحدود الجاهزة (اختر الموقف واضغط لاختيار الجملة):",
      style = MaterialTheme.typography.titleSmall,
      color = DarkGreen,
      fontWeight = FontWeight.Bold
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      readyBoundaryTemplates.keys.forEach { cat ->
        FilterChip(
          selected = selectedSituationTab == cat,
          onClick = { selectedSituationTab = cat },
          label = { Text(cat, fontSize = 12.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = DarkGreen,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    readyBoundaryTemplates[selectedSituationTab]?.forEach { template ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CreamBackground),
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            customBoundarySentence = template
            copied = false
            isSaved = false
          }
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "\"$template\"",
            style = MaterialTheme.typography.bodySmall,
            color = TextDark,
            modifier = Modifier.weight(1f),
            lineHeight = 20.sp
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "استخدم ←",
            style = MaterialTheme.typography.labelSmall,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    BookletTextField("ما الذي أقبله وأستطيع تحمله؟", whatIAccept, { whatIAccept = it; isSaved = false }, "اكتب ما يناسب طاقتك...")
    BookletTextField("ما الذي لا أقبله أو يتجاوز حدودي؟", whatIDontAccept, { whatIDontAccept = it; isSaved = false }, "اكتب ما يستنزفك أو يتجاوز حدودك...")
    BookletTextField("جملتي الهادئة لوضع حد في الموقف القادم:", customBoundarySentence, { customBoundarySentence = it; isSaved = false; copied = false }, "اكتب جملة حدودك الهادئة...")

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
      OutlinedButton(
        onClick = {
          val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
          clipboard?.setPrimaryClip(ClipData.newPlainText("جملة حدود", customBoundarySentence))
          copied = true
        },
        shape = RoundedCornerShape(10.dp)
      ) {
        Icon(if (copied) Icons.Default.Check else Icons.Default.ContentCopy, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(if (copied) "تم نسخ الجملة" else "نسخ جملة الحدود", fontSize = 12.sp, color = DarkGreen, fontWeight = FontWeight.Bold)
      }
    }

    BookletSaveButton(
      text = "حفظ تمرين الحدود في رحلتي",
      isSaved = isSaved,
      onClick = {
        val summary = """
          ما أقبله: $whatIAccept
          ما لا أقبله: $whatIDontAccept
          جملتي الهادئة: $customBoundarySentence
        """.trimIndent()
        onSaveToJourney("🛡️ الحدود ليست قسوة", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// الصفحة 10: عندما يؤذينا شخص آخر
// ============================================================================
@Composable
private fun BookletPage10HarmfulRelationships(onSaveToJourney: (String, String) -> Unit) {
  var whatHurtsMe by remember { mutableStateOf("") }
  var canSetBoundary by remember { mutableStateOf("") }
  var canStepBack by remember { mutableStateOf("") }
  var needPersonHelp by remember { mutableStateOf("") }
  var isSaved by remember { mutableStateOf(false) }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = "ليس كل ألم نفسي سببه مرض نفسي. أحيانًا يكون الإنسان يعيش داخل علاقة مؤذية أو بيئة مؤذية أو ظروف قاسية.\nوهنا لا يكفي أن نقول له: \"فكر بإيجابية\". إذا كان مصدر الأذى ما زال موجودًا، فنحن بحاجة إلى النظر إليه بصدق.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextDark,
      lineHeight = 23.sp
    )

    BookletTextField("ما الذي يؤذيني تحديدًا؟", whatHurtsMe, { whatHurtsMe = it; isSaved = false }, "صف السلوك أو الموقف المؤذي بوضوح...")
    BookletTextField("هل أستطيع وضع حد له؟ كيف؟", canSetBoundary, { canSetBoundary = it; isSaved = false }, "مثال: تقليل النقاش الحاد أو تحديد وقت التعامل...")
    BookletTextField("هل أستطيع الابتعاد عنه أو تقليل التعرض له؟", canStepBack, { canStepBack = it; isSaved = false }, "اكتب المساحة الآمنة الممكنة لك...")
    BookletTextField("هل أحتاج إلى شخص يساعدني على التعامل معه؟", needPersonHelp, { needPersonHelp = it; isSaved = false }, "مثال: شخص موثوق من العائلة أو مختص...")

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(WarmBeige.copy(alpha = 0.6f))
        .padding(14.dp)
    ) {
      Text(
        text = "⚠️ تنبيه أمان: وإذا كان الأذى يتضمن عنفًا أو تهديدًا أو استغلالًا، فلا تتحمل وحدك؛ ابحث عن مساعدة آمنة ومناسبة لوضعك.",
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF7B2D26),
        fontWeight = FontWeight.Bold,
        lineHeight = 21.sp
      )
    }

    BookletSaveButton(
      text = "حفظ خطوة الأمان في رحلتي",
      isSaved = isSaved,
      onClick = {
        val summary = """
          ما يؤذيني تحديدًا: $whatHurtsMe
          هل أستطيع وضع حد له: $canSetBoundary
          هل أستطيع الابتعاد عنه: $canStepBack
          من يساعدني على التعامل معه: $needPersonHelp
        """.trimIndent()
        onSaveToJourney("🤝 عندما يؤذينا شخص آخر", summary)
        isSaved = true
      }
    )
  }
}

// ============================================================================
// الصفحات 11 - 14: الوعي الصحي، الفرق بين الأخصائي والطبيب، والطوارئ
// ============================================================================
@Composable
private fun BookletPages11To14Guide() {
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    // صفحة 11
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = CreamBackground),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text("🌐 الصفحة ١١ | لا تحاول أن تعالج نفسك من الإنترنت", style = MaterialTheme.typography.titleSmall, color = DarkGreen, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "قد تبحث عن أعراضك فتجد عشرات التشخيصات، ثم تبدأ تجربة نصائح أو أدوية أو مكملات من نفسك. وهنا يجب أن نتوقف.\nالمعلومات الصحية تساعدنا على الفهم، لكنها لا تقوم مقام التقييم المهني. دور «نسمة الحياة» هو التوعية والكشف المبكر والتوجيه والإحالة، وليس تشخيص الناس بأنفسهم.",
          style = MaterialTheme.typography.bodySmall,
          color = TextDark,
          lineHeight = 21.sp
        )
      }
    }

    // صفحة 12
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = CreamBackground),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text("🧭 الصفحة ١٢ | هل أحتاج إلى مختص؟", style = MaterialTheme.typography.titleSmall, color = DarkGreen, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "ليس من الضروري أن تصل إلى أسوأ مرحلة حتى تطلب المساعدة. فكر في طلب المساعدة عندما:\n• تستمر الأعراض ولا تتحسن\n• تبدأ في التأثير على عملك أو دراستك\n• تتأثر علاقاتك بشكل واضح\n• تصبح غير قادر على القيام بمهامك المعتادة\n• تشعر أن محاولاتك وحدها لم تعد كافية\nطلب المساعدة ليس حكمًا على شخصيتك. إنه قرار بأنك لا تريد أن تظل عالقًا في الألم.",
          style = MaterialTheme.typography.bodySmall,
          color = TextDark,
          lineHeight = 21.sp
        )
      }
    }

    // صفحة 13
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.45f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text("🩺 الصفحة ١٣ | أخصائي نفسي أم طبيب نفسي؟", style = MaterialTheme.typography.titleSmall, color = DarkGreen, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "• الأخصائي النفسي: يمكنه تقديم التقييم النفسي والعلاج النفسي (الجلسات الحوارية والسلوكية)، بحسب مؤهلاته وترخيصه.\n• الطبيب النفسي: طبيب متخصص في الصحة النفسية، ويمكنه إجراء التقييم الطبي ووصف الأدوية عند الحاجة.\nوقد يحتاج بعض الأشخاص إلى العلاج النفسي، وبعضهم إلى العلاج الدوائي، وبعضهم إلى الاثنين معًا. ليس المطلوب منك أن تعرف وحدك أيهما تحتاج؛ ابدأ بطلب تقييم من مختص مؤهل.",
          style = MaterialTheme.typography.bodySmall,
          color = TextDark,
          lineHeight = 21.sp
        )
      }
    }

    // صفحة 14
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.6f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text("🚨 الصفحة ١٤ | متى تصبح المسألة طارئة؟", style = MaterialTheme.typography.titleSmall, color = Color(0xFF7B2D26), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "هناك مواقف لا ينبغي انتظار موعد عادي فيها: إذا كان الشخص معرضًا لخطر فوري، أو لديه أفكار أو نية لإيذاء نفسه أو شخص آخر، أو أصبح غير قادر على الحفاظ على سلامته.\nفي هذه الحالات: لا تترك الشخص وحده إذا كان هناك خطر مباشر، وتوجه إلى خدمات الطوارئ أو أقرب منشأة صحية مناسبة فورًا. لا تحاول أن تقوم بدور الطبيب أو المعالج بنفسك.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF7B2D26),
          fontWeight = FontWeight.SemiBold,
          lineHeight = 21.sp
        )
      }
    }
  }
}

// ============================================================================
// Shared Booklet UI Helpers
// ============================================================================
@Composable
private fun BookletHighlightNote(text: String) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(SoftMint.copy(alpha = 0.45f))
      .padding(14.dp)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.bodyMedium,
      color = DarkGreen,
      fontWeight = FontWeight.SemiBold,
      lineHeight = 23.sp
    )
  }
}

@Composable
private fun BookletTextField(
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
private fun BookletSaveButton(
  text: String,
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
      Icon(
        imageVector = if (isSaved) Icons.Default.CheckCircle else Icons.Default.BookmarkAdded,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = if (isSaved) "✓ تم الحفظ محليًا في «رحلتي»" else text,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = Color.White
      )
    }
  }
}
