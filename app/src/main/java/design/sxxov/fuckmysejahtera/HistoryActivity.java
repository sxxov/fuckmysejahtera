package design.sxxov.fuckmysejahtera;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import design.sxxov.fuckmysejahtera.blocks.classes.Activity;
import design.sxxov.fuckmysejahtera.history.HistoryAdapter;
import design.sxxov.fuckmysejahtera.history.HistoryHTML;
import design.sxxov.fuckmysejahtera.history.HistoryHeader;
import design.sxxov.fuckmysejahtera.history.HistoryItem;
import design.sxxov.fuckmysejahtera.history.HistoryVerticalSpaceItemDecorator;
import design.sxxov.fuckmysejahtera.history.PausableLinearLayoutManager;
import design.sxxov.fuckmysejahtera.utilities.AsyncUtility;
import design.sxxov.fuckmysejahtera.utilities.WindowUtility;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollState;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;

public class HistoryActivity extends Activity {
    private static final ArrayList<HistoryItem> deletedItems = new ArrayList<>();
    private static final ArrayList<HistoryHTML> deletedImages = new ArrayList<>();
    private final List<HistoryItem> items = new ArrayList<>();
    public BottomSheetBehavior<View> bottomSheetBehavior;
    public WebView webView;
    private boolean isRefreshing = false;
    private WindowUtility windowUtility;
    private HistoryAdapter adapter;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView historyRecyclerView;
    private ImageView refreshImageView;
    private String[] deleteSnackbarTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentViewResId(R.layout.activity_history);
        super.setAppbarViewResId(R.id.history_appbar);
        super.setScrollableViewResId(R.id.history_list);
        super.onCreate(savedInstanceState);

        this.bottomSheetBehavior = BottomSheetBehavior
                .from(
                        this.findViewById(R.id.layout_bottom_sheet_image)
                );
        this.bottomSheetBehavior.setSkipCollapsed(true);
        this.hideImage();

        this.webView = this.findViewById(R.id.history_webview);
        this.windowUtility = new WindowUtility(this);
        this.adapter = new HistoryAdapter(this);
        this.historyRecyclerView = this.findViewById(R.id.history_list);
        this.refreshImageView = this.findViewById(R.id.history_refresh);
        this.itemTouchHelper = new ItemTouchHelper(
                this.getItemTouchHelperCallback()
        );
        this.historyRecyclerView.post(
                () -> itemTouchHelper
                        .attachToRecyclerView(
                                this.historyRecyclerView
                        )
        );
        this.deleteSnackbarTexts = new String[]{
                this.getResources().getString(R.string.history_deleted1),
                this.getResources().getString(R.string.history_deleted2),
                this.getResources().getString(R.string.history_deleted3),
                this.getResources().getString(R.string.history_deleted4),
                this.getResources().getString(R.string.history_deleted5),
                this.getResources().getString(R.string.history_deleted6),
                this.getResources().getString(R.string.history_deleted7),
                this.getResources().getString(R.string.history_deleted8),
                this.getResources().getString(R.string.history_deleted9),
                this.getResources().getString(R.string.history_deleted10)
        };

        this.setupHistoryRecyclerView();
        this.setupPullToRefresh();
        this.setupVerticalSpaceItemDecorator();
        this.setupHistoryWebView();

        new AsyncUtility().executeAsync(() -> {
            this.items.addAll(
                    MainActivity.db.historyDao().getItems()
            );

            this.adapter.submit(this.items);

            return null;
        });
    }

    private void setupVerticalSpaceItemDecorator() {
        this.historyRecyclerView.addItemDecoration(
                new HistoryVerticalSpaceItemDecorator(
                        this.windowUtility.getPx(8)
                )
        );
    }

    private void setupPullToRefresh() {
        VerticalOverScrollBounceEffectDecorator verticalOverScrollBounceEffectDecorator = (
                new VerticalOverScrollBounceEffectDecorator(
                        new RecyclerViewOverScrollDecorAdapter(
                                this.historyRecyclerView,
                                this.getItemTouchHelperCallback()
                        )
                )
        );
        AtomicBoolean isPulled = new AtomicBoolean(false);
        verticalOverScrollBounceEffectDecorator.setOverScrollUpdateListener(
                (IOverScrollDecor decor, int state, float offset) -> {
                    // change transforms
                    this.refreshImageView.setY(
                            (offset / 2)
                                    - (
                                    this.windowUtility.getPx(24)
                                            * Math.min(
                                            Math.max(
                                                    (1 - (offset / 128)),
                                                    0
                                            ),
                                            1
                                    )
                            )
                    );
                    this.refreshImageView.setRotation(offset / 2);

                    this.getAppbar().setY(offset);

                    if (state == IOverScrollState.STATE_BOUNCE_BACK) {
                        this.refreshImageView.setScaleX(Math.max(offset / 1000, 1));
                        this.refreshImageView.setScaleY(Math.max(offset / 1000, 1));
                    }

                    // schedule to be refreshed when let go
                    if (state == IOverScrollState.STATE_BOUNCE_BACK
                            && offset > HistoryActivity.this.windowUtility.getPx(24)) {
                        isPulled.set(true);
                    }

                    // refreshHistory if scheduled and hits 0
                    if (offset == 0
                            && isPulled.get()) {
                        isPulled.set(false);
                        HistoryActivity.this.adapter.notifyDataSetChanged();
                    }

                    // change visibility
                    if (offset > 0) {
                        this.refreshImageView.setVisibility(View.VISIBLE);
                    } else {
                        this.refreshImageView.setVisibility(View.GONE);
                    }
                }
        );
    }

    private ItemTouchHelper.Callback getItemTouchHelperCallback() {
        return new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public int getMovementFlags(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder
            ) {
                if (viewHolder instanceof HistoryHeader) {
                    return 0;
                }

                return ItemTouchHelper.Callback
                        .makeMovementFlags(
                                0,
                                0
                        );
            }

            @Override
            public boolean onMove(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder source,
                    @NonNull RecyclerView.ViewHolder target
            ) {
                return false;
            }

            @Override
            public void onSwiped(
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    int direction
            ) {
                HistoryActivity.this.delete(
                        HistoryActivity.this.adapter
                                .getItem(
                                        viewHolder.getAdapterPosition()
                                )
                );
            }
        };
    }

    public void scheduleRefresh() {
        if (this.isRefreshing) {
            return;
        }

        new AsyncUtility().executeAsync(() -> {
            this.isRefreshing = true;

            this.items.clear();
            this.items.addAll(
                    MainActivity.db.historyDao().getItems()
            );

//            this.adapter.items.clear();
//            this.adapter.items.addAll(this.items);

//            new Handler(Looper.getMainLooper()).post(() -> {
//                this.adapter.refresh();
            this.adapter.submit(this.items);

            this.isRefreshing = false;
//            });

            return null;
        });
    }

    private String getRandomDeleteSnackbarText() {
        int randomInt = new Random()
                .nextInt(
                        this.deleteSnackbarTexts.length - 1
                );

        return this.deleteSnackbarTexts[randomInt];
    }

    private void setupSnackbar(
            ConstraintLayout layout,
            String text
    ) {
        Snackbar snackbar = Snackbar
                .make(
                        layout,
                        text + " (trash: " + HistoryActivity.deletedItems.size() + ")",
                        Snackbar.LENGTH_INDEFINITE
                )
                .setAction(
                        R.string.undo,
                        (View view) -> {
                            if (HistoryActivity.deletedItems.size() <= 0) {
                                return;
                            }

                            HistoryItem historyItem = HistoryActivity
                                    .deletedItems
                                    .get(
                                            HistoryActivity.deletedItems.size() - 1
                                    );
                            HistoryHTML historyHTML = HistoryActivity
                                    .deletedImages
                                    .get(
                                            HistoryActivity.deletedImages.size() - 1
                                    );

                            HistoryActivity.deletedItems.remove(
                                    historyItem
                            );
                            HistoryActivity.deletedImages.remove(
                                    historyHTML
                            );

                            this.items.add(historyItem);
                            this.adapter.submit(this.items);

                            new AsyncUtility().executeAsync(() -> {
                                MainActivity.db.historyDao().insertItems(historyItem);
                                MainActivity.db.historyDao().insertHTMLs(historyHTML);

                                return null;
                            });

                            // spawn another snackbar if there are still things to undo
                            if (HistoryActivity.deletedItems.size() > 0) {
                                setupSnackbar(
                                        layout,
                                        this.getRandomDeleteSnackbarText()
                                );
                            }
                        }
                );

//        // change snackbar margins
//        FrameLayout snackbarView = (FrameLayout) snackbar.getView();
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView
//                .getChildAt(0)
//                .getLayoutParams();
//        params
//                .setMargins(
//                        params.leftMargin,
//                        params.topMargin + this.windowUtility.getPx(6),
//                        params.rightMargin,
//                        params.bottomMargin + this.windowUtility.getPx(7)
//                );
//
//        snackbarView
//                .getChildAt(0)
//                .setLayoutParams(params);
//
//        snackbarView
//                .setBackground(
//                        AppCompatResources
//                                .getDrawable(
//                                        this,
//                                        R.drawable.snackbar_shape
//                                )
//                );
//
//        // change snackbar fonts
//        TextView globalSnackbarTextView = snackbarView
//                .findViewById(
//                        com.google.android.material.R.id.snackbar_text
//                );
//        TextView globalSnackbarActionTextView = snackbarView
//                .findViewById(
//                        com.google.android.material.R.id.snackbar_action
//                );
//
//        Typeface font = ResourcesCompat.getFont(this, R.font.space_mono);
//
//        globalSnackbarTextView.setTypeface(font);
//        globalSnackbarActionTextView.setTypeface(font);
//
//        // change snackbar colours
//        TypedValue typedValue = new TypedValue();
//        this
//                .getTheme()
//                .resolveAttribute(
//                        R.attr.colourTextPrimary,
//                        typedValue,
//                        true
//                );
//        globalSnackbarTextView.setTextColor(typedValue.data);

        // show snackbar
        snackbar.show();
    }

    public void delete(HistoryItem historyItem) {
        HistoryActivity.deletedItems
                .add(historyItem);

        this.items.remove(historyItem);
        this.adapter.submit(this.items);

        new AsyncUtility().executeAsync(() -> {
            HistoryActivity.deletedImages.add(
                    MainActivity.db
                            .historyDao()
                            .getHTMLById(historyItem.id)
                            .get(0)
            );
            MainActivity.db.historyDao().deleteItem(historyItem);
            MainActivity.db.historyDao().deleteHTMLById(historyItem.id);

            return null;
        });

        this.setupSnackbar(
                this.findViewById(R.id.layout_activity_history),
                this.getRandomDeleteSnackbarText()
        );
    }

    private void setupHistoryRecyclerView() {
        this.adapter.setHasStableIds(true);

        this.historyRecyclerView.setAdapter(this.adapter);
        this.historyRecyclerView.setLayoutManager(
                new PausableLinearLayoutManager(this)
        );
        this.historyRecyclerView.addItemDecoration(
                new HistoryVerticalSpaceItemDecorator(
                        this.windowUtility.getPx(32)
                )
        );
    }

    @Override
    public void onBackPressed() {
        if (this.bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            this.hideImage();

            return;
        }

        this.finish();
    }

    public void onHistoryHTMLClick(View v) {
        this.hideImage();
    }

    public void onOpenCameraButtonClick(View v) {
        try {
            PackageManager packageManager = this.getPackageManager();
            this.startActivity(
                    packageManager
                            .getLaunchIntentForPackage(
                                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                            .resolveActivity(packageManager)
                                            .getPackageName()
                            )
            );
        } catch (Exception err) {
            err.printStackTrace();
            Toast.makeText(
                    this,
                    getString(R.string.camera_not_found),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    public void setupHistoryWebView() {
        this.webView
                .getSettings()
                .setJavaScriptEnabled(true);
        this.webView.setVerticalScrollBarEnabled(false);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            return true;
        });
    }

    public void showHTML(String html) {
//        this.imageView.setImageBitmap(bitmap);

        this.webView.loadDataWithBaseURL(
                "https://mysejahtera.malaysia.gov.my/",
                "<script>document.write(" + html + ")</script>",
                "text/html; charset=utf-8",
                "UTF-8",
                null
        );

        this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void hideImage() {
        this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void onAppBarButtonBackClick(View view) {
        this.finish();
    }
}
