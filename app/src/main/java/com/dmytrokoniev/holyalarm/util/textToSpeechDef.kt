package com.dmytrokoniev.holyalarm.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import io.reactivex.Single
import java.util.*

interface ITextProvider {

    fun provideText(verse: Int, line: Int): Single<String>
}

interface ITextToSpeechPlayer {

    fun initialize(context: Context)

    fun playSpeechFromText(text: String)

    fun dispose()
}

class StandartTextToSpeechPlayer : ITextToSpeechPlayer, TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var context: Context? = null
    private var isTtsInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Log.e(this::class.simpleName, "The Language not supported!")
            } else {
                isTtsInitialized = true
            }
        } else {
            Log.w(this::class.simpleName, "TTS was not initialized")
        }
    }

    override fun initialize(context: Context) {
        this.context = context
        tts = TextToSpeech(context, this)
    }

    override fun playSpeechFromText(text: String) {
        if (isTtsInitialized) tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    override fun dispose() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        context = null
        tts = null
    }
}

class TextProvider : ITextProvider {

    override fun provideText(verse: Int, line: Int): Single<String> = Single.create { emitter ->
        emitter.onSuccess(DEFAULT_TEXT)
    }

    companion object {
        const val DEFAULT_TEXT = "возрадуйся"
    }
}
