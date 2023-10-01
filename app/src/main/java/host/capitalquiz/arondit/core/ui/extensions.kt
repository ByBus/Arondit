package host.capitalquiz.arondit.core.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

inline fun Fragment.observeFlows(crossinline observationFunction: suspend (CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycle.coroutineScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            observationFunction(this)
        }
    }
}


inline fun <reified T> Flow<T>.collect(
    owner: LifecycleOwner,
    collector: FlowCollector<T>,
) {
    val flow = this
    owner.lifecycle.coroutineScope.launch {
        owner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collector)
        }
    }
}