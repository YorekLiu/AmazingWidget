package xyz.yorek.widget.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.TextPaint
import android.text.style.ReplacementSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px

/**
 * Created by yorek.liu on 2021/10/25
 *
 * @author yorek.liu
 */
class BoldUnderlineTextSpan(
    @ColorInt val mColorInt: Int,
    @Px val mCornerRadius: Int,
    @ColorInt val mTextForegroundColor: Int? = null
): ReplacementSpan() {

    private val textBounds = Rect()
    private var mDrawable: GradientDrawable? = null

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        if (text.isNullOrEmpty()) return 0

        paint.getTextBounds(text.toString(), start, end, textBounds)
        val rect = textBounds
        if (fm != null) {
            val fmPaint = paint.fontMetricsInt
            val fontHeight = fmPaint.descent - fmPaint.ascent
            val drHeight: Int = rect.bottom - rect.top
            val centerY = fmPaint.ascent + fontHeight / 2
            fm.ascent = centerY - drHeight / 2
            fm.top = fm.ascent
            fm.bottom = centerY + drHeight / 2
            fm.descent = fm.bottom
        }
        return paint.measureText(text.toString(), start, end).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        if (text.isNullOrEmpty()) return
        val textWidth = paint.measureText(text.toString(), start, end)
        val drawable = getDrawable()
        val drawableTop = y - (bottom - top) * 0.2F
        val drawableBottom = y + (bottom - top) * 0.2F
        drawable.setBounds(
            x.toInt(),
            drawableTop.toInt(),
            (x + textWidth).toInt(),
            drawableBottom.toInt()
        )

        drawable.draw(canvas)

        if (mTextForegroundColor != null) {
            paint.color = mTextForegroundColor
        }
        canvas.drawText(text.toString(), start, end, x, y.toFloat(), paint)
    }

    private fun getDrawable(): Drawable {
        return mDrawable ?: GradientDrawable().apply {
            this.shape = GradientDrawable.RECTANGLE
            this.cornerRadius = mCornerRadius.toFloat()
            this.setColor(mColorInt)
            mDrawable = this
        }
    }
}