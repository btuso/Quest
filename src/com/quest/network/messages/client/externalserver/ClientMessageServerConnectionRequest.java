package com.quest.network.messages.client.externalserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.quest.constants.ClientMessageFlags;
import com.quest.network.messages.client.QuestClientMessage;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:23:37 - 21.05.2011
 */
public class ClientMessageServerConnectionRequest extends QuestClientMessage implements ClientMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private String mUserID;
	private String mUsername;
	// ===========================================================
	// Constructors
	// ===========================================================

	@Deprecated
	public ClientMessageServerConnectionRequest() {
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public String getUserID() {
		return this.mUserID;
	}

	public void setUserID(final String pUserID) {
		this.mUserID = pUserID;
	}
	
	public String getUsername() {
		return this.mUsername;
	}

	public void setUsername(final String pUsername) {
		this.mUsername = pUsername;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return NEW_FLAG_MESSAGE_SERVER_CONNECTION_REQUEST;
	}

	@Override
	protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
		 this.mUserID = pDataInputStream.readUTF();
		 this.mUsername = pDataInputStream.readUTF();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeUTF(this.mUserID);
		pDataOutputStream.writeUTF(this.mUsername);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
