package com.quest.network.messages.client;

public interface ClientMessageFlags {

	// ===========================================================
	// Final Fields
	// ===========================================================

	/* Client --> Server */
	public static final short FLAG_MESSAGE_CLIENT_CONNECTION_PING = 200;
	public static final short FLAG_MESSAGE_CLIENT_CONNECTION_REQUEST = 201;
	public static final short FLAG_MESSAGE_CLIENT_CHARACTER_CREATE = 202;
	public static final short FLAG_MESSAGE_CLIENT_MOVE_PLAYER = 210;

}
