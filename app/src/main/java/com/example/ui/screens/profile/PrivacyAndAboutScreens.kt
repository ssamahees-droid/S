package com.example.ui.screens.profile

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    TopAppBar(
      title = {
        Text(
          text = "الخصوصية وحماية البيانات",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("privacy_back_button")
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
      contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = DarkGreen,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "التزامنا بالخصوصية والشفافية",
                style = MaterialTheme.typography.titleLarge,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "تطبيق «نسمة الحياة» مصمم مع إدراك عميق لحساسية مشاعرك وصحتك النفسية. نحن نلتزم بالمعايير الصارمة لسياسات Google Play Health وقانون حماية البيانات الشخصية المصري رقم 151 لسنة 2020.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark,
              lineHeight = 24.sp
            )
          }
        }
      }

      // Nine Points (Page 29)
      val privacyPoints = listOf(
        "1. ما البيانات التي نجمعها؟" to "تسجيلات مشاعرك اليومية الاختيارية، إجابات التقييمات الذاتية، ونصوص طلبات الدعم التي تكتبها بمحض إرادتك.",
        "2. لماذا نجمعها؟" to "لتمكينك من تتبع رحلتك وتطور حالتك في شاشة «رحلتي»، واقتراح محتوى توعوي مناسب لاهتماماتك، والرد على استفساراتك عند طلب الدعم.",
        "3. أين يتم تخزينها؟" to "تخزن البيانات الحساسة وملاحظاتك محلياً على جهازك في مساحة تخزين مشفرة ومحمية من التطبيقات الأخرى.",
        "4. من يستطيع الوصول إليها؟" to "أنت فقط تملك حق الوصول لبياناتك وملاحظاتك. لا يستطيع أي مستخدم آخر الاطلاع على أي حرف تدونه.",
        "5. هل نشارك البيانات مع طرف ثالث؟" to "قطعيّاً لا. لا نبيع بياناتك، ولا نستخدمها لأغراض إعلانية، ولا نشاركها مع أي جهة تجارية أو تحليلات عامة.",
        "6. الصلاحيات غير المطلوبة (Zero Intrusive Permissions)" to "لا يطلب التطبيق الوصول إلى: الموقع الجغرافي، جهات الاتصال، الصور، الكاميرا، أو الميكروفون.",
        "7. كيف يطلب المستخدم حذف البيانات؟" to "يمكنك في أي لحظة النقر على «حذف جميع بياناتي» في شاشة حسابي لمحو كافة التسجيلات فوراً ودون رجعة.",
        "8. سحب الموافقة" to "يمكنك سحب موافقتك على تخزين التقييمات أو تفضيلاتك في أي وقت وبكل سهولة.",
        "9. التواصل والاستفسار" to "يمكنك مراسلة فريق نسمة الحياة للاستفسارات القانونية والخصوصية عبر قسم «محتاج أتكلم» أو البريد المخصص."
      )

      items(privacyPoints) { item ->
        val (question, answer) = item
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = question,
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = answer,
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark,
              lineHeight = 22.sp
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    TopAppBar(
      title = {
        Text(
          text = "عن نسمة الحياة",
          style = MaterialTheme.typography.titleMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("about_back_button")
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
      contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(DarkGreen),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = null,
                tint = CreamBackground,
                modifier = Modifier.size(36.dp)
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "نسمة الحياة",
              style = MaterialTheme.typography.headlineMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )

            Text(
              text = "Nesmat Al Hayat • MVP Version 1.0",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "«كثيرون يعيشون الحياة... ولكن قليلون يستمتعون بها»",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
              text = "تطبيق عربي للتوعية بالصحة النفسية، صُمم ليكون رفيقاً هادئاً ودافئاً يساعد المستخدم على فهم نفسه ومشاعره، واستكشاف ما يستحق الانتباه، واستخدام أدوات المساعدة الذاتية البسيطة والوصول إلى محتوى نفسي موثوق يخضع لمراجعة متخصصة.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark,
              lineHeight = 24.sp
            )
          }
        }
      }

      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = DarkGreen,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "الميثاق الأساسي للتطبيق",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "• نحن لا نبني تطبيقاً طبياً يشخّص الناس.\n• التطبيق ليس بديلاً عن الطبيب أو المعالج النفسي.\n• التطبيق لا يقوم بوصف الأدوية أو تعديلها.\n• الهدف: التوعية + الاستكشاف + الدعم + التوجيه إلى الخطوة المناسبة.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDark,
              lineHeight = 22.sp
            )
          }
        }
      }
    }
  }
}
