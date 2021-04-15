package com.maxfie1d.fragmentlauncher.delegate

import com.maxfie1d.fragmentlauncher.contract.FragmentContract
import com.maxfie1d.fragmentlauncher.contract.FragmentResultContract
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private class FragmentResultContractInputDataDelegate<I : Serializable>(private val contract: FragmentResultContract<I, *>) : ReadOnlyProperty<DialogFragment, I> {
    override fun getValue(thisRef: DialogFragment, property: KProperty<*>): I {
        return contract.parseInput(thisRef.requireArguments()).data
    }
}

private class FragmentContractInputDataDelegate<I : Serializable>(private val contract: FragmentContract<I>) : ReadOnlyProperty<DialogFragment, I> {
    override fun getValue(thisRef: DialogFragment, property: KProperty<*>): I {
        return contract.parseInput(thisRef.requireArguments())
    }
}

fun <I : Serializable> Fragment.inputData(contract: FragmentResultContract<I, *>): ReadOnlyProperty<DialogFragment, I> {
    return FragmentResultContractInputDataDelegate(contract)
}

fun <I : Serializable> Fragment.inputData(contract: FragmentContract<I>): ReadOnlyProperty<DialogFragment, I> {
    return FragmentContractInputDataDelegate(contract)
}
