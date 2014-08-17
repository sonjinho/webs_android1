package com.example.lastactivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WEBS_BOARD_NOTICE_LIST extends Activity implements OnClickListener {
	String title, result;
	String content;
	Handler handler = new Handler();
	final String url = "http://wpg.azurewebsites.net/webs_notice_specific.jsp";
	TextView t_content;
	TextView t_title;
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webs_board_notice_s_list);
		Intent intent = getIntent();
		t_title = (TextView) findViewById(R.id.title_list);
		t_content = (TextView) findViewById(R.id.content_list);
		btn = (Button) findViewById(R.id.comment);
		btn.setOnClickListener(this);
		title = intent.getStringExtra("id");
		t_title.setText(title);
		sendData();
	}

	private void sendData() {
		showDialog(1);
		Log.d("here", "no problem");
		final ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("title", title));
		Log.d("here1", "no problem");

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				InputStream is = requestPost(url, list);
				result = streamToString(is);
				Log.d("result", result);
				handler.post(new Runnable() {

					@Override
					public void run() {

						jsonparse(result);
					}
				});
			}
		});
		t.start();
	}

	public InputStream requestPost(String requestUrl,
			ArrayList<NameValuePair> list) {

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(requestUrl);
			request.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));

			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();

			return is;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String streamToString(InputStream is) {

		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String str = reader.readLine();
			while (str != null) {
				buffer.append(str);
				str = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public void jsonparse(String result) {
		try {
			JSONObject json = new JSONObject(result);
			JSONArray jA = json.getJSONArray("noticeList");
			json = jA.getJSONObject(0);
			title = json.getString("noticeList");
			content = json.getString("content");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		t_title.setText(title);
		t_content.setText(content);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, webs_comment.class);
		intent.putExtra("type", "notice");
		startActivity(intent);
	}
}
