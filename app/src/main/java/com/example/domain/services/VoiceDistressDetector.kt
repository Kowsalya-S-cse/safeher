package com.example.domain.services

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class VoiceMonitoringState(
    val isListening: Boolean = false,
    val currentDecibels: Int = 38,
    val lastRecognizedText: String = "",
    val detectedDistressWord: String? = null,
    val distressConfidencePercent: Int = 0,
    val isDistressTriggered: Boolean = false,
    val waveformAmplitudes: List<Float> = listOf(0.1f, 0.2f, 0.35f, 0.25f, 0.15f, 0.3f, 0.45f, 0.2f, 0.1f, 0.25f, 0.4f, 0.3f, 0.15f, 0.2f, 0.1f, 0.05f)
)

class VoiceDistressDetector(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private val tag = "VoiceDistressDetector"

    private val _monitoringState = MutableStateFlow(VoiceMonitoringState())
    val monitoringState: StateFlow<VoiceMonitoringState> = _monitoringState.asStateFlow()

    private var waveformAnimationJob: Job? = null

    private val defaultDistressKeywords = listOf(
        "help",
        "help me",
        "help me please",
        "please help",
        "please help me",
        "save me",
        "emergency",
        "distress",
        "bachao",
        "stop it",
        "call police",
        "call 911",
        "call 112",
        "danger",
        "let me go",
        "someone help",
        "someone help me",
        "i need help",
        "under attack",
        "attack",
        "scream",
        "screaming",
        "screams",
        "shriek",
        "ayuda",
        "socorro"
    )

    var customKeyword: String = "HELP"
    var onHighConfidenceDistressDetected: ((String, Int) -> Unit)? = null

    fun startListening(userCustomKeyword: String = "HELP") {
        customKeyword = userCustomKeyword.trim().ifEmpty { "HELP" }
        _monitoringState.value = _monitoringState.value.copy(
            isListening = true,
            isDistressTriggered = false,
            currentDecibels = 38
        )
        Log.d(tag, "Voice distress detection armed for keyword: $customKeyword")
        startWaveformAnimation()
    }

    private fun startWaveformAnimation() {
        waveformAnimationJob?.cancel()
        waveformAnimationJob = scope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(350)
                if (!_monitoringState.value.isListening) break
                if (!_monitoringState.value.isDistressTriggered) {
                    val ambientDb = (35..45).random()
                    val dynamicAmps = List(16) { 0.12f + ((2..28).random() / 100f) }
                    _monitoringState.value = _monitoringState.value.copy(
                        currentDecibels = ambientDb,
                        waveformAmplitudes = dynamicAmps
                    )
                }
            }
        }
    }

    fun stopListening() {
        waveformAnimationJob?.cancel()
        waveformAnimationJob = null
        _monitoringState.value = _monitoringState.value.copy(
            isListening = false,
            isDistressTriggered = false
        )
        Log.d(tag, "Voice distress detection stopped")
    }

    fun processSpeechText(text: String): Boolean {
        val cleaned = text.lowercase().replace(Regex("[^a-zA-Z0-9 ]"), " ").trim()
        _monitoringState.value = _monitoringState.value.copy(lastRecognizedText = text)

        val activeKeywords = (defaultDistressKeywords + customKeyword.lowercase().trim())
            .filter { it.isNotBlank() }

        val matchedKeyword = activeKeywords.firstOrNull { kw ->
            val normalizedKw = kw.lowercase().trim()
            cleaned == normalizedKw ||
                cleaned.contains(normalizedKw) ||
                cleaned.split(" ").contains(normalizedKw)
        }

        if (matchedKeyword != null) {
            val confidence = if (cleaned == matchedKeyword) 98 else 90
            _monitoringState.value = _monitoringState.value.copy(
                detectedDistressWord = matchedKeyword.uppercase(),
                distressConfidencePercent = confidence,
                isDistressTriggered = true
            )
            onHighConfidenceDistressDetected?.invoke(matchedKeyword.uppercase(), confidence)
            return true
        }
        return false
    }

    fun triggerSimulatedDistressPhrase(phrase: String = "HELP ME") {
        val isScream = phrase.equals("SCREAM", ignoreCase = true)
        val decibels = if (isScream) 88 else 76
        val spikeAmplitudes = listOf(0.85f, 0.95f, 0.78f, 0.92f, 0.65f, 0.88f, 0.98f, 0.82f, 0.75f, 0.9f, 0.84f, 0.7f, 0.88f, 0.94f, 0.8f, 0.65f)
        _monitoringState.value = _monitoringState.value.copy(
            currentDecibels = decibels,
            waveformAmplitudes = spikeAmplitudes
        )
        processSpeechText(phrase)
    }
}
