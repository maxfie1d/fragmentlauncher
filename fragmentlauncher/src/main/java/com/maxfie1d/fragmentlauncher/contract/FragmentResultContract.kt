package com.maxfie1d.fragmentlauncher.contract

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import java.io.Serializable

interface FragmentResultContractBase<I, O> {
    /**
     * DialogResultContractを区別する名前のようなもの
     * requestKeyの中にこのidが含められる
     */
    val id: String

    /**
     * FragmentResultListenerに指定するrequestKeyを返す
     */
    fun computeRequestKey(tag: String): String

    /**
     * 起動したいDialogFragmentのファクトリ
     */
    fun factory(): DialogFragment

    /**
     * ダイアログに渡されたBundleを復元する
     */
    fun parseInput(data: Bundle): InputPayload<I>

    /**
     * ダイアログに渡したいデータをBundle化する
     */
    fun createInputBundle(input: I, tag: String): Bundle

    /**
     * ダイアログの呼び出し元に返す結果をBundle化する
     */
    fun createResultBundle(result: O): Bundle

    /**
     * ダイアログかわ渡された結果のBundleを復元する
     */
    fun parseResult(data: Bundle): O?
}

interface FragmentResultContract<I : Serializable, O : Serializable> : FragmentResultContractBase<I, O>

interface FragmentResultContractParcelable<I : Parcelable, O : Serializable> : FragmentResultContractBase<I, O>

/**
 * 入力を受け取らないDialogResultContract
 * 便宜上入力の型をIntにしているが実際にはその値は使用されない
 */
typealias NoInputFragmentResultContract<O> = FragmentResultContract<Int, O>
