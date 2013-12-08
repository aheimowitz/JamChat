package com.example.jamchat.wifiDirect;

import android.net.wifi.p2p.WifiP2pDevice;

/**
 * An interface-callback for the activity to listen to fragment interaction
 * events.
 */
public interface WiFi_DeviceActionListenerInterface 
{
    void showDetails(WifiP2pDevice device);

    void cancelDisconnect();

    void connect(WifiP2pDevice device);

    void disconnect();
}
