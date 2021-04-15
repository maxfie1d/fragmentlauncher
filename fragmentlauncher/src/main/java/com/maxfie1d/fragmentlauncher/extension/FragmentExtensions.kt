package com.maxfie1d.fragmentlauncher.extension

import com.maxfie1d.fragmentlauncher.contract.*
import com.maxfie1d.fragmentlauncher.launcher.FragmentLauncher
import com.maxfie1d.fragmentlauncher.launcher.NoInputFragmentLauncher
import com.maxfie1d.fragmentlauncher.launcher.impl.FragmentLauncherImpl
import com.maxfie1d.fragmentlauncher.launcher.impl.NoInputFragmentLauncherImpl
import com.maxfie1d.fragmentlauncher.result.FragmentResultCallback
import com.maxfie1d.fragmentlauncher.result.SetFragmentResultListenerUseCase
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import java.io.Serializable

private fun <I, O> innerRegisterForDialogResult(
    tag: String,
    lifecycleOwner: LifecycleOwner,
    lifecycle: Lifecycle,
    fragmentManager: Lazy<FragmentManager>,
    contract: FragmentResultContractBase<I, O>,
    callback: FragmentResultCallback<O>
): FragmentLauncher<I> {
    SetFragmentResultListenerUseCase(lifecycle)
        .setResultListener(contract, tag, callback, fragmentManager, lifecycleOwner)

    return FragmentLauncherImpl({ input ->
        contract.factory().apply {
            arguments = contract.createInputBundle(input, tag)
        }
    }, fragmentManager)
}

private fun <I : Serializable, O : Serializable> innerRegisterForDialogResultSerializable(
    tag: String,
    lifecycleOwner: LifecycleOwner,
    lifecycle: Lifecycle,
    fragmentManager: Lazy<FragmentManager>,
    contract: FragmentResultContractBase<I, O>,
    callback: FragmentResultCallback<O>
): FragmentLauncher<I> = innerRegisterForDialogResult(tag, lifecycleOwner, lifecycle, fragmentManager, contract, callback)

private fun <I : Parcelable, O : Serializable> innerRegisterForDialogResultParcelable(
    tag: String,
    lifecycleOwner: LifecycleOwner,
    lifecycle: Lifecycle,
    fragmentManager: Lazy<FragmentManager>,
    contract: FragmentResultContractBase<I, O>,
    callback: FragmentResultCallback<O>
): FragmentLauncher<I> = innerRegisterForDialogResult(tag, lifecycleOwner, lifecycle, fragmentManager, contract, callback)

private fun <O : Serializable> innerRegisterForDialogResultNoInput(
    tag: String,
    lifecycleOwner: LifecycleOwner,
    lifecycle: Lifecycle,
    fragmentManager: Lazy<FragmentManager>,
    contract: NoInputFragmentResultContract<O>,
    callback: FragmentResultCallback<O>
): NoInputFragmentLauncher {
    SetFragmentResultListenerUseCase(lifecycle)
        .setResultListener(contract, tag, callback, fragmentManager, lifecycleOwner)

    return NoInputFragmentLauncherImpl({
        contract.factory().apply {
            arguments = contract.createInputBundle(
                0, // 便宜上無意味な値を入れている
                tag
            )
        }
    }, fragmentManager)
}

/**
 * @param tag ダイアログの結果を流す側と受け取る側が1対1対応させるために使用される。1つの画面内で同じtagを指定しないこと。
 */
fun <I : Serializable, O : Serializable> FragmentActivity.dialogLauncher(
    contract: FragmentResultContract<I, O>,
    tag: String,
    callback: FragmentResultCallback<O>
): FragmentLauncher<I> {
    return innerRegisterForDialogResultSerializable(tag, this, lifecycle, lazy { supportFragmentManager }, contract, callback)
}

fun <I : Parcelable, O : Serializable> FragmentActivity.dialogLauncherParcelable(
    contract: FragmentResultContractParcelable<I, O>,
    tag: String,
    callback: FragmentResultCallback<O>
): FragmentLauncher<I> {
    return innerRegisterForDialogResultParcelable(tag, this, lifecycle, lazy { supportFragmentManager }, contract, callback)
}

fun <I : Serializable, O : Serializable> Fragment.dialogLauncher(
    contract: FragmentResultContract<I, O>,
    tag: String,
    callback: FragmentResultCallback<O>
): FragmentLauncher<I> {
    return innerRegisterForDialogResultSerializable(tag, this, lifecycle, lazy { parentFragmentManager }, contract, callback)
}

fun <O : Serializable> FragmentActivity.dialogLauncher(
    contract: NoInputFragmentResultContract<O>,
    tag: String,
    callback: FragmentResultCallback<O>
): NoInputFragmentLauncher {
    return innerRegisterForDialogResultNoInput(tag, this, lifecycle, lazy { supportFragmentManager }, contract, callback)
}

fun <O : Serializable> Fragment.dialogLauncher(
    contract: NoInputFragmentResultContract<O>,
    tag: String,
    callback: FragmentResultCallback<O>
): NoInputFragmentLauncher {
    return innerRegisterForDialogResultNoInput(tag, this, lifecycle, lazy { parentFragmentManager }, contract, callback)
}

fun <I> FragmentActivity.dialogLauncher(
    contract: FragmentContract<I>
): FragmentLauncher<I> {
    return FragmentLauncherImpl({ input ->
        contract.factory().apply {
            arguments = contract.createInputBundle(input)
        }
    }, lazy { supportFragmentManager })
}

fun <I> Fragment.dialogLauncher(
    contract: FragmentContract<I>
): FragmentLauncher<I> {
    return FragmentLauncherImpl({ input ->
        contract.factory().apply {
            arguments = contract.createInputBundle(input)
        }
    }, lazy { parentFragmentManager })
}

fun FragmentActivity.dialogLauncher(
    contract: FragmentContract<Unit>
): NoInputFragmentLauncher {
    return NoInputFragmentLauncherImpl(contract::factory, lazy { supportFragmentManager })
}

fun Fragment.dialogLauncher(
    contract: FragmentContract<Unit>
): NoInputFragmentLauncher {
    return NoInputFragmentLauncherImpl(contract::factory, lazy { parentFragmentManager })
}

@Suppress("unused")
fun <O, T : FragmentResultContractBase<*, O>> DialogFragment.setDialogFragmentResult(contract: T, data: O) {
    val bundle = bundleOf(Constants.FRAGMENT_RESULT_KEY.name to data)
    val key = contract.parseInput(requireArguments()).key
    setFragmentResult(key, bundle)
}
