package com.quest.entities;

import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;

import com.quest.game.Game;


public class Player extends BaseEntity implements IOnScreenControlListener {


	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	

	// ===========================================================
	// Constructors
	// ===========================================================
	public Player() {
		// TODO Auto-generated constructor stub
		super();
		
		this.mEntityType = "Player";
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onControlChange(BaseOnScreenControl pBaseOnScreenControl,
			float pValueX, float pValueY) {

		if(pValueX != 0.0f || pValueY != 0.0f) {
			if(!this.isWalking) {
				// Gets the new Tile
				float moveToXTile = this.getX() + (TILE_WIDTH * pValueX);
				float moveToYTile = this.getY() + (TILE_HEIGHT * pValueY);
				
				// Moves to it
				this.moveToTile(moveToXTile, moveToYTile, 1.5f);
			}
		}
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	

}
