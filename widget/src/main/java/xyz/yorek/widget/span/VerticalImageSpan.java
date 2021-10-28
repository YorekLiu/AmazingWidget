package xyz.yorek.widget.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

/**
 * Created by yorek.liu 2020/5/7
 *
 * @author yorek.liu
 */
public class VerticalImageSpan extends ImageSpan {
    public VerticalImageSpan(@NonNull Drawable drawable) {
        super(drawable);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();

        int transY = top + (bottom - top) / 2 - b.getBounds().height() / 2;

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}