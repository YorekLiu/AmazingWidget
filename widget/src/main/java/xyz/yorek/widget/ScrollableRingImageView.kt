package xyz.yorek.widget

import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

/**
 * Created by yorek.liu on 2021/10/22
 *
 * @author yorek.liu
 */
@Suppress("unused")
class ScrollableRingView(
    context: Context,
    attributeSet: AttributeSet? = null
): View(context, attributeSet) {

    var scrollableBitmap: Bitmap? = null
        set(value) {
            field = value
            mBitmapWidth = value?.width ?: 0
            mBitmapHeight = value?.height ?: 0
            calcScale()
            postInvalidate()
        }
    private var mScale = 0F
    private var mBitmapWidth = 0
    private var mBitmapHeight = 0
    private var mScaledBitmapWidth = 0
    private var mScaledBitmapHeight = 0

    private var mScaledBitmapPosition = 0
    private val mMajorSrcRect = Rect()
    private val mMajorDstRect = Rect()
    private val mMinorSrcRect = Rect()
    private val mMinorDstRect = Rect()

    private var mInterpolator: Interpolator = AccelerateDecelerateInterpolator()
    private var mEvaluator = IntEvaluator()
    private var mDuration = 8_000
    private var mAnimator: ValueAnimator? = null
    private var mStartOffsetX: Float = 0F

    private val mResumeDelay = 160L
    private val mDoResumeRunnable = Runnable {
        val animator = mAnimator ?: return@Runnable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (animator.isPaused) {
                animator.resume()
            } else if (!animator.isRunning) {
                animator.start()
            }
        } else {
            animator.start()
        }
    }

    private val mAnimatorUpdateListener = ValueAnimator.AnimatorUpdateListener {
        mScaledBitmapPosition = mEvaluator.evaluate(it.animatedFraction, 0, mScaledBitmapWidth)
        invalidate()
    }

    init {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.ScrollableRingView)
        mDuration = a.getInt(R.styleable.ScrollableRingView_android_duration, mDuration)
        mStartOffsetX = a.getDimension(R.styleable.ScrollableRingView_startOffsetX, mStartOffsetX)
        a.recycle()
    }

    /**
     * 初始化时设置，在start方法前调用
     */
    fun setInterpolator(interpolator: Interpolator) {
        mInterpolator = interpolator
    }

    /**
     * 初始化时设置，在start方法前调用
     */
    fun setDuration(duration: Int) {
        mDuration = duration
    }

    /**
     * 初始化时设置，在start方法前调用
     */
    fun setStartOffsetX(startOffset: Float) {
        mStartOffsetX = startOffset
    }

    fun start() {
        scrollableBitmap ?: return
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(0, 1).apply {
                this.duration = mDuration.toLong()
                this.interpolator = mInterpolator
                this.repeatCount = ValueAnimator.INFINITE
                this.repeatMode = ValueAnimator.RESTART
                this.addUpdateListener(mAnimatorUpdateListener)
                this.start()
            }
        } else {
            removeCallbacks(mDoResumeRunnable)
            postDelayed(mDoResumeRunnable, mResumeDelay)
        }
    }

    fun stop() {
        scrollableBitmap ?: return
        mAnimator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                it.pause()
            } else {
                it.cancel()
            }
        }
    }

//    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
//        super.onWindowFocusChanged(hasWindowFocus)
//        if (hasWindowFocus) {
//            start()
//        } else {
//            stop()
//        }
//    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calcScale()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mScale == 0F) return
        val bitmap = scrollableBitmap ?: return

        if (!allowScrollable()) {
            val left = (measuredWidth - mScaledBitmapWidth) ushr 1
            mMajorDstRect.set(left, 0, left + mScaledBitmapWidth, measuredHeight)
            canvas.drawBitmap(bitmap, null, mMajorDstRect, null)
            return
        }

        val offsetScaledBitmapPosition = if (mScaledBitmapWidth != 0) {
            (mScaledBitmapWidth + mScaledBitmapPosition - mStartOffsetX.toInt()) % mScaledBitmapWidth
        } else {
            mScaledBitmapPosition
        }

        val restScaledBitmapWidth = mScaledBitmapWidth - offsetScaledBitmapPosition
        if (restScaledBitmapWidth >= measuredWidth) {
            mMajorSrcRect.set(
                (offsetScaledBitmapPosition * mScale).toInt(),
                0,
                ((offsetScaledBitmapPosition + measuredWidth) * mScale).toInt(),
                mBitmapHeight
            )
            mMajorDstRect.set(0, 0, measuredWidth, measuredHeight)
            canvas.drawBitmap(bitmap, mMajorSrcRect, mMajorDstRect, null)
        } else {
            // draw major part
            val majorViewWidth = restScaledBitmapWidth
            mMajorSrcRect.set(
                (offsetScaledBitmapPosition * mScale).toInt(),
                0,
                (mScaledBitmapWidth * mScale).toInt(),
                mBitmapHeight
            )
            mMajorDstRect.set(0, 0, majorViewWidth, measuredHeight)
            canvas.drawBitmap(bitmap, mMajorSrcRect, mMajorDstRect, null)

            // draw minor part
            val minorViewWidth = measuredWidth - majorViewWidth
            mMinorSrcRect.set(0, 0, (minorViewWidth * mScale).toInt(), mBitmapHeight)
            mMinorDstRect.set(majorViewWidth, 0, measuredWidth, measuredHeight)
            canvas.drawBitmap(bitmap, mMinorSrcRect, mMinorDstRect, null)
        }
    }

    private fun calcScale() {
        if (measuredHeight != 0) {
            // 缩放比按照高度进行
            mScale = mBitmapHeight.toFloat() / measuredHeight
            mScaledBitmapWidth = (mBitmapWidth / mScale).toInt()
            mScaledBitmapHeight = (mBitmapHeight / mScale).toInt()
        } else {
            mScale = 0F
            mScaledBitmapWidth = 0
            mScaledBitmapHeight = 0
        }
    }

    private fun clear() {
        removeCallbacks(mDoResumeRunnable)
        mAnimator?.removeAllUpdateListeners()
        mAnimator?.cancel()
        mAnimator = null
    }

    private fun allowScrollable(): Boolean = measuredWidth != 0 && mScaledBitmapWidth > measuredWidth
}