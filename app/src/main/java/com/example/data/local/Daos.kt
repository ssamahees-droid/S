package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AssessmentResult
import com.example.data.model.ContentItem
import com.example.data.model.DailyCheckin
import com.example.data.model.PersonalNote
import com.example.data.model.SupportRequest
import com.example.data.model.UserPreference
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
  @Query("SELECT * FROM content_items WHERE reviewStatus = 'Published' ORDER BY id ASC")
  fun getPublishedContent(): Flow<List<ContentItem>>

  @Query("SELECT * FROM content_items ORDER BY id DESC")
  fun getAllContentForAdmin(): Flow<List<ContentItem>>

  @Query("SELECT * FROM content_items WHERE id = :id")
  fun getContentById(id: Long): Flow<ContentItem?>

  @Query("SELECT * FROM content_items WHERE isFavorite = 1")
  fun getFavoriteContent(): Flow<List<ContentItem>>

  @Query("SELECT * FROM content_items WHERE reviewStatus = 'Published' AND isSuggested = 1")
  fun getSuggestedContent(): Flow<List<ContentItem>>

  @Query("SELECT * FROM content_items WHERE reviewStatus = 'Published' AND (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%')")
  fun searchContent(query: String): Flow<List<ContentItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: ContentItem): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(items: List<ContentItem>)

  @Update
  suspend fun update(item: ContentItem)

  @Delete
  suspend fun delete(item: ContentItem)

  @Query("UPDATE content_items SET isFavorite = :isFav WHERE id = :id")
  suspend fun updateFavorite(id: Long, isFav: Boolean)

  @Query("SELECT COUNT(*) FROM content_items")
  suspend fun getCount(): Int
}

@Dao
interface CheckinDao {
  @Query("SELECT * FROM daily_checkins ORDER BY timestamp DESC")
  fun getAllCheckins(): Flow<List<DailyCheckin>>

  @Query("SELECT * FROM daily_checkins WHERE date = :date LIMIT 1")
  fun getCheckinByDate(date: String): Flow<DailyCheckin?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCheckin(checkin: DailyCheckin): Long

  @Query("DELETE FROM daily_checkins WHERE id = :id")
  suspend fun deleteCheckin(id: Long)

  @Query("DELETE FROM daily_checkins")
  suspend fun clearAll()
}

@Dao
interface NoteDao {
  @Query("SELECT * FROM personal_notes ORDER BY timestamp DESC")
  fun getAllNotes(): Flow<List<PersonalNote>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNote(note: PersonalNote): Long

  @Query("DELETE FROM personal_notes WHERE id = :id")
  suspend fun deleteNote(id: Long)

  @Query("DELETE FROM personal_notes")
  suspend fun clearAll()
}

@Dao
interface AssessmentDao {
  @Query("SELECT * FROM assessment_results ORDER BY timestamp DESC")
  fun getAllAssessments(): Flow<List<AssessmentResult>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAssessment(assessment: AssessmentResult): Long

  @Query("DELETE FROM assessment_results WHERE id = :id")
  suspend fun deleteAssessment(id: Long)

  @Query("DELETE FROM assessment_results")
  suspend fun clearAll()
}

@Dao
interface SupportDao {
  @Query("SELECT * FROM support_requests ORDER BY createdAt DESC")
  fun getAllRequests(): Flow<List<SupportRequest>>

  @Query("SELECT * FROM support_requests WHERE id = :id")
  fun getRequestById(id: Long): Flow<SupportRequest?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRequest(request: SupportRequest): Long

  @Update
  suspend fun updateRequest(request: SupportRequest)

  @Query("DELETE FROM support_requests WHERE id = :id")
  suspend fun deleteRequest(id: Long)

  @Query("DELETE FROM support_requests")
  suspend fun clearAll()
}

@Dao
interface UserPrefDao {
  @Query("SELECT * FROM user_preferences WHERE id = 1")
  fun getPreferences(): Flow<UserPreference?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun savePreferences(pref: UserPreference)

  @Query("DELETE FROM user_preferences")
  suspend fun clearAll()
}
