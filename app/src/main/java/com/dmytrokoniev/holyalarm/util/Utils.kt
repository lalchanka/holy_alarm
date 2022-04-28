package com.dmytrokoniev.holyalarm.util

import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT

/**
 * A convenient function to show a [Toast] using any [View] and it's [Context].
 */
fun View.toast(
    text: String,
    duration: Int = LENGTH_SHORT
) = Toast.makeText(context, text, duration).show()