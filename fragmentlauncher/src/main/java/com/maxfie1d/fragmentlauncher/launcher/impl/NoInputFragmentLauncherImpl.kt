package com.maxfie1d.fragmentlauncher.launcher.impl

import com.maxfie1d.fragmentlauncher.launcher.NoInputFragmentLauncher
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

internal class NoInputFragmentLauncherImpl(
    private val newInstance: () -> DialogFragment,
    private val fragmentManager: Lazy<FragmentManager>
) : NoInputFragmentLauncher {
    override fun launch() = launch(tag = "")

    override fun launch(tag: String) {
        newInstance().show(fragmentManager.value, tag)
    }
}
