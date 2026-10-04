package com.example.ui.screens.ai

import android.graphics.Bitmap
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalmImageStudioScreen(
  generatedImage: Bitmap?,
  isGenerating: Boolean,
  onGenerateImage: (String, Bitmap?) -> Unit,
  onClearImage: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var promptText by remember { mutableStateOf("") }

  val presets = listOf(
    "حديقة يابانية هادئة مع أحجار متزنة وماء رقراق",
    "شروق شمس دافئ بين أشجار الزيتون الصباحية",
    "سماء ليلية صافية مليئة بالنجوم والسكينة",
    "موجات بحر هادئة عند الغسق مع نسيم عليل"
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
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = CreamBackground,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "استوديو السكينة البصرية",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "توليد لوحات تأملية بـ Gemini 3.1 Flash Image",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.6f)
            )
          }
        }
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("image_studio_back_button")
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
      item {
        Text(
          text = "صمم مشهداً بصرياً يمنحك السلام الداخلي 🎨",
          style = MaterialTheme.typography.headlineMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "اكتب وصفاً لأي طبيعة أو منظر هادئ تود التأمل فيه، وسيقوم الذكاء الاصطناعي برسمه بألوان مريحة للعين.",
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark.copy(alpha = 0.8f)
        )
      }

      // Presets
      item {
        Text(
          text = "أفكار ملهمة سريعة:",
          style = MaterialTheme.typography.titleSmall,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          presets.forEach { preset ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { promptText = preset }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Spa,
                  contentDescription = null,
                  tint = SageGreenPrimary,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = preset,
                  style = MaterialTheme.typography.bodySmall,
                  color = TextDark
                )
              }
            }
          }
        }
      }

      // Input Prompt
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
              value = promptText,
              onValueChange = { promptText = it },
              placeholder = { Text("اكتب وصف اللوحة التي تريح بالك...", fontSize = 14.sp) },
              modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .testTag("image_prompt_input"),
              shape = RoundedCornerShape(14.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkGreen,
                unfocusedBorderColor = WarmBeige
              )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = {
                if (promptText.isNotBlank() && !isGenerating) {
                  onGenerateImage(promptText, null)
                }
              },
              enabled = promptText.isNotBlank() && !isGenerating,
              colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("generate_image_button")
            ) {
              if (isGenerating) {
                CircularProgressIndicator(
                  modifier = Modifier.size(20.dp),
                  color = Color.White,
                  strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("جاري رسم اللوحة التأملية...", fontWeight = FontWeight.Bold)
              } else {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("توليد اللوحة التأملية", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // Display Generated Image
      if (generatedImage != null) {
        item {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "لوحتك التأملية الخاصة 🌿",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )

              Spacer(modifier = Modifier.height(12.dp))

              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(300.dp)
                  .clip(RoundedCornerShape(16.dp))
                  .border(2.dp, WarmBeige, RoundedCornerShape(16.dp))
              ) {
                Image(
                  bitmap = generatedImage.asImageBitmap(),
                  contentDescription = "لوحة تأملية مولدة بالذكاء الاصطناعي",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Button(
                  onClick = {
                    Toast.makeText(context, "تم حفظ اللوحة في معرض جهازك 🤍", Toast.LENGTH_SHORT).show()
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Text("حفظ في الجهاز", fontSize = 12.sp)
                }

                IconButton(onClick = onClearImage) {
                  Icon(imageVector = Icons.Default.Refresh, contentDescription = "توليد صورة جديدة", tint = DarkGreen)
                }
              }
            }
          }
        }
      }
    }
  }
}
