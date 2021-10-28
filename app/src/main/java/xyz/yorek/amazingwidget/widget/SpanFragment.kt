package xyz.yorek.amazingwidget.widget

import android.graphics.Typeface
import android.os.Bundle
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import xyz.yorek.amazingwidget.BaseFragment
import xyz.yorek.amazingwidget.R
import xyz.yorek.widget.span.BoldUnderlineTextSpan
import xyz.yorek.widget.span.VerticalImageSpan

/**
 * Created by yorek.liu on 2021/10/28
 *
 * @author yorek.liu
 */
class SpanFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_span

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val textView = requireView().findViewById<TextView>(R.id.textView)
        textView.text = buildSpannedString {
            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_small_test, null)!!
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            inSpans(VerticalImageSpan(drawable)) {
                append("\uFFFC")
            }
            inSpans(
                AbsoluteSizeSpan(20, true),
                StyleSpan(Typeface.BOLD),
                BoldUnderlineTextSpan(0xFFFFF0C2.toInt(), 4, 0xFF995C14.toInt())
            ) {
                append("圆角为4px的颜色为0xFFFFF0C2的底部粗线")
            }
        }
    }
}