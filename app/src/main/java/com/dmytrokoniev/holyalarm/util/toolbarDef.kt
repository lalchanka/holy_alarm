package com.dmytrokoniev.holyalarm.util

import android.view.View
import androidx.core.view.isVisible
import com.dmytrokoniev.holyalarm.R

enum class ToolbarState {
    ICON_CLEAN,
    CONFIRM_CANCEL
}

object ToolbarStateManager {

    fun onStateChanged(toolbar: View, state: ToolbarState) {
        cleanState(toolbar)
        when (state) {
            ToolbarState.ICON_CLEAN -> onIconCleanState(toolbar)
            ToolbarState.CONFIRM_CANCEL -> onConfirmCancelState(toolbar)
        }
    }

    private fun onIconCleanState(toolbar: View) {
        toolbar.run {
            val ivCross = findViewById<View>(R.id.iv_cross)
            ivCross.isVisible = true
        }
    }

    private fun onConfirmCancelState(toolbar: View) {
        toolbar.run {
            val btnCancel = findViewById<View>(R.id.btn_cancel)
            val btnConfirm = findViewById<View>(R.id.btn_confirm)
            val ivCross = findViewById<View>(R.id.iv_cross)

            btnCancel.isVisible = true
            btnConfirm.isVisible = true
            ivCross.isVisible = true
        }
    }

    private fun cleanState(toolbar: View) {
        toolbar.run {
            val btnCancel = findViewById<View>(R.id.btn_cancel)
            val btnConfirm = findViewById<View>(R.id.btn_confirm)
            val ivCross = findViewById<View>(R.id.iv_cross)

            btnCancel.isVisible = false
            btnConfirm.isVisible = false
            ivCross.isVisible = false
        }
    }
}
