package design.sxxov.fuckmysejahtera.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import design.sxxov.fuckmysejahtera.R;

public class HistoryHeader extends HistoryTypedViewHolder {
    public HistoryHeader(
            Context ctx,
            @NonNull ViewGroup parent
    ) {
        super(
                LayoutInflater
                .from(ctx)
                .inflate(
                        R.layout.history_header,
                        parent,
                        false
                )
        );
    }

    public void setHeight(int px) {
        this.itemView.getLayoutParams().height = px;
        this.itemView.requestLayout();
    }

    @Override
    public int getType() {
        return HistoryAdapter.HEADER_VIEW_TYPE;
    }
}
