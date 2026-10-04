package com.example.ui.screens.oasis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import com.example.util.SoundOasisPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundOasisScreen(
  oasisPlayer: SoundOasisPlayer,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val state by oasisPlayer.state.collectAsState()

  val pulseAnim = remember { Animatable(1f) }
  LaunchedEffect(state.isPlaying) {
    if (state.isPlaying) {
      pulseAnim.animateTo(
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
          animation = tween(1800, easing = LinearEasing),
          repeatMode = RepeatMode.Reverse
        )
      )
    } else {
      pulseAnim.snapTo(1f)
    }
  }

  val presets = listOf(
    "مطر الشتاء 🌧️",
    "أمواج الغروب 🌊",
    "تأمل السكينة 🧘",
    "نوم هانئ 🌙",
    "بستان الزيتون 🌿"
  )

  val timerOptions = listOf(
    null to "مستمر",
    15 to "15 د",
    30 to "30 د",
    45 to "45 د",
    60 to "60 د"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    TopAppBar(
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(DarkGreen),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = null,
              tint = CreamBackground,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "واحة السكينة الصوتية 🌿",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "اصنع بيئتك الصوتية الخاصة للهدوء والنوم والتركيز",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.6f)
            )
          }
        }
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("sound_oasis_back_button")
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
      contentPadding = PaddingValues(top = 10.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Hero Card with generated artwork & master control
      item {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp)
          ) {
            Image(
              painter = painterResource(id = R.drawable.nesmat_oasis_art),
              contentDescription = "واحة السكينة",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )

            // Gradient Overlay
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.verticalGradient(
                    colors = listOf(
                      Color.Transparent,
                      DarkGreen.copy(alpha = 0.75f),
                      DarkGreen.copy(alpha = 0.95f)
                    )
                  )
                )
            )

            // Master Play / Pause Button in the center
            Column(
              modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.25f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                  Text(
                    text = state.activePreset,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }

                if (state.timerRemainingMinutes != null) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                      .clip(RoundedCornerShape(12.dp))
                      .background(SageGreenPrimary.copy(alpha = 0.85f))
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Timer,
                      contentDescription = null,
                      tint = DarkGreen,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "${state.timerRemainingMinutes} دقيقة متبقية",
                      color = DarkGreen,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }

              // Large Center Action Button
              Box(
                modifier = Modifier
                  .size(72.dp)
                  .scale(pulseAnim.value)
                  .clip(CircleShape)
                  .background(if (state.isPlaying) SageGreenPrimary else Color.White)
                  .clickable { oasisPlayer.togglePlay() }
                  .testTag("oasis_play_pause_button"),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                  contentDescription = if (state.isPlaying) "إيقاف مؤقت" else "تشغيل الواحة",
                  tint = DarkGreen,
                  modifier = Modifier.size(36.dp)
                )
              }

              Text(
                text = if (state.isPlaying) "الأصوات تتدفق بنقاء... استرخِ وأغمض عينيك 🌿" else "انقر للبدء في الاستماع إلى الطبيعة 🤍",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }

      // Mood Presets Bar
      item {
        Column {
          Text(
            text = "أجواء جاهزة للاختيار السريع:",
            style = MaterialTheme.typography.titleSmall,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(presets) { preset ->
              val isSelected = state.activePreset == preset
              FilterChip(
                selected = isSelected,
                onClick = { oasisPlayer.applyPreset(preset) },
                label = { Text(preset, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = DarkGreen,
                  selectedLabelColor = Color.White,
                  containerColor = Color.White,
                  labelColor = TextDark
                )
              )
            }
          }
        }
      }

      // Multi-layer Sound Studio Sliders
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Text(
              text = "لوحة خلط الأصوات الحية 🎛️",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "اضبط مستوى كل صوت لتصنع مزيجك الفريد من الهدوء:",
              style = MaterialTheme.typography.bodySmall,
              color = TextDark.copy(alpha = 0.7f)
            )

            // 1. Rain Slider
            SoundLayerSlider(
              icon = Icons.Default.WaterDrop,
              label = "خرير المطر الهادئ 🌧️",
              value = state.rainVolume,
              onValueChange = { oasisPlayer.setRain(it) }
            )

            // 2. Wind Breeze Slider
            SoundLayerSlider(
              icon = Icons.Default.Air,
              label = "نسيم بساتين الزيتون 🍃",
              value = state.windVolume,
              onValueChange = { oasisPlayer.setWind(it) }
            )

            // 3. Waves Slider
            SoundLayerSlider(
              icon = Icons.Default.Waves,
              label = "أمواج البحر والمد الساكن 🌊",
              value = state.wavesVolume,
              onValueChange = { oasisPlayer.setWaves(it) }
            )

            // 4. Chimes 432Hz Slider
            SoundLayerSlider(
              icon = Icons.Default.SelfImprovement,
              label = "تردد السكينة 432Hz وأوتار الصفاء 🔔",
              value = state.chimesVolume,
              onValueChange = { oasisPlayer.setChimes(it) }
            )

            // 5. Night Summer Slider
            SoundLayerSlider(
              icon = Icons.Default.NotificationsActive,
              label = "سكون ليل الصيف 🌙",
              value = state.nightVolume,
              onValueChange = { oasisPlayer.setNight(it) }
            )
          }
        }
      }

      // Sleep & Meditation Timer
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(18.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = DarkGreen,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "مؤقت النوم والاسترخاء",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "إيقاف الأصوات تلقائياً لمساعدتك على النوم بهدوء:",
              style = MaterialTheme.typography.bodySmall,
              color = TextDark.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              timerOptions.forEach { (mins, label) ->
                val isSelected = state.totalTimerMinutes == mins
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.4f))
                    .clickable { oasisPlayer.setTimer(mins) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = label,
                    color = if (isSelected) Color.White else DarkGreen,
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
  }
}

@Composable
fun SoundLayerSlider(
  icon: ImageVector,
  label: String,
  value: Float,
  onValueChange: (Float) -> Unit
) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = DarkGreen,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = label,
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark,
          fontWeight = FontWeight.Medium
        )
      }
      Text(
        text = "${(value * 100).toInt()}%",
        style = MaterialTheme.typography.labelSmall,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )
    }

    Slider(
      value = value,
      onValueChange = onValueChange,
      valueRange = 0f..1f,
      colors = SliderDefaults.colors(
        thumbColor = DarkGreen,
        activeTrackColor = SageGreenPrimary,
        inactiveTrackColor = WarmBeige.copy(alpha = 0.5f)
      ),
      modifier = Modifier.fillMaxWidth()
    )
  }
}
