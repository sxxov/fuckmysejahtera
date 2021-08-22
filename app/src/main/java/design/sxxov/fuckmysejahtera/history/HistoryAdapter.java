package design.sxxov.fuckmysejahtera.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import design.sxxov.fuckmysejahtera.HistoryActivity;
import design.sxxov.fuckmysejahtera.R;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final AsyncListDiffer<HistoryItem> differ = new AsyncListDiffer<>(
            this,
            DIFF_CALLBACK
    );
    public static final DiffUtil.ItemCallback<HistoryItem> DIFF_CALLBACK
            = new DiffUtil.ItemCallback<HistoryItem>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull HistoryItem oldUser, @NonNull HistoryItem newUser) {
            // User properties may have changed if reloaded from the DB, but ID is fixed
            return oldUser.id == newUser.id;
        }
        @Override
        public boolean areContentsTheSame(
                @NonNull HistoryItem oldUser, @NonNull HistoryItem newUser) {
            // NOTE: if you use equals, your object must properly override Object#equals()
            // Incorrectly returning false here will result in too many animations.
            return oldUser.id == newUser.id;
        }
    };

    public static final int HEADER_VIEW_TYPE = 0;
    public static final int HOLDER_VIEW_TYPE = 1;
    public static final int PLACEHOLDER_VIEW_TYPE = 2;

    private final HistoryActivity ctx;

    public HistoryHeader historyHeader;

    public HistoryAdapter(
            HistoryActivity ctx
        ) {
        this.ctx = ctx;

        super.setHasStableIds(true);
    }

//    @Deprecated
//    public void refresh() {
//        int cachedLength = this.cachedHistoryItems != null
//                ? this.cachedHistoryItems.size()
//                : 0;
//
//        int firstChangeIndex = cachedLength;
//        int lastChangeIndex = -1;
//
////        int headerOffset = (
////                this.items.size() == 0
////                        || cachedLength == 0
////                        ? 0
////                        : 1
////        );
//        int headerOffset = 1;
//
//        if (Arrays.equals(
//                this.cachedHistoryItems.toArray(new HistoryItem[] {}),
//                this.items.toArray(new HistoryItem[] {})
//        )) {
//            return;
//        }
//
////        if (initialCachedLength == 0
////            || historyItems.length == 0) {
////            // schedule header to be notified as changed
////            headerOffset = 0;
////        }
//
//        // go through each notificationItem and check if ids match
//        // if not, mark the first change into firstChangeIndex
//        // and the last change into lastChangeIndex
//        for (
//                int i = 0;
//                i < Math.max(this.items.size(), this.cachedHistoryItems.size());
//                ++i
//        ) {
////            if (i > historyItems.length - 1) {
////                lastChangeIndex = cachedNotificationItems[cachedNotificationItems.length - 1].index;
////
////                break;
////            }
//
//            HistoryItem historyItem = (
//                    i < this.items.size()
//                            ? this.items.get(i)
//                            : null
//            );
//
////            // mostly for empty notification items
////            if (i > cachedNotificationItems.length - 1) {
////                firstChangeIndex = historyItem.index;
////                lastChangeIndex = historyItems[historyItems.length - 1].index;
////
////                break;
////            }
//
//            HistoryItem cachedHistoryItem = (
//                    i < this.cachedHistoryItems.size()
//                            ? this.cachedHistoryItems.get(i)
//                            : null
//            );
//
//            if (historyItem == null) {
//                lastChangeIndex = getMaxIndexFromHistoryItems(this.cachedHistoryItems);
//                if (firstChangeIndex == cachedLength) {
//                    firstChangeIndex = lastChangeIndex;
//                }
//
//                continue;
//            }
//
//            if (cachedHistoryItem == null) {
//                lastChangeIndex = getMaxIndexFromHistoryItems(this.items);
//                if (firstChangeIndex == cachedLength) {
//                    firstChangeIndex = lastChangeIndex;
//                }
//
//                continue;
//            }
//
//            if (historyItem.id != cachedHistoryItem.id) {
//                int index = this.getItemPosition(historyItem);
//                // firstChangeIndex becomes a singleton
//                if (index < firstChangeIndex) {
//                    firstChangeIndex = index;
//                }
//                // lastChangeIndex will be assigned every time, thus it'll have the last value
//                if (index > lastChangeIndex) {
//                    lastChangeIndex = index;
//                }
//            }
//        }
//
//        this.cachedHistoryItems = new ArrayList(this.items);
//
//        if (firstChangeIndex == -1
//                && lastChangeIndex == -1) {
//            return;
//        }
//
//        int offsettedFirstChangeIndex = firstChangeIndex + headerOffset;
//
//        if (this.items.size() > cachedLength) {
//            this.notifyItemRangeInserted(
//                    offsettedFirstChangeIndex,
//                    this.items.size() - cachedLength
//            );
//        }
//
//        if (this.items.size() < cachedLength) {
//            this.notifyItemRangeRemoved(
//                    offsettedFirstChangeIndex,
//                    cachedLength - this.items.size()
//            );
//        }
//
//        this.notifyItemRangeChanged(
//                offsettedFirstChangeIndex,
//                Math.max(
//                        Math.min(
//                                lastChangeIndex - firstChangeIndex,
//                                this.items.size() - offsettedFirstChangeIndex
//                        ),
//                        1
//                )
//        );
//    }

    public void submit(List<HistoryItem> list) {
        Collections.sort(
                list,
                (obj1, obj2) -> Long.compare(obj2.id, obj1.id));

        this.differ.submitList(list);
        this.notifyDataSetChanged();
    }

    private int getMaxIndexFromHistoryItems(List<HistoryItem> historyItems) {
        int maxIndex = 0;

        for (HistoryItem historyItem : historyItems) {
            int index = this.getItemPosition(historyItem);

            if (index > maxIndex) {
                maxIndex = index;
            }
        }

        return maxIndex;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_VIEW_TYPE: {
                this.historyHeader = new HistoryHeader(
                        this.ctx,
                        parent
                );

                return historyHeader;
            }
            case HOLDER_VIEW_TYPE: {
                return new HistoryHolder(
                        this.ctx,
                        LayoutInflater
                                .from(this.ctx)
                                .inflate(
                                        R.layout.history_item,
                                        parent,
                                        false
                                )
                );
            }
            case PLACEHOLDER_VIEW_TYPE: {
                return new HistoryPlaceholder(
                        LayoutInflater
                                .from(this.ctx)
                                .inflate(
                                        R.layout.history_placeholder,
                                        parent,
                                        false
                                )
                );
            }
            default: {
                throw new IllegalArgumentException("viewType: " + viewType);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (((HistoryTypedViewHolder) holder).getType()) {
            case HEADER_VIEW_TYPE: {
                holder.setIsRecyclable(false);

                break;
            }
            case HOLDER_VIEW_TYPE: {
                ((HistoryHolder) holder)
                        .bind(this.differ.getCurrentList().get(position - 1));

                break;
            }
            case PLACEHOLDER_VIEW_TYPE: {
                ((HistoryPlaceholder) holder)
                        .bind();

                break;
            }
            default: {
                throw new IllegalArgumentException(
                        "holder: " + holder.getClass().getSimpleName()
                );
            }
        }
    }

    public boolean isEmpty() {
        return this.differ.getCurrentList().size() == 0;
    }

    @Override
    public int getItemCount() {
        return this.differ.getCurrentList().size() + 1 + (this.isEmpty() ? 1 : 0);
    }


    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return 0;
        }

        if (this.isEmpty()) {
            return 1;
        }

        return this.getItem(position).id;
    }

    public HistoryItem getItem(int position) {
        return this.differ.getCurrentList().get(position - 1);
    }

    public int getItemPosition(HistoryItem item) {
        return this.differ.getCurrentList().indexOf(item);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW_TYPE;
        }

        if (isEmpty()) {
            return PLACEHOLDER_VIEW_TYPE;
        }

        return HOLDER_VIEW_TYPE;
    }
}
