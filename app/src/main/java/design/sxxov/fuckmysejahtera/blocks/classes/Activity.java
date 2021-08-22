package design.sxxov.fuckmysejahtera.blocks.classes;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import design.sxxov.fuckmysejahtera.R;
import design.sxxov.fuckmysejahtera.settings.SettingsItem;
import design.sxxov.fuckmysejahtera.settings.SettingsStorage;
import design.sxxov.fuckmysejahtera.utilities.ResourceUtility;
import design.sxxov.fuckmysejahtera.utilities.SnackbarUtility;

public class Activity extends AppCompatActivity {
    protected SnackbarUtility snackbarUtility;
    protected ResourceUtility resourceUtility;
    protected SettingsStorage settingsStorage;
    protected SettingsItem state;
    protected int thisLayoutResId;

    protected void setContentViewResId(int thisLayoutResId) {
        this.thisLayoutResId = thisLayoutResId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.settingsStorage = new SettingsStorage(this);
        this.state = this.settingsStorage.get();

        this.setupTheme();

        if (this.thisLayoutResId != -1) {
            this.setContentView(this.thisLayoutResId);
        }

        this.snackbarUtility = new SnackbarUtility(this);
        this.resourceUtility = new ResourceUtility(this);

        this.setupStatusBar();
        this.setupNavigationBar();
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
