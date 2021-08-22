package design.sxxov.fuckmysejahtera.history;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class HistoryTypedViewHolder extends RecyclerView.ViewHolder {
    public HistoryTypedViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract int getType();
}
