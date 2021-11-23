package xyz.yorek.amazingwidget.widget

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import xyz.yorek.amazingwidget.*
import xyz.yorek.widget.tablayout.ImageTabLayout
import xyz.yorek.widget.tablayout.XMTabLayout
import java.lang.reflect.Field

/**
 * Created by yorek.liu on 2021/10/28
 *
 * @author yorek.liu
 */
class TabLayoutFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_tablayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val onImageTabFactory: ImageTabLayout.OnImageTabFactory = object :
            ImageTabLayout.OnImageTabFactory {
            override fun onCreateImageTab(): ImageView {
                val imageView = ImageView(requireContext())
                imageView.layoutParams = ViewGroup.LayoutParams(44f.dp2px().toInt(), 26f.dp2px().toInt())
                return imageView
            }

            override fun onBindImageTab(
                imageTabLayout: ImageTabLayout,
                view: ImageView,
                position: Int,
                imageModel: Any
            ) {
                if (imageModel is Int) {
                    val bitmap = BitmapFactory.decodeResource(resources, imageModel)
                    view.setImageBitmap(bitmap)

                    imageTabLayout.notifyImageTabLoaded(view, position)
                }
//                GlideApp.with(this@TabLayoutFragment)
//                    .load(imageModel)
//                    .addListener(object : RequestListener<Drawable?> {
//                        override fun onLoadFailed(e: GlideException?, o: Any, target: Target<Drawable?>, b: Boolean): Boolean {
//                            return false
//                        }
//
//                        override fun onResourceReady(drawable: Drawable?, o: Any, target: Target<Drawable?>, dataSource: DataSource, b: Boolean): Boolean {
//                            imageTabLayout.notifyImageTabLoaded(view, position)
//                            return false
//                        }
//                    })
//                    .into(view)
            }
        }

        val scroller = CustomDurationScroller(requireContext())
        val scrollerField = ViewPager::class.java.getDeclaredField("mScroller")
        scrollerField.isAccessible = true

        val tabLayout1 = requireView().findViewById<ImageTabLayout>(R.id.tabLayout1)
        val viewPager1 = requireView().findViewById<ViewPager>(R.id.viewPager1)
        initViewPager(tabLayout1, viewPager1, onImageTabFactory, scroller, scrollerField, getData1())

        val tabLayout2 = requireView().findViewById<ImageTabLayout>(R.id.tabLayout2)
        val viewPager2 = requireView().findViewById<ViewPager>(R.id.viewPager2)
        initViewPager(tabLayout2, viewPager2, onImageTabFactory, scroller, scrollerField, getData2())
    }

    private fun initViewPager(
        tabLayout: ImageTabLayout,
        viewPager: ViewPager,
        onImageTabFactory: ImageTabLayout.OnImageTabFactory,
        scroller: CustomDurationScroller,
        scrollerField: Field,
        data: List<Pair<String, Int?>>
    ) {
        tabLayout.setOnImageTabFactory(onImageTabFactory)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.clearOnTabSelectedListeners()
        tabLayout.restoreBuiltInTabSelectedListener()
        tabLayout.addOnTabSelectedListener(object : XMTabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: XMTabLayout.Tab) {}

            override fun onTabSelected(tab: XMTabLayout.Tab) {
                val current = tab.position
                scroller.setNoDuration(true)
                viewPager.setCurrentItem(current, true)
                scroller.setNoDuration(false)
            }

            override fun onTabReselected(tab: XMTabLayout.Tab) {}
        })
        scrollerField.set(viewPager, scroller)

        viewPager.adapter = TabLayoutPagerAdapter(childFragmentManager, data)

        data.forEachIndexed { index, pair ->
            pair.second?.let {
                tabLayout.createImageTab(index, it)
            }
        }
    }

    private fun getData1(): List<Pair<String, Int?>> {
        return listOf(
            "好习惯" to null,
            "推荐" to R.drawable.ic_tab,
            "精品" to R.drawable.ic_tab,
            "哄睡" to null,
            "绘本" to null,
            "故事" to R.drawable.ic_tab,
            "牛津树" to null,
            "儿歌" to null,
            "育儿" to null,
            "牛津英语" to null
        )
    }

    private fun getData2(): List<Pair<String, Int?>> {
        return listOf(
            "好习惯" to null,
            "推荐" to null,
            "精品" to null,
            "哄睡" to null,
            "绘本" to null,
            "故事" to null,
            "牛津树" to null,
            "儿歌" to null,
            "育儿" to null,
            "牛津英语" to null
        )
    }
}

class TabLayoutPagerAdapter(
    fm: FragmentManager,
    data: List<Pair<String, Int?>>
): MultiChannelsPageAdapter<Pair<String, Int?>>(fm, data) {

    override fun createFragment(t: Pair<String, Int?>): Fragment {
        return EmptyFragment()
    }

    override fun getPageTitle(t: Pair<String, Int?>): CharSequence {
        return t.first
    }
}