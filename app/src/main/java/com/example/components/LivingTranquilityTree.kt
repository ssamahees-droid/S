package com.example.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import kotlin.math.sin

@Composable
fun LivingTranquilityTree(
  mindfulMomentsCount: Int,
  modifier: Modifier = Modifier
) {
  var showInspiringNote by remember { mutableStateOf(false) }

  val infiniteTransition = rememberInfiniteTransition(label = "breeze_sway")
  val breezeFactor by infiniteTransition.animateFloat(
    initialValue = -1f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(3200, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "tree_sway"
  )

  val leafShimmer by infiniteTransition.animateFloat(
    initialValue = 0.6f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(2200, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "leaf_shimmer"
  )

  val growthStage = when {
    mindfulMomentsCount < 3 -> "بذرة الوعي والأمل 🌱"
    mindfulMomentsCount < 6 -> "برعم السلام الطري 🌿"
    mindfulMomentsCount < 10 -> "غصن الطمأنينة الأخضر 🌳"
    else -> "شجرة السكينة الوارفة 🌺"
  }

  val encouragingWhisper = remember(mindfulMomentsCount) {
    listOf(
      "كل لحظة وعي واهتمام بنفسك تسقي جذور سلامك الداخلي 🌿",
      "أنت تزهر برفق، خطوة بخطوة ويوماً بعد يوم 🌸",
      "جذورك اليوم أعمق، وقدرتك على تجاوز العواصف أكبر 🌳",
      "سلامك الداخلي شجرة تنمو مع كل نفس هادئ تتنفسه 🍃"
    ).random()
  }

  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(SageGreenPrimary.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Spa,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "شجرة السكينة الحية",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "تنمو مع كل لحظة عناية بصحتك النفسية",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.6f)
            )
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkGreen)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = growthStage,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Animated Living Tree Canvas
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(
            Brush.verticalGradient(
              colors = listOf(
                WarmBeige.copy(alpha = 0.25f),
                SageGreenPrimary.copy(alpha = 0.15f),
                Color.White
              )
            )
          )
          .clickable { showInspiringNote = !showInspiringNote }
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val canvasWidth = size.width
          val canvasHeight = size.height

          val trunkBaseX = canvasWidth / 2f
          val trunkBaseY = canvasHeight - 20f

          // Ground mound
          drawOval(
            color = WarmBeige,
            topLeft = Offset(trunkBaseX - 80f, trunkBaseY - 8f),
            size = androidx.compose.ui.geometry.Size(160f, 24f)
          )

          // Trunk
          val trunkPath = Path().apply {
            moveTo(trunkBaseX - 14f, trunkBaseY)
            quadraticTo(
              trunkBaseX - 8f + breezeFactor * 4f,
              canvasHeight * 0.65f,
              trunkBaseX - 6f + breezeFactor * 12f,
              canvasHeight * 0.45f
            )
            lineTo(trunkBaseX + 6f + breezeFactor * 12f, canvasHeight * 0.45f)
            quadraticTo(
              trunkBaseX + 8f + breezeFactor * 4f,
              canvasHeight * 0.65f,
              trunkBaseX + 14f,
              trunkBaseY
            )
            close()
          }

          drawPath(
            path = trunkPath,
            brush = Brush.verticalGradient(
              colors = listOf(DarkGreen, Color(0xFF4A3728))
            )
          )

          // Branches with sway
          val branchTop = Offset(trunkBaseX + breezeFactor * 12f, canvasHeight * 0.45f)

          // Left main branch
          drawLine(
            color = DarkGreen,
            start = branchTop,
            end = Offset(branchTop.x - 55f + breezeFactor * 6f, canvasHeight * 0.32f),
            strokeWidth = 7f,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
          )

          // Right main branch
          drawLine(
            color = DarkGreen,
            start = branchTop,
            end = Offset(branchTop.x + 55f + breezeFactor * 6f, canvasHeight * 0.30f),
            strokeWidth = 7f,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
          )

          // Center upper branch
          drawLine(
            color = DarkGreen,
            start = branchTop,
            end = Offset(branchTop.x + breezeFactor * 16f, canvasHeight * 0.22f),
            strokeWidth = 6f,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
          )

          // Foliage Clusters / Leaves (count scales with mindfulMomentsCount)
          val clusterCount = (4 + mindfulMomentsCount.coerceAtMost(10))

          val leafCenters = listOf(
            Offset(branchTop.x - 65f + breezeFactor * 8f, canvasHeight * 0.30f),
            Offset(branchTop.x + 65f + breezeFactor * 8f, canvasHeight * 0.28f),
            Offset(branchTop.x + breezeFactor * 18f, canvasHeight * 0.20f),
            Offset(branchTop.x - 30f + breezeFactor * 12f, canvasHeight * 0.25f),
            Offset(branchTop.x + 30f + breezeFactor * 12f, canvasHeight * 0.23f),
            Offset(branchTop.x - 85f + breezeFactor * 6f, canvasHeight * 0.35f),
            Offset(branchTop.x + 85f + breezeFactor * 6f, canvasHeight * 0.34f)
          )

          for (i in 0 until clusterCount.coerceAtMost(leafCenters.size)) {
            val center = leafCenters[i]
            val radius = 24f + (i % 3) * 6f

            // Soft glow background
            drawCircle(
              color = SoftMint.copy(alpha = 0.35f * leafShimmer),
              radius = radius + 6f,
              center = center
            )

            // Main leaf canopy circle
            drawCircle(
              brush = Brush.radialGradient(
                colors = listOf(
                  SoftMint,
                  SageGreenPrimary,
                  DarkGreen
                ),
                center = center,
                radius = radius
              ),
              radius = radius,
              center = center
            )

            // Golden blossoms or flowers for high milestones
            if (mindfulMomentsCount >= 4 && i % 2 == 0) {
              drawCircle(
                color = Color(0xFFFFD54F),
                radius = 5f * leafShimmer,
                center = Offset(center.x + (i * 3f) - 6f, center.y - 8f)
              )
            }
          }

          // Gentle floating petals / fireflies
          for (p in 0..4) {
            val px = (canvasWidth * 0.2f + (p * 60f + breezeFactor * 25f)) % canvasWidth
            val py = canvasHeight * 0.15f + ((p * 28f + (1f - breezeFactor) * 15f) % (canvasHeight * 0.6f))
            drawCircle(
              color = Color(0xFFFFE082).copy(alpha = 0.65f * leafShimmer),
              radius = 3.5f,
              center = Offset(px, py)
            )
          }
        }

        // Tap hint badge
        Box(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.85f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "المس الشجرة لسماع همستها ✨",
              fontSize = 10.sp,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Poetic Reflection Reveal
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(WarmBeige.copy(alpha = 0.35f))
          .padding(12.dp)
      ) {
        Text(
          text = if (showInspiringNote) encouragingWhisper else "رصيدك اليوم: $mindfulMomentsCount لحظات وعي وسكينة. استمر في رعاية نفسك 🤍",
          style = MaterialTheme.typography.bodySmall,
          color = DarkGreen,
          textAlign = TextAlign.Center,
          lineHeight = 20.sp,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}
