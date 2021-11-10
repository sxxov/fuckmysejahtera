package design.sxxov.fuckmysejahtera;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;

import design.sxxov.fuckmysejahtera.blocks.classes.Activity;
import design.sxxov.fuckmysejahtera.settings.SettingsItem;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivity extends Activity {
    private final HashMap<Integer, AnimatorSet> buttonIdToAnimatorSet = new HashMap<>() {
        {
            put(R.id.main_input_button_risk_low, new AnimatorSet());
            put(R.id.main_input_button_risk_high, new AnimatorSet());
            put(R.id.main_input_button_vaccination_true, new AnimatorSet());
            put(R.id.main_input_button_vaccination_false, new AnimatorSet());
        }
    };
    private Snackbar snackbar;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetHelpBehavior;
    private float lastDownTouchEventX;
    private float lastDownTouchEventY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentViewResId(R.layout.activity_main);
        super.setAppbarViewResId(R.id.main_appbar);
        super.setScrollableViewResId(R.id.main_scroll);
        super.onCreate(savedInstanceState);

        final Intent intent = this.getIntent();
        final String from = intent.getStringExtra("from");

        this.setupOverScroll(
                this.findViewById(R.id.main_scroll)
        );
        this.setupEditTexts(
                this.findViewById(R.id.main_input_name),
                this.findViewById(R.id.main_input_contact)
        );
        this.setupHamburger(
                this.findViewById(R.id.main_input_menu_hamburger)
        );
        this.setupBottomSheet(
                this.findViewById(R.id.layout_bottom_sheet_help)
        );
        this.setupLayoutScroll(
                this.findViewById(R.id.main_scroll)
        );
        this.applyStateToButtons();
//        this.setupButtonsAnimationSet();

        if (from != null
                && from.equals(ReceiptActivity.class.getSimpleName())) {
            this.onInputButtonSaveClick(null);
        }

        if (this.state.isFirstRun) {
            this.bottomSheetHelpBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.setupHelpButtonSetDefault(
                this.findViewById(R.id.help_button_set_default)
        );
    }

    // stolen from https://stackoverflow.com/questions/2354336/how-to-exit-when-back-button-is-pressed
    @Override
    public void onBackPressed() {
        if (bottomSheetHelpBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetHelpBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            return;
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);

        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        this.startActivity(intent);
        this.finish();
    }

    // stolen from https://stackoverflow.com/questions/36104379/how-to-dismiss-a-snackbar-when-user-interact-elsewhere
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //#region dismiss snackbar
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // dismiss snackBar
            if (this.snackbar != null
                    && this.snackbar.isShown()) {
                final Rect rect = new Rect();
                this.snackbar.getView().getHitRect(rect);

                // This way the snackbar will only be dismissed if
                // the user clicks outside it.
                if (!rect.contains((int) ev.getX(), (int) ev.getY())) {
                    this.snackbar.dismiss();
                    this.snackbar = null;
                }
            }
        }
        //#endregion

        //#region dismiss keyboard
        // https://stackoverflow.com/a/61290481
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            this.lastDownTouchEventX = ev.getRawX();
            this.lastDownTouchEventY = ev.getRawY();
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();
            int x = (int) ev.getRawX();
            int y = (int) ev.getRawY();

            // Was it a scroll - If skip all
            if (Math.abs(this.lastDownTouchEventX - x) > 5
                    || Math.abs(this.lastDownTouchEventY - y) > 5) {
                return super.dispatchTouchEvent(ev);
            }

            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);

                if (!outRect.contains(x, y)) {
                    boolean touchTargetIsEditText = false;

                    // Check if another editText has been touched
                    for (View vi : v.getRootView().getTouchables()) {
                        if (vi instanceof EditText) {
                            Rect clickedViewRect = new Rect();
                            vi.getGlobalVisibleRect(clickedViewRect);

                            if (clickedViewRect.contains(x, y)) {
                                touchTargetIsEditText = true;
                                break;
                            }
                        }
                    }

                    if (!touchTargetIsEditText) this.dismissSoftKeyboard();
                    v.clearFocus();
                }
            }
        }
        //#endregion

        return super.dispatchTouchEvent(ev);
    }

    public void onOverlayBottomSheetClick(View overlayBottomSheet) {
        this.bottomSheetHelpBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        overlayBottomSheet.setVisibility(View.GONE);
    }

    public void onInputMenuHamburgerButtonOpenHistory(
            View inputMenuHamburgerButtonToggleSun
    ) {
        startActivity(new Intent(this, HistoryActivity.class));
    }

    public void onInputMenuHamburgerButtonToggleSunClick(
            View inputMenuHamburgerButtonToggleSun
    ) {
        this.settingsStorage.set(
                SettingsItem.IS_NIGHT_MODE_KEY,
                !this.state.isNightMode
        );

        this.recreate();
    }

    public void onInputMenuHamburgerButtonHelpClick(View v) {
        this.bottomSheetHelpBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        this.findViewById(R.id.main_overlay_bottom_sheet).setVisibility(View.VISIBLE);
    }

    public void onInputButtonSaveClick(View inputButtonSave) {
        boolean isValid = true;
        int snackbarMessageResId = R.string.snackbar_save_complete;
        int snackbarColourResId;

        if (this.state.contact == null
                || this.state.contact.length() < 8
                || !this.state.contact.matches("^(\\+|)[0-9|]{8,14}$")) {
            snackbarMessageResId = R.string.snackbar_save_contact_invalid;
            isValid = false;
        }

        if (this.state.contact == null
                || this.state.contact.length() == 0) {
            snackbarMessageResId = R.string.snackbar_save_contact_empty;
            isValid = false;
        }

        if (this.state.name == null
                || this.state.name.length() == 0) {
            snackbarMessageResId = R.string.snackbar_save_name_empty;
            isValid = false;
        }

        if (isValid) {
            this.settingsStorage.set(this.state);

            snackbarColourResId = R.color.colorGood;
        } else {
            snackbarColourResId = R.color.colorBad;
        }

        this.snackbar = this.snackbarUtility.create(
                snackbarMessageResId,
                snackbarColourResId,
                this.findViewById(R.id.layout_activity_main)
        );
        this.snackbar.show();
    }

    public void onInputButtonVaccinationTrueClick(View v) {
        this.state.isVaccinated = true;

        this.runButtonAnimation((Button) v);
        this.applyStateToButtons();
    }

    public void onInputButtonVaccinationFalseClick(View v) {
        this.state.isVaccinated = false;

        this.runButtonAnimation((Button) v);
        this.applyStateToButtons();
    }

    public void onInputButtonRiskLowClick(View v) {
        this.state.isHighRisk = false;

        this.runButtonAnimation((Button) v);
        this.applyStateToButtons();
    }

    public void onInputButtonRiskHighClick(View v) {
        this.state.isHighRisk = true;

        this.runButtonAnimation((Button) v);
        this.applyStateToButtons();
    }

    private boolean isAppSetAsDefault() {
        // STUB
        return false;

//        Intent intent = new Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse(
//                        this.getString(R.string.url_mysejahtera_intent)
//                )
//        );
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        ResolveInfo resolveInfo = this
//                .getPackageManager()
//                .resolveActivity(
//                        intent,
//                        PackageManager.MATCH_DEFAULT_ONLY
//                );
//
//        if (resolveInfo == null
//                || resolveInfo.activityInfo == null
//                || resolveInfo.activityInfo.packageName == null
//        )
//            return false;
//
//        return this.getPackageName().equals(resolveInfo.activityInfo.packageName);
    }

    public void setupHelpButtonSetDefault(
            Button helpButtonSetDefault
    ) {
        if (this.isAppSetAsDefault())
            helpButtonSetDefault.setVisibility(View.GONE);
    }

    private void dismissFirstRun() {
        this.state.isFirstRun = false;
        this.settingsStorage.set(this.state);
    }

    public void onHelpButtonClose(View v) {
        this.bottomSheetHelpBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        this.findViewById(R.id.main_overlay_bottom_sheet).setVisibility(View.GONE);

        this.dismissFirstRun();
    }

    public void onHelpButtonSetDefault(View v) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                        this.getString(R.string.url_mysejahtera_intent)
                                + this.getString(R.string.url_mysejahtera_set_default_hash)
                )
        );

        this.startActivity(intent);

        this.dismissFirstRun();
    }

    private void setupLayoutScroll(
            ScrollView layoutScroll
    ) {
        IOverScrollDecor overScrollDecor = OverScrollDecoratorHelper.setUpOverScroll(layoutScroll);

        overScrollDecor.setOverScrollUpdateListener(
                (decor, state1, offset) -> this.getAppbar().setY(offset)
        );
    }

    private void setupBottomSheet(
            ConstraintLayout bottomSheetDialogHelp
    ) {
        final View overlayBottomSheet = this.findViewById(R.id.main_overlay_bottom_sheet);

        this.bottomSheetHelpBehavior = BottomSheetBehavior
                .from(bottomSheetDialogHelp);
        this.bottomSheetHelpBehavior
                .setState(BottomSheetBehavior.STATE_HIDDEN);
        this.bottomSheetHelpBehavior
                .setSkipCollapsed(true);
        this.bottomSheetHelpBehavior
                .addBottomSheetCallback(
                        new BottomSheetBehavior.BottomSheetCallback() {
                            @Override
                            public void onStateChanged(@NonNull View view, int i) {
                                if (i == BottomSheetBehavior.STATE_HIDDEN) {
                                    overlayBottomSheet.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onSlide(@NonNull View view, float offset) {
                            }
                        });
    }

    private void setupHamburger(
            FloatingActionMenu floatingActionMenuHamburger
    ) {
        AnimatorSet animatorSetOpen = new AnimatorSet();
        AnimatorSet animatorSetClosed = new AnimatorSet();

        floatingActionMenuHamburger.setClosedOnTouchOutside(true);

        ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(
                floatingActionMenuHamburger.getMenuIconView(),
                "rotation",
                90,
                0
        );

        ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(
                floatingActionMenuHamburger.getMenuIconView(),
                "rotation",
                0,
                90
        );

        animatorSetOpen.play(expandAnimator);
        animatorSetClosed.play(collapseAnimator);

        animatorSetOpen.setInterpolator(new OvershootInterpolator());
        animatorSetClosed.setInterpolator(new AnticipateOvershootInterpolator());

        animatorSetOpen.setDuration(400);
        animatorSetClosed.setDuration(400);

        try {
            final Field fieldOpen = FloatingActionMenu
                    .class
                    .getDeclaredField("mOpenAnimatorSet");
            final Field fieldClosed = FloatingActionMenu
                    .class
                    .getDeclaredField("mCloseAnimatorSet");

            fieldOpen.setAccessible(true);
            fieldClosed.setAccessible(true);

            fieldOpen.set(floatingActionMenuHamburger, animatorSetOpen);
            fieldClosed.set(floatingActionMenuHamburger, animatorSetClosed);
        } catch (IllegalAccessException | NoSuchFieldException err) {
            Log.e("Reflection Failed:", err.toString());
        }
    }

    private void setupEditTexts(
            EditText editTextName,
            EditText editTextContact
    ) {
        final String name = this.state.name;
        final String contact = this.state.contact;

        editTextName.setText(
                name != null
                        ? name
                        : ""
        );
        editTextContact.setText(
                contact != null
                        ? contact
                        : ""
        );

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.this.state.name = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editTextContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.this.state.contact = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void runButtonAnimation(Button button) {
        AnimatorSet animatorSet = Objects.requireNonNull(
                this.buttonIdToAnimatorSet.get(
                        button.getId()
                )
        );

        animatorSet.cancel();
        animatorSet.start();
    }

    private void applyStateToButtons() {
        Button buttonRiskLow = this.findViewById(R.id.main_input_button_risk_low);
        Button buttonRiskHigh = this.findViewById(R.id.main_input_button_risk_high);
        Button buttonVaccinationTrue = this.findViewById(R.id.main_input_button_vaccination_true);
        Button buttonVaccinationFalse = this.findViewById(R.id.main_input_button_vaccination_false);

        ColorStateList colourBad = ContextCompat.getColorStateList(
                this,
                R.color.colorBad
        );

        ColorStateList colourGood = ContextCompat.getColorStateList(
                this,
                R.color.colorGood
        );

        buttonRiskHigh.setBackgroundTintList(
                this.state.isHighRisk
                        ? colourBad
                        : null
        );

        buttonRiskLow.setBackgroundTintList(
                !this.state.isHighRisk
                        ? colourGood
                        : null
        );

        buttonVaccinationFalse.setBackgroundTintList(
                !this.state.isVaccinated
                        ? colourBad
                        : null
        );

        buttonVaccinationTrue.setBackgroundTintList(
                this.state.isVaccinated
                        ? colourGood
                        : null
        );
    }

    private void setupOverScroll(ScrollView scrollView) {
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
    }

    private void dismissSoftKeyboard() {
        // dismiss soft keyboard
        final InputMethodManager imm = (
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)
        );
        final View focusedView = this.getCurrentFocus();

        // verify if the soft keyboard is open
        if (imm.isAcceptingText()
                && focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }
}
