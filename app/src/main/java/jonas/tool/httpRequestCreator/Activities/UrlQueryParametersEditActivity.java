package jonas.tool.httpRequestCreator.Activities;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import jonas.tool.httpRequestCreator.R;
import okhttp3.HttpUrl;
import android.content.Intent;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Adapter;
import java.util.Map;
import java.util.HashMap;
import android.widget.SimpleAdapter;
import android.widget.ListAdapter;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import jonas.tool.httpRequestCreator.Util.UndoHistoryStack;

public class UrlQueryParametersEditActivity extends Activity {
	private EditText urlEntry;
	private EditText addPathSegment;
	private EditText addQueryParameterName;
	private EditText addQueryParameterValue;
	private ListView queryParameterList;
	
	private Menu menu;
	
	private UndoHistoryStack<HttpUrl> undoHistory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.url_query_parameters_edit_activity);
		
		urlEntry = (EditText) findViewById(R.id.activity_urleditactivity_edittext_urlEntryBox);
		queryParameterList = (ListView) findViewById(R.id.activity_urleditactivity_listview_queryParameterList);
		
		addPathSegment = (EditText) findViewById(R.id.activity_urleditactivity_edittext_addPathSegment_name);
		
		findViewById(R.id.activity_urleditactivity_button_addPathSegment_addButton).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					undoHistory.setCurrent(undoHistory.getCurrent().newBuilder().addPathSegment(addPathSegment.getText().toString()).build());
					urlEntry.setText(undoHistory.getCurrent().toString());

					addPathSegment.getText().clear();
				}		
			});
		
		addQueryParameterName = (EditText) findViewById(R.id.activity_urleditactivity_edittext_addQueryParamer_name);
		addQueryParameterValue = (EditText) findViewById(R.id.activity_urleditactivity_edittext_addQueryParamer_value);
		
		findViewById(R.id.activity_urleditactivity_button_addQueryParameter_addButton).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					if (undoHistory.getCurrent().queryParameter(addQueryParameterName.getText().toString()) != null){
						Toast.makeText(UrlQueryParametersEditActivity.this, "Duplicate query parameters are not supported, the URL already has the query parameter \"" + addQueryParameterName.getText().toString() +"\".", Toast.LENGTH_LONG).show();
						return;
					}
					undoHistory.setCurrent(undoHistory.getCurrent().newBuilder().addQueryParameter(addQueryParameterName.getText().toString(), addQueryParameterValue.getText().toString()).build());
					createQueryParameterList();
					urlEntry.setText(undoHistory.getCurrent().toString());
					
					addQueryParameterName.getText().clear();
					addQueryParameterValue.getText().clear();
				}		
			});
			
		urlEntry.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));
		undoHistory = new UndoHistoryStack<HttpUrl>(HttpUrl.parse(urlEntry.getText().toString()), new UndoHistoryStack.OnChangeListener() {
			@Override
			public void onChange() {
				setMenuItemsState();
			}
		});
		
		if (undoHistory.getCurrent() == null) {
			showInvalidUrlMessage();
		} else {
			createQueryParameterList();
		}
			
		urlEntry.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {}

				@Override
				public void afterTextChanged(Editable newText) {
					if (HttpUrl.parse(newText.toString()) == null) {
						showInvalidUrlMessage();
					} else {
						undoHistory.setCurrent(HttpUrl.parse(newText.toString()));
						hideInvalidUrlMessage();
						createQueryParameterList();
					}
				}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_edit, menu);
		this.menu = menu;
		setMenuItemsState();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = new Intent();
		switch (item.getItemId()) {
			case R.id.action_done:
				i.putExtra(Intent.EXTRA_TEXT, urlEntry.getText().toString());
				setResult(Activity.RESULT_OK, i);
				finish();
				break;
			case R.id.action_undo:
				if (undoHistory.undo().getCurrent() == null) {
					showInvalidUrlMessage();
				} else {
					urlEntry.setText(undoHistory.getCurrent().toString());
					hideInvalidUrlMessage();
					createQueryParameterList();
				}
				break;
			case R.id.action_redo:
				
				
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setMenuItemsState () {
		menu.findItem(R.id.action_undo).setEnabled(undoHistory.canUndo());
		menu.findItem(R.id.action_redo).setEnabled(undoHistory.canRedo());
	}
	
	private void createQueryParameterList () {
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		
		for (String queryParameterName : undoHistory.getCurrent().queryParameterNames()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", queryParameterName);
			map.put("value", undoHistory.getCurrent().queryParameter(queryParameterName));
			items.add(map);
		}
		ListAdapter adapter = new ListItemsAdapter(this, items, R.layout.url_query_parameters_listitem, new String[] {"name", "value"}, new int[] {R.id.activity_urleditactivity_queryparameterlist_name, R.id.activity_urleditactivity_queryparameterlist_value});
		queryParameterList.setAdapter(adapter);
	}
	
	private class ListItemsAdapter extends SimpleAdapter {
		public ListItemsAdapter(Context context, List<? extends Map<String, ?>> items, int listItemLayout, String[] mapValues, int[] targetViews) {
			super(context, items, listItemLayout, mapValues, targetViews);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = super.getView(position, convertView, parent);
			
			ImageButton editButton = (ImageButton) convertView.findViewById(R.id.activity_urleditactivity_queryparameterlist_button_edit);
			editButton.setTag(position);
			editButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View clicked) {
						addQueryParameterName.setText(undoHistory.getCurrent().queryParameterName(clicked.getTag()));
						addQueryParameterValue.setText(undoHistory.getCurrent().queryParameter(undoHistory.getCurrent().queryParameterName(clicked.getTag())));
						undoHistory.setCurrent(undoHistory.getCurrent().newBuilder().removeAllQueryParameters(undoHistory.getCurrent().queryParameterName(clicked.getTag())).build());
						urlEntry.setText(undoHistory.getCurrent().toString());
						createQueryParameterList();
					}
			});
			
			ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.activity_urleditactivity_queryparameterlist_button_delete);
			deleteButton.setTag(position);
			deleteButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View clicked) {
						undoHistory.setCurrent(undoHistory.getCurrent().newBuilder().removeAllQueryParameters(undoHistory.getCurrent().queryParameterName(clicked.getTag())).build());
						urlEntry.setText(undoHistory.getCurrent().toString());
						createQueryParameterList();
					}
				});
				
			return convertView;
		}
	}
	
	private void showInvalidUrlMessage () {
		TextView t = (TextView) findViewById(R.id.activity_urleditactivity_textview_currenturllabel);
		t.setText("Please enter a valid URL:");
		
		findViewById(R.id.activity_urleditactivity_button_addQueryParameter_addButton).setEnabled(false);
		findViewById(R.id.activity_urleditactivity_button_addPathSegment_addButton).setEnabled(false);
		
		addPathSegment.setEnabled(false);
		addQueryParameterName.setEnabled(false);
		addQueryParameterValue.setEnabled(false);
	}
	
	private void hideInvalidUrlMessage () {
		TextView t = (TextView) findViewById(R.id.activity_urleditactivity_textview_currenturllabel);
		t.setText("Current URL:");

		findViewById(R.id.activity_urleditactivity_button_addQueryParameter_addButton).setEnabled(true);
		findViewById(R.id.activity_urleditactivity_button_addPathSegment_addButton).setEnabled(true);

		addPathSegment.setEnabled(true);
		addQueryParameterName.setEnabled(true);
		addQueryParameterValue.setEnabled(true);
	}
}
