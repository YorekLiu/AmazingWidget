package xyz.yorek.amazingwidget;

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by yorek.liu on 2021/10/28
 *
 * @author yorek.liu
 */

class PlaceholderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placeholder)

        initView()
    }

    private fun initView() {
        title = intent.getStringExtra(EXTRA_TITLE)
        val clazz = intent.getSerializableExtra(EXTRA_PAGE) as? Class<Fragment> ?: return
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, clazz.newInstance())
            .commitAllowingStateLoss()
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_PAGE = "extra_page"
    }
}