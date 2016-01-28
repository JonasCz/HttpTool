package jonas.tool.httpRequestCreator.Activities;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jonas.tool.httpRequestCreator.R;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuInflater;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import jonas.tool.httpRequestCreator.Constants.HttpHeadersList;
import okhttp3.Headers;
import android.widget.ImageButton;
import android.view.ViewGroup;
import android.content.Context;

public class RequestHeadersEditActivity extends Activity {
	private AutoCompleteTextView addHeaderName;
	private EditText addHeaderValue;
	
	private ListView headersList;
	
	private Headers.Builder headersBuilder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.headers_edit_activity);
		
		addHeaderName = (AutoCompleteTextView) findViewById(R.id.activity_headereditactivity_edittext_addHeader_name);
		addHeaderName.setThreshold(1);
		addHeaderName.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, HttpHeadersList.STANDARD_REQUEST_HEADER_NAMES));
		
		addHeaderValue = (EditText) findViewById(R.id.activity_headereditactivity_edittext_addHeader_value);
		
		headersList = (ListView) findViewById(R.id.activity_headereditactivity_listview_headerList);
		
		findViewById(R.id.activity_headereditactivity_button_addHeader_addButton).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View clicked) {
					headersBuilder.add(addHeaderName.getText().toString(), addHeaderValue.getText().toString());
					addHeaderName.getText().clear();
					addHeaderValue.getText().clear();
					
					createHeaderList();
				}		
			});
			
		this.headersBuilder = MainActivity.headersBuilder.build().newBuilder();
		createHeaderList();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_edit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_done:
				MainActivity.headersBuilder = this.headersBuilder;
				setResult(Activity.RESULT_OK);
				finish();
				break;

			case R.id.action_discard:
				this.headersBuilder = new Headers.Builder();
				setResult(Activity.RESULT_CANCELED);
				finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void createHeaderList () {
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		for (Map.Entry entry  : this.headersBuilder.build().toMultimap().entrySet()) {
			for (String value : (List<String>) entry.getValue()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", entry.getKey().toString());
				map.put("value", value);
				items.add(map);
			}	
		}
		ListAdapter adapter = new ListItemsAdapter(this, items, R.layout.headers_edit_listitem, new String[] {"name", "value"}, new int[] {R.id.activity_headerseditactivity_headerlist_name, R.id.activity_headerseditactivity_headerlist_value});
		headersList.setAdapter(adapter);
	}
	
	private Headers.Builder removeHeaderByIndex (Headers.Builder b, int indexOfHeaderToRemove) {
		Headers.Builder newBuilder = new Headers.Builder();
		for (int i = 0; i < b.build().size(); i++) {
			if (i == indexOfHeaderToRemove) {
				continue;
			} else {
				newBuilder.add(b.build().name(i), b.build().value(i));
			}
		}
		
		return newBuilder;
	}
	
	private class ListItemsAdapter extends SimpleAdapter {
		public ListItemsAdapter(Context context, List<? extends Map<String, ?>> items, int listItemLayout, String[] mapValues, int[] targetViews) {
			super(context, items, listItemLayout, mapValues, targetViews);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = super.getView(position, convertView, parent);

			ImageButton editButton = (ImageButton) convertView.findViewById(R.id.activity_headerseditactivity_headerlist_button_edit);
			editButton.setTag(position);
			editButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View clicked) {
						addHeaderName.setText(headersBuilder.build().name(clicked.getTag()));
						addHeaderValue.setText(headersBuilder.build().value(clicked.getTag()));
						
						headersBuilder = removeHeaderByIndex(headersBuilder, clicked.getTag());
						createHeaderList();
					}
				});

			ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.activity_headerseditactivity_headerlist_button_delete);
			deleteButton.setTag(position);
			deleteButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View clicked) {
						headersBuilder = removeHeaderByIndex(headersBuilder, clicked.getTag());
						createHeaderList();
					}
				});

			return convertView;
		}
	}

}
