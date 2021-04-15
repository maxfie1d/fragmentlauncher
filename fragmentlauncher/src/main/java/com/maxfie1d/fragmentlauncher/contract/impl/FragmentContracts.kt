package com.maxfie1d.fragmentlauncher.contract.impl

import com.maxfie1d.fragmentlauncher.contract.Constants
import com.maxfie1d.fragmentlauncher.contract.FragmentContract
import android.os.Bundle
import androidx.core.os.bundleOf

class FragmentContracts {
    @Suppress("UNCHECKED_CAST")
    abstract class Default<I> : FragmentContract<I> {
        override fun parseInput(data: Bundle): I {
            return data.getSerializable(Constants.FRAGMENT_INPUT_KEY.name) as I
        }

        override fun createInputBundle(input: I): Bundle {
            return bundleOf(Constants.FRAGMENT_INPUT_KEY.name to input)
        }
    }

    abstract class NoInput : Default<Unit>() {
        override fun parseInput(data: Bundle) = Unit

        override fun createInputBundle(input: Unit): Bundle = Bundle()
    }
}
