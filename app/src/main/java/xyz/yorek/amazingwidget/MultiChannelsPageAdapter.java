package xyz.yorek.amazingwidget;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public abstract class MultiChannelsPageAdapter<T> extends FragmentStatePagerAdapter {
    private final HashMap<Integer, Fragment> mItems;
    private WeakReference<ViewGroup> mContainerRef;

    private final List<T> mData;

    public MultiChannelsPageAdapter(FragmentManager fm, List<T> data) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mData = data;

        mItems = new HashMap<>();
    }

    @NotNull
    @Override
    public Fragment getItem(int i) {
        Fragment fragment = mItems.get(i);
        if (fragment == null) {
            T t = mData.get(i);
            fragment = createFragment(i, t);
            mItems.put(i, fragment);
        }
        return fragment;
    }

    @Nullable
    public Fragment getFragmentAt(int index) {
        if (index < 0 || index >= getCount()) {
            return null;
        }

        return getItem(index);
    }

    public abstract Fragment createFragment(int position, @NonNull T t);

    public abstract CharSequence getPageTitle(int position, @NonNull T t);

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mContainerRef = new WeakReference<>(container);
//             mItems.remove(position);
//             super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return getPageTitle(position, mData.get(position));
    }

//    public void clear() {
//        destroyItems();
//        removeFromAdapterInnerField();
//        mItems.clear();
//    }
//
//    public void destroyItems() {
//        destroyItems(Integer.MIN_VALUE);
//    }
//
//    public void destroyItemsExclude(int position) {
//        destroyItems(position);
//    }
//
//    private void destroyItems(int keepIndex) {
//        try {
//            if (mContainerRef != null && mContainerRef.get() != null) {
//                ViewGroup viewGroup = mContainerRef.get();
//                for (int i = 0; i < mItems.size(); i++) {
//                    if (isExclude(keepIndex, i)) {
//                        continue;
//                    }
//
//                    Fragment fragment = mItems.get(i);
//                    if (fragment == null) {
//                        continue;
//                    }
//                    super.destroyItem(viewGroup, i, fragment);
//                    mItems.put(i, null);
//                }
//                super.finishUpdate(viewGroup);
//            }
//
//            if (keepIndex >= 0) {
//                cleanSavedState(keepIndex);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void removeFromAdapterInnerField() {
//        Class pageAdapterClass = FragmentStatePagerAdapter.class;
//        try {
//            ArrayList fragments = (ArrayList) ReflectionUtils.getFieldValue(pageAdapterClass, this, "mFragments");
//            if (fragments != null) {
//                fragments.clear();
//            }
//            fragments = (ArrayList) ReflectionUtils.getFieldValue(pageAdapterClass, this, "mSavedState");
//            if (fragments != null) {
//                fragments.clear();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            ReflectionUtils.setFieldValue(pageAdapterClass, this, "mCurTransaction", null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @SuppressWarnings("deprecation")
//    public void dispatchUserVisibleHint(int index, boolean userVisibleHint) {
//        Fragment fragment = mItems.get(index);
//        if (fragment == null) {
//            return;
//        }
//        fragment.setUserVisibleHint(userVisibleHint);
//    }
//
//    @SuppressWarnings({"unchecked", "ConstantConditions"})
//    private void cleanSavedState(int keepIndex) {
//        try {
//            ArrayList<Fragment.SavedState> savedStateList = (ArrayList<Fragment.SavedState>) ReflectionUtils.getFieldValue(FragmentStatePagerAdapter.class, this, "mSavedState");
//            if (savedStateList != null) {
//                for (int i = 0; i < savedStateList.size(); i++) {
//                    if (isExclude(keepIndex, i)) {
//                        continue;
//                    }
//
//                    savedStateList.set(i, null);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean isExclude(int keepIndex, int index) {
//        if (keepIndex == Integer.MIN_VALUE) return false;
//        return (index == keepIndex) || (index - 1 == keepIndex) || (index + 1 == keepIndex);
//    }
}