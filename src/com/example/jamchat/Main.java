package com.example.jamchat;

import java.util.ArrayList;
import java.util.List;

import com.example.jamchat.chat.Messaging;
import com.example.jamchat.wifiDirect.WiFi_DeviceActionListenerInterface;
import com.example.jamchat.wifiDirect.WiFi_DirectBroadcastReceiver;
import com.example.jamchat.wifiDirect.WiFi_PeersListAdapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends Activity implements WiFi_DeviceActionListenerInterface, ChannelListener
{
		public static final String TAG = "Jam Chat";
		private WifiP2pManager manager;
		private boolean isWifiP2pEnabled = false;
		private boolean retryChannel = false;
		
		private final IntentFilter intentFilter = new IntentFilter();
		private Channel channel;
		private BroadcastReceiver receiver = null;
		
		private List<WifiP2pDevice> listOfPeers = new ArrayList<WifiP2pDevice>();
		private WifiP2pDevice[] p2pDeviceArray = {}; 

		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.connection);

			//Initialize the onClickListener for the exit button
			Button discoverButton = (Button) findViewById(R.id.discover_peers);
			discoverButton.setOnClickListener(discoverPeersButtonListener);
			
			//Initialize the onClickListener for the create chatroom button
			Button createButton = (Button) findViewById(R.id.create_chatroom);
			createButton.setOnClickListener(createChatroomListener);
			
			
			//Adds the necessary actions to the intentFilter to allow the app to
			// use Wifi Direct
	        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

	        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
	        channel = manager.initialize(this, getMainLooper(), null);
		}
		
		/**
		 * This is the OnClickListener for the discover peers button
		 */
		private OnClickListener createChatroomListener = new OnClickListener() 
		{
			public void onClick(View v)
			{
				Toast.makeText(Main.this, "Creating chatroom",
                        Toast.LENGTH_SHORT).show();	
				
				Intent i = new Intent(Main.this, Messaging.class);
            	i.putExtra("nick", "default");
            	startActivityForResult(i,9);
			}
		};
		
		/** register the BroadcastReceiver with the intent values to be matched */
	    @Override
	    public void onResume()
	    {
	        super.onResume();
	        receiver = new WiFi_DirectBroadcastReceiver(manager, channel, this);
	        registerReceiver(receiver, intentFilter);
	    }
	    
	    @Override
	    public void onPause() 
	    {
	        super.onPause();
	        unregisterReceiver(receiver);
	    }
	    
			private PeerListListener pListener = new PeerListListener()
		    {
				@Override
				public void onPeersAvailable(WifiP2pDeviceList peers) 
				{
					//Clears the previous list of peers
					listOfPeers.clear();
					
					//Add all of the peers found (if any) to the ArrayList
					listOfPeers.addAll(peers.getDeviceList());
					
					if(listOfPeers.size() == 0)
					{
	                    Toast.makeText(Main.this, "No Peers Found",
	                            Toast.LENGTH_SHORT).show();
					}
					else
					{
	                    Toast.makeText(Main.this, "Peers Discovered",
	                            Toast.LENGTH_SHORT).show();	
	                    
	                    setContentView(R.layout.connection);
	                    
	                    ListView listV = (ListView)findViewById(R.id.groupsList);
	                    
	                    WiFi_PeersListAdapter tempAdapt = new WiFi_PeersListAdapter(Main.this, android.R.layout.simple_list_item_1);
	                    tempAdapt.addAll(listOfPeers);
	                    
	                    listV.setAdapter(tempAdapt);
	                    listV.setOnItemClickListener(new OnItemClickListener()
	                    {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) 
							{
								//Attempt to connect to the peer that was clicked
								connect(listOfPeers.get(arg2));
							}
	                    	
	                    });
	                    
					}
				}
		    	
		    };
	    
		/**
		 * This is the OnClickListener for the discover peers button
		 */
		private OnClickListener discoverPeersButtonListener = new OnClickListener() 
		{
			public void onClick(View v) 
			{
				if (!isWifiP2pEnabled) 
	            {
	                    Toast.makeText(Main.this, R.string.p2p_off_warning,
	                            Toast.LENGTH_SHORT).show();
	            }
				else
				{
					//Attempt to discover peers
					manager.discoverPeers(channel, new WifiP2pManager.ActionListener() 
	                {
	                    @Override
	                    public void onSuccess() 
	                    {
	                        Toast.makeText(Main.this, "Wifi discovery initiated",
	                                Toast.LENGTH_SHORT).show();
	                        
	                        //Request the peers
	                        manager.requestPeers(channel, pListener);
	                    }
	
	                    @Override
	                    public void onFailure(int reasonCode) 
	                    {
	                        Toast.makeText(Main.this, "Discovery Failed : " + reasonCode,
	                                Toast.LENGTH_SHORT).show();
	                    }
	                });
				}
				
			}
		};
		
		/**
		 * This is the setter for setting the boolean indicating if wifiP2P is enables
		 * @param isWifiP2pEnabled
		 */
	    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled)
	    {
	        this.isWifiP2pEnabled = isWifiP2pEnabled;
	    }
	    
	    @Override
	    public void connect(WifiP2pDevice device) 
	    {
            Toast.makeText(Main.this, "Attempting to connect to peer...",
                    Toast.LENGTH_SHORT).show();
	    	
	    	//These three lines convert the device object into a 
	    	// WifiP2pConfig object
	    	WifiP2pConfig config = new WifiP2pConfig();
	    	config.deviceAddress = device.deviceAddress;
	    	config.wps.setup = WpsInfo.PBC;
	      	
	        manager.connect(channel, config, new ActionListener() 
	        {
	            @Override
	            public void onSuccess() 
	            {
	            	Intent i = new Intent(Main.this, Messaging.class);
	            	i.putExtra("nick", "default");
	            	startActivityForResult(i,9);
	            }

	            @Override
	            public void onFailure(int reason) 
	            {
	                Toast.makeText(Main.this, "Connect failed. Retry.",
	                        Toast.LENGTH_SHORT).show();
	            }
	        });
	    }
	    
	    @Override
	    public void disconnect() 
	    {
	    	//Modify list of peers/change stuff as possible
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
	            	//Change view or form
	                //fragment.getView().setVisibility(View.GONE);
	            }

	        });
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
	                    || fragment.getDevice().status == WifiP2pDevice.CONNECTED) 
	                    {
	                disconnect();
	            } else if (fragment.getDevice().status == WifiP2pDevice.AVAILABLE
	                    || fragment.getDevice().status == WifiP2pDevice.INVITED) 
	                    {

	                manager.cancelConnect(channel, new ActionListener() 
	                {

	                    @Override
	                    public void onSuccess() 
	                    {
	                        Toast.makeText(WiFiDirectActivity.this, "Aborting connection",
	                                Toast.LENGTH_SHORT).show();
	                    }

	                    @Override
	                    public void onFailure(int reasonCode) 
	                    {
	                        Toast.makeText(WiFiDirectActivity.this,
	                                "Connect abort request failed. Reason Code: " + reasonCode,
	                                Toast.LENGTH_SHORT).show();
	                    }
	                });*/
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
		public void showDetails(WifiP2pDevice device) {
			// TODO Auto-generated method stub
			
		}
	    
}
