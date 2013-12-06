package com.example.jamchat;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity 
{
	public static final String TAG = "wifidirectdemo";
	private WifiP2pManager manager;
	private boolean isWifiP2pEnabled = false;
	private boolean retryChannel = false;
	
	private final IntentFilter intentFilter = new IntentFilter();
	private Channel channel;
	//private BroadcastReceiver receiver = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Setting up onClickListeners for Join Chat button
		Button loginButton = (Button) findViewById(R.id.useThisButton);
		loginButton.setOnClickListener(useThisButtonListener);
		
		//Adds the neccesary actions to the intentFilter to allow the app to
		// use Wifi Direct
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        //channel = manager.initialize(this, getMainLooper(), null);
	}

	//When the join chat button is pushed make sure the nickname isn't blank
	//Then call start a new intent and send it the inputed nickname
	private OnClickListener useThisButtonListener = new OnClickListener() 
	{
		public void onClick(View v) 
		{
			EditText userTypedMessage = (EditText) findViewById(R.id.nameEditText);
			String nick = userTypedMessage.getText().toString();
			if (nick.compareTo("") != 0) 
			{
				startNewIntent(nick);
			}
		}
	};

	//Starts a new intent of the messenger, sends it the nickname and starts it
	public void startNewIntent(String nick) 
	{
		Intent i = new Intent(this, Messaging.class);
		i.putExtra("nick", nick);
		startActivityForResult(i, 9);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
