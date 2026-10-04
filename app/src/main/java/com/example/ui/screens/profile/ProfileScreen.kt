package com.example.ui.screens.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserPreference
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.MoodDifficult
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@Composable
fun ProfileScreen(
  userPref: UserPreference?,
  onNavigateToJourney: () -> Unit,
  onNavigateToSupport: () -> Unit,
  onNavigateToSaved: () -> Unit,
  onNavigateToNotifications: () -> Unit,
  onNavigateToPrivacy: () -> Unit,
  onNavigateToAbout: () -> Unit,
  onNavigateToAdmin: () -> Unit,
  onNavigateToChat: () -> Unit,
  onNavigateToImageStudio: () -> Unit,
  onExportData: () -> Unit,
  onClearAllData: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showClearDataDialog by remember { mutableStateOf(false) }
  var showHelpDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    // Header
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
      Text(
        text = "حسابي",
        style = MaterialTheme.typography.headlineMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "إعداداتك وخصوصيتك في مكان آمن وواضح",
        style = MaterialTheme.typography.bodyMedium,
        color = TextDark.copy(alpha = 0.75f)
      )
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      contentPadding = PaddingValues(bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // User Profile Card
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(DarkGreen),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
              )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
              Text(
                text = "صديق نسمة الحياة 🌱",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "الدور الحالي: ${userPref?.userRole ?: "مستخدم"}",
                style = MaterialTheme.typography.labelMedium,
                color = SageGreenPrimary,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = "حساب محلي آمن لا يشارك أي بيانات خاصة",
                style = MaterialTheme.typography.labelSmall,
                color = TextDark.copy(alpha = 0.55f)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
      }

      // Menu Rows (Page 20)
      item {
        ProfileMenuItem(
          title = "رحلتي وملاحظاتي",
          subtitle = "سجل الأيام والتقييمات السابقة",
          icon = Icons.Default.Spa,
          testTag = "profile_menu_journey",
          onClick = onNavigateToJourney
        )
      }

      item {
        ProfileMenuItem(
          title = "طلبات الدعم",
          subtitle = "متابعة الرسائل وحالة التواصل مع الفريق",
          icon = Icons.Default.SupportAgent,
          testTag = "profile_menu_support",
          onClick = onNavigateToSupport
        )
      }

      item {
        ProfileMenuItem(
          title = "المحفوظات",
          subtitle = "المقالات والتمارين المفضلة لديك",
          icon = Icons.Default.Bookmark,
          testTag = "profile_menu_saved",
          onClick = onNavigateToSaved
        )
      }

      item {
        ProfileMenuItem(
          title = "الإشعارات والتذكيرات",
          subtitle = "تذكيرات لطيفة هادئة غير ضاغطة",
          icon = Icons.Default.Notifications,
          testTag = "profile_menu_notifications",
          onClick = onNavigateToNotifications
        )
      }

      item {
        ProfileMenuItem(
          title = "الخصوصية وحماية البيانات",
          subtitle = "سياسة الخصوصية وقانون حماية البيانات المصري 151",
          icon = Icons.Default.Security,
          testTag = "profile_menu_privacy",
          onClick = onNavigateToPrivacy
        )
      }

      item {
        ProfileMenuItem(
          title = "عن نسمة الحياة",
          subtitle = "رؤيتنا ورسالتنا الإنسانية في التوعية النفسية",
          icon = Icons.Default.Info,
          testTag = "profile_menu_about",
          onClick = onNavigateToAbout
        )
      }

      item {
        ProfileMenuItem(
          title = "المساعدة والأسئلة الشائعة",
          subtitle = "إجابات واضحة عن التطبيق وأدواته",
          icon = Icons.Default.HelpOutline,
          testTag = "profile_menu_help",
          onClick = { showHelpDialog = true }
        )
      }

      item {
        ProfileMenuItem(
          title = "رفيق نسمة الحياة الذكي (AI Chat)",
          subtitle = "محادثة هادئة، بحث Google، وإدخال صوتي",
          icon = Icons.Default.Spa,
          iconColor = SageGreenPrimary,
          testTag = "profile_menu_ai_chat",
          onClick = onNavigateToChat
        )
      }

      item {
        ProfileMenuItem(
          title = "استوديو السكينة البصرية (AI Art)",
          subtitle = "توليد لوحات تأملية بـ Gemini 3.1 Flash Image",
          icon = Icons.Default.AutoAwesome,
          iconColor = SageGreenPrimary,
          testTag = "profile_menu_ai_image",
          onClick = onNavigateToImageStudio
        )
      }

      // Admin Dashboard (Page 20, 26-27)
      item {
        ProfileMenuItem(
          title = "لوحة التحكم وإدارة المحتوى (Admin)",
          subtitle = "إدارة المقالات، مراجعة طلبات الدعم، والصلاحيات",
          icon = Icons.Default.AdminPanelSettings,
          iconColor = SageGreenPrimary,
          testTag = "profile_menu_admin",
          onClick = onNavigateToAdmin
        )
      }

      // Data Export (Backup/export mechanism)
      item {
        ProfileMenuItem(
          title = "تصدير ونسخ بياناتي احتياطياً",
          subtitle = "تصدير تسجيلاتك ونتائجك بملف آمن وفق حق نقل البيانات",
          icon = Icons.Default.Share,
          iconColor = SageGreenPrimary,
          testTag = "profile_menu_export",
          onClick = onExportData
        )
      }

      // Privacy Data Erasure (Page 28-29)
      item {
        Spacer(modifier = Modifier.height(10.dp))

        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, MoodDifficult.copy(alpha = 0.5f)),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showClearDataDialog = true }
            .testTag("profile_clear_data_button")
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MoodDifficult.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.DeleteSweep,
                contentDescription = null,
                tint = MoodDifficult,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "حذف جميع بياناتي المسجلة",
                style = MaterialTheme.typography.bodyLarge,
                color = MoodDifficult,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "حق المحو وفقاً لقانون حماية البيانات وسياسة الخصوصية",
                style = MaterialTheme.typography.labelSmall,
                color = TextDark.copy(alpha = 0.6f)
              )
            }
          }
        }
      }
    }
  }

  // Clear data confirmation dialog
  if (showClearDataDialog) {
    AlertDialog(
      onDismissRequest = { showClearDataDialog = false },
      title = { Text("تأكيد مسح البيانات", color = MoodDifficult, fontWeight = FontWeight.Bold) },
      text = {
        Text(
          "هل أنت متأكد من رغبتك في حذف جميع تسجيلات المشاعر والخواطر ونتائج التقييمات المخزنة محلياً على هذا الجهاز؟ هذا الإجراء لا يمكن التراجع عنه.",
          style = MaterialTheme.typography.bodyMedium,
          color = TextDark
        )
      },
      confirmButton = {
        Button(
          onClick = {
            onClearAllData()
            showClearDataDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MoodDifficult)
        ) {
          Text("نعم، احذف بياناتي")
        }
      },
      dismissButton = {
        TextButton(onClick = { showClearDataDialog = false }) {
          Text("إلغاء", color = TextDark)
        }
      }
    )
  }

  // FAQ Dialog
  if (showHelpDialog) {
    AlertDialog(
      onDismissRequest = { showHelpDialog = false },
      title = { Text("الأسئلة الشائعة ❓", color = DarkGreen, fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            "س: هل تطبيق نسمة الحياة بديل عن الطبيب النفسي؟",
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            fontSize = 13.sp
          )
          Text(
            "ج: إطلاقاً؛ التطبيق مساحة توعوية ودعم ذاتي، ولا يقوم بتشخيص طبي أو وصف أدوية.",
            fontSize = 12.sp,
            color = TextDark
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "س: أين يتم حفظ بيانات تقييمي وملاحظاتي؟",
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            fontSize = 13.sp
          )
          Text(
            "ج: تحفظ محلياً على جهازك في قاعدة بيانات مشفرة، ولا يتم بيعها أو مشاركتها مع أي جهة.",
            fontSize = 12.sp,
            color = TextDark
          )
        }
      },
      confirmButton = {
        Button(
          onClick = { showHelpDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
        ) {
          Text("فهمت")
        }
      }
    )
  }
}

@Composable
fun ProfileMenuItem(
  title: String,
  subtitle: String,
  icon: ImageVector,
  testTag: String,
  iconColor: Color = DarkGreen,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag(testTag)
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(WarmBeige.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconColor,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.labelSmall,
          color = TextDark.copy(alpha = 0.65f)
        )
      }

      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
        tint = TextDark.copy(alpha = 0.4f),
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
