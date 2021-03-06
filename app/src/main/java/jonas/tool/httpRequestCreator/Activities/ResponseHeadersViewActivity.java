package jonas.tool.httpRequestCreator.Activities;
import android.app.Activity;
import android.os.Bundle;

import jonas.tool.httpRequestCreator.R;
import android.widget.ListView;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class ResponseHeadersViewActivity extends Activity {
	private ListView headersList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.respone_headers_view_activity);
		
		headersList = (ListView) findViewById(R.id.activity_responseviewactivity_headerlistview);
		createHeaderList();
	}
	
	private void createHeaderList () {
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		for (Map.Entry entry  : MainActivity.response.headers().toMultimap().entrySet()) {
			for (String value : (List<String>) entry.getValue()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", entry.getKey().toString());
				map.put("value", value);
				items.add(map);
			}	
		}
		ListAdapter adapter = new ListItemsAdapter(this, items, R.layout.response_headers_listitem, new String[] {"name", "value"}, new int[] {R.id.activity_responseviewactivity_headerlist_name, R.id.activity_responseviewactivity_headerlist_value});
		headersList.setAdapter(adapter);
	}

	private class ListItemsAdapter extends SimpleAdapter {
		public ListItemsAdapter(Context context, List<? extends Map<String, ?>> items, int listItemLayout, String[] mapValues, int[] targetViews) {
			super(context, items, listItemLayout, mapValues, targetViews);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = super.getView(position, convertView, parent);

			ImageButton copyButton = (ImageButton) convertView.findViewById(R.id.activity_responseviewactivity_headerlist_button_copy);
			copyButton.setTag(position);
			copyButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View clicked) {

					}
				});
			return convertView;
		}
	}
}
