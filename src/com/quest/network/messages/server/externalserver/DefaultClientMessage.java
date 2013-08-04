package com.quest.network.messages.server.externalserver;

import org.json.JSONArray;

public interface DefaultClientMessage extends Runnable{
	void readData(JSONArray data);
}
