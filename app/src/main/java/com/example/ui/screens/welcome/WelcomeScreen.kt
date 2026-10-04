package com.example.ui.screens.welcome

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@Composable
fun WelcomeScreen(
  onStartJourney: () -> Unit,
  onAboutClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp, vertical = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // App Icon / Logo Badge
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .background(DarkGreen),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Spa,
          contentDescription = "شعار نسمة الحياة",
          tint = CreamBackground,
          modifier = Modifier.size(44.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Title
      Text(
        text = "نسمة الحياة",
        style = MaterialTheme.typography.displayMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Text(
        text = "مساحة آمنة تفهمك وتساندك",
        style = MaterialTheme.typography.titleMedium,
        color = TextDark.copy(alpha = 0.8f),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Hero Illustration Art
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBeige),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(190.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.nesmat_welcome_art_1791033197437),
          contentDescription = "لوحة نسمة الحياة الهادئة",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Slogan Card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "«كثيرون يعيشون الحياة... ولكن قليلون يستمتعون بها»",
          style = MaterialTheme.typography.bodyLarge,
          color = TextDark,
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center,
          lineHeight = 28.sp,
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        )
      }

      Spacer(modifier = Modifier.height(36.dp))

      // Primary Button: ابدأ رحلتك
      Button(
        onClick = onStartJourney,
        colors = ButtonDefaults.buttonColors(
          containerColor = DarkGreen,
          contentColor = CreamBackground
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("start_journey_button")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "ابدأ رحلتك 🌱",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Secondary Button: عن نسمة الحياة
      OutlinedButton(
        onClick = onAboutClick,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("about_button")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "عن نسمة الحياة",
            style = MaterialTheme.typography.titleMedium
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
