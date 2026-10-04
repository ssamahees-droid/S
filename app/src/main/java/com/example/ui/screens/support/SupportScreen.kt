package com.example.ui.screens.support

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SupportRequest
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.StatusDone
import com.example.ui.theme.StatusNew
import com.example.ui.theme.StatusReview
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
  requests: List<SupportRequest>,
  onSubmitRequest: (String, String, (SupportRequest) -> Unit) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: طلب جديد, 1: طلباتي السابقة
  val selectedNeeds = remember { mutableStateListOf<String>() }
  var messageText by remember { mutableStateOf("") }
  var submittedRequest by remember { mutableStateOf<SupportRequest?>(null) }
  var isSubmitting by remember { mutableStateOf(false) }

  val needOptions = remember {
    listOf(
      "حد يسمعني",
      "محتاج توجيه",
      "محتاج أوصل لمتخصص",
      "مش عارف أعمل إيه"
    )
  }

  // Word count calculation (Page 15: حد أقصى 500 كلمة)
  val wordCount = remember(messageText) {
    if (messageText.isBlank()) 0 else messageText.trim().split("\\s+".toRegex()).size
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    TopAppBar(
      title = {
        Text(
          text = "محتاج أتكلم؟",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("support_back_button")
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

    // Tabs
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = CreamBackground,
      contentColor = DarkGreen
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = { Text("طلب دعم جديد", fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = { Text("طلباتي (${requests.size})", fontWeight = FontWeight.Bold) }
      )
    }

    if (selectedTab == 0) {
      // New Request Form (Page 14-16)
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
      ) {
        // Section 9: شاشة تنبيه السلامة (🆘 محتاج مساعدة؟)
        item {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 18.dp)
              .testTag("support_safety_notice_card")
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Text(
                text = "🆘 محتاج مساعدة؟",
                style = MaterialTheme.typography.titleLarge,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "نسمة حياة تقدم محتوى تثقيفيًا وتمارين للمساعدة على فهم النفس والتعامل مع بعض أوقات الضغط. لكنها ليست بديلًا عن الطبيب أو المعالج النفسي.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark,
                lineHeight = 23.sp
              )
              Spacer(modifier = Modifier.height(10.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(WarmBeige.copy(alpha = 0.55f))
                  .padding(14.dp)
              ) {
                Text(
                  text = "إذا كنت في خطر فوري، أو لديك أفكار بإيذاء نفسك أو شخص آخر، لا تعتمد على التطبيق وحده. اطلب مساعدة فورية من خدمات الطوارئ المحلية أو شخص موثوق، ولا تبقَ وحدك.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = Color(0xFF7B2D26),
                  fontWeight = FontWeight.Bold,
                  lineHeight = 23.sp
                )
              }
            }
          }
        }

        if (submittedRequest != null) {
          // Success State
          item {
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Box(
                  modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(SageGreenPrimary.copy(alpha = 0.25f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = DarkGreen,
                    modifier = Modifier.size(36.dp)
                  )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                  text = "تم إرسال طلبك بنجاح 🤍",
                  style = MaterialTheme.typography.titleLarge,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = "رقم الطلب: ${submittedRequest?.requestNumber}",
                  style = MaterialTheme.typography.headlineSmall,
                  color = DarkGreen,
                  fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                  text = "حالة الطلب الحالية: «جديد». سيتم التعامل مع طلبك وفق آلية الدعم المتاحة وسياسة الخصوصية الخاصة بنسمة الحياة.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = TextDark,
                  lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                  onClick = {
                    submittedRequest = null
                    messageText = ""
                    selectedNeeds.clear()
                    selectedTab = 1
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text("متابعة طلباتي السابقة")
                }
              }
            }
          }
        } else {
          item {
            Text(
              text = "مش لازم تعرف تقول كل حاجة.",
              style = MaterialTheme.typography.headlineMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "ابدأ بس من اللي حاسس بيه دلوقتي، وسنكون بجانبك خطوة بخطوة.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // What do you need?
            Text(
              text = "ماذا تحتاج؟ (ممكن تختار أكثر من حاجة)",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              needOptions.forEach { need ->
                val isSelected = selectedNeeds.contains(need)
                FilterChip(
                  selected = isSelected,
                  onClick = {
                    if (isSelected) selectedNeeds.remove(need) else selectedNeeds.add(need)
                  },
                  label = {
                    Text(
                      text = need,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DarkGreen,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = TextDark
                  ),
                  border = FilterChipDefaults.filterChipBorder(
                    borderColor = if (isSelected) DarkGreen else WarmBeige,
                    selectedBorderColor = DarkGreen,
                    enabled = true,
                    selected = isSelected
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.fillMaxWidth()
                )
              }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Message Field
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "احكيلنا براحتك...",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "$wordCount / 500 كلمة",
                style = MaterialTheme.typography.labelSmall,
                color = if (wordCount > 500) Color.Red else TextDark.copy(alpha = 0.6f)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = messageText,
              onValueChange = { messageText = it },
              placeholder = { Text("اكتب ما يجول في خاطرك ومشاعرك هنا...", fontSize = 14.sp) },
              modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .testTag("support_message_input"),
              shape = RoundedCornerShape(16.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkGreen,
                unfocusedBorderColor = WarmBeige,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
              )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mandatory Privacy statement (Page 16)
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Lock,
                  contentDescription = null,
                  tint = DarkGreen,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = "«سيتم التعامل مع طلبك وفق آلية الدعم المتاحة حالياً وسياسة الخصوصية الخاصة بنسمة الحياة».",
                  style = MaterialTheme.typography.labelMedium,
                  color = TextDark,
                  lineHeight = 18.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
              onClick = {
                if (messageText.isNotBlank() && wordCount <= 500) {
                  isSubmitting = true
                  val type = if (selectedNeeds.isEmpty()) "طلب عام" else selectedNeeds.joinToString("، ")
                  onSubmitRequest(type, messageText) { created ->
                    isSubmitting = false
                    submittedRequest = created
                  }
                }
              },
              enabled = messageText.isNotBlank() && wordCount <= 500 && !isSubmitting,
              colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("submit_support_request_button")
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (isSubmitting) "جاري الإرسال..." else "إرسال طلب المساعدة",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    } else {
      // Past requests list
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        if (requests.isEmpty()) {
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(text = "🕊️", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "لا توجد طلبات سابقة",
                  style = MaterialTheme.typography.titleMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "عند إرسال طلب مساعدة، ستتمكن من متابعة حالته وردود الفريق هنا.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = TextDark.copy(alpha = 0.7f),
                  textAlign = TextAlign.Center
                )
              }
            }
          }
        } else {
          items(requests, key = { it.id }) { req ->
            val dateStr = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault()).format(Date(req.createdAt))
            val statusColor = when (req.status) {
              "جديد" -> StatusNew
              "تم الاستلام", "قيد المراجعة" -> StatusReview
              "تم التواصل", "تمت الإحالة" -> StatusDone
              else -> TextDark.copy(alpha = 0.5f)
            }

            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = req.requestNumber,
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )

                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(statusColor.copy(alpha = 0.18f))
                      .padding(horizontal = 10.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = req.status,
                      style = MaterialTheme.typography.labelSmall,
                      color = statusColor,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = "النوع: ${req.requestType}",
                  style = MaterialTheme.typography.labelMedium,
                  color = SageGreenPrimary,
                  fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = req.message,
                  style = MaterialTheme.typography.bodyMedium,
                  color = TextDark,
                  lineHeight = 22.sp
                )

                if (!req.adminReply.isNullOrBlank()) {
                  Spacer(modifier = Modifier.height(10.dp))
                  Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                      Text(
                        text = "رد فريق نسمة الحياة:",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold
                      )
                      Spacer(modifier = Modifier.height(2.dp))
                      Text(
                        text = req.adminReply,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDark
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = dateStr,
                  style = MaterialTheme.typography.labelSmall,
                  color = TextDark.copy(alpha = 0.5f)
                )
              }
            }
          }
        }
      }
    }
  }
}
