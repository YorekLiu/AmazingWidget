package xyz.yorek.amazingwidget

import android.util.TypedValue

/**
 * Created by yorek.liu on 2021/11/23
 *
 * @author yorek.liu
 */
fun Float.dp2px(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this, App.getApplication().resources.displayMetrics
    )
}

fun Int.dp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), App.getApplication().resources.displayMetrics
    )
}