package com.quest.display.hud;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.quest.entities.Player;
import com.quest.game.Game;

public class ControlsHud extends HUD {

	// ===========================================================
	// Constants
	// ===========================================================
	
	
	// ===========================================================
	// Fields
	// ===========================================================
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;
	private DigitalOnScreenControl mDigitalOnScreenControl;
	private Player mPlayer;
	
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public ControlsHud(Player pPlayer)
	{
		// Set internal Fields a
		this.mPlayer = pPlayer;
				
		// Load controls texture into memory
		this.mOnScreenControlTexture = new BitmapTextureAtlas(Game.getInstance().getTextureManager(), 512, 128);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, Game.getInstance().getApplicationContext(), "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, Game.getInstance().getApplicationContext(), "onscreen_control_knob.png", 128, 0);
		Game.getInstance().getTextureManager().loadTexture(this.mOnScreenControlTexture);

		// Register ControlListener
		this.mDigitalOnScreenControl = new DigitalOnScreenControl(15, Game.getInstance().getSceneManager().getDisplay().getCameraHeight() - this.mOnScreenControlBaseTextureRegion.getHeight()-15,
				Game.getInstance().getSceneManager().getDisplay().getCamera(),
				this.mOnScreenControlBaseTextureRegion,
				this.mOnScreenControlKnobTextureRegion, 0.01f,
				Game.getInstance().getVertexBufferObjectManager(),
				new IOnScreenControlListener() {
			
					@Override
					public void onControlChange(BaseOnScreenControl pBaseOnScreenControl, float pValueX, float pValueY) {
						
						// Controls it's attached Entity
						ControlsHud.this.mPlayer.onControlChange(pBaseOnScreenControl, pValueX, pValueY);
					}
				});

		this.mDigitalOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl.getControlBase().setAlpha(0.3f);
		this.mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		this.mDigitalOnScreenControl.getControlBase().setScale(1.0f);
		this.mDigitalOnScreenControl.getControlKnob().setScale(1.0f);
		this.mDigitalOnScreenControl.refreshControlKnobPosition();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public DigitalOnScreenControl getDigitalOnScreenControl() {
		return this.mDigitalOnScreenControl;
	}
	
	public void setDigitalOnScreenControl(DigitalOnScreenControl pDigitalOnScreenControl) {
		this.mDigitalOnScreenControl = pDigitalOnScreenControl;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	
}
