// Copyright 2011 Google Inc. All Rights Reserved.

package com.example.jamchat.wifiDirect;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.example.jamchat.chat.ChatMain;

/**
 * A service that process each file transfer request i.e Intent by opening a
 * socket connection with the WiFi Direct Group Owner and writing the file
 */
public class WiFi_TransferService extends IntentService 
{
    private static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SEND_FILE = "com.example.android.wifidirect.SEND_FILE";
    public static final String EXTRAS_FILE_PATH = "file_url";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";

    public WiFi_TransferService(String name) 
    {
        super(name);
    }

    public WiFi_TransferService() 
    {
        super("TransferService");
    }

    /*
     * (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) 
    {
        Context context = getApplicationContext();
        if (intent.getAction().equals(ACTION_SEND_FILE)) 
        {
            String fileUri = intent.getExtras().getString(EXTRAS_FILE_PATH);
            String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
            
            //Initializes a new socket
            Socket socket = new Socket();
            
            //Gets the port number
            int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);

            try {
                Log.d(ChatMain.TAG, "Opening client socket - ");
                
                //Sets the socket to the proper port number and address
                socket.bind(null);
                socket.connect((new InetSocketAddress(host, port)), SOCKET_TIMEOUT);

                Log.d(ChatMain.TAG, "Client socket - " + socket.isConnected());
                OutputStream stream = socket.getOutputStream();
                ContentResolver cr = context.getContentResolver();
                InputStream is = null;
                
                //Transfers the file
                try 
                {
                    is = cr.openInputStream(Uri.parse(fileUri));
                }
                catch (FileNotFoundException e) 
                {
                    Log.d(ChatMain.TAG, e.toString());
                }
                
                
                //WiFi_DeviceDetailFragment.copyFile(is, stream);
                
                //FIXME: Change the above line to the below
                //DeviceDetailFragment.copyText(is, stream);
                
                //Writes to the log that data was written successfully to the client
                Log.d(ChatMain.TAG, "Client: Data written");
            } 
            catch (IOException e) 
            {
                Log.e(ChatMain.TAG, e.getMessage());
            }
            finally
            {
                if (socket != null) 
                {
                	//Checks if the socket is still connected
                    if (socket.isConnected()) 
                    {
                        try 
                        {
                        	//Closes the socket
                            socket.close();
                        }
                        catch (IOException e) 
                        {
                            // Give up
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
