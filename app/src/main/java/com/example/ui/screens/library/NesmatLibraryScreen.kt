package com.example.ui.screens.library

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NesmatLibraryScreen(
  onOpenArticle: (Int) -> Unit,
  onSaveToJourney: (String, String) -> Unit,
  onOpenWorkshopsAndAudio: () -> Unit = {},
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  var selectedSectionTab by remember { mutableIntStateOf(0) } // 0: المقالات والتطبيق, 1: 🧯 خطط الإنقاذ
  val articles = remember { NesmatLibraryRepository.articles }
  val rescuePlans = remember { NesmatLibraryRepository.rescuePlans }
  var expandedRescueId by remember { mutableStateOf<String?>(rescuePlans.firstOrNull()?.id) }
  val rescueAnswers = remember { mutableStateMapOf<String, String>() }
  val rescueSavedFlags = remember { mutableStateMapOf<String, Boolean>() }

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
          modifier = Modifier.testTag("library_back_button")
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

    // Library Header Banner
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
      Text(
        text = "🌿 مكتبة نسمة حياة",
        style = MaterialTheme.typography.headlineMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "مش كل وجع بيبان… خلينا نفهم اللي جواك خطوة بخطوة.",
        style = MaterialTheme.typography.bodyLarge,
        color = TextDark.copy(alpha = 0.85f),
        lineHeight = 24.sp
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Section Tabs: Articles vs Rescue Plans
    TabRow(
      selectedTabIndex = selectedSectionTab,
      containerColor = CreamBackground,
      contentColor = DarkGreen,
      modifier = Modifier.padding(horizontal = 20.dp)
    ) {
      Tab(
        selected = selectedSectionTab == 0,
        onClick = { selectedSectionTab = 0 },
        modifier = Modifier.testTag("library_tab_articles"),
        text = {
          Text(
            text = "📖 المقالات (١٠)",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      )
      Tab(
        selected = selectedSectionTab == 1,
        onClick = { selectedSectionTab = 1 },
        modifier = Modifier.testTag("library_tab_rescue"),
        text = {
          Text(
            text = "🌿 خطط الإنقاذ (٨)",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      )
      Tab(
        selected = selectedSectionTab == 2,
        onClick = { selectedSectionTab = 2 },
        modifier = Modifier.testTag("library_tab_booklet"),
        text = {
          Text(
            text = "📘 الكتيب وخطتي",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      )
    }

    when (selectedSectionTab) {
      0 -> {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          contentPadding = PaddingValues(top = 14.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Featured banner for the 15-page Booklet & Personal Plan
          item {
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.5f)),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedSectionTab = 2 }
                .testTag("library_open_booklet_banner")
            ) {
              Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "📘 كتيب «دليل مبسط للصحة النفسية» + خطتي الخاصة",
                    style = MaterialTheme.typography.titleSmall,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Spacer(modifier = Modifier.height(3.dp))
                  Text(
                    text = "\"كثيرون يعيشون الحياة... وقليلون يستمتعون بها\" — ١٥ محطة تفاعلية وسجل النوم والحدود وخطتك الشخصية.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDark.copy(alpha = 0.85f)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                  onClick = { selectedSectionTab = 2 },
                  colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text("افتح الكتيب", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          items(articles, key = { it.id }) { article ->
            ArticleLibraryCard(
              article = article,
              onReadAndTry = { onOpenArticle(article.id) }
            )
          }

          // Quick link to Rescue Plans at bottom of articles list
          item {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.45f)),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedSectionTab = 1 }
                .testTag("library_switch_to_rescue_banner")
            ) {
              Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "🌿 خطط الإنقاذ — نسمة حياة",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "مش لازم تحل حياتك كلها دلوقتي. أحيانًا محتاج بس تعدّي الدقائق الصعبة بطريقة أهدى.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDark.copy(alpha = 0.8f)
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                  onClick = { selectedSectionTab = 1 },
                  colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text("افتح الخطط", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenWorkshopsAndAudio() }
                .testTag("library_open_workshops_banner")
            ) {
              Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "🧰 الورش التطبيقية والمحتوى الصوتي (أفهم نفسي)",
                    style = MaterialTheme.typography.titleSmall,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "١٠ ورش عملية متكاملة (صندوق أدواتي النفسية، غرفة الطوارئ، خلاف بدون معركة، وخطة العودة لنفسي).",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDark.copy(alpha = 0.75f)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                  onClick = onOpenWorkshopsAndAudio,
                  colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text("الورش", fontSize = 12.sp, color = DarkGreen, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      1 -> {
        // 🌿 خطط الإنقاذ — نسمة حياة
        RescuePlansContentList(
          onSaveToJourney = onSaveToJourney
        )
      }

      2 -> {
        // 📘 كتيب دليل مبسط للصحة النفسية + خطة نسمة الحياة الخاصة بي
        MentalHealthBookletSection(
          onSaveToJourney = onSaveToJourney
        )
      }
    }
  }
}

@Composable
private fun ArticleLibraryCard(
  article: NesmatLibraryArticle,
  onReadAndTry: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onReadAndTry() }
      .testTag("library_article_card_${article.id}")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
      ) {
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

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "${article.id}. ${article.title}",
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = article.shortDescription,
            style = MaterialTheme.typography.bodyMedium,
            color = TextDark.copy(alpha = 0.8f),
            lineHeight = 21.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        Button(
          onClick = onReadAndTry,
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("library_read_and_try_${article.id}")
        ) {
          Text(
            text = "اقرأ وجرب",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color.White
          )
        }
      }
    }
  }
}

@Composable
private fun RescuePlanCard(
  plan: RescuePlanItem,
  isExpanded: Boolean,
  answerText: String,
  isSaved: Boolean,
  onToggleExpand: () -> Unit,
  onAnswerChange: (String) -> Unit,
  onCompletePlan: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .border(
        width = if (isExpanded) 1.5.dp else 0.dp,
        color = if (isExpanded) SageGreenPrimary else Color.Transparent,
        shape = RoundedCornerShape(18.dp)
      )
      .testTag("rescue_plan_card_${plan.id}")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onToggleExpand() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(WarmBeige.copy(alpha = 0.55f)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = plan.emoji, fontSize = 22.sp)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "${plan.emoji} ${plan.title}",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "\"${plan.subtitle}\"",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark.copy(alpha = 0.8f)
            )
          }
        }

        IconButton(onClick = onToggleExpand) {
          Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = if (isExpanded) "طي" else "توسيع",
            tint = DarkGreen
          )
        }
      }

      AnimatedVisibility(visible = isExpanded) {
        Column(modifier = Modifier.padding(top = 14.dp)) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(CreamBackground)
              .padding(14.dp)
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Text(
                text = "خطوات هادئة وسريعة:",
                style = MaterialTheme.typography.labelLarge,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              plan.steps.forEach { step ->
                Text(
                  text = step,
                  style = MaterialTheme.typography.bodyMedium,
                  color = TextDark,
                  lineHeight = 22.sp
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "🧩 ${plan.exercisePrompt}",
            style = MaterialTheme.typography.bodyMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = answerText,
            onValueChange = onAnswerChange,
            placeholder = { Text(plan.exercisePlaceholder, fontSize = 13.sp) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = DarkGreen,
              unfocusedBorderColor = WarmBeige,
              focusedContainerColor = CreamBackground,
              unfocusedContainerColor = CreamBackground
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("rescue_plan_input_${plan.id}")
          )

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = onCompletePlan,
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("rescue_plan_done_${plan.id}")
          ) {
            Text(
              text = if (isSaved) "✓ تم حفظ التمرين محليًا" else "✓ طبّقت الخطوة",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
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
    }
  }
}
