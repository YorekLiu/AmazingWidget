package xyz.yorek.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import androidx.core.content.ContextCompat

class SimpleSkeletonView(
    context: Context,
    attributes: AttributeSet? = null
) : FrameLayout(context, attributes) {

    private val mPointLeftTop = Point()
    private val mPointRightTop = Point()
    private val mPointRightBottom = Point()
    private val mPointLeftBottom = Point()
    private val mPath = Path()
    private val mPaint = Paint()
    private lateinit var mShape: LinearGradient
    private var mRunningAnimator: ValueAnimator? = null

    private var mDuration = 1_000

    init {
        mPaint.isAntiAlias = true
        setWillNotDraw(false)

        val a = context.obtainStyledAttributes(attributes, R.styleable.SimpleSkeletonView)
        mDuration = a.getInteger(R.styleable.SimpleSkeletonView_skeletonDuration, mDuration)
        a.recycle()
    }

    override fun setAlpha(alpha: Float) {
        super.setAlpha(alpha)
        if (alpha == 1.0F) {
            start()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initPoint()
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        if (::mShape.isInitialized) {
            mPaint.shader = mShape
            canvas?.drawPath(mPath, mPaint)
        }
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

    private fun initPoint() {
        mPointLeftTop.set(0, 0)
        mPointRightTop.set(measuredWidth, 0)
        mPointRightBottom.set(measuredWidth, measuredHeight)
        mPointLeftBottom.set(0, measuredHeight)

        mPath.reset()
        mPath.moveTo(mPointLeftTop.x.toFloat(), mPointLeftTop.y.toFloat())
        mPath.lineTo(mPointRightTop.x.toFloat(), mPointRightTop.y.toFloat())
        mPath.lineTo(mPointRightBottom.x.toFloat(), mPointRightBottom.y.toFloat())
        mPath.lineTo(mPointLeftBottom.x.toFloat(), mPointLeftBottom.y.toFloat())
        mPath.close()
    }

    /**
     * 请确保在调用时，可以正确获取到View的宽高
     */
    fun start() {
        val offset = dp2px(40F)
        val colors: IntArray = intArrayOf(0x00FFFFFF, 0xCCFFFFFF.toInt(), 0x00FFFFFF)
        val start = mPointLeftTop.x
        val end = mPointRightTop.x
        with(ValueAnimator.ofFloat(start - offset, end + offset)) {
            mRunningAnimator?.cancel()
            mRunningAnimator = this

            this.duration = mDuration.toLong()
            this.repeatCount = ValueAnimator.INFINITE
            this.addUpdateListener {
                val moving = it.animatedValue as Float
                mShape = LinearGradient(
                    moving,
                    0F,
                    moving + offset,
                    0F,
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                invalidate()
            }
            this.start()
        }
    }

    fun stop() {
        mRunningAnimator?.end()
        mRunningAnimator?.cancel()
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }
}