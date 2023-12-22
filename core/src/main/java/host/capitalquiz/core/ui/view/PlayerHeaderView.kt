package host.capitalquiz.core.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import host.capitalquiz.core.databinding.PlayerHeaderLayoutBinding


class PlayerHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : RelativeLayout(context, attrs) {
    private val binding = PlayerHeaderLayoutBinding.inflate(LayoutInflater.from(context), this)

    fun setScore(value: Int) {
        binding.fieldPlayerScore.text = value.toString()
    }

    fun setName(value: String) {
        binding.fieldPlayerName.setText(value)
    }

    fun setColor(@ColorInt value: Int) {
        val tintList = ColorStateList.valueOf(value)
        with(binding) {
            fieldPlayerName.setBackgroundColor(value)
            fieldPlayerScore.setBackgroundColor(value)
            leftHeaderTintPiece.imageTintList = tintList
            rightHeaderTintPiece.imageTintList = tintList
        }
    }

    fun addPlayerButton(): ImageButton = binding.addPlayerButton

    fun removePlayerButton(): ImageButton = binding.removePlayerButton

}