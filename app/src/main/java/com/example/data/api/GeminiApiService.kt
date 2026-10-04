package com.example.data.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

data class ChatMessage(
  val id: String = java.util.UUID.randomUUID().toString(),
  val role: String, // "user" or "model"
  val text: String,
  val timestamp: Long = System.currentTimeMillis(),
  val isGroundedWithSearch: Boolean = false,
  val searchSources: List<String> = emptyList()
)

data class GroundedChatResult(
  val text: String,
  val searchSources: List<String>
)

class GeminiApiService {
  private val client = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

  private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

  private val systemInstructionText = """
    أنت «رفيق نسمة الحياة»، مساعد ودود، إنساني، ودافئ للتوعية بالصحة النفسية والاستكشاف الذاتي الآمن باللغة العربية.
    مهمتك:
    - الاستماع بتعاطف عميق ومساندة المستخدم دون إصدار أحكام.
    - اقتراح تقنيات التهدئة البسيطة، تمارين التنفس، ومهارات الرأفة بالنفس.
    - عدم تشخيص أي مرض نفسي وعدم وصف أو تعديل أي أدوية.
    - إذا عبر المستخدم عن رغبة في إيذاء نفسه، كن رحيماً جداً وذكره بقيمته وحياته ووجهه فوراً للخط الساخن للصحة النفسية (16328) وخدمات الطوارئ.
  """.trimIndent()

  /**
   * Multi-turn Chat with Google Search Grounding using gemini-3.5-flash
   */
  suspend fun sendChatMessage(
    history: List<ChatMessage>,
    newMessage: String,
    useSearchGrounding: Boolean = true
  ): GroundedChatResult = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext GroundedChatResult(
        text = "مرحباً بك! للاستمتاع برفيق نسمة الحياة الذكي، يرجى ضبط مفتاح Gemini API Key في Secrets panel في AI Studio.",
        searchSources = emptyList()
      )
    }

    val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

    val rootJson = JSONObject()

    // System instruction
    val systemObj = JSONObject().apply {
      put("parts", JSONArray().apply {
        put(JSONObject().apply { put("text", systemInstructionText) })
      })
    }
    rootJson.put("systemInstruction", systemObj)

    // Contents (history + new message)
    val contentsArray = JSONArray()
    for (msg in history.takeLast(10)) {
      val contentObj = JSONObject().apply {
        put("role", msg.role)
        put("parts", JSONArray().apply {
          put(JSONObject().apply { put("text", msg.text) })
        })
      }
      contentsArray.put(contentObj)
    }

    // Append new user message
    val userMsgObj = JSONObject().apply {
      put("role", "user")
      put("parts", JSONArray().apply {
        put(JSONObject().apply { put("text", newMessage) })
      })
    }
    contentsArray.put(userMsgObj)
    rootJson.put("contents", contentsArray)

    // Google Search Grounding tool
    if (useSearchGrounding) {
      val toolsArray = JSONArray().apply {
        put(JSONObject().apply {
          put("googleSearch", JSONObject())
        })
      }
      rootJson.put("tools", toolsArray)
    }

    val body = rootJson.toString().toRequestBody(jsonMediaType)
    val request = Request.Builder().url(url).post(body).build()

    try {
      val response = client.newCall(request).execute()
      val responseString = response.body?.string().orEmpty()

      if (!response.isSuccessful) {
        return@withContext GroundedChatResult(
          text = "عذراً، حدث خطأ في التواصل مع رفيق نسمة الحياة ($responseString).",
          searchSources = emptyList()
        )
      }

      val resJson = JSONObject(responseString)
      val candidates = resJson.optJSONArray("candidates")
      val firstCandidate = candidates?.optJSONObject(0)
      val content = firstCandidate?.optJSONObject("content")
      val parts = content?.optJSONArray("parts")

      val textBuilder = StringBuilder()
      if (parts != null) {
        for (i in 0 until parts.length()) {
          val p = parts.optJSONObject(i)
          val t = p?.optString("text").orEmpty()
          textBuilder.append(t)
        }
      }

      // Extract search grounding metadata if present
      val sources = mutableListOf<String>()
      val groundingMetadata = firstCandidate?.optJSONObject("groundingMetadata")
      val webSearchQueries = groundingMetadata?.optJSONArray("webSearchQueries")
      if (webSearchQueries != null) {
        for (i in 0 until webSearchQueries.length()) {
          sources.add(webSearchQueries.optString(i))
        }
      }
      val groundingChunks = groundingMetadata?.optJSONArray("groundingChunks")
      if (groundingChunks != null) {
        for (i in 0 until groundingChunks.length()) {
          val chunk = groundingChunks.optJSONObject(i)
          val web = chunk?.optJSONObject("web")
          val title = web?.optString("title").orEmpty()
          if (title.isNotBlank() && !sources.contains(title)) {
            sources.add(title)
          }
        }
      }

      val finalText = if (textBuilder.isNotEmpty()) textBuilder.toString() else "لم أستطع صياغة رد في هذه اللحظة، هل تود إعادة السؤال؟"
      GroundedChatResult(text = finalText, searchSources = sources)
    } catch (e: Exception) {
      GroundedChatResult(
        text = "تعذر الاتصال بالخادم: ${e.localizedMessage ?: "تأكد من اتصال الإنترنت"}",
        searchSources = emptyList()
      )
    }
  }

  /**
   * Create or edit calming images using gemini-3.1-flash-image-preview
   */
  suspend fun generateCalmingImage(
    prompt: String,
    referenceBitmap: Bitmap? = null
  ): Bitmap? = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") return@withContext null

    val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-image-preview:generateContent?key=$apiKey"

    val rootJson = JSONObject()
    val partsArray = JSONArray()

    val enrichedPrompt = "Serene, tranquil, warm and peaceful art style for mental wellness and inner peace: $prompt. Soft sage green #8FAF9A, dark moss green, warm cream and golden sunlight tones, soothing aesthetics."
    partsArray.put(JSONObject().apply { put("text", enrichedPrompt) })

    if (referenceBitmap != null) {
      val outputStream = ByteArrayOutputStream()
      referenceBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
      val base64Data = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
      val inlineObj = JSONObject().apply {
        put("mimeType", "image/jpeg")
        put("data", base64Data)
      }
      partsArray.put(JSONObject().apply { put("inlineData", inlineObj) })
    }

    val contentObj = JSONObject().apply {
      put("parts", partsArray)
    }
    rootJson.put("contents", JSONArray().apply { put(contentObj) })

    val genConfig = JSONObject().apply {
      put("responseModalities", JSONArray().apply {
        put("IMAGE")
      })
      put("imageConfig", JSONObject().apply {
        put("aspectRatio", "1:1")
        put("imageSize", "1K")
      })
    }
    rootJson.put("generationConfig", genConfig)

    val body = rootJson.toString().toRequestBody(jsonMediaType)
    val request = Request.Builder().url(url).post(body).build()

    try {
      val response = client.newCall(request).execute()
      val responseString = response.body?.string().orEmpty()
      if (!response.isSuccessful) return@withContext null

      val resJson = JSONObject(responseString)
      val candidate = resJson.optJSONArray("candidates")?.optJSONObject(0)
      val parts = candidate?.optJSONObject("content")?.optJSONArray("parts")

      if (parts != null) {
        for (i in 0 until parts.length()) {
          val part = parts.optJSONObject(i)
          val inlineData = part?.optJSONObject("inlineData")
          val dataStr = inlineData?.optString("data")
          if (!dataStr.isNullOrBlank()) {
            val bytes = Base64.decode(dataStr, Base64.DEFAULT)
            return@withContext BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
          }
        }
      }
      null
    } catch (_: Exception) {
      null
    }
  }

  /**
   * Transcribe recorded audio using gemini-3.5-transcribe
   */
  suspend fun transcribeAudio(
    audioBytes: ByteArray,
    mimeType: String = "audio/mp4"
  ): String = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext "يرجى تهيئة مفتاح Gemini API Key في Secrets panel."
    }

    val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-transcribe:generateContent?key=$apiKey"

    val base64Audio = Base64.encodeToString(audioBytes, Base64.NO_WRAP)

    val rootJson = JSONObject()
    val partsArray = JSONArray()

    partsArray.put(JSONObject().apply {
      put("text", "يرجى تحويل هذا التسجيل الصوتي باللغة العربية إلى نص مكتوب بدقة ووضوح.")
    })

    val inlineObj = JSONObject().apply {
      put("mimeType", mimeType)
      put("data", base64Audio)
    }
    partsArray.put(JSONObject().apply { put("inlineData", inlineObj) })

    val contentObj = JSONObject().apply { put("parts", partsArray) }
    rootJson.put("contents", JSONArray().apply { put(contentObj) })

    val body = rootJson.toString().toRequestBody(jsonMediaType)
    val request = Request.Builder().url(url).post(body).build()

    try {
      val response = client.newCall(request).execute()
      val responseString = response.body?.string().orEmpty()
      if (!response.isSuccessful) {
        return@withContext "عذراً، فشل تحويل الصوت إلى نص ($responseString)."
      }

      val resJson = JSONObject(responseString)
      val candidate = resJson.optJSONArray("candidates")?.optJSONObject(0)
      val parts = candidate?.optJSONObject("content")?.optJSONArray("parts")
      val text = parts?.optJSONObject(0)?.optString("text").orEmpty()
      if (text.isNotBlank()) text else "لم يتم التعرف على أي كلمات في التسجيل."
    } catch (e: Exception) {
      "خطأ في التحويل الصوتي: ${e.localizedMessage ?: "حدث خطأ غير متوقع"}"
    }
  }
}
