package com.quest.network.messages.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.quest.constants.ClientMessageFlags;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:00:36 - 21.05.2011
 */
public class ClientMessageMovePlayer extends ClientMessage implements ClientMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private String mPlayerKey;
	private byte mPlayerDirection;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ClientMessageMovePlayer() {
		this.mPlayerKey = "";
		this.mPlayerDirection = 0;
		
	}
	
	public ClientMessageMovePlayer(String pPlayerKey, byte pPlayerDirection) {
		this.mPlayerKey = pPlayerKey;
		this.mPlayerDirection = pPlayerDirection;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setPlayerKey(String mPlayerKey) {
		this.mPlayerKey = mPlayerKey;
	}

	public void setPlayerDirection(byte mPlayerDirection) {
		this.mPlayerDirection = mPlayerDirection;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
		this.mPlayerKey = pDataInputStream.readUTF();
		this.mPlayerDirection = pDataInputStream.readByte();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeUTF(this.mPlayerKey);
		pDataOutputStream.writeByte(this.mPlayerDirection);
	}

	@Override
	public short getFlag() {
		// TODO Auto-generated method stub
		return 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
