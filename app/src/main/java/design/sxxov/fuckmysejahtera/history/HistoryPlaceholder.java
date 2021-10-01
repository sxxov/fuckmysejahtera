package design.sxxov.fuckmysejahtera.history;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import design.sxxov.fuckmysejahtera.R;

public class HistoryPlaceholder extends HistoryTypedViewHolder {
    private final TextView titleTextView;
    private final TextView descriptionTextView;
    private final Button ctaButton;
    private final View historyItemBackgroundView;
    private final View historyItemView;

    public HistoryPlaceholder(
            View historyItemView
    ) {
        super(historyItemView);

        this.historyItemView = historyItemView;
        this.titleTextView = this.historyItemView
                .findViewById(R.id.history_placeholder_title);
        this.descriptionTextView = this.historyItemView
                .findViewById(R.id.history_placeholder_desc);
        this.ctaButton = this.historyItemView
                .findViewById(R.id.history_placeholder_cta);
        this.historyItemBackgroundView = this.historyItemView
                .findViewById(R.id.history_placeholder_bg);
    }

    @Override
    public int getType() {
        return HistoryAdapter.PLACEHOLDER_VIEW_TYPE;
    }

    public View getView() {
        return this.historyItemView;
    }

    private void setupFadeIn(
            TextView titleTextView,
            TextView descriptionTextView,
            Button ctaButton,
            View historyItemBackgroundView
    ) {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(200);

        titleTextView.startAnimation(fadeIn);
        descriptionTextView.startAnimation(fadeIn);
        ctaButton.startAnimation(fadeIn);
        historyItemBackgroundView.startAnimation(fadeIn);
    }

    public void bind() {
        this.setupFadeIn(
                this.titleTextView,
                this.descriptionTextView,
                this.ctaButton,
                this.historyItemBackgroundView
        );
    }
}
