package com.dmytrokoniev.holyalarm.util

import io.reactivex.Single

interface ITextProvider {

    fun provideText(verse: Int, line: Int): Single<String>
}

class TextProvider: ITextProvider {

    override fun provideText(verse: Int, line: Int): Single<String> = Single.create { emitter ->
        emitter.onSuccess(DEFAULT_TEXT)
    }

    companion object {
        const val DEFAULT_TEXT = "возрадуйся"
    }
}
