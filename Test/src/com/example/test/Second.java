package com.example.test;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class Second extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		TextView tv =(TextView)findViewById(R.id.textView1);
		Intent intent =getIntent();
		String title = intent.getStringExtra("id");
		Log.d("title", title+"test");
	}

}
