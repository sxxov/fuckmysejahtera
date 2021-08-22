package design.sxxov.fuckmysejahtera.history;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryVerticalSpaceItemDecorator extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public HistoryVerticalSpaceItemDecorator(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        // determine if it's a holder item and not the header
        if (parent
                .getAdapter()
                .getItemViewType(parent.getChildAdapterPosition(view))
                ==
                HistoryAdapter.HOLDER_VIEW_TYPE
        ) {
                // set the bottom offset to the specified height
                outRect.top = verticalSpaceHeight;
        }

        // determine if it's the last holder
        if (
                parent
                        .getChildAdapterPosition(view)
                        ==
                        parent
                                .getAdapter()
                                .getItemCount()
                                - 1
        ) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
