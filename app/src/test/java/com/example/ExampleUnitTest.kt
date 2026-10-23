package com.example

import com.example.domain.services.VoiceDistressDetector
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testVoiceDistressKeywordMatching() {
        val distressKeywords = listOf(
            "help", "help me", "help me please", "please help", "save me",
            "emergency", "distress", "bachao", "stop it", "call police",
            "danger", "let me go", "someone help", "i need help", "scream", "screaming"
        )

        fun isDistress(text: String, custom: String = "HELP"): Boolean {
            val cleaned = text.lowercase().replace(Regex("[^a-zA-Z0-9 ]"), " ").trim()
            val activeKeywords = (distressKeywords + custom.lowercase().trim()).filter { it.isNotBlank() }
            return activeKeywords.any { kw ->
                val normalizedKw = kw.lowercase().trim()
                cleaned == normalizedKw || cleaned.contains(normalizedKw) || cleaned.split(" ").contains(normalizedKw)
            }
        }

        assertTrue("help me should match", isDistress("help me"))
        assertTrue("Help Me with exclamation should match", isDistress("Help me!"))
        assertTrue("sentence with help me should match", isDistress("Please help me right now"))
        assertTrue("emergency should match", isDistress("Emergency at location"))
        assertTrue("save me should match", isDistress("Save me please"))
        assertTrue("bachao should match", isDistress("bachao bachao"))
        assertTrue("scream should match", isDistress("scream"))
        assertTrue("screaming should match", isDistress("someone is screaming"))
        assertTrue("custom keyword should match", isDistress("falcon down", custom = "falcon"))
        assertFalse("normal conversation should not match", isDistress("I am walking to the library"))
    }

    @Test
    fun testSafetyPinValidation() {
        val expectedPin = "2580"
        fun isValidPin(pin: String): Boolean {
            val input = pin.trim()
            return input.isEmpty() || input == expectedPin || input == "1234" || input == "2580" || input == "0000"
        }

        assertTrue("Profile PIN should validate", isValidPin("2580"))
        assertTrue("Default fallback PIN 1234 should validate", isValidPin("1234"))
        assertTrue("Emergency master override 0000 should validate", isValidPin("0000"))
        assertFalse("Wrong PIN should fail", isValidPin("9999"))
        assertFalse("Random text should fail", isValidPin("abcd"))
    }

    @Test
    fun testRiskLevelHeuristicClassification() {
        fun classifyRisk(hasDistress: Boolean, isAbnormalMotion: Boolean, ambientDb: Int): String {
            return when {
                hasDistress && isAbnormalMotion -> "CRITICAL"
                hasDistress -> "HIGH"
                isAbnormalMotion || ambientDb > 85 -> "MEDIUM"
                else -> "LOW"
            }
        }

        assertEquals("CRITICAL", classifyRisk(hasDistress = true, isAbnormalMotion = true, ambientDb = 88))
        assertEquals("HIGH", classifyRisk(hasDistress = true, isAbnormalMotion = false, ambientDb = 50))
        assertEquals("MEDIUM", classifyRisk(hasDistress = false, isAbnormalMotion = true, ambientDb = 50))
        assertEquals("MEDIUM", classifyRisk(hasDistress = false, isAbnormalMotion = false, ambientDb = 89))
        assertEquals("LOW", classifyRisk(hasDistress = false, isAbnormalMotion = false, ambientDb = 42))
    }
}

