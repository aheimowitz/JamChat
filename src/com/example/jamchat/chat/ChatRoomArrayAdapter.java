package com.example.jamchat.chat;

import java.util.ArrayList;
import java.util.List;

import com.example.jamchat.R;
import com.example.jamchat.R.drawable;
import com.example.jamchat.R.id;
import com.example.jamchat.R.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatRoomArrayAdapter extends ArrayAdapter<ChatMessage> 
{
	private TextView messageView;
	private List<ChatMessage> messages = new ArrayList<ChatMessage>();
	private LinearLayout wrapper;

	@Override
	public void add(ChatMessage object) 
	{
		messages.add(object);
		super.add(object);
	}

	public ChatRoomArrayAdapter(Context context, int textViewResourceId) 
	{
		super(context, textViewResourceId);
	}

	public int getCount() 
	{
		return this.messages.size();
	}

	/**
	 * This method is the getter to get a chat message at a specific index
	 */
	public ChatMessage getItem(int index) 
	{
		return this.messages.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		if (row == null) 
		{
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.listitem_discuss, parent, false);
		}

		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);

		ChatMessage coment = getItem(position);

		messageView = (TextView) row.findViewById(R.id.comment);

		messageView.setText(coment.getComment());

		messageView.setBackgroundResource(coment.getLeft() ? R.drawable.bubble_yellow : R.drawable.bubble_green);
		wrapper.setGravity(coment.getLeft() ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) 
	{
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}