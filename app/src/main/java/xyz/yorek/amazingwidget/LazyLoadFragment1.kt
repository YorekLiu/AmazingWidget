package xyz.yorek.amazingwidget

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlin.random.Random

/**
 * Created by yorek.liu on 2021/11/23
 *
 * @author yorek.liu
 */
class LazyLoadFragment1(
    private val pos: Int
): BaseFragment() {
    private var mLoaded = false

    override fun layoutId(): Int = R.layout.fragment_empty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lazy1", "create $pos")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val random = Random(System.currentTimeMillis())
        view.setBackgroundColor(Color.argb(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256)))
        Log.d("Lazy1", "createView $pos")
    }

    override fun onResume() {
        super.onResume()
        if (!mLoaded) {
            mLoaded = true
            Log.d("Lazy1", "load $pos")
        }
    }
}