package com.example.ui.screens.library

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentCreatorScreen(
  onPublish: (
    title: String,
    category: String,
    description: String,
    body: String,
    duration: String,
    author: String
  ) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("مقالات") }
  var description by remember { mutableStateOf("") }
  var body by remember { mutableStateOf("") }
  var duration by remember { mutableStateOf("٥ دقائق قراءة") }
  var author by remember { mutableStateOf("أخصائي نفسي / فريق نسمة") }
  var isPublishedSuccess by remember { mutableStateOf(false) }

  val categories = listOf("مقالات", "ورش تطبيقية", "خطط إنقاذ", "كتيبات وإرشادات", "خفة ولعب")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "إضافة ونشر محتوى جديد ✍️",
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            fontSize = 17.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("creator_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "رجوع",
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
      contentPadding = PaddingValues(18.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      if (isPublishedSuccess) {
        item {
          Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(64.dp)
                  .clip(CircleShape)
                  .background(SoftMint.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = DarkGreen,
                  modifier = Modifier.size(36.dp)
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = "تم نشر المحتوى بنجاح في المكتبة! 🎉",
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                fontSize = 18.sp
              )

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = "مقالك الآن متاح لجميع مستخدمي التطبيق ومحفوظ محلياً في قاعدة البيانات.",
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )

              Spacer(modifier = Modifier.height(20.dp))

              Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text("الانتقال إلى المكتبة لعرضه", color = Color.White, fontWeight = FontWeight.Bold)
              }

              Spacer(modifier = Modifier.height(8.dp))

              Button(
                onClick = {
                  title = ""
                  description = ""
                  body = ""
                  isPublishedSuccess = false
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = WarmBeige.copy(alpha = 0.5f),
                  contentColor = DarkGreen
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text("كتابة مقال آخر", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      } else {
        // Explanatory Banner
        item {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SoftMint.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                tint = DarkGreen,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "يمكنك كتابة مقال تثقيفي، ورشة عمل، أو خطة إنقاذ سريعة ليتم حفظها مباشرة في مكتبة نسمة.",
                style = MaterialTheme.typography.bodySmall,
                color = DarkGreen,
                lineHeight = 20.sp
              )
            }
          }
        }

        // Form Card
        item {
          Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(18.dp),
              verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              Text(
                text = "بيانات المقال أو النشاط",
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                fontSize = 15.sp
              )

              // Category chips
              Text(
                text = "اختر القسم المناسب:",
                style = MaterialTheme.typography.bodySmall,
                color = TextDark.copy(alpha = 0.7f)
              )

              LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(categories) { cat ->
                  FilterChip(
                    selected = category == cat,
                    onClick = { category = cat },
                    label = { Text(cat, fontSize = 12.sp) },
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
                value = title,
                onValueChange = { title = it },
                label = { Text("عنوان المقال أو النشاط *") },
                placeholder = { Text("مثال: ٥ خطوات للتعامل مع نوبة القلق المفاجئة") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = DarkGreen,
                  cursorColor = DarkGreen
                )
              )

              OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("نبذة مختصرة أو الفكرة الأساسية *") },
                placeholder = { Text("دليل تطبيقي سريع لاستعادة الهدوء وتخفيف التوتر في دقائق...") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = DarkGreen,
                  cursorColor = DarkGreen
                )
              )

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                OutlinedTextField(
                  value = duration,
                  onValueChange = { duration = it },
                  label = { Text("مدة القراءة/التطبيق") },
                  singleLine = true,
                  modifier = Modifier.weight(1f),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    cursorColor = DarkGreen
                  )
                )

                OutlinedTextField(
                  value = author,
                  onValueChange = { author = it },
                  label = { Text("الكاتب أو المصدر") },
                  singleLine = true,
                  modifier = Modifier.weight(1f),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    cursorColor = DarkGreen
                  )
                )
              }

              OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("نص المحتوى الكامل والتفاصيل *") },
                placeholder = { Text("اكتب هنا محاور الدليل، الخطوات العملية، النصائح، وتمارين التطبيق...") },
                minLines = 6,
                maxLines = 14,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = DarkGreen,
                  cursorColor = DarkGreen
                )
              )

              Spacer(modifier = Modifier.height(6.dp))

              Button(
                onClick = {
                  if (title.isNotBlank() && body.isNotBlank()) {
                    onPublish(
                      title.trim(),
                      category,
                      description.ifBlank { title.trim() },
                      body.trim(),
                      duration.ifBlank { "٥ دقائق قراءة" },
                      author.ifBlank { "فريق نسمة الحياة" }
                    )
                    isPublishedSuccess = true
                  }
                },
                enabled = title.isNotBlank() && body.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("publish_content_button")
              ) {
                Text(
                  text = "نشر المقال في المكتبة الآن ✨",
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontSize = 15.sp
                )
              }
            }
          }
        }
      }
    }
  }
}
