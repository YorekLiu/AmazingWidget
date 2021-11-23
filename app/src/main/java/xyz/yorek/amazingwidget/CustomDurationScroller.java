package xyz.yorek.amazingwidget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class CustomDurationScroller extends Scroller {
    private boolean mNoDuration;

    private static final Interpolator sInterpolator = t -> {
        t -= 1.0f;
        return t * t * t * t * t + 1.0f;
    };

    public CustomDurationScroller(Context context) {
        super(context, sInterpolator);
    }

    public void setNoDuration(boolean noDuration) {
        mNoDuration = noDuration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mNoDuration ? 0 : duration);
    }
}