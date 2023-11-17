package host.capitalquiz.editgamerule.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import host.capitalquiz.core.ui.BindingFragment

abstract class BaseGameRuleFragment<VB : ViewBinding> : BindingFragment<VB>() {
    abstract val fab: ExtendedFloatingActionButton
    abstract val recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING && fab.isShown.not()) {
                        fab.show()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 10 && fab.isShown) fab.hide()
                    if (dy < -10 && fab.isShown.not()) fab.show()
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.clearOnScrollListeners()
    }

}