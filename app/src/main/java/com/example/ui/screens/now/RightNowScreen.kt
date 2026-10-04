package com.example.ui.screens.now

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RightNowScreen(
  onSelectStateRoute: ((String) -> Unit)? = null,
  onSaveToJourney: (String, String) -> Unit,
  onOpenRelatedArticle: (Int) -> Unit,
  onNavigateToSupport: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedState by remember { mutableStateOf<RightNowStateItem?>(null) }

  BackHandler {
    if (selectedState != null) {
      selectedState = null
    } else {
      onBack()
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
          text = if (selectedState == null) "🌿 أنا دلوقتي..." else "${selectedState?.emoji} ${selectedState?.title}",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = {
            if (selectedState != null) {
              selectedState = null
            } else {
              onBack()
            }
          },
          modifier = Modifier.testTag("right_now_back_button")
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

    val currentSelection = selectedState
    if (currentSelection == null) {
      RightNowStatesSelectionList(
        states = RightNowRepository.states,
        onSelectState = { item ->
          if (onSelectStateRoute != null) {
            onSelectStateRoute(item.id)
          } else {
            selectedState = item
          }
        }
      )
    } else {
      RightNowShortFocusedView(
        stateItem = currentSelection,
        onSaveToJourney = onSaveToJourney,
        onOpenRelatedArticle = onOpenRelatedArticle,
        onNavigateToSupport = onNavigateToSupport,
        onChooseAnotherState = { selectedState = null }
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RightNowStateDetailScreen(
  stateId: String,
  onSaveToJourney: (String, String) -> Unit,
  onOpenRelatedArticle: (Int) -> Unit,
  onNavigateToSupport: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val stateItem = remember(stateId) {
    RightNowRepository.states.find { it.id == stateId }
      ?: RightNowRepository.states.first()
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    TopAppBar(
      title = {
        Text(
          text = "${stateItem.emoji} ${stateItem.title}",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("right_now_detail_back_button")
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

    RightNowShortFocusedView(
      stateItem = stateItem,
      onSaveToJourney = onSaveToJourney,
      onOpenRelatedArticle = onOpenRelatedArticle,
      onNavigateToSupport = onNavigateToSupport,
      onChooseAnotherState = onBack
    )
  }
}

@Composable
private fun RightNowStatesSelectionList(
  states: List<RightNowStateItem>,
  onSelectState: (RightNowStateItem) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 20.dp),
    contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.5f)),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("right_now_question_header")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "إنت محتاج إيه دلوقتي؟",
            style = MaterialTheme.typography.headlineSmall,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "اختار أقرب حالة للي حاسس بيه الآن عشان نبدأ بخطوة واحدة هادية ومختصرة:",
            style = MaterialTheme.typography.bodyMedium,
            color = TextDark.copy(alpha = 0.85f),
            lineHeight = 22.sp
          )
        }
      }
    }

    items(states, key = { it.id }) { item ->
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, SageGreenPrimary.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
          .clickable { onSelectState(item) }
          .testTag("right_now_card_${item.id}")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(WarmBeige.copy(alpha = 0.55f)),
              contentAlignment = Alignment.Center
            ) {
              Text(text = item.emoji, fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Text(
                text = "${item.emoji} ${item.title}",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.78f),
                lineHeight = 20.sp
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun RightNowShortFocusedView(
  stateItem: RightNowStateItem,
  onSaveToJourney: (String, String) -> Unit,
  onOpenRelatedArticle: (Int) -> Unit,
  onNavigateToSupport: () -> Unit,
  onChooseAnotherState: () -> Unit
) {
  val scrollState = rememberScrollState()
  var selectedChoice by remember(stateItem.id) {
    mutableStateOf(stateItem.tryChoices.firstOrNull() ?: "")
  }
  var userInput by remember(stateItem.id) { mutableStateOf("") }
  var isSavedLocally by remember(stateItem.id) { mutableStateOf(false) }
  var isCopied by remember(stateItem.id) { mutableStateOf(false) }
  val context = LocalContext.current

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(horizontal = 20.dp, vertical = 10.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header Badge
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
            .size(50.dp)
            .clip(CircleShape)
            .background(SoftMint.copy(alpha = 0.6f)),
          contentAlignment = Alignment.Center
        ) {
          Text(text = stateItem.emoji, fontSize = 26.sp)
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
          Text(
            text = "${stateItem.emoji} ${stateItem.title}",
            style = MaterialTheme.typography.titleLarge,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = stateItem.subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = TextDark.copy(alpha = 0.8f)
          )
        }
      }
    }

    // 1. 🌱 افهم (معلومة نفسية قصيرة)
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.45f)),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("right_now_understand_card")
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "🌱 افهم",
          style = MaterialTheme.typography.titleLarge,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = stateItem.understandText,
          style = MaterialTheme.typography.bodyLarge,
          color = TextDark,
          lineHeight = 26.sp
        )
      }
    }

    // 2. 🧩 جرّب (تمرين عملي مختصر ومباشر)
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier
        .fillMaxWidth()
        .border(1.5.dp, SageGreenPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
        .testTag("right_now_try_card")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = "🧩 جرّب — ${stateItem.tryExerciseTitle}",
          style = MaterialTheme.typography.titleLarge,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )

        Text(
          text = stateItem.tryExercisePrompt,
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark,
          lineHeight = 23.sp
        )

        stateItem.tryChoices.forEach { choice ->
          val isSelected = selectedChoice == choice
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) SoftMint.copy(alpha = 0.45f) else CreamBackground)
              .clickable {
                selectedChoice = choice
                isSavedLocally = false
              }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            RadioButton(
              selected = isSelected,
              onClick = {
                selectedChoice = choice
                isSavedLocally = false
              },
              colors = RadioButtonDefaults.colors(selectedColor = DarkGreen)
            )
            Text(
              text = choice,
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
          }
        }

        // حقل الإجابة الشخصية المحلي
        Text(
          text = stateItem.inputLabel,
          style = MaterialTheme.typography.titleSmall,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
          value = userInput,
          onValueChange = {
            userInput = it
            isSavedLocally = false
          },
          placeholder = { Text(stateItem.inputPlaceholder, fontSize = 13.sp) },
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = WarmBeige,
            focusedContainerColor = CreamBackground,
            unfocusedContainerColor = CreamBackground
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("right_now_exercise_input")
        )

        // رسالة جاهزة للنسخ إن وجدت
        stateItem.readyMessageToCopy?.let { readyMsg ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "💬 رسالة جاهزة لو حابب تبعتها لشخص آمن:",
                style = MaterialTheme.typography.labelMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "\"$readyMsg\"",
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark,
                fontWeight = FontWeight.SemiBold
              )
              Spacer(modifier = Modifier.height(8.dp))
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                  onClick = {
                    val clipboard =
                      context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                    clipboard?.setPrimaryClip(ClipData.newPlainText("رسالة نسمة حياة", readyMsg))
                    isCopied = true
                  },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                  Icon(
                    imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                    contentDescription = null,
                    tint = DarkGreen,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = if (isCopied) "تم نسخ الرسالة" else "نسخ الرسالة",
                    fontSize = 12.sp,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }

        // تنبيه خاص لحالة "حاسس إن الموضوع أكبر مني"
        if (stateItem.id == "too_much_for_me") {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.65f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "⚠️ لو في خطر فوري على سلامتك الآن:",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF7B2D26),
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "لا تبقَ وحدك؛ تواصل فورًا مع شخص تثق به أو توجه لأقرب خدمة طوارئ محلية.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF7B2D26),
                fontWeight = FontWeight.SemiBold
              )
              Spacer(modifier = Modifier.height(8.dp))
              OutlinedButton(
                onClick = onNavigateToSupport,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = "الانتقال إلى قسم «طلب دعم ومساندة»",
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
            }
          }
        }

        // خلاصة سريعة مطمئنة
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SoftMint.copy(alpha = 0.35f))
            .padding(12.dp)
        ) {
          Text(
            text = "💡 ${stateItem.takeWithYouLine}",
            style = MaterialTheme.typography.bodySmall,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
        }

        // زر حفظ التمرين محليًا
        Button(
          onClick = {
            val summary = buildString {
              appendLine("حالتي في «أنا دلوقتي...»: ${stateItem.emoji} ${stateItem.title}")
              if (selectedChoice.isNotBlank()) {
                appendLine("الخطوة المختارة: $selectedChoice")
              }
              if (userInput.isNotBlank()) {
                appendLine("ما كتبته لنفسي: $userInput")
              }
              appendLine("خُد معك: ${stateItem.takeWithYouLine}")
            }.trimIndent()
            onSaveToJourney("🌿 أنا دلوقتي: ${stateItem.title}", summary)
            isSavedLocally = true
          },
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("right_now_finish_button")
        ) {
          Text(
            text = if (isSavedLocally) "✓ تم حفظ التمرين محليًا" else "✓ خلصت التمرين",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }

        AnimatedVisibility(visible = isSavedLocally) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "تم حفظ خطوتك محليًا على جهازك فقط داخل صفحة «رحلتي» 🤍",
              style = MaterialTheme.typography.labelSmall,
              color = DarkGreen
            )
          }
        }
      }
    }

    // أزرار الانتقال الاختيارية بعد التمرين
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      OutlinedButton(
        onClick = { onOpenRelatedArticle(stateItem.linkedArticleId) },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = "📖 مقال: ${stateItem.linkedArticleTitle}",
          color = DarkGreen,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }

      OutlinedButton(
        onClick = onChooseAnotherState,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = "اختيار حالة أخرى",
          color = DarkGreen,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Spacer(modifier = Modifier.height(40.dp))
  }
}
