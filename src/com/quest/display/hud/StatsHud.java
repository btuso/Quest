package com.quest.display.hud;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

import android.graphics.Color;
import android.graphics.Typeface;

import com.quest.game.Game;

public class StatsHud extends HUD {

	// ===========================================================
	// Constants
	// ===========================================================
	
	
	// ===========================================================
	// Fields
	// ===========================================================
	private BitmapTextureAtlas mFontTexture;
	private StrokeFont mFont;
	private Text mTermono;
	// ===========================================================
	// Constructors
	// ===========================================================
	public StatsHud() {
				
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mFontTexture = new BitmapTextureAtlas(Game.getInstance().getTextureManager(), 256, 256);
		
		this.mFont = new StrokeFont(Game.getInstance().getFontManager(), this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.BLACK, 2, Color.WHITE);
		
		Game.getInstance().getEngine().getTextureManager().loadTexture(this.mFontTexture);
		Game.getInstance().getEngine().getFontManager().loadFont(this.mFont);
		
		this.mTermono = new Text(20, 20, this.mFont, "Termono", "Tuviejaaaaaaaaaaaaaa000000000000000000000000aaaaaaaa".length(), Game.getInstance().getVertexBufferObjectManager());
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Text getTermono() {
		return mTermono;
	}

	public void setTermono(Text pTermono) {
		this.mTermono = pTermono;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
}
