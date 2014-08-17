package com.example.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	ListView lv;
	String strurl = "http://wpg.azurewebsites.net/webs_free_board_list.jsp?";
	Context mctx;
	String[] list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webs_free_board);
		lv = (ListView) findViewById(R.id.lv_webs_free_board_notice);
		DownloadTask downloadTask = new DownloadTask();

		downloadTask.execute(strurl);
		mctx = this;
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(MainActivity.this, Second.class);
				TextView tv = (TextView)view.findViewById(R.id.webs_free_board_notice_list);
				intent.putExtra("id", tv.getText().toString());
				startActivity(intent);

			}
		});

	}

	private String downloadUrl(String strurl) throws IOException {
		String data = "";
		InputStream iStream = null;
		try {
			URL url = new URL(strurl);

			// Creating an http connection to communicate with url
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
		}

		return data;
	}

	private class DownloadTask extends AsyncTask<String, Integer, String> {
		String data = null;

		@Override
		protected String doInBackground(String... url) {
			// TODO Auto-generated method stub
			try {
				data = downloadUrl(url[0]);
				Log.d("url", data);
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {

			// The parsing of the xml data is done in a non-ui thread
			ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();

			// Start parsing xml data
			listViewLoaderTask.execute(result);

		}

	}

	//
	private class ListViewLoaderTask extends
			AsyncTask<String, Void, SimpleAdapter> {
		JSONObject jObject;

		@Override
		protected SimpleAdapter doInBackground(String... strJson) {
			try {
				jObject = new JSONObject(strJson[0]);
				WEBS_BOARD_FREE_BOARD_JSONPARSER f_board_JsonParser = new WEBS_BOARD_FREE_BOARD_JSONPARSER();
				f_board_JsonParser.parse(jObject);
			} catch (Exception e) {
				Log.d("JSON Exception1", e.toString());
			}

			WEBS_BOARD_FREE_BOARD_JSONPARSER free_board_JsonParser = new WEBS_BOARD_FREE_BOARD_JSONPARSER();
			List<HashMap<String, Object>> testjson = null;
			try {
				testjson = free_board_JsonParser.parse(jObject);
				Log.i("here4", "success");
			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}

			String[] from = { "notice", "id" };
			String x = from[0];
			Log.d("d", x);
			int[] to = { R.id.webs_free_board_notice_list };

			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),
					testjson, R.layout.webs_free_board_notice_list_item, from,
					to);

			return adapter;
		}

		@Override
		protected void onPostExecute(SimpleAdapter adapter) {
			// TODO Auto-generated method stub
			lv.setAdapter(adapter);
			for (int i = 0; i < adapter.getCount(); i++) {
				HashMap<String, Object> hm = (HashMap<String, Object>) adapter
						.getItem(i);
			}

		}
	}

}
