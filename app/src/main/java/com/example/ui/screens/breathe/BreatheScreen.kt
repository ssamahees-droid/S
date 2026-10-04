package com.example.ui.screens.breathe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.MoodDifficult
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import com.example.util.AmbientAudioPlayer
import kotlinx.coroutines.delay

data class BreathingExercise(
  val id: Int,
  val title: String,
  val durationMinutes: Int,
  val description: String,
  val icon: ImageVector,
  val inhaleSec: Int = 4,
  val holdSec: Int = 4,
  val exhaleSec: Int = 4,
  val restSec: Int = 4
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreatheScreen(
  audioPlayer: AmbientAudioPlayer,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val exercises = remember {
    listOf(
      BreathingExercise(1, "تهدئة سريعة", 2, "تثبيت النبض والعودة إلى اللحظة الحاضرة بهدوء", Icons.Default.Timer, 4, 4, 4, 4),
      BreathingExercise(2, "تخفيف التوتر", 5, "تنفس عميق لتهدئة الجهاز العصبي المشحون", Icons.Default.SelfImprovement, 4, 7, 8, 0),
      BreathingExercise(3, "قبل النوم", 10, "إبطاء موجات الدماغ وإرخاء الجسد للاستغراق في النوم", Icons.Default.Bedtime, 4, 4, 6, 2),
      BreathingExercise(4, "عقلي مزدحم", 5, "تصفية تسارع الأفكار وإعادة التركيز الواعي", Icons.Default.GraphicEq, 4, 2, 6, 2),
      BreathingExercise(5, "تأملات روحية", 5, "سكينة قلبية واستشعار الطمأنينة والسلام الداخلي", Icons.Default.Spa, 5, 3, 5, 3)
    )
  }

  var selectedExercise by remember { mutableStateOf(exercises[0]) }
  var isBreathingActive by remember { mutableStateOf(false) }
  var breathingPhase by remember { mutableStateOf("شهيق") }
  var phaseCountdown by remember { mutableIntStateOf(4) }
  var postCheckinSelected by remember { mutableStateOf<String?>(null) }
  var showPostCheckinDialog by remember { mutableStateOf(false) }

  val playerState by audioPlayer.playerState.collectAsState()

  // Dynamic Scale animation for breathing circle
  val scaleAnim = remember { Animatable(1f) }

  LaunchedEffect(isBreathingActive, breathingPhase) {
    if (isBreathingActive) {
      when (breathingPhase) {
        "شهيق" -> {
          scaleAnim.animateTo(
            targetValue = 1.35f,
            animationSpec = tween(
              durationMillis = selectedExercise.inhaleSec * 1000,
              easing = FastOutSlowInEasing
            )
          )
        }
        "حبس النفس" -> {
          // stay expanded
        }
        "زفير" -> {
          scaleAnim.animateTo(
            targetValue = 0.95f,
            animationSpec = tween(
              durationMillis = selectedExercise.exhaleSec * 1000,
              easing = FastOutSlowInEasing
            )
          )
        }
        "راحة" -> {
          scaleAnim.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(
              durationMillis = selectedExercise.restSec * 1000,
              easing = LinearEasing
            )
          )
        }
      }
    } else {
      scaleAnim.snapTo(1f)
    }
  }

  // Breathing loop timer
  LaunchedEffect(isBreathingActive, selectedExercise) {
    if (isBreathingActive) {
      while (isBreathingActive) {
        // 1. Inhale
        breathingPhase = "شهيق"
        for (i in selectedExercise.inhaleSec downTo 1) {
          phaseCountdown = i
          delay(1000)
        }

        // 2. Hold
        if (selectedExercise.holdSec > 0) {
          breathingPhase = "حبس النفس"
          for (i in selectedExercise.holdSec downTo 1) {
            phaseCountdown = i
            delay(1000)
          }
        }

        // 3. Exhale
        breathingPhase = "زفير"
        for (i in selectedExercise.exhaleSec downTo 1) {
          phaseCountdown = i
          delay(1000)
        }

        // 4. Rest
        if (selectedExercise.restSec > 0) {
          breathingPhase = "راحة"
          for (i in selectedExercise.restSec downTo 1) {
            phaseCountdown = i
            delay(1000)
          }
        }
      }
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
          text = "خد نفس... 🌿",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = {
            audioPlayer.stop()
            onBack()
          },
          modifier = Modifier.testTag("breathe_back_button")
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

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
    ) {
      // Intro quote (Page 16)
      item {
        Text(
          text = "مش لازم تحل كل حاجة دلوقتي.",
          style = MaterialTheme.typography.headlineMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "اختر التمرين المريح لك وأعطِ نفسك مساحة للاسترخاء.",
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark.copy(alpha = 0.75f)
        )
        Spacer(modifier = Modifier.height(16.dp))
      }

      // Exercise Selector Chips
      item {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(exercises, key = { it.id }) { ex ->
            val isSelected = selectedExercise.id == ex.id
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isSelected) DarkGreen else Color.White
              ),
              border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = if (isSelected) DarkGreen else WarmBeige
              ),
              modifier = Modifier
                .clickable {
                  selectedExercise = ex
                  isBreathingActive = false
                  breathingPhase = "شهيق"
                  phaseCountdown = ex.inhaleSec
                }
                .testTag("exercise_chip_${ex.id}")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = ex.icon,
                  contentDescription = null,
                  tint = if (isSelected) Color.White else DarkGreen,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = ex.title,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) Color.White else DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "${ex.durationMinutes} دقائق",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) CreamBackground.copy(alpha = 0.8f) else TextDark.copy(alpha = 0.6f)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }

      // Central Breathing Circle
      item {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 32.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = selectedExercise.title,
              style = MaterialTheme.typography.titleLarge,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = selectedExercise.description,
              style = MaterialTheme.typography.labelMedium,
              color = TextDark.copy(alpha = 0.7f),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Animated Breathing Node
            Box(
              modifier = Modifier
                .size(200.dp)
                .scale(scaleAnim.value),
              contentAlignment = Alignment.Center
            ) {
              // Outer glow circle
              Box(
                modifier = Modifier
                  .size(190.dp)
                  .clip(CircleShape)
                  .background(SageGreenPrimary.copy(alpha = 0.25f))
              )

              // Inner solid circle
              Box(
                modifier = Modifier
                  .size(140.dp)
                  .clip(CircleShape)
                  .background(if (isBreathingActive) DarkGreen else SageGreenPrimary),
                contentAlignment = Alignment.Center
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = if (isBreathingActive) breathingPhase else "ابدأ",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                  )
                  if (isBreathingActive) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = "$phaseCountdown",
                      style = MaterialTheme.typography.headlineMedium,
                      color = CreamBackground,
                      fontWeight = FontWeight.ExtraBold
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Start / Stop Breathing Button
            Button(
              onClick = {
                isBreathingActive = !isBreathingActive
                if (!isBreathingActive) {
                  showPostCheckinDialog = true
                }
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isBreathingActive) MoodDifficult else DarkGreen
              ),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)
                .testTag("toggle_breathing_session")
            ) {
              Text(
                text = if (isBreathingActive) "إيقاف مؤقت ⏸️" else "بدء التمرين 🌱",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }

      // Audio Player Controls (Page 17: Play, Pause, Progress, Duration, Volume)
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "أصوات طبيعية مهدئة 🍃",
                  style = MaterialTheme.typography.titleMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = if (playerState.isPlaying) "نغمات السكينة قيد التشغيل" else "أضف صوتاً هادئاً للتمرين",
                  style = MaterialTheme.typography.labelSmall,
                  color = TextDark.copy(alpha = 0.65f)
                )
              }

              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(DarkGreen),
                contentAlignment = Alignment.Center
              ) {
                IconButton(
                  onClick = {
                    if (playerState.isPlaying) {
                      audioPlayer.pause()
                    } else {
                      audioPlayer.playTrack("أصوات السكينة والطبيعة", selectedExercise.durationMinutes * 60, 174.0)
                    }
                  },
                  modifier = Modifier.testTag("breathe_audio_toggle")
                ) {
                  Icon(
                    imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (playerState.isPlaying) "إيقاف" else "تشغيل",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Slider
            Slider(
              value = playerState.currentPositionSeconds.toFloat(),
              onValueChange = { audioPlayer.seekTo(it.toInt()) },
              valueRange = 0f..playerState.totalDurationSeconds.toFloat(),
              colors = SliderDefaults.colors(
                thumbColor = DarkGreen,
                activeTrackColor = SageGreenPrimary,
                inactiveTrackColor = WarmBeige
              ),
              modifier = Modifier.fillMaxWidth()
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = String.format("%02d:%02d", playerState.currentPositionSeconds / 60, playerState.currentPositionSeconds % 60),
                style = MaterialTheme.typography.labelSmall,
                color = TextDark.copy(alpha = 0.6f)
              )
              Text(
                text = String.format("%02d:%02d", playerState.totalDurationSeconds / 60, playerState.totalDurationSeconds % 60),
                style = MaterialTheme.typography.labelSmall,
                color = TextDark.copy(alpha = 0.6f)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Volume
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = DarkGreen,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Slider(
                value = playerState.volume,
                onValueChange = { audioPlayer.setVolume(it) },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                  thumbColor = DarkGreen,
                  activeTrackColor = SageGreenPrimary,
                  inactiveTrackColor = WarmBeige
                ),
                modifier = Modifier.weight(1f)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }

      // Post-Activity Feedback (Page 17)
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.45f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text(
              text = "إيه إحساسك دلوقتي؟",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "شاركنا شعورك بعد التمرين البسيط:",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              listOf(
                "أفضل" to "😊",
                "زي ما أنا" to "😐",
                "لسه متعب" to "😔"
              ).forEach { (label, emoji) ->
                val isSelected = postCheckinSelected == label
                Button(
                  onClick = { postCheckinSelected = label },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) DarkGreen else Color.White
                  ),
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) DarkGreen else WarmBeige),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier
                    .weight(1f)
                    .testTag("post_breathe_$label")
                ) {
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(emoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = label,
                      fontSize = 12.sp,
                      color = if (isSelected) Color.White else TextDark,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "هذه ليست نتيجة طبية، إنما لمساعدتك على ملاحظة أثر التهدئة على جسدك.",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.6f)
            )
          }
        }
      }
    }
  }
}
