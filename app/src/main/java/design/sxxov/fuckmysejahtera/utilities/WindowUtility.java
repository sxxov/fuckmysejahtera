package design.sxxov.fuckmysejahtera.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class WindowUtility {
    private Context ctx;
    private Resources resources;

    public WindowUtility(Context ctx) {
        this.ctx = ctx;
        this.resources = this.ctx.getResources();
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
        Resources r = this.resources;

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }
}
