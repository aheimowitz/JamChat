package com.example.jamchat.wifiDirect;

import java.util.ArrayList;
import java.util.List;

import com.example.jamchat.R;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WiFi_PeersListAdapter extends ArrayAdapter<WifiP2pDevice>
{
	private List<WifiP2pDevice> peersList = new ArrayList<WifiP2pDevice>(); 
	private TextView messageView;

	/**
	 * Class constructor
	 * 
	 * @param context 
	 * @param textViewResourceId
	 */
	public WiFi_PeersListAdapter(Context context, int textViewResourceId) 
	{
		super(context, textViewResourceId);
	}
	
	@Override
	public void add(WifiP2pDevice peer) 
	{
		peersList.add(peer);
		super.add(peer);
	}
	
	/**
	 * Sets the class peer list to the list passed
	 * @param peer
	 */
	public void addAll(List<WifiP2pDevice> peer)
	{
		peersList = peer;
		for(int i = 0; i < peer.size(); i++)
		{
			super.add(peer.get(i));
		}
	}
	
	/**
	 * This method gets the number of devices that are currently in the class object
	 */
	public int getCount() 
	{
		return this.peersList.size();
	}
	
	/**
	 * This method is the getter to get a chat message at a specific index
	 */
	public WifiP2pDevice getItem(int index) 
	{
		return this.peersList.get(index);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		if (row == null) 
		{
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.listitem_discuss, parent, false);
		}

		LinearLayout wrapper = (LinearLayout) row.findViewById(R.id.wrapper);

		WifiP2pDevice tempDevice = getItem(position); 
		
		messageView = (TextView) row.findViewById(R.id.comment);
		messageView.setText(tempDevice.deviceName);
		
		return row;
	}
	
}
