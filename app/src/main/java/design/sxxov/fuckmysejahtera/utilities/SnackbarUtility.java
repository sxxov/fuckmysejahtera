package design.sxxov.fuckmysejahtera.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import design.sxxov.fuckmysejahtera.R;

public class SnackbarUtility {
    private Context ctx;
    private ResourceUtility resourceUtility;

    public SnackbarUtility(Activity ctx) {
        this.ctx = ctx;
        this.resourceUtility = new ResourceUtility(ctx);
    }

    public Snackbar create(int messageResId, @NonNull View view) {
        return this.create(this.resourceUtility.getString(messageResId), view);
    }

    public Snackbar create(String message, @NonNull View view) {
        return this.create(message, R.color.colorAccent, view);
    }

    public Snackbar create(int messageResId, int colourResId, @NonNull View view) {
        return this.create(this.resourceUtility.getString(messageResId), colourResId, view);
    }

    public Snackbar create(String message, int colourResId, @NonNull View view) {
        return this.create(message, colourResId, Snackbar.LENGTH_SHORT, view);
    }

    public Snackbar create(int messageResId, int colourResId, int length, @NonNull View view) {
        return this.create(this.resourceUtility.getString(messageResId), colourResId, length, view);
    }

    public Snackbar create(String message, int colourResId, int length, @NonNull View view) {
        final Snackbar snackbar = Snackbar
                .make(
                        view,
                        message,
                        length
                );

        snackbar.setAction(
                R.string.snackbar_common_action_ok,
                v -> {}
        );

        snackbar
                .getView()
                .setBackgroundTintList(
                        ContextCompat.getColorStateList(
                                this.ctx,
                                colourResId
                        )
                );

        return snackbar;
    }
}
