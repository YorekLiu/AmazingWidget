package xyz.yorek.amazingwidget.widget

import android.os.Bundle
import android.view.View
import xyz.yorek.amazingwidget.BaseFragment
import xyz.yorek.amazingwidget.R
import xyz.yorek.widget.SimpleSkeletonView

/**
 * Created by yorek.liu on 2021/10/28
 *
 * @author yorek.liu
 */
class SimpleSkeletonFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_simple_skeleton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        var mRunning = false
        val simpleSkeletonView: SimpleSkeletonView = requireView().findViewById(R.id.simpleSkeletonView)
        simpleSkeletonView.setOnClickListener {
            if (mRunning) {
                simpleSkeletonView.stop()
            } else {
                simpleSkeletonView.start()
            }
            mRunning = !mRunning
        }
        simpleSkeletonView.post {
            simpleSkeletonView.performClick()
        }
    }
}