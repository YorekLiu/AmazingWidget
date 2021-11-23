/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.yorek.widget.tablayout;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.Dimension;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import xyz.yorek.widget.tablayout.XMTabLayout.TabView;

class FixedTabIndicatorInterpolator extends XMTabIndicatorInterpolator {

    @Dimension(unit = Dimension.DP)
    private static final int FIXED_INDICATOR_WIDTH = 32;

    static RectF calculateTabViewContentBounds(
            @NonNull TabView tabView) {
        int tabViewContentHeight = tabView.getContentHeight();
        int tabViewContentWidth = (int) XMViewUtils.dpToPx(tabView.getContext(), FIXED_INDICATOR_WIDTH);

        int tabViewCenterX = (tabView.getLeft() + tabView.getRight()) / 2;
        int tabViewCenterY = (tabView.getTop() + tabView.getBottom()) / 2;
        int contentLeftBounds = tabViewCenterX - (tabViewContentWidth / 2);
        int contentTopBounds = tabViewCenterY - (tabViewContentHeight / 2);
        int contentRightBounds = tabViewCenterX + (tabViewContentWidth / 2);
        int contentBottomBounds = tabViewCenterY + (tabViewCenterX / 2);

        return new RectF(contentLeftBounds, contentTopBounds, contentRightBounds, contentBottomBounds);
    }

    static RectF calculateIndicatorWidthForTab(XMTabLayout tabLayout, @Nullable View tab) {
        if (tab == null) {
            return new RectF();
        }

        // If the indicator should fit to the tab's content, calculate the content's width
        if (!tabLayout.isTabIndicatorFullWidth() && tab instanceof TabView) {
            return calculateTabViewContentBounds((TabView) tab);
        }

        // Return the entire width of the tab
        return new RectF(tab.getLeft(), tab.getTop(), tab.getRight(), tab.getBottom());
    }

    void setIndicatorBoundsForTab(XMTabLayout tabLayout, View tab, @NonNull Drawable indicator) {
        RectF startIndicator = calculateIndicatorWidthForTab(tabLayout, tab);
        indicator.setBounds(
                (int) startIndicator.left,
                indicator.getBounds().top,
                (int) startIndicator.right,
                indicator.getBounds().bottom);
    }

    void setIndicatorBoundsForOffset(
            XMTabLayout tabLayout,
            View startTitle,
            View endTitle,
            @FloatRange(from = 0.0, to = 1.0) float offset,
            @NonNull Drawable indicator) {
        RectF startIndicator = calculateIndicatorWidthForTab(tabLayout, startTitle);
        RectF endIndicator = calculateIndicatorWidthForTab(tabLayout, endTitle);

        if (endIndicator.left >= startIndicator.left) {
            // 滑动翻页、向右边跳着切换时触发
            if (offset <= 0.5F) {
                indicator.setBounds(
                        (int) startIndicator.left,
                        indicator.getBounds().top,
                        XMAnimationUtils.lerp((int) startIndicator.right, (int) endIndicator.right, offset * 2),
                        indicator.getBounds().bottom);
            } else {
                indicator.setBounds(
                        XMAnimationUtils.lerp((int) startIndicator.left, (int) endIndicator.left, (offset - 0.5F) * 2),
                        indicator.getBounds().top,
                        (int) endIndicator.right,
                        indicator.getBounds().bottom);
            }
        } else {
            // 向左边跳着切换时触发
            if (offset <= 0.5F) {
                indicator.setBounds(
                        XMAnimationUtils.lerp((int) startIndicator.left, (int) endIndicator.left, offset * 2),
                        indicator.getBounds().top,
                        (int) startIndicator.right,
                        indicator.getBounds().bottom);
            } else {
                indicator.setBounds(
                        (int) endIndicator.left,
                        indicator.getBounds().top,
                        XMAnimationUtils.lerp((int) startIndicator.right, (int) endIndicator.right, (offset - 0.5F) * 2),
                        indicator.getBounds().bottom);
            }
        }
    }
}