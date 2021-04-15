package com.maxfie1d.fragmentlauncher.result

import com.maxfie1d.fragmentlauncher.contract.FragmentResultContractBase
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

internal class SetFragmentResultListenerUseCase(private val lifecycle: Lifecycle) {
    fun <I, O> setResultListener(
        contract: FragmentResultContractBase<I, O>,
        tag: String,
        callback: FragmentResultCallback<O>,
        fragmentManager: Lazy<FragmentManager>,
        lifecycleOwner: LifecycleOwner
    ) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            setResultListenerNow(contract, tag, callback, fragmentManager, lifecycleOwner)
        } else if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
            setResultListenerOnCreate(contract, tag, callback, fragmentManager, lifecycleOwner)
        }
    }

    private fun <I, O> setResultListenerNow(
        contract: FragmentResultContractBase<I, O>,
        tag: String,
        callback: FragmentResultCallback<O>,
        fragmentManager: Lazy<FragmentManager>,
        lifecycleOwner: LifecycleOwner
    ) = innerSetResultListener(contract, tag, callback, fragmentManager, lifecycleOwner)

    private fun <I, O> setResultListenerOnCreate(
        contract: FragmentResultContractBase<I, O>,
        tag: String,
        callback: FragmentResultCallback<O>,
        fragmentManager: Lazy<FragmentManager>,
        lifecycleOwner: LifecycleOwner
    ) {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                innerSetResultListener(contract, tag, callback, fragmentManager, lifecycleOwner)
            }
        })
    }

    private fun <I, O> innerSetResultListener(
        contract: FragmentResultContractBase<I, O>,
        tag: String,
        callback: FragmentResultCallback<O>,
        fragmentManager: Lazy<FragmentManager>,
        lifecycleOwner: LifecycleOwner
    ) {
        fragmentManager.value.setFragmentResultListener(
            contract.computeRequestKey(tag),
            lifecycleOwner,
            FragmentResultListener { _, data ->
                val result = contract.parseResult(data) ?: return@FragmentResultListener
                callback(result)
            }
        )
    }
}
