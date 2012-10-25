package com.quest.network.messages.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.quest.constants.ClientMessageFlags;

public class ClientMessageChangeMap extends ClientMessage implements ClientMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mMapID;
	private String mPlayerKey;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	@Deprecated
	public ClientMessageChangeMap() {
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @return the mMapID
	 */
	public int getMapID() {
		return mMapID;
	}

	/**
	 * @param mMapID the mMapID to set
	 */
	public void setMapID(int mMapID) {
		this.mMapID = mMapID;
	}

	/**
	 * @return the mPlayerKey
	 */
	public String getPlayerKey() {
		return mPlayerKey;
	}
	
	/**
	 * @param mPlayerKey the mPlayerKey to set
	 */
	public void setPlayerKey(String mPlayerKey) {
		this.mPlayerKey = mPlayerKey;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mMapID = pDataInputStream.readInt();
		this.mPlayerKey = pDataInputStream.readUTF();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mMapID);
		pDataOutputStream.writeUTF(this.mPlayerKey);
	}

	@Override
	public short getFlag() {
		return FLAG_MESSAGE_CLIENT_PLAYER_CHANGED_MAP;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}