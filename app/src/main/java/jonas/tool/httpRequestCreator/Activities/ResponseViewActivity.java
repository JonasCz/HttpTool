package jonas.tool.httpRequestCreator.Activities;
import android.app.Activity;
import android.os.Bundle;
import jonas.tool.httpRequestCreator.R;
import android.widget.ListView;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import jonas.tool.httpRequestCreator.Constants.ResponseBodyViewTypes;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import jonas.tool.httpRequestCreator.Util.RegexExtract;
import jonas.tool.httpRequestCreator.Constants.ResponseBodyRegexExtractTypes;
import jonas.tool.httpRequestCreator.Constants.ResponseBodyCssSelectorOptions;
import android.widget.EditText;
import jonas.tool.httpRequestCreator.Util.CssSelectorExtract;
import android.widget.TextView;


public class ResponseViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.response_view_activity);
		
		final Spinner bodyViewTypes = (Spinner) findViewById(R.id.activity_responseviewactivity_spinner_viewas);
		bodyViewTypes.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ResponseBodyViewTypes.TYPES));
		
		final TextView responseHeadersInfoText = (TextView) findViewById(R.id.activity_responseviewactivity_text_headersinfo);
		responseHeadersInfoText.setText(MainActivity.response.headers().size() -2 + " headers: ");
		
		List<String> headerNames = new ArrayList<String>(MainActivity.response.headers().names());
		for (String s : headerNames) {
			if (!s.startsWith("OkHttp")) {//we don't want the okhttp internal headers here
				//inefficient and slow ? todo: fix.
				if (headerNames.indexOf(s) < headerNames.size() - 1) {
					responseHeadersInfoText.append(s + ", ");
				} else {
					responseHeadersInfoText.append(s + ".");
				}
			}
		}
		
		findViewById(R.id.activity_responseviewactivity_button_viewheaders).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					Intent i = new Intent(ResponseViewActivity.this, ResponseHeadersViewActivity.class);
					startActivity(i);
				}	
		});
		
		findViewById(R.id.activity_responseviewactivity_button_tools_deminify).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ResponseViewActivity.this);
					dialogBuilder.setMessage("This will deminify HTML, ie, it will structure it to be readable with newlines, correct indentation, and it will also fix up invalid HTML. This currently works only for HTML, as it uses Jsoup under the hood. Continue ?");
					dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface d, int i) {
								MainActivity.responseBodyStringExtracted = CssSelectorExtract.deminify(MainActivity.responseBodyStringExtracted);
								d.cancel();
							}
						});
					dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface d, int i) {
								d.cancel();
							}
						});
					dialogBuilder.create().show();
				}
		});
		
		findViewById(R.id.activity_responseviewactivity_button_tools_regexextract).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ResponseViewActivity.this);
					View dialogContentView = getLayoutInflater().inflate(R.layout.dialog_body_regex_extract, null);
					dialogBuilder.setView(dialogContentView);
					
					final Spinner extractTypes = (Spinner) dialogContentView.findViewById(R.id.dialog_extract_regex_spinner_extractionmode);
					extractTypes.setAdapter(new ArrayAdapter(ResponseViewActivity.this, android.R.layout.simple_spinner_dropdown_item, ResponseBodyRegexExtractTypes.TYPES));
					
					dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface d, int i) {
								d.cancel();
							}
					});
					dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface d, int i) {
								d.cancel();
							}
						});
					dialogBuilder.create().show();
				}
		});
		
		findViewById(R.id.activity_responseviewactivity_button_tools_cssextract).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ResponseViewActivity.this);
					View dialogContentView = getLayoutInflater().inflate(R.layout.dialog_body_css_selector_extract, null);
					dialogBuilder.setView(dialogContentView);
					
					final EditText cssSelectorEdit = (EditText) dialogContentView.findViewById(R.id.dialog_extract_css_selectorentrybox);

					final Spinner elementsToReturn = (Spinner) dialogContentView.findViewById(R.id.dialog_extract_css_spinner_elementstoreturn);
					elementsToReturn.setAdapter(new ArrayAdapter(ResponseViewActivity.this, android.R.layout.simple_spinner_dropdown_item, ResponseBodyCssSelectorOptions.ELEMENTS_TO_RETURN));
					
					final Spinner extractionMode = (Spinner) dialogContentView.findViewById(R.id.dialog_extract_css_spinner_outputtype);
					extractionMode.setAdapter(new ArrayAdapter(ResponseViewActivity.this, android.R.layout.simple_spinner_dropdown_item, ResponseBodyCssSelectorOptions.OUPUT_OPTIONS));

					dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface d, int i) {
								MainActivity.responseBodyStringExtracted = CssSelectorExtract.extract(MainActivity.responseBodyStringExtracted, cssSelectorEdit.getText().toString(), ResponseBodyCssSelectorOptions.ELEMENTS_TO_RETURN[elementsToReturn.getSelectedItemPosition()], ResponseBodyCssSelectorOptions.OUPUT_OPTIONS[extractionMode.getSelectedItemPosition()]);
								d.cancel();
							}
						});
					dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface d, int i) {
								d.cancel();
							}
						});
					dialogBuilder.create().show();
				}
			});
		
		
		findViewById(R.id.activity_responseviewactivity_button_actions_view).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					Intent i = new Intent(ResponseViewActivity.this, ResponseBodyContentViewActivity.class);
					i.putExtra(Intent.EXTRA_TEXT, ResponseBodyViewTypes.TYPES[bodyViewTypes.getSelectedItemPosition()]);
					startActivity(i);
				}
		});
	}
}
