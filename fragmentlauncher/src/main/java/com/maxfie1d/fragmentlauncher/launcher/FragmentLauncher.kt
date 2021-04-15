package com.maxfie1d.fragmentlauncher.launcher

interface FragmentLauncher<I> {
    fun launch(input: I)
    fun launch(input: I, tag: String)
}
