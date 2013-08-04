package com.quest.scenes;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector.ISocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.debug.Debug;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.quest.constants.GameFlags;
import com.quest.data.ProfileData;
import com.quest.game.Game;
import com.quest.helpers.AttacksHelper;
import com.quest.helpers.BattleHelper;
import com.quest.helpers.ItemHelper;
import com.quest.helpers.MobHelper;
import com.quest.helpers.TextHelper;
import com.quest.network.Client;
import com.quest.network.QClient;

public class MainMenuScene extends Scene implements GameFlags{
	// ===========================================================
	// Constants
	// ===========================================================
	
	
	// ===========================================================
	// Fields
	// ===========================================================
	private BitmapTextureAtlas mMainMenuTextureAtlas;
	private ITextureRegion mBoxTextureRegion;
	private ITextureRegion mSignsTextureRegion;
	private ITextureRegion mBackgroundTextureRegion;
	private Sprite mSignsSprite;
	private Sprite mBackgroundSprite;
	private Sprite mPlaySprite;
	private Sprite mOptionsSprite;
	private Sprite mQuitSprite;
	private Text mPlayText;
	private Text mOptionsText;
	private Text mQuitText;
	// ===========================================================
	// Constructors
	// ===========================================================
	public MainMenuScene(){
		Game.setTextHelper(new TextHelper());
		Game.setMobHelper(new MobHelper());
		Game.setBattleHelper(new BattleHelper());
		Game.setAttacksHelper(new AttacksHelper());
		Game.setItemHelper(new ItemHelper());
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Interfaces/MainMenu/");
		this.mMainMenuTextureAtlas = new BitmapTextureAtlas(Game.getInstance().getTextureManager(), 1024,1024, TextureOptions.BILINEAR);		
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMainMenuTextureAtlas, Game.getInstance().getApplicationContext(), "Background.png", 0, 0);
		this.mBoxTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMainMenuTextureAtlas, Game.getInstance().getApplicationContext(), "boxy.png", 0, 480);
		this.mSignsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMainMenuTextureAtlas, Game.getInstance().getApplicationContext(), "chainwood.png", 220, 480);
		this.mMainMenuTextureAtlas.load();
		
		//Background
		this.mBackgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, Game.getInstance().getVertexBufferObjectManager());
		this.attachChild(mBackgroundSprite);
			
		
		//Signs
		this.mSignsSprite = new Sprite(28, 0,this.mSignsTextureRegion, Game.getInstance().getVertexBufferObjectManager()) {};
		this.attachChild(mSignsSprite);
			
		
		//Play
		this.mPlaySprite = new Sprite(11, 49,this.mBoxTextureRegion, Game.getInstance().getVertexBufferObjectManager()) {
			boolean mGrabbed = false;
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					mGrabbed = true;					
					break;
				case TouchEvent.ACTION_UP:
					if(mGrabbed) {
						mGrabbed = false;
//						Game.getTextHelper().FlushTexts("MainMenuScene");
						showUsernameInput();
					}
				}
				return true;
			}
				
		};
		this.mPlaySprite.setScale(0.725f);
	
		
		//Opciones
		this.mOptionsSprite = new Sprite(13, 182,this.mBoxTextureRegion, Game.getInstance().getVertexBufferObjectManager()) {
//			boolean mGrabbed = false;
//			@Override
//			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
//			switch(pSceneTouchEvent.getAction()) {
//				case TouchEvent.ACTION_DOWN:
//						mGrabbed = true;					
//						break;
//					case TouchEvent.ACTION_UP:
//						if(mGrabbed) {
//							mGrabbed = false;
//							Game.getTextHelper().FlushTexts("MainMenuScene");
//							Game.getSceneManager().setOptionsScene();
//							//Game.getSceneManager().setTestScene();
//							break;
//						}
//					}
//				return true;
//			}
		};
		this.mOptionsSprite.setScale(0.725f);
		this.mOptionsSprite.setAlpha(0.4f);

		//Quit
		this.mQuitSprite = new Sprite(13, 326,this.mBoxTextureRegion, Game.getInstance().getVertexBufferObjectManager()) {
			boolean mGrabbed = false;
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
						mGrabbed = true;					
						break;
					case TouchEvent.ACTION_UP:
						if(mGrabbed) {
							mGrabbed = false;
							Game.getInstance().finish();
							break;
						}
					}
				return true;
			}
		};
		this.mQuitSprite.setScale(0.725f);
		
		//Attach a la escena
		this.attachChild(mPlaySprite);
		this.attachChild(mOptionsSprite);
		this.attachChild(mQuitSprite);
		
		//Registro Touch Areas
		this.registerTouchArea(this.mPlaySprite);
		this.registerTouchArea(this.mOptionsSprite);
		this.registerTouchArea(this.mQuitSprite);

		//texto	
		this.mPlayText = Game.getTextHelper().addNewText(FLAG_TEXT_TYPE_FANCY, 90, 87,"PLAY","MainMenuScene;Play");
		this.mOptionsText = Game.getTextHelper().addNewText(FLAG_TEXT_TYPE_FANCY, 60, 217,"OPTIONS","MainMenuScene;Options");
		this.mQuitText = Game.getTextHelper().addNewText(FLAG_TEXT_TYPE_FANCY, 90, 359,"QUIT","MainMenuScene;Quit");
		this.attachChild(this.mPlayText);
		this.attachChild(this.mOptionsText);
		this.attachChild(this.mQuitText);
		

		//mSignsSprite.registerEntityModifier(new MoveModifier(0.001f, 30, -450, -30, 0, EaseBackOut.getInstance()));
	}
	
	public void showUsernameInput() {
		Game.getInstance().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(Game.getInstance());

				alert.setTitle("Enter a username");
				alert.setMessage("Welcome to Quest! please enter the name that will be displayed to other players");
				final EditText editText = new EditText(Game.getInstance());
				editText.setTextSize(15f);
				editText.setText("");
				editText.setGravity(Gravity.CENTER_HORIZONTAL);
				alert.setView(editText);
				alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						if(editText.getText().toString().equals(null)||editText.getText().toString().equals("")||editText.getText().toString().equals(" ")){
							showUsernameInput();
						}else{
							Game.setProfileData(new ProfileData(Game.getUserID(),editText.getText().toString()));
							requestConnection();
						}
					}
				});
				alert.setCancelable(false);
				final AlertDialog dialog = alert.create();
				dialog.setOnShowListener(new OnShowListener() {
					@Override
					public void onShow(DialogInterface dialog) {
						editText.requestFocus();
						final InputMethodManager imm = (InputMethodManager) Game.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
					}
				});
				dialog.show();
			}
		});
	}
	
	private void requestConnection() {
		Game.setClient(new Client(Game.SERVER_IP, Game.SERVER_PORT));
		Game.getClient().sendServerConnectionRequestMessage(Game.getProfileData());
		/*
		 * Cambiar a loading screen
		 */
		/*
		 * el server me devuelve estado del mapa
		 * 	lo cargo
		 * 	mando ok
		 * el server me agrega a la lista de broadcasts 
		 * 
		 * 
		 */
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	

	// ===========================================================
	// Methods
	// ===========================================================
	

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
