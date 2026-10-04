package com.example.ui.screens.journal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PersonalNote
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GratitudeJournalScreen(
  notes: List<PersonalNote>,
  onAddNote: (title: String, content: String, tag: String, moodEmoji: String) -> Unit,
  onDeleteNote: (id: Long) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isWritingNew by remember { mutableStateOf(false) }
  var titleInput by remember { mutableStateOf("") }
  var contentInput by remember { mutableStateOf("") }
  var selectedTag by remember { mutableStateOf("امتنان") }
  var selectedMoodEmoji by remember { mutableStateOf("🌸") }
  var filterTag by remember { mutableStateOf("الكل") }
  var searchQuery by remember { mutableStateOf("") }

  val tags = listOf("امتنان", "عائلة", "عمل", "صحة", "أمل", "سكينة")
  val moodEmojis = listOf("🌸", "☀️", "🌿", "🌊", "☕", "✨", "🕊️")

  val filteredNotes = notes.filter { note ->
    val matchesTag = filterTag == "الكل" || note.tag == filterTag
    val matchesSearch = searchQuery.isBlank() ||
      note.title.contains(searchQuery, ignoreCase = true) ||
      note.content.contains(searchQuery, ignoreCase = true)
    matchesTag && matchesSearch
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "مفكرة الامتنان والمشاعر ✨",
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            fontSize = 17.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("gratitude_back_button")
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
            onClick = { isWritingNew = !isWritingNew },
            modifier = Modifier.testTag("toggle_new_gratitude_button")
          ) {
            Icon(
              imageVector = if (isWritingNew) Icons.Default.EditNote else Icons.Default.Add,
              contentDescription = "كتابة سطر امتنان جديد",
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
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Daily prompt banner
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.35f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = SageGreenPrimary,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "سؤال الامتنان لليوم ✨",
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                fontSize = 14.sp
              )
              Text(
                text = "ما هو الشيء البسيط غير المتوقع الذي أدخل طمأنينة على قلبك اليوم؟",
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.8f)
              )
            }
          }
        }
      }

      // Compose form
      item {
        AnimatedVisibility(visible = isWritingNew) {
          Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(18.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Text(
                text = "تدوين خاطرة أو امتنان جديد ✍️",
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                fontSize = 15.sp
              )

              // Mood selector
              Text(
                text = "اختر رمز مشاعرك الآن:",
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.65f)
              )
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                moodEmojis.forEach { emoji ->
                  val isSelected = selectedMoodEmoji == emoji
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.4f))
                      .clickable { selectedMoodEmoji = emoji },
                    contentAlignment = Alignment.Center
                  ) {
                    Text(text = emoji, fontSize = 18.sp)
                  }
                }
              }

              // Tag selector
              Text(
                text = "تصنيف الخاطرة:",
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.65f)
              )
              LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(tags) { tag ->
                  FilterChip(
                    selected = selectedTag == tag,
                    onClick = { selectedTag = tag },
                    label = { Text(tag, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                      selectedContainerColor = DarkGreen,
                      selectedLabelColor = Color.White,
                      containerColor = WarmBeige.copy(alpha = 0.3f),
                      labelColor = TextDark
                    )
                  )
                }
              }

              OutlinedTextField(
                value = titleInput,
                onValueChange = { titleInput = it },
                label = { Text("عنوان أو ومضة سريعة") },
                placeholder = { Text("مثال: فنجان قهوة الصباح مع نسمة هواء") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = DarkGreen,
                  cursorColor = DarkGreen
                )
              )

              OutlinedTextField(
                value = contentInput,
                onValueChange = { contentInput = it },
                label = { Text("ما الذي شعرت به أو تشعر بالامتنان لوجوده؟") },
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = DarkGreen,
                  cursorColor = DarkGreen
                )
              )

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
              ) {
                Button(
                  onClick = { isWritingNew = false },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = TextDark.copy(alpha = 0.6f)
                  )
                ) {
                  Text("إلغاء")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                  onClick = {
                    if (contentInput.isNotBlank()) {
                      val finalTitle = titleInput.ifBlank { "امتنان $selectedTag" }
                      onAddNote(finalTitle, contentInput.trim(), selectedTag, selectedMoodEmoji)
                      titleInput = ""
                      contentInput = ""
                      isWritingNew = false
                    }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                  shape = RoundedCornerShape(12.dp),
                  enabled = contentInput.isNotBlank()
                ) {
                  Text("حفظ في مفكرتي ✨", color = Color.White, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      // Filter chips and search
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("ابحث في ذكرياتك وامناناتك...") },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TextDark.copy(alpha = 0.5f)
              )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color.White,
              unfocusedContainerColor = Color.White,
              focusedBorderColor = DarkGreen,
              unfocusedBorderColor = Color.Transparent
            )
          )

          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
              FilterChip(
                selected = filterTag == "الكل",
                onClick = { filterTag = "الكل" },
                label = { Text("الكل", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = DarkGreen,
                  selectedLabelColor = Color.White,
                  containerColor = Color.White,
                  labelColor = TextDark
                )
              )
            }
            items(tags) { tag ->
              FilterChip(
                selected = filterTag == tag,
                onClick = { filterTag = tag },
                label = { Text(tag, fontSize = 12.sp) },
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

      // Notes list
      if (filteredNotes.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(text = "🌿", fontSize = 42.sp)
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "مفكرتك تنتظر أول سطر امتنان",
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                fontSize = 16.sp
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "التدريب على ملاحظة النعم البسيطة يرفع هرمونات السعادة ويقلل إجهاد الدماغ.",
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.65f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
              Spacer(modifier = Modifier.height(16.dp))
              Button(
                onClick = { isWritingNew = true },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("+ اكتب أول لحظة امتنان", color = Color.White)
              }
            }
          }
        }
      } else {
        items(filteredNotes, key = { it.id }) { note ->
          val dateStr = remember(note.timestamp) {
            SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault()).format(Date(note.timestamp))
          }

          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = note.moodEmoji, fontSize = 20.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    fontSize = 15.sp
                  )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(SoftMint.copy(alpha = 0.5f))
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = note.tag,
                      fontSize = 11.sp,
                      color = DarkGreen,
                      fontWeight = FontWeight.SemiBold
                    )
                  }

                  IconButton(
                    onClick = { onDeleteNote(note.id) },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.DeleteOutline,
                      contentDescription = "حذف الخاطرة",
                      tint = TextDark.copy(alpha = 0.4f),
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark,
                lineHeight = 22.sp
              )

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = dateStr,
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.45f),
                fontSize = 10.sp
              )
            }
          }
        }
      }
    }
  }
}
