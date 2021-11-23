package xyz.yorek.amazingwidget

import android.app.Application
import android.content.Context

/**
 * Created by yorek.liu on 2021/11/23
 *
 * @author yorek.liu
 */
public class App : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        sContext = this
    }

    companion object {
        private lateinit var sContext: Application

        fun getApplication(): Application = sContext
    }
}