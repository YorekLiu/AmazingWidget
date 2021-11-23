package xyz.yorek.amazingwidget

import android.graphics.Color
import android.os.Bundle
import android.view.View
import kotlin.random.Random

/**
 * Created by yorek.liu on 2021/11/23
 *
 * @author yorek.liu
 */
class EmptyFragment: BaseFragment() {
    override fun layoutId(): Int = R.layout.fragment_empty

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val random = Random(System.currentTimeMillis())
        view.setBackgroundColor(Color.argb(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256)))
    }
}