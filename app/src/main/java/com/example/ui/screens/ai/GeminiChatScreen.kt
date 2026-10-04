package com.example.ui.screens.ai

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.core.content.ContextCompat
import com.example.data.api.ChatMessage
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.MoodDifficult
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import com.example.util.AudioRecorderHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiChatScreen(
  messages: List<ChatMessage>,
  isLoading: Boolean,
  isTranscribing: Boolean,
  audioRecorder: AudioRecorderHelper,
  onSendMessage: (String, Boolean) -> Unit,
  onTranscribeAudio: (ByteArray, (String) -> Unit) -> Unit,
  onClearChat: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var inputText by remember { mutableStateOf("") }
  var useSearchGrounding by remember { mutableStateOf(true) }
  var isRecordingVoice by remember { mutableStateOf(false) }

  val listState = rememberLazyListState()

  // Scroll to bottom on new message
  LaunchedEffect(messages.size, isLoading) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  // Permission launcher for microphone recording
  val recordAudioLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    if (isGranted) {
      val started = audioRecorder.startRecording()
      if (started) {
        isRecordingVoice = true
        Toast.makeText(context, "بدأ التسجيل الصوتي... انقر مجدداً للإرسال", Toast.LENGTH_SHORT).show()
      } else {
        Toast.makeText(context, "تعذر بدء الميكروفون", Toast.LENGTH_SHORT).show()
      }
    } else {
      Toast.makeText(context, "يلزم إذن الميكروفون لتحويل الصوت إلى نص", Toast.LENGTH_SHORT).show()
    }
  }

  val starterPrompts = listOf(
    "كيف أهدئ تفكيري المتسارع قبل النوم؟",
    "أشعر بضغط العمل والإرهاق اليوم",
    "اقترح عليّ تمرين تنفس مريح",
    "ما هي أحدث دراسات اليقظة الذهنية والامتنان؟"
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
              imageVector = Icons.Default.Spa,
              contentDescription = null,
              tint = CreamBackground,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "رفيق نسمة الحياة",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "مدعوم بنموذج Gemini 3.5 Flash",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.6f)
            )
          }
        }
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("chat_back_button")
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
          onClick = onClearChat,
          modifier = Modifier.testTag("chat_clear_button")
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "مسح المحادثة",
            tint = TextDark.copy(alpha = 0.5f)
          )
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
    )

    // Search Grounding Toggle Bar
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.45f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Language,
            contentDescription = null,
            tint = if (useSearchGrounding) DarkGreen else TextDark.copy(alpha = 0.5f),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "بحث الويب الموثوق (Google Search Grounding)",
            style = MaterialTheme.typography.labelSmall,
            color = DarkGreen,
            fontWeight = FontWeight.Medium
          )
        }

        FilterChip(
          selected = useSearchGrounding,
          onClick = { useSearchGrounding = !useSearchGrounding },
          label = {
            Text(
              text = if (useSearchGrounding) "مفعل 🌐" else "معطل",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = DarkGreen,
            selectedLabelColor = Color.White
          ),
          modifier = Modifier.testTag("toggle_search_grounding")
        )
      }
    }

    // Messages Thread
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(messages, key = { it.id }) { msg ->
        val isUser = msg.role == "user"

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (isUser) Arrangement.Start else Arrangement.End
        ) {
          if (!isUser) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(DarkGreen)
                .align(Alignment.Top),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
          }

          Card(
            shape = RoundedCornerShape(
              topStart = 18.dp,
              topEnd = 18.dp,
              bottomStart = if (isUser) 4.dp else 18.dp,
              bottomEnd = if (isUser) 18.dp else 4.dp
            ),
            colors = CardDefaults.cardColors(
              containerColor = if (isUser) DarkGreen else Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.widthIn(max = 280.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = msg.text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isUser) Color.White else TextDark,
                lineHeight = 22.sp
              )

              if (!isUser && msg.searchSources.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(WarmBeige.copy(alpha = 0.5f))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = DarkGreen,
                    modifier = Modifier.size(12.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "مصادر مؤكدة من بحث Google",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }

      if (isLoading) {
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              modifier = Modifier.padding(4.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                CircularProgressIndicator(
                  modifier = Modifier.size(16.dp),
                  strokeWidth = 2.dp,
                  color = DarkGreen
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = "رفيقك يفكر ويكتب بهدوء...",
                  style = MaterialTheme.typography.labelSmall,
                  color = TextDark.copy(alpha = 0.7f)
                )
              }
            }
          }
        }
      }

      if (isTranscribing) {
        item {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WarmBeige),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = DarkGreen
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "جاري تحويل التسجيل الصوتي إلى نص عبر Gemini 3.5 Transcribe...",
                style = MaterialTheme.typography.labelSmall,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // Starter suggestions if history is short
    if (messages.size <= 2) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        starterPrompts.forEach { prompt ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(Color.White)
              .border(1.dp, WarmBeige, RoundedCornerShape(12.dp))
              .clickable { onSendMessage(prompt, useSearchGrounding) }
              .padding(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Text(
              text = prompt,
              style = MaterialTheme.typography.labelSmall,
              color = DarkGreen
            )
          }
        }
      }
    }

    // Input Bar with Voice Transcription
    Card(
      shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Voice Record Button (gemini-3.5-transcribe)
        IconButton(
          onClick = {
            if (isRecordingVoice) {
              // Stop recording and transcribe
              val audioBytes = audioRecorder.stopRecording()
              isRecordingVoice = false
              if (audioBytes != null && audioBytes.isNotEmpty()) {
                onTranscribeAudio(audioBytes) { transcribed ->
                  if (transcribed.isNotBlank()) {
                    inputText = if (inputText.isBlank()) transcribed else "$inputText $transcribed"
                  }
                }
              } else {
                Toast.makeText(context, "لم يتم تسجيل أي صوت", Toast.LENGTH_SHORT).show()
              }
            } else {
              // Check audio permission
              val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
              ) == PackageManager.PERMISSION_GRANTED

              if (hasPermission) {
                val started = audioRecorder.startRecording()
                if (started) {
                  isRecordingVoice = true
                  Toast.makeText(context, "جاري التسجيل... اضغط مجدداً للإرسال والتحويل لنص", Toast.LENGTH_SHORT).show()
                }
              } else {
                recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
              }
            }
          },
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(if (isRecordingVoice) MoodDifficult else WarmBeige.copy(alpha = 0.5f))
            .testTag("chat_voice_mic_button")
        ) {
          Icon(
            imageVector = if (isRecordingVoice) Icons.Default.MicOff else Icons.Default.Mic,
            contentDescription = "تسجيل صوتي",
            tint = if (isRecordingVoice) Color.White else DarkGreen
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
          value = inputText,
          onValueChange = { inputText = it },
          placeholder = { Text("شاركني بما تشعر به...", fontSize = 14.sp) },
          modifier = Modifier
            .weight(1f)
            .testTag("chat_message_input"),
          maxLines = 3,
          shape = RoundedCornerShape(20.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = WarmBeige,
            focusedContainerColor = CreamBackground,
            unfocusedContainerColor = CreamBackground
          )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Send Button
        IconButton(
          onClick = {
            if (inputText.isNotBlank() && !isLoading) {
              val text = inputText.trim()
              inputText = ""
              onSendMessage(text, useSearchGrounding)
            }
          },
          enabled = inputText.isNotBlank() && !isLoading,
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(if (inputText.isNotBlank() && !isLoading) DarkGreen else Color.LightGray)
            .testTag("chat_send_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "إرسال",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
