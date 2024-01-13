package com.dr.jjsembako.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Modifier

@Composable
fun <T : Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
    return rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                if (stateList.isNotEmpty()) {
                    val first = stateList.first()
                    if (!canBeSaved(first)) {
                        throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                    }
                }
                stateList.toList()
            },
            restore = { it.toMutableStateList() }
        )
    ) {
        elements.toList().toMutableStateList()
    }
}

@Composable
fun <K, V> rememberMutableStateMapOf(vararg pairs: Pair<K, V>)  =
    rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateMap() }
        )
    ) {
        pairs.toList().toMutableStateMap()
    }

// Currently not needed, but might be in the future
//@Composable
//fun <K, V> rememberMutableStateMapOf(defaultValue: V, vararg inputs: Any?)  =
//    rememberSaveable(
//        inputs = inputs,
//        saver = listSaver(
//            save = { it.toList() },
//            restore = { it.toMutableStateMap().withDefault { defaultValue } }
//        )
//    ) {
//        mutableStateMapOf<K, V>().withDefault { defaultValue }
//    }

fun Modifier.conditional(condition : Boolean, modifier : Modifier.() -> Modifier) : Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}