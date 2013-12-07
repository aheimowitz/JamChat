package com.example.jamchat.chat;

public class ChatMessage 
{
	private boolean left;
	private String comment;

	public ChatMessage(boolean left, String comment) 
	{
		super();
		this.left = left;
		this.comment = comment;
	}
	
	public String getComment()
	{
		return comment;
	}
	
	public boolean getLeft()
	{
		return left;
	}

}