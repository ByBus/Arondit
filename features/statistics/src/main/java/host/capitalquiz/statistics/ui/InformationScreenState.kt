package host.capitalquiz.statistics.ui

import androidx.core.view.isVisible
import host.capitalquiz.statistics.R
import host.capitalquiz.statistics.databinding.FragmentStatisticsBinding

class InformationScreenState(private val visible: Boolean) {

    fun update(binding: FragmentStatisticsBinding) {
        with(binding) {
            information.root.isVisible = visible
            table.statisticsTable.isVisible = visible.not()
            appBarLayout.isVisible = visible.not()
            if (visible) {
                information.infoImage.setImageResource(R.drawable.joker)
                information.infoText.setText(R.string.no_statistics_yet)
                information.infoButton.setText(R.string.close)
            }
        }
    }
}