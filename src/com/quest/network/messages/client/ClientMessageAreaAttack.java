package com.quest.network.messages.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.quest.constants.ClientMessageFlags;

public class ClientMessageAreaAttack  extends ClientMessage implements ClientMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mAttack_Flag;
	private int mTileX;
	private int mTileY;
	// ===========================================================
	// Constructors
	// ===========================================================

	@Deprecated
	public ClientMessageAreaAttack() {
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	/**
	 * @return the mAttack_Flag
	 */
	public int getAttack_Flag() {
		return mAttack_Flag;
	}

	/**
	 * @param mAttack_Flag the mAttack_Flag to set
	 */
	public void setAttack_Flag(int mAttack_Flag) {
		this.mAttack_Flag = mAttack_Flag;
	}

	/**
	 * @return the mTileX
	 */
	public int getTileX() {
		return mTileX;
	}

	/**
	 * @param mTileX the mTileX to set
	 */
	public void setTileX(int mTileX) {
		this.mTileX = mTileX;
	}

	/**
	 * @return the mTileY
	 */
	public int getTileY() {
		return mTileY;
	}

	/**
	 * @param mTileY the mTileY to set
	 */
	public void setTileY(int mTileY) {
		this.mTileY = mTileY;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


	@Override
	public short getFlag() {
		return FLAG_MESSAGE_CLIENT_AREA_ATTACK_MESSAGE;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mAttack_Flag = pDataInputStream.readInt();
		this.mTileX = pDataInputStream.readInt();
		this.mTileY = pDataInputStream.readInt();
	}


	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mAttack_Flag);
		pDataOutputStream.writeInt(this.mTileX);
		pDataOutputStream.writeInt(this.mTileY);
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}