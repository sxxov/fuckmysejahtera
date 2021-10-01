package design.sxxov.fuckmysejahtera.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class WindowUtility {
    private final Resources resources;

    public WindowUtility(Context ctx) {
        this.resources = ctx.getResources();
    }

    public int getStatusBarHeight() {
        int resourceId = this.resources
                .getIdentifier(
                        "status_bar_height",
                        "dimen",
                        "android"
                );

        if (resourceId <= 0) {
            return 0;
        }

        return this.resources
                .getDimensionPixelSize(resourceId);
    }

    public int getPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                this.resources.getDisplayMetrics()
        );
    }
}
