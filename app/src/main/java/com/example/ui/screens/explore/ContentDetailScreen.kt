package com.example.ui.screens.explore

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentItem
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import com.example.util.AmbientAudioPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDetailScreen(
  item: ContentItem?,
  audioPlayer: AmbientAudioPlayer,
  onBack: () -> Unit,
  onToggleFavorite: (Long, Boolean) -> Unit,
  onNavigateToSupport: () -> Unit,
  onSaveWorkshopNote: (String, String) -> Unit = { _, _ -> },
  modifier: Modifier = Modifier
) {
  BackHandler {
    audioPlayer.stop()
    onBack()
  }

  val context = LocalContext.current
  val playerState by audioPlayer.playerState.collectAsState()
  val scrollState = rememberScrollState()

  if (item == null) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(CreamBackground),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "المحتوى غير موجود",
        style = MaterialTheme.typography.titleMedium,
        color = TextDark
      )
    }
    return
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    TopAppBar(
      title = {
        Text(
          text = item.category,
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
          modifier = Modifier.testTag("content_detail_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "الرجوع",
            tint = DarkGreen
          )
        }
      },
      actions = {
        IconButton(
          onClick = { onToggleFavorite(item.id, item.isFavorite) },
          modifier = Modifier.testTag("content_detail_favorite_button")
        ) {
          Icon(
            imageVector = if (item.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = "حفظ",
            tint = DarkGreen
          )
        }
        IconButton(
          onClick = {
            val shareIntent = Intent().apply {
              action = Intent.ACTION_SEND
              putExtra(
                Intent.EXTRA_TEXT,
                "${item.title}\n\n${item.description}\n\nاقرأ المزيد عبر تطبيق نسمة الحياة 🌿"
              )
              type = "text/plain"
            }
            context.startActivity(Intent.createChooser(shareIntent, "مشاركة المحتوى"))
          },
          modifier = Modifier.testTag("content_detail_share_button")
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "مشاركة",
            tint = DarkGreen
          )
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
      // Badges
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SageGreenPrimary.copy(alpha = 0.25f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = item.category,
            style = MaterialTheme.typography.labelMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
          text = "المدة: ${item.duration}",
          style = MaterialTheme.typography.labelMedium,
          color = TextDark.copy(alpha = 0.7f)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Title
      Text(
        text = item.title,
        style = MaterialTheme.typography.headlineLarge,
        color = DarkGreen,
        fontWeight = FontWeight.Bold,
        lineHeight = 34.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Reviewer and Author info
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "بقلم: ${item.author}",
          style = MaterialTheme.typography.labelSmall,
          color = TextDark.copy(alpha = 0.6f)
        )

        Text(
          text = "مراجعة: ${item.reviewer}",
          style = MaterialTheme.typography.labelSmall,
          color = SageGreenPrimary,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Audio Player Card (Page 10, 11: زر استمع مع Play, Pause, Progress, Duration, Volume)
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "استمع للمحتوى بنغمات مهدئة 🎧",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (playerState.isPlaying) "جاري التشغيل بهدوء..." else "انقر للاستماع",
                style = MaterialTheme.typography.labelMedium,
                color = TextDark.copy(alpha = 0.65f)
              )
            }

            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(DarkGreen),
              contentAlignment = Alignment.Center
            ) {
              IconButton(
                onClick = {
                  if (playerState.isPlaying) {
                    audioPlayer.pause()
                  } else {
                    audioPlayer.playTrack(item.title, 180, 216.0)
                  }
                },
                modifier = Modifier.testTag("audio_play_pause_button")
              ) {
                Icon(
                  imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                  contentDescription = if (playerState.isPlaying) "إيقاف مؤقت" else "تشغيل",
                  tint = Color.White,
                  modifier = Modifier.size(26.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Progress Bar / Slider
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

          Spacer(modifier = Modifier.height(6.dp))

          // Volume control row
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.VolumeUp,
              contentDescription = "مستوى الصوت",
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

      // Main Article Body
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(20.dp)
        ) {
          Text(
            text = item.body,
            style = MaterialTheme.typography.bodyLarge,
            color = TextDark,
            lineHeight = 28.sp
          )
        }
      }

      if (item.contentType == "workshop" || item.category == "ورش تطبيقية") {
        Spacer(modifier = Modifier.height(20.dp))
        InteractiveWorkshopSection(
          item = item,
          onSaveWorkshopNote = onSaveWorkshopNote
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // "محتاج أتكلم" Action Card (Page 11)
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "حاسس إنك محتاج حد يسمعك؟",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "فريق نسمة الحياة هنا للتوجيه والدعم الأولي.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark.copy(alpha = 0.75f)
            )
          }

          Button(
            onClick = onNavigateToSupport,
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("content_need_to_talk_button")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.SupportAgent,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("محتاج أتكلم", fontSize = 13.sp)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Rules / Disclaimer Notice (Page 11)
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = SageGreenPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "تنبيه توعوي: هذا المحتوى مقدم لأغراض التوعية والدعم الذاتي، ولا يقدم تشخيصاً فردياً، أو وصفاً لعلاج، أو بديلاً عن الاستشارة الطبية المتخصصة.",
            style = MaterialTheme.typography.labelSmall,
            color = TextDark.copy(alpha = 0.7f),
            lineHeight = 18.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}
