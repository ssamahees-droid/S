package com.example.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentItem
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@Composable
fun ExploreScreen(
  contentList: List<ContentItem>,
  searchQuery: String,
  selectedCategory: String,
  onSearchChange: (String) -> Unit,
  onCategorySelect: (String) -> Unit,
  onContentClick: (Long) -> Unit,
  onToggleFavorite: (Long, Boolean) -> Unit,
  onNavigateToLibrary: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val categories = remember {
    listOf(
      "الكل",
      "تمارين وتطبيقات",
      "ورش تطبيقية",
      "أعرف نفسي",
      "مشاعري",
      "القلق والضغط",
      "العلاقات",
      "الأسرة",
      "العمل والدراسة",
      "الراحة والطاقة",
      "الروح والنفس"
    )
  }

  val categoryScrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CreamBackground)
  ) {
    // Header & Search
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      Text(
        text = "أفهم نفسي",
        style = MaterialTheme.typography.headlineMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "مكتبة المحتوى والتوعية الموثوقة",
        style = MaterialTheme.typography.bodyMedium,
        color = TextDark.copy(alpha = 0.75f)
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Search Field (Page 8)
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text("ابحث عن موضوع...", fontSize = 14.sp) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = DarkGreen
          )
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchChange("") }) {
              Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح البحث")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = DarkGreen,
          unfocusedBorderColor = WarmBeige,
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("explore_search_input")
      )
    }

    // Category Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(categoryScrollState)
        .padding(horizontal = 20.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      categories.forEach { cat ->
        val isSelected = selectedCategory == cat
        FilterChip(
          selected = isSelected,
          onClick = { onCategorySelect(cat) },
          label = {
            Text(
              text = cat,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              fontSize = 13.sp
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
          modifier = Modifier.testTag("category_chip_$cat")
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Content Items List
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = DarkGreen),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToLibrary() }
            .testTag("explore_open_nesmat_library_banner")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "🌿 مكتبة نسمة حياة + 🧯 خطط الإنقاذ",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "مش كل وجع بيبان… خلينا نفهم اللي جواك خطوة بخطوة (١٠ مقالات وتمارين تطبيقية)",
                style = MaterialTheme.typography.bodySmall,
                color = CreamBackground.copy(alpha = 0.9f)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Text(
                text = "اقرأ وجرب ←",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
          }
        }
      }

      if (contentList.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 20.dp)
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "🌱",
                fontSize = 36.sp
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "لم يتم العثور على محتوى مطابق للبحث",
                style = MaterialTheme.typography.titleMedium,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "جميع الأقسام والتمارين جاهزة؛ يمكنك النقر أدناه لعرض كل التمارين والمقالات فوراً",
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
              Spacer(modifier = Modifier.height(14.dp))
              androidx.compose.material3.Button(
                onClick = {
                  onCategorySelect("الكل")
                  onSearchChange("")
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("عرض جميع التمارين والمقالات 🌿", color = Color.White)
              }
            }
          }
        }
      } else {
        items(contentList, key = { it.id }) { item ->
          ContentCard(
            item = item,
            onClick = { onContentClick(item.id) },
            onToggleFavorite = { onToggleFavorite(item.id, item.isFavorite) }
          )
        }
      }
    }
  }
}

@Composable
fun ContentCard(
  item: ContentItem,
  onClick: () -> Unit,
  onToggleFavorite: () -> Unit,
  modifier: Modifier = Modifier
) {
  val typeIcon = when (item.contentType) {
    "workshop" -> Icons.Default.Extension
    "audio" -> Icons.Default.Headphones
    "video" -> Icons.Default.PlayCircle
    "exercise" -> Icons.Default.FitnessCenter
    else -> Icons.Default.Article
  }

  val typeName = when (item.contentType) {
    "workshop" -> "ورشة تطبيقية"
    "audio" -> "صوت"
    "video" -> "فيديو"
    "exercise" -> "تمرين"
    else -> "مقال"
  }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("content_card_${item.id}")
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
          // Category Badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(SageGreenPrimary.copy(alpha = 0.25f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = item.category,
              style = MaterialTheme.typography.labelMedium,
              color = DarkGreen,
              fontWeight = FontWeight.SemiBold
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Content type pill
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(WarmBeige.copy(alpha = 0.6f))
              .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = typeIcon,
              contentDescription = null,
              tint = DarkGreen,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = typeName,
              style = MaterialTheme.typography.labelSmall,
              color = DarkGreen
            )
          }

          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "• ${item.duration}",
            style = MaterialTheme.typography.labelSmall,
            color = TextDark.copy(alpha = 0.6f)
          )
        }

        IconButton(
          onClick = onToggleFavorite,
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = if (item.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = if (item.isFavorite) "محفوظ" else "حفظ",
            tint = if (item.isFavorite) DarkGreen else TextDark.copy(alpha = 0.4f)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = item.title,
        style = MaterialTheme.typography.titleMedium,
        color = DarkGreen,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = item.description,
        style = MaterialTheme.typography.bodyMedium,
        color = TextDark.copy(alpha = 0.8f),
        lineHeight = 22.sp,
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "مراجعة: ${item.reviewer}",
          style = MaterialTheme.typography.labelSmall,
          color = TextDark.copy(alpha = 0.55f)
        )

        Text(
          text = "اقرأ المزيد ←",
          style = MaterialTheme.typography.labelMedium,
          color = DarkGreen,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
