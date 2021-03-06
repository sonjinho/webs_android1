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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WEBS_FREE_BOARD_ADD extends Activity {
	EditText e_title, e_content;
	Button btn1;
	String result;
	String k;
	Handler handler =new Handler();
	String url="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webs_free_board_add);
		e_title =(EditText)findViewById(R.id.title_of_free_board);
		e_content =(EditText)findViewById(R.id.content_of_free_board);
		btn1 = (Button)findViewById(R.id.free_board_add);
		Intent intent = getIntent();
		k=intent.getStringExtra("content");
		Log.d("type", k);
		url="http://wpg.azurewebsites.net/webs_"+k+"_add.jsp";
		btn1.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title=e_title.getText().toString();
				String content=e_content.getText().toString();
				Log.d("text", title+"/"+content);
				sendData(title,content,url);
				finish();
			}
		});
			}
	private void sendData(final String title, final String content,final String url) {

		final ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				list.add(new BasicNameValuePair("title", title));
				list.add(new BasicNameValuePair("content", content));
				InputStream is = requestPost(url, list);
				result = streamToString(is);
				Log.d("result", result);
				handler.post(new Runnable() {

					@Override
					public void run() {
						Log.d("result", result);
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
}
