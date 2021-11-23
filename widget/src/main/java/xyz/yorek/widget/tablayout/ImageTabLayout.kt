package xyz.yorek.widget.tablayout

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import xyz.yorek.widget.R
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

/**
 * Created by yorek.liu on 2021/7/12
 *
 * @author yorek.liu
 */
class ImageTabLayout(
    context: Context,
    attributeSet: AttributeSet? = null
): XMTabLayout(context, attributeSet) {

    private val mScrollEffectV2 = true

    private var mOnImageTabFactory: OnImageTabFactory? = null
    private val mWeakImageTabList = mutableListOf<WeakReference<ImageView>>()
    private var mTabTextSize: Int = 0
    private var mSelectedTabTextSize: Int = 0
    private var mSelectedScale = 1F

    private var mPositionOffset = 0F

    private val mOnTabSelectedListener = object: OnTabSelectedListener {
        override fun onTabSelected(tab: Tab) {
            updateCustomTab(tab, mSelectedScale, true)
        }

        override fun onTabUnselected(tab: Tab) {
            updateCustomTab(tab, 1F, false)
        }

        override fun onTabReselected(tab: Tab) {}
    }

    init {
        restoreBuiltInTabSelectedListener()

        val a = context.obtainStyledAttributes(attributeSet, R.styleable.ImageTabLayout)
        val tabTextAppearance = a.getResourceId(com.google.android.material.R.styleable.TabLayout_tabTextAppearance, com.google.android.material.R.style.TextAppearance_Design_Tab)
        val ta = context.obtainStyledAttributes(tabTextAppearance, androidx.appcompat.R.styleable.TextAppearance)
        mTabTextSize = ta.getDimensionPixelSize(androidx.appcompat.R.styleable.TextAppearance_android_textSize, 0)
        mSelectedTabTextSize = a.getDimensionPixelSize(R.styleable.ImageTabLayout_tabSelectedTextSize, mTabTextSize)
        ta.recycle()
        a.recycle()

        mSelectedScale = if (mTabTextSize == 0) 1F else (1F * mSelectedTabTextSize / mTabTextSize)
    }

    fun restoreBuiltInTabSelectedListener() {
        addOnTabSelectedListener(mOnTabSelectedListener)
    }

    fun setOnImageTabFactory(onImageTabFactory: OnImageTabFactory) {
        mOnImageTabFactory = onImageTabFactory
    }

    fun createImageTab(position: Int, imageModel: Any) {
        getTabAt(position) ?: return
        val onImageTabFactory = mOnImageTabFactory ?: return

        val view = onImageTabFactory.onCreateImageTab()
        mWeakImageTabList.add(WeakReference(view))
        onImageTabFactory.onBindImageTab(this, view, position, imageModel)
    }

    fun notifyImageTabLoaded(view: View, position: Int) {
        getTabAt(position)?.customView = null
        getTabAt(position)?.customView = view
    }

    fun clearImageRequest(clear: (View) -> Unit) {
        mWeakImageTabList.forEach {
            it.get()?.let { iv ->
                clear(iv)
            }
        }
        mWeakImageTabList.clear()
    }

    fun resetCustomView() {
        for (i in 0 until tabCount) {
            getTabAt(i)?.customView = null
        }
    }

    override fun addTab(tab: Tab, position: Int, setSelected: Boolean) {
        tab.customView = newCustomView(tab)
        super.addTab(tab, position, setSelected)
    }

    private fun newCustomView(tab: Tab): View {
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.design_layout_tab_text, tab.view, false) as TextView
        customView.setTextColor(tabTextColors)
        customView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize.toFloat())
        customView.text = tab.text

        return customView
    }

    private fun updateCustomTab(tab: Tab, scale: Float, fakeBold: Boolean) {
        // 滑动过程中松手会触发选中状态的变化，此时更新缩放值会导致放大效果抖动
        if (isClickTab2Jump()) {
            tab.view.scaleX = scale
            tab.view.scaleY = scale
        }

        (tab.customView as? TextView)?.let {
            if (mScrollEffectV2) {
                if (isClickTab2Jump()) {
                    val child = slidingTabIndicator.getChildAt(tab.position) as TabView
                    (child.tab?.customView as? TextView)?.typeface = if (fakeBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                }
            } else {
                it.paint.isFakeBoldText = fakeBold
                it.invalidate()
            }
        }
    }

    override fun removeAllTabs() {
        getTabAt(selectedTabPosition)?.let {
            updateCustomTab(it, 1F, false)
        }
        super.removeAllTabs()
    }

    override fun setScrollPosition(
        position: Int,
        positionOffset: Float,
        updateSelectedText: Boolean,
        updateIndicatorPosition: Boolean
    ) {
        mPositionOffset = positionOffset
        scaleTabTween(position, positionOffset)
        if (mScrollEffectV2) {
            tryUpdateSelectedText(position, positionOffset, updateSelectedText)
        }

        super.setScrollPosition(
            position,
            positionOffset,
            updateSelectedText,
            updateIndicatorPosition
        )
    }

    private fun scaleTabTween(position: Int, positionOffset: Float) {
        if (mSelectedScale == 1F) {
            return
        }
        val firstTab = getTabAt(position)
        val secondTab = getTabAt(position + 1)

        val maxScale = mSelectedScale
        val minScale = 1F

        firstTab?.let {
            it.view.scaleX = minScale + (maxScale - minScale) * (1 - positionOffset)
            it.view.scaleY = minScale + (maxScale - minScale) * (1 - positionOffset)
        }

        secondTab?.let {
            it.view.scaleX = minScale + (maxScale - minScale) * positionOffset
            it.view.scaleY = minScale + (maxScale - minScale) * positionOffset
        }
    }

    private fun tryUpdateSelectedText(position: Int,
                                      positionOffset: Float,
                                      updateSelectedText: Boolean) {
        val roundedPosition = (position + positionOffset).roundToInt()
        if (roundedPosition < 0 || roundedPosition >= slidingTabIndicator.childCount) {
            return
        }

        if (updateSelectedText) {
            setSelectedTabView(roundedPosition)
        }
    }

    private fun setSelectedTabView(position: Int) {
        val tabCount = slidingTabIndicator.childCount
        if (position < tabCount) {
            for (i in 0 until tabCount) {
                val child = slidingTabIndicator.getChildAt(i) as TabView
                (child.tab?.customView as? TextView)?.typeface = if (i == position) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            }
        }
    }

    private fun isClickTab2Jump(): Boolean {
        return mPositionOffset == 0F
    }

    interface OnImageTabFactory {
        fun onCreateImageTab(): ImageView

        /**
         * 加载图片到给定的view上，加载完毕之后调用ImageTabLayout#notifyImageTabLoaded方法进行刷新
         */
        fun onBindImageTab(imageTabLayout: ImageTabLayout, view: ImageView, position: Int, imageModel: Any)
    }
}