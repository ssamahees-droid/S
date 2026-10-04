package com.example.lib.storage

import android.content.Context
import android.content.Intent
import com.example.data.model.AssessmentResult
import com.example.data.model.DailyCheckin
import com.example.data.model.PersonalNote
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DataExportHelper {

  fun generateExportJson(
    checkins: List<DailyCheckin>,
    notes: List<PersonalNote>,
    assessments: List<AssessmentResult>
  ): String {
    val root = JSONObject()
    root.put("appName", "نسمة الحياة - Nesmat Al Hayat")
    root.put("exportDate", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
    root.put("version", "1.0-MVP")

    // Checkins
    val checkinsArray = JSONArray()
    for (c in checkins) {
      val obj = JSONObject().apply {
        put("date", c.date)
        put("mood", c.moodValue)
        put("note", c.note ?: "")
        put("timestamp", c.timestamp)
      }
      checkinsArray.put(obj)
    }
    root.put("dailyCheckins", checkinsArray)

    // Notes
    val notesArray = JSONArray()
    for (n in notes) {
      val obj = JSONObject().apply {
        put("title", n.title)
        put("content", n.content)
        put("timestamp", n.timestamp)
      }
      notesArray.put(obj)
    }
    root.put("personalNotes", notesArray)

    // Assessments
    val assessmentsArray = JSONArray()
    for (a in assessments) {
      val obj = JSONObject().apply {
        put("type", a.assessmentType)
        put("score", a.score)
        put("maxScore", a.maxScore)
        put("title", a.resultTitle)
        put("recommendation", a.recommendation)
        put("timestamp", a.timestamp)
      }
      assessmentsArray.put(obj)
    }
    root.put("assessments", assessmentsArray)

    return root.toString(2)
  }

  fun shareExportedData(
    context: Context,
    checkins: List<DailyCheckin>,
    notes: List<PersonalNote>,
    assessments: List<AssessmentResult>
  ) {
    val summaryText = buildString {
      appendLine("🌱 تقرير وتصدير بيانات رحلتي - تطبيق نسمة الحياة")
      appendLine("تاريخ التصدير: ${SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())}")
      appendLine("--------------------------------------")
      appendLine("• إجمالي أيام تسجيل المشاعر: ${checkins.size} يوماً")
      appendLine("• إجمالي الخواطر المدونة: ${notes.size} خاطرة")
      appendLine("• إجمالي التقييمات الذاتية: ${assessments.size} تقييم")
      appendLine("--------------------------------------")
      appendLine("بيانات JSON المشفرة والآمنة للنسخ الاحتياطي:")
      appendLine(generateExportJson(checkins, notes, assessments))
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_SUBJECT, "نسخة احتياطية من بيانات نسمة الحياة")
      putExtra(Intent.EXTRA_TEXT, summaryText)
    }
    context.startActivity(Intent.createChooser(intent, "تصدير ومشاركة بياناتي"))
  }

  /**
   * تصدير ملخص «خطة نسمة الحياة الخاصة بي» والتمارين كبطاقة نصية منسقة
   * جاهزة للمشاركة مع الأخصائي أو الطبيب النفسي أو الاحتفاظ بها (بدون أي تشخيص طبي).
   */
  fun shareSpecialistSummaryReport(
    context: Context,
    checkins: List<DailyCheckin>,
    notes: List<PersonalNote>,
    completedLibraryCount: Int,
    totalLibraryCount: Int
  ) {
    val dateNow = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
    val personalPlanNote = notes.firstOrNull { it.title.contains("خطة نسمة الحياة الخاصة بي") }
    val sleepNote = notes.firstOrNull { it.title.contains("سجل النوم") }
    val recentRightNowNotes = notes.filter { it.title.contains("أنا دلوقتي") }.take(3)
    val recentLibraryNotes = notes.filter { it.title.contains("تمرين مكتبة نسمة حياة") }.take(3)

    val reportText = buildString {
      appendLine("🌿 بطاقة متابعة شخصية — برنامج «نسمة الحياة»")
      appendLine("تاريخ إعداد البطاقة: $dateNow")
      appendLine("(ملاحظة: هذا الملخص وصفي شخصي لمساعدتي في التعبير عما أمر به، وليس تشخيصًا طبيًا)")
      appendLine("======================================")
      appendLine("📊 نظرة عامة:")
      appendLine("• عدد أيام تسجيل الحالة الشعورية مؤخرًا: ${checkins.size} يوم")
      appendLine("• التمارين المكتملة في مكتبة نسمة حياة: $completedLibraryCount من $totalLibraryCount")
      if (checkins.isNotEmpty()) {
        val recentMoods = checkins.take(5).joinToString(" ، ") { "${it.date}: ${it.moodValue}" }
        appendLine("• آخر الحالات المسجلة: $recentMoods")
      }
      appendLine("--------------------------------------")
      if (personalPlanNote != null) {
        appendLine("📘 خطة نسمة الحياة الخاصة بي:")
        appendLine(personalPlanNote.content)
        appendLine("--------------------------------------")
      }
      if (sleepNote != null) {
        appendLine("🌙 آخر ملاحظة من سجل النوم:")
        appendLine(sleepNote.content)
        appendLine("--------------------------------------")
      }
      if (recentRightNowNotes.isNotEmpty()) {
        appendLine("🌿 محطات «أنا دلوقتي...» الأخيرة:")
        recentRightNowNotes.forEach { n ->
          appendLine("• ${n.title}:")
          appendLine(n.content)
        }
        appendLine("--------------------------------------")
      }
      if (recentLibraryNotes.isNotEmpty()) {
        appendLine("🧩 أبرز التمارين المكتملة في المكتبة:")
        recentLibraryNotes.forEach { n ->
          appendLine("• ${n.title}:")
          appendLine(n.content)
        }
        appendLine("--------------------------------------")
      }
      appendLine("نسمة الحياة — نحو مجتمع يهتم بالصحة النفسية كما يهتم بالصحة الجسدية.")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_SUBJECT, "ملخص خطتي في نسمة الحياة ($dateNow)")
      putExtra(Intent.EXTRA_TEXT, reportText)
    }
    context.startActivity(Intent.createChooser(intent, "مشاركة أو حفظ ملخص خطتي وملاحظاتي"))
  }
}
