package com.maxfie1d.fragmentlauncher.contract

import android.os.Bundle
import androidx.fragment.app.DialogFragment

interface FragmentContract<I> {
    fun factory(): DialogFragment

    fun parseInput(data: Bundle): I

    fun createInputBundle(input: I): Bundle
}
