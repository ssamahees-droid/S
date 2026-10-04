package com.example.features.light

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.MoodGood
import com.example.ui.theme.MoodGreat
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.SoftMint
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class BubbleItem(
  val id: Int,
  val text: String,
  val color: Color,
  val sizeDp: Int,
  var isPopped: Boolean = false
)

data class GratitudeFlower(
  val id: Int,
  val text: String,
  val color: Color,
  val emoji: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightActivitiesScreen(
  onNavigateToBreathe: () -> Unit,
  onNavigateToGwayaHekaya: () -> Unit = {},
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: فقاعات, 1: إطلاق الهموم, 2: التجذير 5-4-3-2-1, 3: حديقة الامتنان, 4: أحجار السكينة
  val scope = rememberCoroutineScope()

  // 1. Bubble Pop State
  val affirmations = listOf(
    "أنت كافٍ ومقدر كما أنت 🤍",
    "تنفس بعمق وبهدوء... كل شيء سيكون بخير 🌿",
    "دع القلق يمر كما يمر السحاب الخفيف ☁️",
    "لحظة سلام لك وحدك هنا والآن 🕊️",
    "أنت لست وحدك في رحلتك 🌱",
    "كل يوم هو فرصة جديدة للبدء بحب ☀️",
    "أعطِ نفسك مساحة للراحة دون لوم 🌸",
    "خطوة صغيرة وهادئة تصنع فارقاً كبيراً ✨"
  )

  val bubbleColors = listOf(
    SageGreenPrimary,
    SoftMint,
    WarmBeige,
    Color(0xFFB8D8C8),
    Color(0xFFE2D6C5),
    Color(0xFFC7DECF)
  )

  var poppedCount by remember { mutableIntStateOf(0) }
  var latestPoppedText by remember { mutableStateOf("انقر على أي فقاعة لتفريغ الشحنة واستقبال رسالة طمأنينة 🌿") }

  val bubbles = remember {
    mutableStateListOf<BubbleItem>().apply {
      for (i in 0 until 9) {
        add(
          BubbleItem(
            id = i,
            text = affirmations[i % affirmations.size],
            color = bubbleColors[i % bubbleColors.size],
            sizeDp = Random.nextInt(75, 105)
          )
        )
      }
    }
  }

  // 2. Worry Release State
  var worryInput by remember { mutableStateOf("") }
  var isReleasingWorry by remember { mutableStateOf(false) }
  var releasedCount by remember { mutableIntStateOf(1) }
  var releaseComfortMessage by remember { mutableStateOf<String?>(null) }
  val worryDissolveAlpha = remember { Animatable(1f) }
  val worryDissolveScale = remember { Animatable(1f) }

  // 3. Grounding 5-4-3-2-1 State
  var groundingStep by remember { mutableIntStateOf(0) }
  val groundingCompleted = remember { mutableStateListOf(false, false, false, false, false) }

  // 4. Gratitude Garden State
  val flowers = remember {
    mutableStateListOf(
      GratitudeFlower(1, "نسمة هواء لطيفة في الصباح", MoodGreat, "🌸"),
      GratitudeFlower(2, "فنجان قهوة دافئ وهادئ", MoodGood, "🌻"),
      GratitudeFlower(3, "رسالة طيبة من شخص عزيز", SageGreenPrimary, "🌷")
    )
  }
  var newGratitudeInput by remember { mutableStateOf("") }

  // 5. Zen Stones Balance State
  var stoneStackCount by remember { mutableIntStateOf(3) }
  val stoneColors = listOf(
    Color(0xFF5A7264),
    Color(0xFF7E9787),
    Color(0xFF9FB6A6),
    Color(0xFFB8CBBB),
    Color(0xFFD2DFD5)
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
              imageVector = Icons.Default.Gamepad,
              contentDescription = null,
              tint = CreamBackground,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "خفة ولعب (أنشطة السكينة التفاعلية)",
              style = MaterialTheme.typography.titleMedium,
              color = DarkGreen,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "ألعاب وطقوس تفريغ نفسي لإرخاء الأعصاب وطرد القلق",
              style = MaterialTheme.typography.labelSmall,
              color = TextDark.copy(alpha = 0.6f)
            )
          }
        }
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("light_back_button")
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

    // Featured Game Card: جوايا حكاية — نسمة الحياة
    Card(
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = DarkGreen),
      elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp)
        .clickable { onNavigateToGwayaHekaya() }
        .testTag("light_gwaya_hekaya_card")
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(SageGreenPrimary.copy(alpha = 0.3f))
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = "لعبة الاستبصار التفاعلية ✨",
                fontSize = 10.sp,
                color = SoftMint,
                fontWeight = FontWeight.Bold
              )
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "جوايا حكاية — نسمة الحياة 🌿",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "٦ محطات تفاعلية لفهم المواقف، الأفكار، المشاعر، وإشارات الجسد بهدوء",
            style = MaterialTheme.typography.labelSmall,
            color = CreamBackground.copy(alpha = 0.85f)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
          onClick = onNavigateToGwayaHekaya,
          colors = ButtonDefaults.buttonColors(containerColor = SoftMint),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Text(
            text = "ابدأ الرحلة ←",
            color = DarkGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // Scrollable Tab Row for All Creative Activities
    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      containerColor = CreamBackground,
      contentColor = DarkGreen,
      edgePadding = 16.dp
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = { Text("فقاعات التهدئة 🫧", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = { Text("إطلاق الهموم 🕊️", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 2,
        onClick = { selectedTab = 2 },
        text = { Text("التجذير 5-4-3-2-1 ⚓", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 3,
        onClick = { selectedTab = 3 },
        text = { Text("حديقة الامتنان 🌸", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 4,
        onClick = { selectedTab = 4 },
        text = { Text("أحجار السكينة 🪨", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = false,
        onClick = onNavigateToGwayaHekaya,
        text = { Text("جوايا حكاية 📖", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
    }

    when (selectedTab) {
      0 -> {
        // Tab 0: Bubble Pop Relaxation
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          item {
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "فرقعة الفقاعات المهدئة 🫧",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "المفرقعات: $poppedCount",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                  text = latestPoppedText,
                  style = MaterialTheme.typography.bodyMedium,
                  color = TextDark,
                  textAlign = TextAlign.Center,
                  lineHeight = 22.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }

          // Bubbles Grid Field
          item {
            Card(
              shape = RoundedCornerShape(24.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
            ) {
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .padding(12.dp)
              ) {
                Column(
                  modifier = Modifier.fillMaxSize(),
                  verticalArrangement = Arrangement.SpaceEvenly
                ) {
                  for (row in 0..2) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceEvenly,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      for (col in 0..2) {
                        val index = row * 3 + col
                        if (index < bubbles.size) {
                          val bubble = bubbles[index]
                          BubbleNode(
                            bubble = bubble,
                            onPop = {
                              if (!bubble.isPopped) {
                                bubble.isPopped = true
                                poppedCount++
                                latestPoppedText = bubble.text
                              }
                            }
                          )
                        }
                      }
                    }
                  }
                }
              }
            }
          }

          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Button(
                onClick = {
                  bubbles.forEach { it.isPopped = false }
                  latestPoppedText = "تم تجديد الفقاعات! استمتع بلحظات من الهدوء 🌿"
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(12.dp)
              ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("تجديد الفقاعات", fontSize = 12.sp)
              }

              Button(
                onClick = onNavigateToBreathe,
                colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("تمارين التنفس ←", color = DarkGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      1 -> {
        // Tab 1: Worry Release Ritual (طقس إطلاق الهموم)
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          item {
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(18.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "طقس إطلاق الهموم وتفريغ الصدر 🕊️",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(SageGreenPrimary.copy(alpha = 0.25f))
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = "أثقال تخلّصت منها: $releasedCount",
                      style = MaterialTheme.typography.labelSmall,
                      color = DarkGreen,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "أحياناً مجرد إخراج الفكرة من ذهنك ووضعها أمامك يسلبها سطوتها عليك. اكتب هنا ما يثقل قلبك أو يقلقك، ثم أطلقه لتشاهده يتلاشى بسلام في الفضاء.",
                  style = MaterialTheme.typography.bodySmall,
                  color = TextDark.copy(alpha = 0.75f),
                  lineHeight = 20.sp
                )
              }
            }
          }

          // Ritual Area
          item {
            Card(
              shape = RoundedCornerShape(24.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(20.dp)
              ) {
                Column(
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  // Poetic art asset
                  Box(
                    modifier = Modifier
                      .size(80.dp)
                      .clip(CircleShape)
                      .background(WarmBeige.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                  ) {
                    Image(
                      painter = painterResource(id = R.drawable.nesmat_peace_art),
                      contentDescription = null,
                      contentScale = ContentScale.Crop,
                      modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                    )
                  }

                  Spacer(modifier = Modifier.height(16.dp))

                  // Animated dissolvable text input or comfort message
                  if (releaseComfortMessage != null) {
                    Box(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SageGreenPrimary.copy(alpha = 0.15f))
                        .padding(20.dp),
                      contentAlignment = Alignment.Center
                    ) {
                      Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🕊️✨", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                          text = releaseComfortMessage!!,
                          style = MaterialTheme.typography.bodyMedium,
                          color = DarkGreen,
                          textAlign = TextAlign.Center,
                          fontWeight = FontWeight.Bold,
                          lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                          onClick = {
                            releaseComfortMessage = null
                            worryInput = ""
                            scope.launch {
                              worryDissolveAlpha.snapTo(1f)
                              worryDissolveScale.snapTo(1f)
                            }
                          },
                          colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                          shape = RoundedCornerShape(12.dp)
                        ) {
                          Text("تفريغ ثِقل آخر", fontSize = 12.sp)
                        }
                      }
                    }
                  } else {
                    Box(
                      modifier = Modifier
                        .fillMaxWidth()
                        .scale(worryDissolveScale.value)
                        .alpha(worryDissolveAlpha.value)
                    ) {
                      OutlinedTextField(
                        value = worryInput,
                        onValueChange = { worryInput = it },
                        placeholder = {
                          Text(
                            "اكتب هنا: ما الذي يُقلقك؟ ما الفكرة المزعجة التي تود التحرر منها؟",
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                          )
                        },
                        modifier = Modifier
                          .fillMaxWidth()
                          .height(140.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                          focusedBorderColor = DarkGreen,
                          unfocusedBorderColor = WarmBeige
                        )
                      )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                      Button(
                        onClick = {
                          if (worryInput.isNotBlank() && !isReleasingWorry) {
                            isReleasingWorry = true
                            scope.launch {
                              worryDissolveScale.animateTo(0.6f, tween(1200, easing = FastOutSlowInEasing))
                              worryDissolveAlpha.animateTo(0f, tween(1200))
                              delay(200)
                              releasedCount++
                              releaseComfortMessage = "لقد أطلقت ثِقلك في الأفق كالرماد المتطاير... تذكّر أنك لست أفكارك، وأن هذا الشعور سيمر ويترك مكانه للنور والسلام 🌿"
                              isReleasingWorry = false
                            }
                          }
                        },
                        enabled = worryInput.isNotBlank() && !isReleasingWorry,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                      ) {
                        Text(
                          text = if (isReleasingWorry) "جاري الإطلاق... 🍃" else "أطلقه للنسمة 🍃",
                          fontSize = 13.sp,
                          fontWeight = FontWeight.Bold
                        )
                      }

                      Button(
                        onClick = {
                          if (worryInput.isNotBlank() && !isReleasingWorry) {
                            isReleasingWorry = true
                            scope.launch {
                              worryDissolveScale.animateTo(1.2f, tween(600))
                              worryDissolveAlpha.animateTo(0f, tween(600))
                              delay(200)
                              releasedCount++
                              releaseComfortMessage = "تحوّل القلق إلى طمأنينة... أودعت همك في يد الرحمة الواسعة، فاسترح الآن وتنفس ملء رئتيك 🤍"
                              isReleasingWorry = false
                            }
                          }
                        },
                        enabled = worryInput.isNotBlank() && !isReleasingWorry,
                        colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                      ) {
                        Text(
                          text = "حوّله إلى سلام ✨",
                          color = DarkGreen,
                          fontSize = 13.sp,
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      2 -> {
        // Tab 2: Grounding 5-4-3-2-1 Technique (التجذير الحسي للتهدئة السريعة)
        val steps = listOf(
          Triple("👀 5 أشياء تراها بعينيك", "انظر حولك الآن في غرفتك... حدد 5 أشياء تراها بوضوح (ساعة، نافذة، كتاب، لون الستارة، يدك)", Icons.Default.RemoveRedEye),
          Triple("✋ 4 أشياء يمكنك لمسها", "المس 4 أشياء بيدك واشعر بملمسها (قماش ملابسك، برودة الطاولة، سطح هاتفك، شعرك)", Icons.Default.PanTool),
          Triple("👂 3 أصوات تسمعها الآن", "أنصت بعناية... حدد 3 أصوات حولك (صوت تنفسك، مروحة المكيف، صوت بعيد من الشارع)", Icons.Default.Hearing),
          Triple("👃 شيئان يمكنك شمهما", "خذ شهيقاً... حاول تمييز رائحتين (عطرك، رائحة القهوة، أو هواء الغرفة المنعش)", Icons.Default.Spa),
          Triple("🤍 شيء واحد تشعر بالامتنان له في نفسك", "ابتسم واشكر نفسك على محاولتك وصمودك حتى هذه اللحظة", Icons.Default.SelfImprovement)
        )

        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          item {
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(18.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "تقنية التجذير 5-4-3-2-1 ⚓",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(DarkGreen)
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = "مرساة الأمان",
                      style = MaterialTheme.typography.labelSmall,
                      color = Color.White,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "أقوى تمرين مثبت علمياً لقطع نوبات الهلع والقلق الشديد عبر إعادة توجيه حواسك الخمس إلى اللحظة الحاضرة والواقع الملموس.",
                  style = MaterialTheme.typography.bodySmall,
                  color = TextDark.copy(alpha = 0.75f),
                  lineHeight = 20.sp
                )
              }
            }
          }

          items(steps.indices.toList()) { index ->
            val (title, instruction, icon) = steps[index]
            val isDone = groundingCompleted[index]

            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isDone) SageGreenPrimary.copy(alpha = 0.2f) else Color.White
              ),
              border = if (isDone) androidx.compose.foundation.BorderStroke(1.5.dp, DarkGreen) else null,
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  groundingCompleted[index] = !groundingCompleted[index]
                }
            ) {
              Row(
                modifier = Modifier
                  .padding(16.dp)
                  .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isDone) DarkGreen else WarmBeige.copy(alpha = 0.5f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (isDone) Icons.Default.Check else icon,
                    contentDescription = null,
                    tint = if (isDone) Color.White else DarkGreen,
                    modifier = Modifier.size(20.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = instruction,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDark.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                  )
                }
              }
            }
          }

          item {
            val allDone = groundingCompleted.all { it }
            if (allDone) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(DarkGreen)
                  .padding(16.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "أحسنت صنعاً! 🤍 أنت الآن متصل بحواسك وجسدك هنا والآن، ولست أسيراً لأفكارك المقلقة.",
                  color = Color.White,
                  style = MaterialTheme.typography.bodyMedium,
                  textAlign = TextAlign.Center,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Button(
              onClick = {
                for (i in groundingCompleted.indices) {
                  groundingCompleted[i] = false
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = WarmBeige),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("إعادة التمرين من البداية", color = DarkGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      3 -> {
        // Tab 3: Gratitude Garden
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          item {
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(18.dp)) {
                Text(
                  text = "حديقة الامتنان والتفتح 🌸",
                  style = MaterialTheme.typography.titleMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "التركيز على النعم الصغيرة يروي الروح ويصنع السلام. اكتب شيئاً واحداً تشعر بالامتنان لوجوده اليوم لتزرع زهرة جديدة في حديقتك.",
                  style = MaterialTheme.typography.bodySmall,
                  color = TextDark.copy(alpha = 0.75f),
                  lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  OutlinedTextField(
                    value = newGratitudeInput,
                    onValueChange = { newGratitudeInput = it },
                    placeholder = { Text("أنا ممتن لـ...", fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                      focusedBorderColor = DarkGreen,
                      unfocusedBorderColor = WarmBeige
                    )
                  )

                  Spacer(modifier = Modifier.width(8.dp))

                  Button(
                    onClick = {
                      if (newGratitudeInput.isNotBlank()) {
                        val emojis = listOf("🌸", "🌻", "🌷", "🌺", "🌼", "🌿")
                        flowers.add(
                          GratitudeFlower(
                            id = flowers.size + 1,
                            text = newGratitudeInput.trim(),
                            color = bubbleColors[flowers.size % bubbleColors.size],
                            emoji = emojis[flowers.size % emojis.size]
                          )
                        )
                        newGratitudeInput = ""
                      }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(12.dp)
                  ) {
                    Text("ازرع 🌷", fontSize = 12.sp)
                  }
                }
              }
            }
          }

          items(flowers, key = { it.id }) { flower ->
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = flower.emoji, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  text = flower.text,
                  style = MaterialTheme.typography.bodyMedium,
                  color = TextDark,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }
      }

      4 -> {
        // Tab 4: Zen Stones Balance Game
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          item {
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = "توازن أحجار السكينة (Zen Stones) 🪨",
                  style = MaterialTheme.typography.titleMedium,
                  color = DarkGreen,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "رص الأحجار بهدوء وتركيز يساعد العقل على التباطؤ والرسو في اللحظة الحاضرة.",
                  style = MaterialTheme.typography.bodySmall,
                  color = TextDark.copy(alpha = 0.7f),
                  textAlign = TextAlign.Center
                )
              }
            }
          }

          item {
            Card(
              shape = RoundedCornerShape(24.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxSize()
                  .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
              ) {
                for (i in stoneStackCount downTo 1) {
                  val widthDp = (80 + i * 24).dp
                  val heightDp = 34.dp
                  val stoneColor = stoneColors[(i - 1) % stoneColors.size]

                  Box(
                    modifier = Modifier
                      .width(widthDp)
                      .height(heightDp)
                      .clip(RoundedCornerShape(20.dp))
                      .background(stoneColor)
                      .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "سكينة",
                      style = MaterialTheme.typography.labelSmall,
                      color = Color.White.copy(alpha = 0.85f),
                      fontSize = 11.sp
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                }

                Box(
                  modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(WarmBeige)
                )
              }
            }
          }

          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Button(
                onClick = {
                  if (stoneStackCount < 6) stoneStackCount++
                },
                enabled = stoneStackCount < 6,
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(12.dp)
              ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("إضافة حجر سكينة", fontSize = 12.sp)
              }

              Button(
                onClick = { stoneStackCount = 2 },
                colors = ButtonDefaults.buttonColors(containerColor = WarmBeige),
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("إعادة الترتيب", color = DarkGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun BubbleNode(
  bubble: BubbleItem,
  onPop: () -> Unit
) {
  val scaleAnim = remember { Animatable(1f) }

  Box(
    modifier = Modifier
      .size(72.dp)
      .scale(if (bubble.isPopped) 0.85f else scaleAnim.value)
      .clip(CircleShape)
      .background(if (bubble.isPopped) WarmBeige.copy(alpha = 0.25f) else bubble.color.copy(alpha = 0.5f))
      .border(
        width = 1.5.dp,
        color = if (bubble.isPopped) Color.LightGray else DarkGreen.copy(alpha = 0.4f),
        shape = CircleShape
      )
      .clickable { onPop() }
      .testTag("bubble_${bubble.id}"),
    contentAlignment = Alignment.Center
  ) {
    if (bubble.isPopped) {
      Text(text = "✨", fontSize = 20.sp)
    } else {
      Text(
        text = "🫧",
        fontSize = 24.sp
      )
    }
  }
}

