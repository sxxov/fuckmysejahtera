package design.sxxov.fuckmysejahtera.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.google.android.material.snackbar.Snackbar;

import design.sxxov.fuckmysejahtera.R;
import design.sxxov.fuckmysejahtera.blocks.classes.Activity;
import design.sxxov.fuckmysejahtera.utilities.ResourceUtility;
import design.sxxov.fuckmysejahtera.utilities.SnackbarUtility;

public class WebViewClient extends android.webkit.WebViewClient {
    private Activity ctx;

    public WebViewClient(Activity ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onReceivedError(
            WebView webView,
            WebResourceRequest request,
            WebResourceError error
    ) {
        try {
            webView.stopLoading();
        } catch (Exception err) {
            //
        }

        if (webView.canGoBack()) {
            webView.goBack();
        }

        final ResourceUtility resourceUtility = new ResourceUtility(this.ctx);

        webView.loadUrl("about:blank");
        webView.loadDataWithBaseURL(
                "about:blank",
                "<body style=\"margin:0;display:block;background:#"
                        + Integer.toHexString(
                                resourceUtility.getAttrColour(
                                        R.attr.colourBackground
                                )
                        )
                        // remove java alpha which is first 2 chars
                        // while css alpha is last 2 chars
                        .substring(2)
                        + ";height:100%;width:100%\"></body>",
                "text/html; charset=UTF-8",
                null,
                null
        );
        new SnackbarUtility(this.ctx)
                .create(
                        resourceUtility.getString(R.string.snackbar_webview_error)
                                + " — "
                                + error.getDescription(),
                        R.color.colorBad,
                        Snackbar.LENGTH_INDEFINITE,
                        this.ctx.findViewById(R.id.layout_activity_receipt)
                )
                .setAction(
                        R.string.snackbar_common_action_retry,
                        (v) -> webView.loadUrl(request.getUrl().toString())
                )
                .show();

        super.onReceivedError(webView, request, error);
    }

    @SuppressLint("JavascriptInterface")
    public static void evaluateJavascriptAsync(WebView webView, String script, ValueCallback<String> callback) {
        webView.addJavascriptInterface(callback, "__bridge__");
        webView.evaluateJavascript("__bridge__.onReceiveValue(" + script + ")", null);
    }

    @Override
    public boolean shouldOverrideUrlLoading(android.webkit.WebView webView, String url) {
        if (url.contains("mysejahtera")) {
            return false;
        }

        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
        );
        this.ctx.startActivity(intent);

        return true;
    }
}
