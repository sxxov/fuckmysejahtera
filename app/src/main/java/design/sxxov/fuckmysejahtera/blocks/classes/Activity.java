package design.sxxov.fuckmysejahtera.blocks.classes;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.atomic.AtomicBoolean;

import design.sxxov.fuckmysejahtera.R;
import design.sxxov.fuckmysejahtera.settings.SettingsItem;
import design.sxxov.fuckmysejahtera.settings.SettingsStorage;
import design.sxxov.fuckmysejahtera.utilities.ResourceUtility;
import design.sxxov.fuckmysejahtera.utilities.SnackbarUtility;
import design.sxxov.fuckmysejahtera.utilities.WindowUtility;

public class Activity extends AppCompatActivity {
    private final AtomicBoolean isAppbarLayoutScheduled = new AtomicBoolean();
    protected WindowUtility windowUtility;
    protected SnackbarUtility snackbarUtility;
    protected ResourceUtility resourceUtility;
    protected SettingsStorage settingsStorage;
    protected SettingsItem state;
    protected int thisLayoutResId;
    protected int appbarResId;
    protected int scrollableViewResId;
    private View appbar;
    private View scrollableView;
    private int initialAppbarHeight;

    public View getAppbar() {
        return this.appbar;
    }

    public int getInitialAppbarHeight() {
        return this.initialAppbarHeight;
    }

    protected void setInitialAppbarHeight(int initialAppbarHeight) {
        this.appbar.getLayoutParams().height = initialAppbarHeight;

        if (this.isAppbarLayoutScheduled.get())
            return;

        this.isAppbarLayoutScheduled.set(true);

        this.appbar.postOnAnimation(() -> {
            if (!this.isAppbarLayoutScheduled.get())
                return;

            this.isAppbarLayoutScheduled.set(false);
            this.appbar.requestLayout();
        });
    }

    protected void setScrollableViewResId(int scrollableViewResId) {
        this.scrollableViewResId = scrollableViewResId;
    }

    protected void setAppbarViewResId(int appbarViewResId) {
        this.appbarResId = appbarViewResId;
    }

    protected void setContentViewResId(int thisLayoutResId) {
        this.thisLayoutResId = thisLayoutResId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.settingsStorage = new SettingsStorage(this);
        this.state = this.settingsStorage.get();

        this.setupTheme();

        if (this.thisLayoutResId > 0) {
            this.setContentView(this.thisLayoutResId);
        }

        this.windowUtility = new WindowUtility(this);

        if (this.appbarResId > 0 && this.scrollableViewResId > 0) {
            this.appbar = this.findViewById(this.appbarResId);
            this.scrollableView = this.findViewById(this.scrollableViewResId);

            final ViewTreeObserver observer = this.appbar.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Activity.this.initialAppbarHeight = Activity.this.appbar.getHeight();

                    Activity.this.setupResponsiveAppbar();

                    Activity.this.appbar
                            .getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                }
            });
        }

        this.snackbarUtility = new SnackbarUtility(this);
        this.resourceUtility = new ResourceUtility(this);

        this.setupStatusBar();
        this.setupNavigationBar();
    }

    private void setupResponsiveAppbar() {
        View scrollableView = this.findViewById(this.scrollableViewResId);

        if (scrollableView instanceof ScrollView) {
            scrollableView.getViewTreeObserver().addOnScrollChangedListener(
                    () -> this.setInitialAppbarHeight(
                            Math.max(
                                    this.initialAppbarHeight - this.scrollableView.getScrollY(),
                                    (int) (this.initialAppbarHeight / 2f)
                            )
                    ));

            return;
        }

        if (scrollableView instanceof RecyclerView) {
            ((RecyclerView) scrollableView)
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            Activity.this.setInitialAppbarHeight(
                                    Math.max(
                                            initialAppbarHeight
                                                    - recyclerView.computeVerticalScrollOffset(),
                                            (int) (initialAppbarHeight / 2f)
                                    )
                            );
                        }
                    });

            return;
        }

        throw new IllegalStateException(
                "Activity#scrollableView is not instance of either ScrollView or RecyclerView"
        );
    }

    private void setupTheme() {
        if (this.state.isNightMode) {
            this.setTheme(R.style.Dark);

            return;
        }

        this.setTheme(R.style.Light);
    }

    private void setupStatusBar() {
        final boolean isNightMode = this.state.isNightMode;
        boolean isDifferent = false;

        int flags = this
                .getWindow()
                .getDecorView()
                .getSystemUiVisibility();
        int processedFlags;
        final int colour = ContextCompat.getColor(this, R.color.colorAccent);

        if (isNightMode) {
            processedFlags = flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

            if (flags != 0) {
                isDifferent = true;
            }
        } else {
            processedFlags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

            if (flags == 0) {
                isDifferent = true;
            }
        }

        // only commit changes if they're different
        if (isDifferent) {
            this
                    .getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(processedFlags);
        }

        // set status bar COLOUR
        this
                .getWindow()
                .setStatusBarColor(
                        colour
                );

    }

    private void setupNavigationBar() {
        final boolean isNightMode = this.state.isNightMode;
        @ColorInt int colour = ContextCompat.getColor(this, R.color.colorLightDark);

        if (isNightMode) {
            colour = ContextCompat.getColor(this, R.color.colorPrimary);
        }

        super.getWindow()
                .setNavigationBarColor(colour);
    }

    public SettingsItem getState() {
        return this.state;
    }
}
