package host.capitalquiz.editgamerule.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import host.capitalquiz.core.ui.BindingFragment

abstract class BaseGameRuleFragment<VB: ViewBinding>: BindingFragment<VB>() {
    abstract val fab: ExtendedFloatingActionButton
    abstract val recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val dy = scrollY - oldScrollY
            if (dy > 10 && fab.isShown) fab.hide()
            if (dy < -10 && fab.isShown.not()) fab.show()
        }
    }
}