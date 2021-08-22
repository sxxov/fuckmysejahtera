package design.sxxov.fuckmysejahtera.utilities;

import android.app.Activity;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import design.sxxov.fuckmysejahtera.R;

public class ResourceUtility {
    private Activity ctx;

    public ResourceUtility(Activity ctx) {
        this.ctx = ctx;
    }

    public String getString(int resId) {
        return this.ctx.getResources().getString(resId);
    }

    @ColorInt
    public int getAttrColour(int resId) {
        TypedValue typedValue = new TypedValue();
        this.ctx
                .getTheme()
                .resolveAttribute(
                        resId,
                        typedValue,
                        true
                );

        return typedValue.data;
    }

    @ColorInt
    public int getColour(int resId) {
        return ContextCompat.getColor(this.ctx, resId);
    }
}
