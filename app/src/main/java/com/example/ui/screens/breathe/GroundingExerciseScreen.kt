package com.example.ui.screens.breathe

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

data class GroundingStep(
  val stepNumber: Int,
  val count: Int,
  val senseTitle: String,
  val icon: ImageVector,
  val emoji: String,
  val prompt: String,
  val suggestion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroundingExerciseScreen(
  onBack: () -> Unit,
  onCompleteExercise: () -> Unit,
  modifier: Modifier = Modifier
) {
  val steps = remember {
    listOf(
      GroundingStep(
        stepNumber = 1,
        count = 5,
        senseTitle = "حاسة البصر 👁️",
        icon = Icons.Default.Visibility,
        emoji = "👁️",
        prompt = "انظر حولك وابحث عن ٥ أشياء يمكنك رؤيتها الآن بالعين المجردة",
        suggestion = "مثال: لون الحائط، ضوء الشباك، كوب الماء، يدك، نمط السجادة..."
      ),
      GroundingStep(
        stepNumber = 2,
        count = 4,
        senseTitle = "حاسة اللمس ✋",
        icon = Icons.Default.PanTool,
        emoji = "✋",
        prompt = "المس ٤ أشياء مختلفة من حولك وركّز في ملمسها وحرارتها",
        suggestion = "مثال: ملمس ملابسك، برودة شاشة الهاتف، نعومة الغطاء، ملمس الطاولة..."
      ),
      GroundingStep(
        stepNumber = 3,
        count = 3,
        senseTitle = "حاسة السمع 👂",
        icon = Icons.Default.Hearing,
        emoji = "👂",
        prompt = "أغمض عينيك لثوانٍ وأنصت إلى ٣ أصوات يمكنك تمييزها في محيطك",
        suggestion = "مثال: صوت مروحة أو تكييف، صوت سيارات بعيدة، صوت أنفاسك الهادئة..."
      ),
      GroundingStep(
        stepNumber = 4,
        count = 2,
        senseTitle = "حاسة الشم 👃",
        icon = Icons.Default.Spa,
        emoji = "👃",
        prompt = "خذ نفساً وركّز في رائحتين يمكنك شمهما في الهواء حولك",
        suggestion = "مثال: رائحة قهوة، هواء نقي، عطر ملابسك، أو ملمس يدك المغسولة بالصابون..."
      ),
      GroundingStep(
        stepNumber = 5,
        count = 1,
        senseTitle = "حاسة التذوق والتنفس 👅",
        icon = Icons.Default.WaterDrop,
        emoji = "👅",
        prompt = "ركّز في طعم موجود في فمك الآن، أو خذ رشفة ماء بوعي وتأمل",
        suggestion = "أو خذ نفساً عميقاً وقل لنفسك: «أنا هنا الآن، أنا في أمان تام»."
      )
    )
  }

  var currentStepIndex by remember { mutableIntStateOf(0) }
  var isCompleted by remember { mutableStateOf(false) }

  // Checkboxes for items in the current step
  val checkedItems = remember { mutableStateListOf<Boolean>() }

  fun resetCheckedForStep(count: Int) {
    checkedItems.clear()
    repeat(count) { checkedItems.add(false) }
  }

  remember(currentStepIndex) {
    if (currentStepIndex < steps.size) {
      resetCheckedForStep(steps[currentStepIndex].count)
    }
    true
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "تمرين التأريض الحسي 5-4-3-2-1",
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            fontSize = 17.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("grounding_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "رجوع",
              tint = DarkGreen
            )
          }
        },
        actions = {
          IconButton(
            onClick = {
              currentStepIndex = 0
              isCompleted = false
              resetCheckedForStep(steps[0].count)
            }
          ) {
            Icon(
              imageVector = Icons.Default.RestartAlt,
              contentDescription = "إعادة التمرين",
              tint = DarkGreen
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
      )
    },
    containerColor = CreamBackground,
    modifier = modifier.fillMaxSize()
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentPadding = PaddingValues(20.dp),
      verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
      if (!isCompleted) {
        val currentStep = steps[currentStepIndex]
        val progress = (currentStepIndex + 1).toFloat() / steps.size

        // Progress & Step indicator
        item {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "الخطوة ${currentStepIndex + 1} من ٥",
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                fontSize = 13.sp
              )
              Text(
                text = currentStep.senseTitle,
                color = SageGreenPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = SageGreenPrimary,
              trackColor = WarmBeige.copy(alpha = 0.5f)
            )
          }
        }

        // Main Exercise Card
        item {
          Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(22.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              // Sense circle icon
              Box(
                modifier = Modifier
                  .size(80.dp)
                  .clip(CircleShape)
                  .background(
                    Brush.radialGradient(
                      colors = listOf(SoftMint.copy(alpha = 0.6f), WarmBeige.copy(alpha = 0.4f))
                    )
                  ),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = currentStep.count.toString(),
                  fontSize = 34.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = DarkGreen
                )
              }

              Spacer(modifier = Modifier.height(16.dp))

              Text(
                text = currentStep.prompt,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
              )

              Spacer(modifier = Modifier.height(12.dp))

              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(WarmBeige.copy(alpha = 0.35f))
                  .padding(14.dp)
              ) {
                Text(
                  text = currentStep.suggestion,
                  style = MaterialTheme.typography.bodySmall,
                  color = TextDark.copy(alpha = 0.8f),
                  textAlign = TextAlign.Center,
                  modifier = Modifier.fillMaxWidth()
                )
              }

              Spacer(modifier = Modifier.height(20.dp))

              Text(
                text = "انقر لتحديد كل ما رصدته بحواسك الآن:",
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.6f)
              )

              Spacer(modifier = Modifier.height(12.dp))

              // Checkable buttons for the count
              Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                for (i in 0 until currentStep.count) {
                  val isChecked = if (i < checkedItems.size) checkedItems[i] else false
                  Box(
                    modifier = Modifier
                      .size(44.dp)
                      .clip(CircleShape)
                      .background(if (isChecked) DarkGreen else WarmBeige.copy(alpha = 0.4f))
                      .clickable {
                        if (i < checkedItems.size) {
                          checkedItems[i] = !checkedItems[i]
                        }
                      },
                    contentAlignment = Alignment.Center
                  ) {
                    if (isChecked) {
                      Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                      )
                    } else {
                      Text(
                        text = "${i + 1}",
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                      )
                    }
                  }
                }
              }
            }
          }
        }

        // Navigation Action Buttons
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (currentStepIndex > 0) {
              Button(
                onClick = { currentStepIndex-- },
                colors = ButtonDefaults.buttonColors(
                  containerColor = WarmBeige.copy(alpha = 0.6f),
                  contentColor = DarkGreen
                ),
                shape = RoundedCornerShape(16.dp)
              ) {
                Text("السابق", fontWeight = FontWeight.Bold)
              }
            } else {
              Spacer(modifier = Modifier.width(1.dp))
            }

            Button(
              onClick = {
                if (currentStepIndex < steps.size - 1) {
                  currentStepIndex++
                } else {
                  isCompleted = true
                  onCompleteExercise()
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.testTag("grounding_next_button")
            ) {
              Text(
                text = if (currentStepIndex == steps.size - 1) "إنهاء والعودة للسكينة ✨" else "التالي",
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }

        // Helpful clinical note
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = DarkGreen,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = "تقنية التأريض 5-4-3-2-1 تعيد إشارات الأمان إلى الجهاز العصبي المركزي وتوقف نوبة الهلع عن طريق تحفيز الحواس الخمس في الوقت الفعلي.",
                style = MaterialTheme.typography.bodySmall,
                color = DarkGreen,
                lineHeight = 20.sp
              )
            }
          }
        }
      } else {
        // Completion Card
        item {
          Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(26.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(90.dp)
                  .clip(CircleShape)
                  .background(SoftMint.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = DarkGreen,
                  modifier = Modifier.size(54.dp)
                )
              }

              Spacer(modifier = Modifier.height(18.dp))

              Text(
                text = "أحسنت! أنت الآن هنا والآن في أمان تام 🌱",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                textAlign = TextAlign.Center
              )

              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = "لقد نجحت في إعادة توجيه عقلك الواعي إلى اللحظة الحالية وفصلت انتباهك عن حلقة القلق. تمت إضافة نقطة يقظة ذهنية إلى رصيد شجرتك.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
              )

              Spacer(modifier = Modifier.height(24.dp))

              Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("grounding_finish_button")
              ) {
                Text("العودة إلى الرئيسية 🌿", fontWeight = FontWeight.Bold, color = Color.White)
              }

              Spacer(modifier = Modifier.height(10.dp))

              Button(
                onClick = {
                  currentStepIndex = 0
                  isCompleted = false
                  resetCheckedForStep(steps[0].count)
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = WarmBeige.copy(alpha = 0.5f),
                  contentColor = DarkGreen
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text("إعادة التمرين مرة أخرى", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}
