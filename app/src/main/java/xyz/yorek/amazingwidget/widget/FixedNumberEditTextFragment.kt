package xyz.yorek.amazingwidget.widget

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import xyz.yorek.amazingwidget.BaseFragment
import xyz.yorek.amazingwidget.R
import xyz.yorek.widget.FixedNumberEditText

/**
 * Created by yorek.liu on 2021/10/28
 *
 * @author yorek.liu
 */
class FixedNumberEditTextFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_fixed_number_edit_text

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val listener = object: FixedNumberEditText.OnFixedNumberCompletedListener {
            override fun onFixedNumberCompleted(number: String) {
                Toast.makeText(requireContext().applicationContext, number, Toast.LENGTH_SHORT).apply {
                    this.setGravity(Gravity.CENTER, 0, 0)
                    this.show()
                }
            }
        }

        requireView().findViewById<FixedNumberEditText>(R.id.etCode1).setOnFixedNumberCompletedListener(listener)
        requireView().findViewById<FixedNumberEditText>(R.id.etCode2).setOnFixedNumberCompletedListener(listener)
        requireView().findViewById<FixedNumberEditText>(R.id.etCode3).setOnFixedNumberCompletedListener(listener)
        requireView().findViewById<FixedNumberEditText>(R.id.etCode4).setOnFixedNumberCompletedListener(listener)
    }
}