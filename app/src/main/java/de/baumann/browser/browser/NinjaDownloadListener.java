package de.baumann.browser.browser;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;

import de.baumann.browser.R;
import de.baumann.browser.unit.BrowserUnit;
import de.baumann.browser.unit.HelperUnit;
public class NinjaDownloadListener implements DownloadListener {
    private final Context context;
    private final WebView webView;
    public NinjaDownloadListener(Context context, WebView webView) {
        super();
        this.context = context;
        this.webView = webView;
    }
    private String getExtension(String mimeType) {
        if (mimeType == null) return "bin";
        if (mimeType.contains("pdf")) return "pdf";
        if (mimeType.contains("image/png")) return "png";
        if (mimeType.contains("image/jpeg")) return "jpg";
        if (mimeType.contains("zip")) return "zip";
        return "bin";
    }
    @Override
    public void onDownloadStart(final String url, String userAgent, final String contentDisposition, final String mimeType, long contentLength) {
        // Create a background thread that has a Looper
        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        // Create a handler to execute tasks in the background thread.
        Handler backgroundHandler = new Handler(handlerThread.getLooper());
        Message msg = backgroundHandler.obtainMessage();
        webView.requestFocusNodeHref(msg);
        final String[] msgString = {(String) msg.getData().get("url")};
        if (msgString[0] == null) {
            msgString[0] = url;
        }
        if (url.startsWith("blob:")) {

            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = java.time.LocalDateTime.now().format(formatter);
            String generatedFileName = HelperUnit.domain(webView.getUrl()) + "_" + timestamp + "." + getExtension(mimeType);
            String d = webView.getContext().getString(R.string.dialog_title_download) + " - " + generatedFileName;

            HelperUnit.showCustomSnackbarWithTwoActions(
                    webView.getContext(), webView, null,
                    webView.getTitle(), d,
                    R.drawable.icon_check, () -> {
                        String jsCode = "javascript: (function() {" +
                                "   var xhr = new XMLHttpRequest();" +
                                "   xhr.open('GET', '" + url + "', true);" +
                                "   xhr.responseType = 'blob';" +
                                "   xhr.onload = function() {" +
                                "       if (xhr.status === 200) {" +
                                "           var blob = xhr.response;" +
                                "           var reader = new FileReader();" +
                                "           reader.onloadend = function() {" +
                                "               var base64data = reader.result;" +
                                "               var fileName = '" + generatedFileName + "';" + // <--- Hier eingesetzt
                                "               AndroidInterface.processBlob(base64data, '" + mimeType + "', fileName);" +
                                "           };" +
                                "           reader.readAsDataURL(blob);" +
                                "       }" +
                                "   };" +
                                "   xhr.send();" +
                                "})();";
                        webView.evaluateJavascript(jsCode, null);
                        return true;
                    },
                    R.drawable.icon_close, () -> true
            );
        } else {
            String filename = URLUtil.guessFileName(url, contentDisposition, mimeType);
            if (filename.length() > 150) {
                filename = filename.substring(0, 150) + " [...]?\"";
            }
            String d = webView.getContext().getString(R.string.dialog_title_download) + " - " + filename;
            String finalFilename = filename;
            HelperUnit.showCustomSnackbarWithTwoActions(
                    webView.getContext(), webView, null,
                    webView.getTitle(), d,
                    R.drawable.icon_check, () -> {
                        BrowserUnit.download(context, msgString[0], finalFilename, mimeType);
                        return true;
                    },
                    R.drawable.icon_close, () -> true
            );
        }
    }
}
