package com.example.jamchat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements ChannelListener, DeviceActionListenerInterface
{
	public static final String TAG = "jamchat";
	private WifiP2pManager manager;
	private boolean isWifiP2pEnabled = false;
	private boolean retryChannel = false;
	
	private final IntentFilter intentFilter = new IntentFilter();
	private Channel channel;
	private BroadcastReceiver receiver = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Setting up onClickListeners for Join Chat button
		Button loginButton = (Button) findViewById(R.id.useThisButton);
		loginButton.setOnClickListener(useThisButtonListener);
		
		//Adds the necessary actions to the intentFilter to allow the app to
		// use Wifi Direct
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
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
	
	/** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume()
    {
        super.onResume();
        //receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        //registerReceiver(receiver, intentFilter);
    }
    
    @Override
    public void onPause() 
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

	
	
    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled)
    {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }
    
    /**
     * Remove all peers and clear all fields. This is called on
     * BroadcastReceiver receiving a state change event.
     */
    public void resetData() 
    {
        /*DeviceListFragment fragmentList = (DeviceListFragment) getFragmentManager()
                .findFragmentById(R.id.frag_list);
        DeviceDetailFragment fragmentDetails = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        if (fragmentList != null) 
        {
            fragmentList.clearPeers();
        }
        if (fragmentDetails != null) 
        {
            fragmentDetails.resetViews();
        }*/
    }
    
    @Override
    public void connect(WifiP2pConfig config) 
    {
        manager.connect(channel, config, new ActionListener() 
        {
            @Override
            public void onSuccess() 
            {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) 
            {
                Toast.makeText(MainActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() 
    {
       /* final DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.resetViews();
        manager.removeGroup(channel, new ActionListener() 
        {
            @Override
            public void onFailure(int reasonCode) 
            {
                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
            }

            @Override
            public void onSuccess() 
            {
                fragment.getView().setVisibility(View.GONE);
            }

        });*/
    }

    @Override
    public void onChannelDisconnected() 
    {
        // we will try once more
        if (manager != null && !retryChannel) 
        {
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            resetData();
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        } 
        else
        {
            Toast.makeText(this,
                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cancelDisconnect()
    {
        /*
         * A cancel abort request by user. Disconnect i.e. removeGroup if
         * already connected. Else, request WifiP2pManager to abort the ongoing
         * request
         */
       /* if (manager != null) 
        {
            final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
                    .findFragmentById(R.id.frag_list);
            if (fragment.getDevice() == null
                    || fragment.getDevice().status == WifiP2pDevice.CONNECTED) {
                disconnect();
            } else if (fragment.getDevice().status == WifiP2pDevice.AVAILABLE
                    || fragment.getDevice().status == WifiP2pDevice.INVITED) {

                manager.cancelConnect(channel, new ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(WiFiDirectActivity.this, "Aborting connection",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(WiFiDirectActivity.this,
                                "Connect abort request failed. Reason Code: " + reasonCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });*/
     }
    
    @Override
    public void showDetails(WifiP2pDevice device) 
    {
        //DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
        //        .findFragmentById(R.id.frag_detail);
       // fragment.showDetails(device);

    }
    
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
