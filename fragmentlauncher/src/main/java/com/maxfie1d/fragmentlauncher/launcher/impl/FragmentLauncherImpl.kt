package com.maxfie1d.fragmentlauncher.launcher.impl

import com.maxfie1d.fragmentlauncher.launcher.FragmentLauncher
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

internal class FragmentLauncherImpl<I>(
    private val newInstance: (input: I) -> DialogFragment,
    private val fragmentManager: Lazy<FragmentManager>
) : FragmentLauncher<I> {
    override fun launch(input: I) = launch(input, tag = "")

    override fun launch(input: I, tag: String) {
        newInstance(input).show(fragmentManager.value, tag)
    }
}
