package xyz.yorek.amazingwidget.widget

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import xyz.yorek.amazingwidget.BaseFragment
import xyz.yorek.amazingwidget.R
import xyz.yorek.widget.FixedNumberEditText
import xyz.yorek.widget.ScrollableRingView

/**
 * Created by yorek.liu on 2021/10/28
 *
 * @author yorek.liu
 */
class ScrollableRingFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_scrollable_ring

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_ip_scrollable)

        requireView().findViewById<ScrollableRingView>(R.id.scrollableRingView).let {
            it.scrollableBitmap = bitmap
            it.start()
        }

    }
}