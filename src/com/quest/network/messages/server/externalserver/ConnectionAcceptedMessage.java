package com.quest.network.messages.server.externalserver;

import org.json.JSONArray;

public class ConnectionAcceptedMessage implements DefaultClientMessage {

	JSONArray message;
	
	@Override
	public void readData(JSONArray data) {
		message = data;		
	}
	
	@Override
	public void run() {
		message.toString();
	}



}
