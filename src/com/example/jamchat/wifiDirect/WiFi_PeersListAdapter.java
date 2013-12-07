package com.example.jamchat.wifiDirect;

import java.util.ArrayList;
import java.util.List;

import android.net.wifi.p2p.WifiP2pDevice;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WiFi_PeersListAdapter 
{
	private List<WifiP2pDevice> peersList = new ArrayList<WifiP2pDevice>(); 
	
	private TextView deviceView;
	private LinearLayout wrapper;

	
	public void syncronize(List<WifiP2pDevice> peersList)
	{
		//Initializes the class peer list with the peer list passed
		this.peersList = peersList;
		
	}
	
	
}
