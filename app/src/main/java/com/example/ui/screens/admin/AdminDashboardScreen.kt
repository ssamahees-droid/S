package com.example.ui.screens.admin

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.ContentItem
import com.example.data.model.SupportRequest
import com.example.data.model.UserPreference
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.MoodDifficult
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.StatusDone
import com.example.ui.theme.StatusNew
import com.example.ui.theme.StatusReview
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
  userPref: UserPreference?,
  allContent: List<ContentItem>,
  allRequests: List<SupportRequest>,
  onUpdateRole: (String) -> Unit,
  onSaveContent: (ContentItem, () -> Unit) -> Unit,
  onDeleteContent: (ContentItem) -> Unit,
  onUpdateRequestStatus: (Long, String, String?) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: نظرة عامة, 1: إدارة المحتوى, 2: طلبات الدعم
  val roles = listOf("Super Admin", "Content Manager", "Support Supervisor", "Support Member", "Reviewer / Specialist")
  var activeRole by remember { mutableStateOf(userPref?.userRole.takeIf { it in roles } ?: "Super Admin") }
  var roleDropdownExpanded by remember { mutableStateOf(false) }

  // Content Dialog State
  var showContentDialog by remember { mutableStateOf(false) }
  var editingContent by remember { mutableStateOf<ContentItem?>(null) }
  var contentTitle by remember { mutableStateOf("") }
  var contentDescription by remember { mutableStateOf("") }
  var contentBody by remember { mutableStateOf("") }
  var contentCategory by remember { mutableStateOf("أعرف نفسي") }
  var contentStatus by remember { mutableStateOf("Published") }

  // Support Reply Dialog State
  var showReplyDialog by remember { mutableStateOf(false) }
  var selectedRequestForReply by remember { mutableStateOf<SupportRequest?>(null) }
  var replyText by remember { mutableStateOf("") }
  var newRequestStatus by remember { mutableStateOf("تم التواصل") }

  // Calculated Stats (Page 26)
  val publishedCount = allContent.count { it.reviewStatus == "Published" }
  val reviewCount = allContent.count { it.reviewStatus == "Review" || it.reviewStatus == "Draft" }
  val openRequestsCount = allRequests.count { it.status != "مغلق" }
  val newRequestsCount = allRequests.count { it.status == "جديد" }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    TopAppBar(
      title = {
        Text(
          text = "لوحة التحكم (Admin)",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("admin_back_button")
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

    // Role Switcher Banner (Page 27)
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AdminPanelSettings,
            contentDescription = null,
            tint = DarkGreen,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "الدور الحالي:",
            style = MaterialTheme.typography.labelMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
          )
        }

        ExposedDropdownMenuBox(
          expanded = roleDropdownExpanded,
          onExpandedChange = { roleDropdownExpanded = !roleDropdownExpanded }
        ) {
          Box(
            modifier = Modifier
              .menuAnchor()
              .clip(RoundedCornerShape(8.dp))
              .background(Color.White)
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text(
              text = activeRole,
              style = MaterialTheme.typography.labelMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
          }

          ExposedDropdownMenu(
            expanded = roleDropdownExpanded,
            onDismissRequest = { roleDropdownExpanded = false }
          ) {
            roles.forEach { role ->
              DropdownMenuItem(
                text = { Text(role) },
                onClick = {
                  activeRole = role
                  onUpdateRole(role)
                  roleDropdownExpanded = false
                }
              )
            }
          }
        }
      }
    }

    // Tabs
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = CreamBackground,
      contentColor = DarkGreen
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = { Text("المؤشرات العامة", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = { Text("إدارة المحتوى", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 2,
        onClick = { selectedTab = 2 },
        text = { Text("طلبات الدعم", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
    }

    when (selectedTab) {
      0 -> {
        // Overview Dashboard (Page 26)
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          item {
            Text(
              text = "إحصائيات المنظومة العامة",
              style = MaterialTheme.typography.titleLarge,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "«لا نعرض بيانات نفسية حساسة في الإحصائيات العامة»",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.65f)
            )
            Spacer(modifier = Modifier.height(10.dp))
          }

          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              AdminStatCard(
                title = "المستخدمين",
                value = "1,248",
                icon = Icons.Default.People,
                modifier = Modifier.weight(1f)
              )
              AdminStatCard(
                title = "المحتوى المنشور",
                value = "$publishedCount",
                icon = Icons.Default.Article,
                modifier = Modifier.weight(1f)
              )
            }
          }

          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              AdminStatCard(
                title = "قيد المراجعة",
                value = "$reviewCount",
                icon = Icons.Default.Assignment,
                modifier = Modifier.weight(1f)
              )
              AdminStatCard(
                title = "طلبات جديدة",
                value = "$newRequestsCount",
                icon = Icons.Default.SupportAgent,
                modifier = Modifier.weight(1f)
              )
            }
          }

          item {
            AdminStatCard(
              title = "إجمالي طلبات الدعم المفتوحة",
              value = "$openRequestsCount",
              icon = Icons.Default.SupportAgent,
              modifier = Modifier.fillMaxWidth()
            )
          }

          item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Text(
                  text = "دورة عمل المحتوى النفسي (Workflow)",
                  style = MaterialTheme.typography.titleMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "Draft  →  Review  →  Approved  →  Published  →  Archived\n\nيخضع كل محتوى لمراجعة أخصائي نفسي معتمد قبل اعتماده ونشره للمستخدمين لضمان الأمان والموثوقية.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = TextDark,
                  lineHeight = 22.sp
                )
              }
            }
          }
        }
      }

      1 -> {
        // Content Management (Page 26-27)
        val canManageContent = activeRole in listOf("Super Admin", "Content Manager")

        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "قائمة المحتوى (${allContent.size})",
                style = MaterialTheme.typography.titleLarge,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )

              if (canManageContent) {
                Button(
                  onClick = {
                    editingContent = null
                    contentTitle = ""
                    contentDescription = ""
                    contentBody = ""
                    contentCategory = "أعرف نفسي"
                    contentStatus = "Published"
                    showContentDialog = true
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.testTag("admin_add_content_button")
                ) {
                  Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("إضافة مقال", fontSize = 12.sp)
                }
              }
            }
          }

          items(allContent, key = { it.id }) { item ->
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(SageGreenPrimary.copy(alpha = 0.25f))
                      .padding(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = item.category,
                      style = MaterialTheme.typography.labelSmall,
                      color = DarkGreen,
                      fontWeight = FontWeight.Bold
                    )
                  }

                  Text(
                    text = "الحالة: ${item.reviewStatus}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (item.reviewStatus == "Published") StatusDone else StatusReview,
                    fontWeight = FontWeight.Bold
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = item.title,
                  style = MaterialTheme.typography.titleMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = item.description,
                  style = MaterialTheme.typography.bodySmall,
                  color = TextDark,
                  maxLines = 2
                )

                if (canManageContent) {
                  Spacer(modifier = Modifier.height(10.dp))
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                  ) {
                    IconButton(
                      onClick = {
                        editingContent = item
                        contentTitle = item.title
                        contentDescription = item.description
                        contentBody = item.body
                        contentCategory = item.category
                        contentStatus = item.reviewStatus
                        showContentDialog = true
                      }
                    ) {
                      Icon(imageVector = Icons.Default.Edit, contentDescription = "تعديل", tint = DarkGreen)
                    }
                    IconButton(
                      onClick = { onDeleteContent(item) }
                    ) {
                      Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = MoodDifficult)
                    }
                  }
                }
              }
            }
          }
        }
      }

      2 -> {
        // Support Management (Page 27)
        val canManageSupport = activeRole in listOf("Super Admin", "Support Supervisor", "Support Member")

        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          item {
            Text(
              text = "طلبات الدعم الواردة (${allRequests.size})",
              style = MaterialTheme.typography.titleLarge,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "التعامل مع الطلبات وفق آلية الدعم المتاحة وسياسة الخصوصية",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.65f)
            )
          }

          items(allRequests, key = { it.id }) { req ->
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
                    text = "${req.requestNumber} • ${req.requestType}",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(SageGreenPrimary.copy(alpha = 0.2f))
                      .padding(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = req.status,
                      style = MaterialTheme.typography.labelSmall,
                      color = DarkGreen,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = req.message,
                  style = MaterialTheme.typography.bodyMedium,
                  color = TextDark,
                  lineHeight = 22.sp
                )

                if (!req.adminReply.isNullOrBlank()) {
                  Spacer(modifier = Modifier.height(8.dp))
                  Text(
                    text = "الرد الحالي: ${req.adminReply}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SageGreenPrimary,
                    fontWeight = FontWeight.Medium
                  )
                }

                if (canManageSupport) {
                  Spacer(modifier = Modifier.height(10.dp))
                  Button(
                    onClick = {
                      selectedRequestForReply = req
                      replyText = req.adminReply ?: ""
                      newRequestStatus = req.status
                      showReplyDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.align(Alignment.End)
                  ) {
                    Text("الرد وتحديث الحالة", fontSize = 12.sp)
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  // Add/Edit Content Dialog
  if (showContentDialog) {
    AlertDialog(
      onDismissRequest = { showContentDialog = false },
      title = {
        Text(
          text = if (editingContent == null) "إضافة مقال جديد" else "تعديل المقال",
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = contentTitle,
            onValueChange = { contentTitle = it },
            label = { Text("العنوان") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = contentDescription,
            onValueChange = { contentDescription = it },
            label = { Text("الوصف المختصر") },
            maxLines = 2,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = contentCategory,
            onValueChange = { contentCategory = it },
            label = { Text("التصنيف (مثال: القلق والضغط، أعرف نفسي)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = contentStatus,
            onValueChange = { contentStatus = it },
            label = { Text("الحالة: Draft, Review, Approved, Published, Archived") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = contentBody,
            onValueChange = { contentBody = it },
            label = { Text("نص المقال الكامل") },
            modifier = Modifier
              .fillMaxWidth()
              .height(120.dp)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (contentTitle.isNotBlank()) {
              val itemToSave = editingContent?.copy(
                title = contentTitle,
                description = contentDescription,
                body = contentBody,
                category = contentCategory,
                reviewStatus = contentStatus
              ) ?: ContentItem(
                title = contentTitle,
                description = contentDescription,
                body = contentBody,
                category = contentCategory,
                contentType = "article",
                duration = "5 دقائق قراءة",
                reviewStatus = contentStatus
              )
              onSaveContent(itemToSave) {
                showContentDialog = false
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
        ) {
          Text("حفظ")
        }
      },
      dismissButton = {
        TextButton(onClick = { showContentDialog = false }) {
          Text("إلغاء", color = TextDark)
        }
      }
    )
  }

  // Reply Dialog for Support Request
  if (showReplyDialog && selectedRequestForReply != null) {
    val req = selectedRequestForReply!!
    AlertDialog(
      onDismissRequest = { showReplyDialog = false },
      title = {
        Text("تحديث طلب الدعم ${req.requestNumber}", color = DarkGreen, fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("الرسالة الأصلية:\n${req.message}", style = MaterialTheme.typography.bodySmall, color = TextDark)

          OutlinedTextField(
            value = newRequestStatus,
            onValueChange = { newRequestStatus = it },
            label = { Text("الحالة (جديد، تم الاستلام، قيد المراجعة، تم التواصل، تمت الإحالة، مغلق)") },
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = replyText,
            onValueChange = { replyText = it },
            label = { Text("نص الرد والتوجيه للمستخدم") },
            modifier = Modifier
              .fillMaxWidth()
              .height(100.dp)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onUpdateRequestStatus(req.id, newRequestStatus, replyText)
            showReplyDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
        ) {
          Text("حفظ التحديث")
        }
      },
      dismissButton = {
        TextButton(onClick = { showReplyDialog = false }) {
          Text("إلغاء", color = TextDark)
        }
      }
    )
  }
}

@Composable
fun AdminStatCard(
  title: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(SageGreenPrimary.copy(alpha = 0.25f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(24.dp))
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column {
        Text(
          text = value,
          style = MaterialTheme.typography.headlineMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = title,
          style = MaterialTheme.typography.labelSmall,
          color = TextDark.copy(alpha = 0.65f)
        )
      }
    }
  }
}
