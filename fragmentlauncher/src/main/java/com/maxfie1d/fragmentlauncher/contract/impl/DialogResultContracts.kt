package com.maxfie1d.fragmentlauncher.contract.impl

import com.maxfie1d.fragmentlauncher.contract.*
import android.os.Bundle
import androidx.core.os.bundleOf
import com.maxfie1d.fragmentlauncher.contract.FragmentResultContract
import com.maxfie1d.fragmentlauncher.contract.FragmentResultContractParcelable
import com.maxfie1d.fragmentlauncher.contract.InputPayload
import java.io.Serializable

private fun computeKey(id: String, tag: String): String {
    return "${id}_tag_$tag"
}

class DialogResultContracts {
    @Suppress("UNCHECKED_CAST")
    abstract class Default<I : Serializable, O : Serializable>(override val id: String) : FragmentResultContract<I, O> {
        override fun parseInput(data: Bundle): InputPayload<I> {
            val tag = data.getString(Constants.FRAGMENT_TAG_KEY.name) ?: ""
            return InputPayload(
                key = computeKey(id, tag),
                data = data.getSerializable(Constants.FRAGMENT_INPUT_KEY.name) as I
            )
        }

        override fun createInputBundle(input: I, tag: String): Bundle {
            return bundleOf(
                Constants.FRAGMENT_INPUT_KEY.name to input,
                Constants.FRAGMENT_TAG_KEY.name to tag
            )
        }

        override fun parseResult(data: Bundle): O? {
            return data.getSerializable(Constants.FRAGMENT_RESULT_KEY.name) as? O
        }

        override fun createResultBundle(result: O): Bundle {
            return bundleOf(Constants.FRAGMENT_RESULT_KEY.name to result)
        }

        override fun computeRequestKey(tag: String): String {
            return computeKey(id, tag)
        }
    }

    abstract class NoInput<O : Serializable>(override val id: String) : NoInputFragmentResultContract<O> {
        override fun parseInput(data: Bundle): InputPayload<Int> {
            val tag = data.getString(Constants.FRAGMENT_TAG_KEY.name) ?: ""
            return InputPayload(
                key = computeKey(id, tag),
                data = 0 // 便宜上無意味な値を入れている
            )
        }

        override fun createInputBundle(input: Int, tag: String): Bundle {
            // inputは使用しないので無視
            return bundleOf(
                Constants.FRAGMENT_TAG_KEY.name to tag
            )
        }

        override fun parseResult(data: Bundle): O? {
            return data.getSerializable(Constants.FRAGMENT_RESULT_KEY.name) as? O
        }

        override fun createResultBundle(result: O): Bundle {
            return bundleOf(Constants.FRAGMENT_RESULT_KEY.name to result)
        }

        override fun computeRequestKey(tag: String): String {
            return computeKey(id, tag)
        }
    }

    @Suppress("UNCHECKED_CAST")
    abstract class Parcelable<I : android.os.Parcelable, O : Serializable>(override val id: String) :
        FragmentResultContractParcelable<I, O> {
        override fun parseInput(data: Bundle): InputPayload<I> {
            val tag = data.getString(Constants.FRAGMENT_TAG_KEY.name) ?: ""
            return InputPayload(
                key = computeKey(id, tag),
                data = data.getParcelable<I>(Constants.FRAGMENT_INPUT_KEY.name) as I
            )
        }

        override fun createInputBundle(input: I, tag: String): Bundle {
            return bundleOf(
                Constants.FRAGMENT_INPUT_KEY.name to input,
                Constants.FRAGMENT_TAG_KEY.name to tag
            )
        }

        override fun parseResult(data: Bundle): O? {
            return data.getSerializable(Constants.FRAGMENT_RESULT_KEY.name) as? O
        }

        override fun createResultBundle(result: O): Bundle {
            return bundleOf(Constants.FRAGMENT_RESULT_KEY.name to result)
        }

        override fun computeRequestKey(tag: String): String {
            return computeKey(id, tag)
        }
    }
}
