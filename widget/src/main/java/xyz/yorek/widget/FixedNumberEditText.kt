package xyz.yorek.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.SystemClock
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

/**
 * Created by yorek.liu on 2021/6/2
 *
 * @author yorek.liu
 */
class FixedNumberEditText(
    context: Context,
    attributeSet: AttributeSet? = null
): AppCompatEditText(context, attributeSet) {

    private companion object {
        private const val BLINK = 500
    }

    private var mCellCount = 6
    private var mCellBackground: Drawable?
    private var mCellGap: Float = 0F
    private var mCellWidth: Float = 0F

    private var mTextCursorDrawableRes: Int = 0
    private var mTextCursorDrawable: Drawable? = null

    private var mCursorShowTs = 0L

    private var mOnFixedNumberCompletedListener: OnFixedNumberCompletedListener? = null

    fun setOnFixedNumberCompletedListener(listener: OnFixedNumberCompletedListener?) {
        mOnFixedNumberCompletedListener = listener
    }

    init {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.FixedNumberEditText)
        mCellBackground = a.getDrawable(R.styleable.FixedNumberEditText_cellBackground)
        mCellCount = a.getColor(R.styleable.FixedNumberEditText_cellCount, mCellCount)
        mCellGap = a.getDimension(R.styleable.FixedNumberEditText_cellGap, 10F)
        mCellWidth = a.getDimension(R.styleable.FixedNumberEditText_cellWidth, 48F)
        mTextCursorDrawableRes = a.getResourceId(R.styleable.FixedNumberEditText_android_textCursorDrawable, mTextCursorDrawableRes)
        a.recycle()

        inputType = InputType.TYPE_CLASS_NUMBER
        background = null
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(mCellCount))
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == mCellCount) {
                    mOnFixedNumberCompletedListener?.onFixedNumberCompleted(s.toString())
                }
            }
        })
        if (mTextCursorDrawableRes != 0) {
            mTextCursorDrawable = ContextCompat.getDrawable(context, mTextCursorDrawableRes)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = mCellWidth * mCellCount + (mCellGap - 1) * mCellCount
        val height = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(width.toInt(), height)
    }

    override fun onDraw(canvas: Canvas) {
        // draw cell background
        var cellBackgroundLeft = 0F
        for (i in 0 until mCellCount) {
            mCellBackground?.let {
                it.setBounds(cellBackgroundLeft.toInt(), 0, (cellBackgroundLeft + mCellWidth).toInt(), measuredHeight)
                it.draw(canvas)
            }
            cellBackgroundLeft += mCellWidth + mCellGap
        }

        // draw text
        val mTempText = text ?: ""
        val textCount = mTempText.length
        cellBackgroundLeft = 0F
        var cellBackgroundRight = mCellWidth
        for (i in 0 until textCount) {
            val ch = mTempText[i]
            val textWidth = paint.measureText(mTempText, i, i + 1)
            val drawingX = cellBackgroundLeft + ((cellBackgroundRight - cellBackgroundLeft - textWidth) / 2)
            val drawingY = (measuredHeight - (paint.ascent() + paint.descent())) / 2
            canvas.drawText(ch.toString(), drawingX, drawingY, paint)

            cellBackgroundLeft += mCellWidth + mCellGap
            cellBackgroundRight = cellBackgroundLeft + mCellWidth
        }

        // draw cursor
        if (shouldBlink()) {
            val cursorDrawable: Drawable = mTextCursorDrawable!!
            val cursorHeight = paint.descent() - paint.ascent()

            val cursorLeft = (textCount) * (mCellWidth + mCellGap) + ((mCellWidth - cursorDrawable.intrinsicWidth) / 2)
            val cursorTop = (measuredHeight - (cursorHeight)) / 2
            cursorDrawable.setBounds(
                cursorLeft.toInt(),
                cursorTop.toInt(),
                (cursorLeft + cursorDrawable.intrinsicWidth).toInt(),
                (cursorTop + cursorHeight).toInt()
            )
            cursorDrawable.draw(canvas)
            mCursorShowTs = SystemClock.uptimeMillis()
        }
    }

    private fun shouldBlink(): Boolean {
        return hasFocus() && (text ?: "").length < mCellCount && mTextCursorDrawable != null && shouldRenderCursor()
    }

    private fun shouldRenderCursor(): Boolean {
        if (!isCursorVisible) {
            return false
        }
        val showCursorDelta: Long = SystemClock.uptimeMillis() - mCursorShowTs
        return showCursorDelta % (2 * BLINK) < BLINK
    }

    interface OnFixedNumberCompletedListener {
        fun onFixedNumberCompleted(number: String)
    }
}