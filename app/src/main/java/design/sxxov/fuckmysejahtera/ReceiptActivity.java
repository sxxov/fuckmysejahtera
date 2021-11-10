package design.sxxov.fuckmysejahtera;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import design.sxxov.fuckmysejahtera.blocks.classes.Activity;
import design.sxxov.fuckmysejahtera.blocks.interfaces.common.Callback;
import design.sxxov.fuckmysejahtera.history.HistoryHTML;
import design.sxxov.fuckmysejahtera.history.HistoryHTMLFactory;
import design.sxxov.fuckmysejahtera.history.HistoryItem;
import design.sxxov.fuckmysejahtera.history.HistoryItemFactory;
import design.sxxov.fuckmysejahtera.receipt.ReceiptItemAdapter;
import design.sxxov.fuckmysejahtera.utilities.AsyncUtility;
import design.sxxov.fuckmysejahtera.webview.WebViewClient;

public class ReceiptActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentViewResId(R.layout.activity_receipt);
        super.onCreate(savedInstanceState);

        final WebView webView = findViewById(R.id.receipt_webview);
        final String intentUrl = this.getIntentURL();
        final String configuredDataJSON = this.getConfiguredDataJSON();

        if (configuredDataJSON == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("from", ReceiptActivity.class.getSimpleName());
            this.startActivity(intent);

            this.finish();
            return;
        }

        final String URL = (
                intentUrl
                        + (
                        intentUrl.contains("?")
                                ? "&"
                                : "?"
                )
                        + "data="
                        + this.encodeToBase64(
                        configuredDataJSON
                )
        );

        this.setupWebView(webView);

        webView.loadUrl(URL);
    }

    public void onExitButtonClick(View v) {
        this.finish();
    }

    // stolen from https://stackoverflow.com/questions/2354336/how-to-exit-when-back-button-is-pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);

        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        this.startActivity(intent);
        this.finish();
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void setupWebView(WebView webView) {
        webView
                .getSettings()
                .setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener((v, event) -> true);
        // 0. lmao what the fuck
        // 2. injects js into the page to loop on a promise until elem is resolved
        webView
                .setWebViewClient(
                        new WebViewClient(this) {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);

                                if (webView.getProgress() == 100) {
                                    webView.evaluateJavascript(
                                            "(async () => { while (!document.getElementById('location')) { await new Promise((resolve) => setTimeout(resolve, 0)); } __bridge__.callback(null) })()",
                                            null
                                    );
                                }
                            }
                        }
                );
        // 1. injects java callback into js
        webView.addJavascriptInterface(new Callback<Void, Void>() {
            @JavascriptInterface
            @Override
            public Void callback(Void unused) {
                // 3. sends a message to main thread to exec js
                new Handler(Looper.getMainLooper()).post(() -> {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 4. gets innerText
                    webView.evaluateJavascript(
                            "document.getElementById('location').innerText",
                            (String location) -> webView.evaluateJavascript(
                                    "document.documentElement.outerHTML",
                                    (String html) -> {
                                        @SuppressLint("SimpleDateFormat")
                                        String time = new SimpleDateFormat(
                                                "h:mm:ssa\nd/M/yy"
                                        ).format(new Date());
                                        HistoryItem historyItem = new HistoryItemFactory()
                                                .create(
                                                        location
                                                                .replaceAll(
                                                                        "^\"|\"$",
                                                                        ""
                                                                ),
                                                        time
                                                );
                                        HistoryHTML historyHTML = new HistoryHTMLFactory()
                                                .create(
                                                        historyItem.id,
                                                        html
                                                );
                                        new AsyncUtility().executeAsync(() -> {
                                            ReceiptActivity.this
                                                    .getAppDatabase()
                                                    .historyDao()
                                                    .insertItems(
                                                            historyItem
                                                    );

                                            ReceiptActivity.this
                                                    .getAppDatabase()
                                                    .historyDao()
                                                    .insertHTMLs(
                                                            historyHTML
                                                    );

                                            return null;
                                        });
                                    }
                            )
                    );
                });

                return null;
            }
        }, "__bridge__");
    }

    @NonNull
    private String getIntentURL() {
        final String dataString = this.getIntent().getDataString();

        if (dataString == null) {
            throw new NullPointerException("dataString == null");
        }

        return dataString.replace("http://", "https://");
    }

    private String getConfiguredDataJSON() {
        if (this.state.name == null
                || this.state.contact == null) {
            return null;
        }

        return ReceiptItemAdapter.adaptSettingsItem(this.state).toJSONString();
    }

    private String encodeToBase64(String string) {
        return Base64
                .encodeToString(
                        string.getBytes(StandardCharsets.UTF_8),
                        Base64.DEFAULT
                );
    }
}
