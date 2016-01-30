package jonas.tool.httpRequestCreator.Activities;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import java.io.IOException;
import android.widget.Toast;
import java.util.Arrays;
import jonas.tool.httpRequestCreator.Constants.ResponseBodyViewTypes;
import android.content.Intent;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.app.ProgressDialog;
import android.text.Html;

public class ResponseBodyContentViewActivity extends Activity {
	private final String HTML_1 = "<html><head><link href=\"prettify/prettify.css\" type=\"text/css\" rel=\"stylesheet\" /><script type=\"text/javascript\" src=\"prettify/prettify.js\"></script></head><body onload=\"prettyPrint()\"><pre class=\"prettyprint\"><code>";
	private final String HTML_2 = "</code></pre></body></html>";
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		webView = new WebView(this);
		setContentView(webView);

		final String viewType = getIntent().getStringExtra(Intent.EXTRA_TEXT);

		if (viewType.equals(ResponseBodyViewTypes.TYPES[0])) { //raw text
			webView.loadDataWithBaseURL(null, MainActivity.responseBodyStringExtracted, "text/plain", "UTF-8", null);

		} else if (viewType.equals(ResponseBodyViewTypes.TYPES[1])) {//prettifyed text
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadDataWithBaseURL("file:///android_asset/", HTML_1 + Html.escapeHtml(MainActivity.responseBodyStringExtracted) + HTML_2, "text/html", "UTF-8", null);

		} else if (viewType.equals(ResponseBodyViewTypes.TYPES[2])) {//html no assets
			webView.setWebViewClient(new WebViewClient() {
					@Override
					public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
						return new WebResourceResponse(null, null, null);
					}
				});
			webView.loadDataWithBaseURL(MainActivity.request.url().toString(), MainActivity.responseBodyStringExtracted, "text/html", "UTF-8", null);

		} else if (viewType.equals(ResponseBodyViewTypes.TYPES[3])) {//html with assets
			webView.loadDataWithBaseURL(MainActivity.request.url().toString(), MainActivity.responseBodyStringExtracted, "text/html", "UTF-8", null);
		}
	}
}
