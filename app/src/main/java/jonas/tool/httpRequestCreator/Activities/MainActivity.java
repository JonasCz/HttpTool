package jonas.tool.httpRequestCreator.Activities;

import android.app.Activity;
import android.os.Bundle;

import okhttp3.OkHttpClient;

import jonas.tool.httpRequestCreator.R;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuInflater;
import android.text.TextWatcher;
import android.text.Editable;
import okhttp3.HttpUrl;
import java.util.Map;
import java.util.HashMap;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import jonas.tool.httpRequestCreator.Constants.HttpMethods;
import android.widget.AdapterView;
import android.widget.Adapter;
import android.widget.Button;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Headers;
import android.widget.Toast;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final int ACTIVITY_EDIT_URL_RESULT = 1;
	private final int ACTIVITY_EDIT_HEADERS_RESULT = 2;
	private final int ACTIVITY_EDIT_BODY_RESULT = 3;
	
	private EditText urlEntry;
	private Button editBodybutton;
	private Button goButton;
	private TextView urlHint;
	
	private OkHttpClient okClient = new OkHttpClient();
	
	public static Headers.Builder headersBuilder = new Headers.Builder();
	public static Request request;
	public static Response response;
	public static byte[] responseBodyBytes;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		urlEntry = (EditText) findViewById(R.id.activity_main_edittext_urlEntryBox);
		editBodybutton = (Button) findViewById(R.id.activity_main_button_editRequestBody);
		goButton = (Button) findViewById(R.id.activity_main_button_goButton);
		urlHint = (TextView) findViewById(R.id.activity_main_text_urlhint);
		
		Spinner methods = (Spinner) findViewById(R.id.activity_main_spinner_selectmethod);
		methods.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, HttpMethods.METHODS));
		methods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View selectedView, int position, long id) {
					if (position == 0 || position == 2 || position == 5) {
						editBodybutton.setEnabled(false);
					} else {
						editBodybutton.setEnabled(true);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		findViewById(R.id.activity_main_button_editQueryParameters).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					Intent i = new Intent(MainActivity.this, UrlQueryParametersEditActivity.class);
					i.putExtra(Intent.EXTRA_TEXT, urlEntry.getText().toString());
					startActivityForResult(i, ACTIVITY_EDIT_URL_RESULT);
				}		
		});
		
		findViewById(R.id.activity_main_button_editRequestHeaders).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					Intent i = new Intent(MainActivity.this, RequestHeadersEditActivity.class);
					startActivityForResult(i, ACTIVITY_EDIT_HEADERS_RESULT);
				}		
			});
			
		editBodybutton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					Intent i = new Intent(MainActivity.this, RequestBodyEditActivity.class);
					startActivityForResult(i, ACTIVITY_EDIT_BODY_RESULT);
				}		
			});
			
		goButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					new ExecuteRequestTask().execute();
				}		
			});
			
		urlEntry.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {}

				@Override
				public void afterTextChanged(Editable newText) {
					updateGoButtonState(newText);
				}
			});
    }

	@Override
	protected void onResume() {
		updateGoButtonState(urlEntry.getText());
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}
		
		switch (requestCode) {
			case ACTIVITY_EDIT_URL_RESULT:
				urlEntry.setText(data.getStringExtra(Intent.EXTRA_TEXT));
				break;
			case ACTIVITY_EDIT_HEADERS_RESULT:
				break;
			case ACTIVITY_EDIT_BODY_RESULT:
				break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	private void updateGoButtonState (Editable newText) {
		if (newText.toString().isEmpty()) {
			urlHint.setText("Enter URL:");
			goButton.setEnabled(false);
		} else if (HttpUrl.parse(newText.toString()) == null) {
			urlHint.setText("Enter a valid URL:");
			goButton.setEnabled(false);
		} else {
			urlHint.setText("Current URL:");
			goButton.setEnabled(true);
		}
	}
	
	private class ExecuteRequestTask extends AsyncTask<Void, String, String> {
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressDialog.show();
		}

		@Override
		protected String doInBackground(Void[] p1) {
			try {
				request = new Request.Builder()
					.url(urlEntry.getText().toString())
					.headers(headersBuilder.build())
					.build();
				
				publishProgress("Executing request...");
				response = okClient.newCall(request).execute();
				
				publishProgress("Reading response body...");
				responseBodyBytes = response.body().bytes();
				
				return "Success";
			} catch (Exception e) {
				return "Error: " + e.getMessage() + ".";
			}
		}

		@Override
		protected void onProgressUpdate(String[] progress) {
			progressDialog.setMessage(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.cancel();
			if (result.equals("Success")) {
				Intent i = new Intent(MainActivity.this, ResponseViewActivity.class);
				startActivity(i);
			} else {
				Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
			}
		}
	}
}
