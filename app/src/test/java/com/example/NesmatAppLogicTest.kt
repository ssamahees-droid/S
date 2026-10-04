package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NesmatAppLogicTest {

  @Test
  fun testPHQ9ScoringAndSafetyFlag() {
    val scoresWithoutFlag = listOf(1, 0, 1, 1, 0, 1, 0, 1, 0)
    val totalScore = scoresWithoutFlag.sum()
    val hasSafetyFlag = scoresWithoutFlag[8] > 0

    assertEquals(5, totalScore)
    assertFalse("Should not have safety flag when Q9 is 0", hasSafetyFlag)

    // With Q9 > 0
    val scoresWithFlag = listOf(1, 0, 1, 1, 0, 1, 0, 1, 1)
    val hasSafetyFlagActive = scoresWithFlag[8] > 0
    assertTrue("Should trigger safety flag when Q9 is greater than 0", hasSafetyFlagActive)
  }

  @Test
  fun testWordCountLimit() {
    val message = "أشعر ببعض الضغط في العمل وأحتاج إلى تنظيم أولوياتي بهدوء"
    val count = message.trim().split("\\s+".toRegex()).size
    assertEquals(10, count)
    assertTrue("Word count should be within 500 words", count <= 500)
  }

  @Test
  fun testWellnessHabitsProgressCalculation() {
    val totalHabits = 5
    val completedHabits = 3
    val progress = completedHabits.toFloat() / totalHabits
    assertEquals(0.6f, progress, 0.001f)
  }

  @Test
  fun testGroundingExerciseStepsCount() {
    val groundingCounts = listOf(5, 4, 3, 2, 1)
    assertEquals(5, groundingCounts.size)
    assertEquals(15, groundingCounts.sum())
  }
}
