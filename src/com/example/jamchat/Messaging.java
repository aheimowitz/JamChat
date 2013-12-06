package com.example.jamchat;

import com.example.jamchat.ChatMessage;
import com.example.jamchat.ChatRoomArrayAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Messaging extends Activity {
	// Settings

	public static String nick = "default"; //User's nickname
	public static String userMessage = "";
	
	private com.example.jamchat.ChatRoomArrayAdapter adapter;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messaging);

		// Receive the Nickname from previous activity
		Bundle extras = getIntent().getExtras();
		nick = extras.getString("nick");

		// Setting up onClickListeners for send button
		Button loginButton = (Button) findViewById(R.id.sendButton);
		loginButton.setOnClickListener(sendButtonListener);
		
		//add the adapter array to the listview object listView1
		lv = (ListView) findViewById(R.id.listView1);
		adapter = new ChatRoomArrayAdapter(getApplicationContext(), R.layout.messaging);
		lv.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	//When the send button is pressed get the user typed message and send
	//it to the trySendingData function which writes it to the file
	private OnClickListener sendButtonListener = new OnClickListener() {
		public void onClick(View v) {
			EditText userTypedMessage = (EditText) findViewById(R.id.userMessageBox);
			userMessage = " " + userTypedMessage.getText().toString() + " ";
			localMessage(userMessage);
			
			// Clear the field..
			userTypedMessage.setText("");

		}
	};

	//Disable send button
	//Used to disable send button while processing a message
	public void disableSendButton() {
		Button sendButton = (Button) this.findViewById(R.id.sendButton);
		sendButton.setEnabled(false);
		sendButton.setText("wait!");
	}

	//Enable send button
	//reenables the send button once a message has been processed
	public void enableSendButton() {
		Button sendButton = (Button) this.findViewById(R.id.sendButton);
		sendButton.setEnabled(true);
		sendButton.setText("Send");
	}

	//Process a message from the current device
	public void localMessage(String message) {
		disableSendButton(); //May not be necessary
		adapter.add(new ChatMessage(false, message));
		lv.setSelection(adapter.getCount());
		enableSendButton();
	}

	//Process a message from an external user
	public void recievedMessage(String message) {
		adapter.add(new ChatMessage(true, message));
	}
}
