/*
 * Copyright (C) 2017 The Android Open Source Project
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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import androidx.appcompat.content.res.AppCompatResources;

/**
 * Utility methods to resolve resources for components.
 */
class XMMaterialResources {
    private static final float FONT_SCALE_1_3 = 1.3F;
    private static final float FONT_SCALE_2_0 = 2.0F;

    private XMMaterialResources() {
    }

    @Nullable
    public static ColorStateList getColorStateList(@NonNull Context context, @NonNull TypedArray attributes, @StyleableRes int index) {
        int color;
        if (attributes.hasValue(index)) {
            color = attributes.getResourceId(index, 0);
            if (color != 0) {
                ColorStateList value = AppCompatResources.getColorStateList(context, color);
                if (value != null) {
                    return value;
                }
            }
        }

        if (VERSION.SDK_INT <= 15) {
            color = attributes.getColor(index, -1);
            if (color != -1) {
                return ColorStateList.valueOf(color);
            }
        }

        return attributes.getColorStateList(index);
    }

//    @Nullable
//    public static ColorStateList getColorStateList(@NonNull Context context, @NonNull TintTypedArray attributes, @StyleableRes int index) {
//        int color;
//        if (attributes.hasValue(index)) {
//            color = attributes.getResourceId(index, 0);
//            if (color != 0) {
//                ColorStateList value = AppCompatResources.getColorStateList(context, color);
//                if (value != null) {
//                    return value;
//                }
//            }
//        }
//
//        if (VERSION.SDK_INT <= 15) {
//            color = attributes.getColor(index, -1);
//            if (color != -1) {
//                return ColorStateList.valueOf(color);
//            }
//        }
//
//        return attributes.getColorStateList(index);
//    }

    @Nullable
    public static Drawable getDrawable(@NonNull Context context, @NonNull TypedArray attributes, @StyleableRes int index) {
        if (attributes.hasValue(index)) {
            int resourceId = attributes.getResourceId(index, 0);
            if (resourceId != 0) {
                Drawable value = AppCompatResources.getDrawable(context, resourceId);
                if (value != null) {
                    return value;
                }
            }
        }

        return attributes.getDrawable(index);
    }

//    @Nullable
//    public static TextAppearance getTextAppearance(@NonNull Context context, @NonNull TypedArray attributes, @StyleableRes int index) {
//        if (attributes.hasValue(index)) {
//            int resourceId = attributes.getResourceId(index, 0);
//            if (resourceId != 0) {
//                return new TextAppearance(context, resourceId);
//            }
//        }
//
//        return null;
//    }

    public static int getDimensionPixelSize(@NonNull Context context, @NonNull TypedArray attributes, @StyleableRes int index, int defaultValue) {
        TypedValue value = new TypedValue();
        if (attributes.getValue(index, value) && value.type == 2) {
            TypedArray styledAttrs = context.getTheme().obtainStyledAttributes(new int[]{value.data});
            int dimension = styledAttrs.getDimensionPixelSize(0, defaultValue);
            styledAttrs.recycle();
            return dimension;
        } else {
            return attributes.getDimensionPixelSize(index, defaultValue);
        }
    }

    public static boolean isFontScaleAtLeast1_3(@NonNull Context context) {
        return context.getResources().getConfiguration().fontScale >= 1.3F;
    }

    public static boolean isFontScaleAtLeast2_0(@NonNull Context context) {
        return context.getResources().getConfiguration().fontScale >= 2.0F;
    }

    @StyleableRes
    static int getIndexWithValue(@NonNull TypedArray attributes, @StyleableRes int a, @StyleableRes int b) {
        return attributes.hasValue(a) ? a : b;
    }
}