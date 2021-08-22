package design.sxxov.fuckmysejahtera.history;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import design.sxxov.fuckmysejahtera.HistoryActivity;
import design.sxxov.fuckmysejahtera.MainActivity;
import design.sxxov.fuckmysejahtera.R;
import design.sxxov.fuckmysejahtera.utilities.AsyncUtility;
import design.sxxov.fuckmysejahtera.utilities.ImageUtility;
import design.sxxov.fuckmysejahtera.utilities.SnackbarUtility;
import design.sxxov.fuckmysejahtera.utilities.WindowUtility;

public class HistoryHolder extends HistoryTypedViewHolder {
    private final HistoryActivity ctx;

    private final TextView titleTextView;
    private final TextView descriptionTextView;
    private final FloatingActionButton xFAB;
    private final View historyItemBackgroundView;
    private final View historyItemView;

    private final WindowUtility windowUtility;

    public HistoryHolder(
            HistoryActivity ctx,
            View historyItemView
    ) {
        super(historyItemView);

        this.windowUtility = new WindowUtility(ctx);
        this.ctx = ctx;

        this.historyItemView = historyItemView;
        this.titleTextView = this.historyItemView
                .findViewById(R.id.history_item_title);
        this.descriptionTextView = this.historyItemView
                .findViewById(R.id.history_item_desc);
        this.xFAB = this.historyItemView
                .findViewById(R.id.history_x);
        this.historyItemBackgroundView = this.historyItemView
                .findViewById(R.id.history_item_bg);
    }

    public View getView() {
        return this.historyItemView;
    }

    private void setupTextViewMargins(
            TextView titleTextView,
            TextView descriptionTextView
    ) {
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        int[] titleMargins = new int[] {
                36,
                28,
                36,
                0
        };

        int[] descriptionMargins = new int[] {
                36,
                9,
                36,
                28
        };

        titleParams.setMargins(
                this.windowUtility.getPx(titleMargins[0]),
                this.windowUtility.getPx(titleMargins[1]),
                this.windowUtility.getPx(titleMargins[2]),
                this.windowUtility.getPx(titleMargins[3])
        );
        descriptionParams.setMargins(
                this.windowUtility.getPx(descriptionMargins[0]),
                this.windowUtility.getPx(descriptionMargins[1]),
                this.windowUtility.getPx(descriptionMargins[2]),
                this.windowUtility.getPx(descriptionMargins[3])
        );

        titleTextView.setLayoutParams(titleParams);
        descriptionTextView.setLayoutParams(descriptionParams);
    }

    private void setupXFABOnClick(
            FloatingActionButton xFAB,
            HistoryItem historyItem
    ) {
        xFAB.setOnClickListener(
                (View view) -> {
                    try {
                        HistoryHolder.this.ctx
                                .delete(historyItem);
                    } catch (IllegalArgumentException ignored) {}
                }
        );
    }

    private void setupHistoryItemBackgroundOnClick(
            View historyItemBackgroundView,
            HistoryItem historyItem
    ) {
        historyItemBackgroundView.setOnClickListener(
                (View view) -> {
                    new AsyncUtility()
                            .executeAsync(
                                    () -> MainActivity.db
                                        .historyDao()
                                        .getHTMLById(historyItem.id)
                                        .get(0)
                                        .html,
                                    new AsyncUtility.Callback<>() {
                                    @Override
                                        public void onComplete(String result) {
                                            HistoryHolder.this.ctx.showHTML(
                                                    result
                                            );
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            if (e instanceof IndexOutOfBoundsException) {
                                                new SnackbarUtility(HistoryHolder.this.ctx)
                                                        .create(
                                                                R.string.history_generic_unavailable,
                                                                historyItemBackgroundView
                                                        )
                                                        .show();

                                                return;
                                            }

                                            e.printStackTrace();
                                        }
                                    }
                            );
                }
        );
    }

    private void setupTitleText(
            TextView titleTextView,
            String text
    ) {
        titleTextView.setText(text);
    }

    private void setupDescriptionText(
            TextView descriptionTextView,
            String text
    ) {
        descriptionTextView.setText(text);
    }

    private void setupFadeIn(
            TextView titleTextView,
            TextView descriptionTextView,
            View historyItemBackgroundView
    ) {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(200);

        titleTextView.startAnimation(fadeIn);
        descriptionTextView.startAnimation(fadeIn);
        historyItemBackgroundView.startAnimation(fadeIn);
    }

    public void bind(HistoryItem historyItem) {
        this.setupTextViewMargins(
                this.titleTextView,
                this.descriptionTextView
        );

        this.setupXFABOnClick(
                this.xFAB,
                historyItem
        );

        this.setupHistoryItemBackgroundOnClick(
                this.historyItemBackgroundView,
                historyItem
        );

        this.setupTitleText(
                this.titleTextView,
                historyItem.time
        );

        this.setupDescriptionText(
                this.descriptionTextView,
                historyItem.location
        );

        this.setupFadeIn(
                this.titleTextView,
                this.descriptionTextView,
                this.historyItemBackgroundView
        );
    }

    @Override
    public int getType() {
        return HistoryAdapter.HOLDER_VIEW_TYPE;
    }
}
