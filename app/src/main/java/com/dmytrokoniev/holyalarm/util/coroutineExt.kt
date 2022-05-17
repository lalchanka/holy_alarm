package com.dmytrokoniev.holyalarm.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

/**
 * Launches a new coroutine with respect of [Fragment]'s lifecycle. See [lifecycleScope] for
 * more information.
 */
fun Fragment.launchInFragmentScope(block: suspend () -> Unit) =
    viewLifecycleOwner.lifecycleScope.launch { block() }

/**
 * Launches a new coroutine with respect of [AppCompatActivity]'s lifecycle. See [lifecycleScope]
 * for more information.
 */
fun AppCompatActivity.launchInActivityScope(block: suspend () -> Unit) =
    lifecycleScope.launch { block() }
