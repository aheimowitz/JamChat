package com.example.jamchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Setting up onClickListeners for Join Chat button
		Button loginButton = (Button) findViewById(R.id.useThisButton);
		loginButton.setOnClickListener(useThisButtonListener);
	}

	//When the join chat button is pushed make sure the nickname isnt blank
	//Then call start a new intent and send it the inputted nickname
	private OnClickListener useThisButtonListener = new OnClickListener() {
		public void onClick(View v) {
			EditText userTypedMessage = (EditText) findViewById(R.id.nameEditText);
			String nick = userTypedMessage.getText().toString();
			if (nick.compareTo("") != 0) {
				startNewIntent(nick);
			}
		}
	};

	//Starts a new intent of the messenger, sends it the nickname and starts it
	public void startNewIntent(String nick) {
		Intent i = new Intent(this, Messaging.class);
		i.putExtra("nick", nick);
		startActivityForResult(i, 9);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
