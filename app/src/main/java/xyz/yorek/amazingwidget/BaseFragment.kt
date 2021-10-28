package xyz.yorek.amazingwidget

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import xyz.yorek.amazingwidget.R
import xyz.yorek.widget.FixedNumberEditText

/**
 * Created by yorek.liu on 2021/10/28
 *
 * @author yorek.liu
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    @LayoutRes abstract fun layoutId(): Int
}