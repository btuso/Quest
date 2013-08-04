package com.quest.network;

import com.quest.network.messages.server.externalserver.ConnectionAcceptedMessage;
import com.quest.network.messages.server.externalserver.DefaultClientMessage;

public enum Messages {
	CONNECTION_ACCEPTED("connectionAccepted", ConnectionAcceptedMessage.class);
	
	private String header;
	private Class<? extends DefaultClientMessage> runnableClass;
	
	private Messages(String header, Class<? extends DefaultClientMessage> runnableClass) {
		this.header = header;
		this.runnableClass = runnableClass;
	}
	
	public String getHeader() {
		return header;
	}
	
	public Class<? extends DefaultClientMessage> getRunnableClass() {
		return runnableClass;
	}
}
