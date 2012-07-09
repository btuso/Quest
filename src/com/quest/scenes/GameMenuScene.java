package com.quest.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

import com.quest.database.DataHandler;
import com.quest.entities.objects.Item;
import com.quest.game.Game;
import com.quest.helpers.EquipmentHelper;


public class GameMenuScene extends Scene{// implements IOnSceneTouchListener{
	// ===========================================================
	// Constants
	// ===========================================================
	
	
	// ===========================================================
	// Fields
	// ===========================================================
	//HACER UN BOOLEAN PARA CURRENT TAB
	
	
	//Entidades
	private Entity mGameMenuEntity;
	private Entity mInventoryEntity;
	private Entity mEquipmentEntity;
		private Entity mEquipmentUnEquippedItemsEntity;
	private Entity mSkillsEntity;
	private Entity mAttributesEntity;
	private Entity mInfoEntity;
	private Entity mSettingsEntity;
	private Entity mCurrentEntity;
	private Entity mSkillTreeEntity; // (FACU)

	//Textures Atlas
	private BitmapTextureAtlas mSceneTextureAtlas;
	private BitmapTextureAtlas mInventoryTextureAtlas;
	private BitmapTextureAtlas mEquipmentTextureAtlas;
		private BitmapTextureAtlas mEquipmentEquippedItemsTextureAtlas;
		private BitmapTextureAtlas mEquipmentUnEquippedItemsTextureAtlas;
	private BitmapTextureAtlas mSkillsTextureAtlas;
	private BitmapTextureAtlas mAttributesTextureAtlas;
	private BitmapTextureAtlas mInfoTextureAtlas;
	private BitmapTextureAtlas mSkillTreeAtlas; // (FACU)
		
	//Texture regions
	private ITextureRegion mInventoryTabTextureRegion;
		private ITextureRegion mInventoryUseTextureRegion;
		private ITextureRegion mInventoryTossTextureRegion;
		private ITextureRegion mInventoryMoneyTextureRegion;
		private ITextureRegion mItemTextureRegion;
	private ITextureRegion mEquipmentTabTextureRegion;
		private ITextureRegion mEquipmentBoxTextureRegion;
		private ITextureRegion mEquipmentAttributesTextureRegion;
		private ITextureRegion mEquipmentItemsTextureRegion;
		private ITextureRegion mEquipmentHeadTextureRegion;
		private ITextureRegion mEquipmentBodyTextureRegion;
		private ITextureRegion mEquipmentLegsTextureRegion;
		private ITextureRegion mEquipmentWeaponTextureRegion;
		private ITextureRegion mEquipmentOffhandTextureRegion;
		private ITextureRegion mEquipmentExtraTextureRegion;
	private ITextureRegion mSkillsTabTextureRegion;
	private ITextureRegion mAttributesTabTextureRegion;
	private ITextureRegion mInfoTabTextureRegion;
	private ITextureRegion mSettingsTextureRegion;
	private ITextureRegion mBackgroundTextureRegion;
		
		private ITextureRegion mSkillTreeTextureRegion; // (FACU)
	
	
	//Sprites
	private Sprite mInventoryTabSprite;
		private Sprite mInventoryUseSprite;
		private Sprite mInventoryTossSprite;
		private Sprite mInventoryMoneySprite;
		private Sprite mItemSprite;
	private Sprite mEquipmentTabSprite;
		private Sprite mEquipmentBoxSprite;
		private Sprite mEquipmentBox2Sprite;
		private Sprite mEquipmentAttributesSprite;
		private Sprite mEquipmentItemsSprite;
		private Sprite mEquipmentHeadSprite;
		private Sprite mEquipmentBodySprite;
		private Sprite mEquipmentLegsSprite;
		private Sprite mEquipmentWeaponSprite;
		private Sprite mEquipmentOffhandSprite;
		private Sprite mEquipmentExtraSprite;
		
	private Sprite mSkillTreeBackgroundSprite; // (FACU)
	
	private Sprite mSkillsTabSprite;
	private Sprite mAttributesTabSprite;
	private Sprite mInfoTabSprite;
	private Sprite mSettingsSprite;
	private Sprite mBackgroundSprite;
	
	
	private EquipmentHelper mEquipmentManager;
	private DataHandler mDataHandler;
	
	private int mUnEquippedCount = 0; 
	private Item[] mItemsArray;
	private int[] mCountArray;
	//FALTA HACER BIEN LA CARGA DE ENTIDADES
	//FALTA HACER LA CARGA DE TEXTURAS DINAMICAS
	
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public GameMenuScene(){
		this.mDataHandler = new DataHandler();
		this.mGameMenuEntity = new Entity(0,0);
		this.mInventoryEntity = new Entity(0,0);
		this.mEquipmentEntity = new Entity(0,0);
		this.mSkillsEntity = new Entity(0,0);
		this.mAttributesEntity = new Entity(0,0);
		this.mInfoEntity = new Entity(0,0);
		this.mSettingsEntity = new Entity(0,0);
		
		
		this.mEquipmentManager = new EquipmentHelper(this.mDataHandler,GameMenuScene.this);
		
		//###################COMIENZO DE ENTIDAD PRINCIPAL############################
		
		this.attachChild(mGameMenuEntity);					
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Interfaces/InGameMenu/");
		this.mSceneTextureAtlas = new BitmapTextureAtlas(Game.getInstance().getTextureManager(), 1024,1024, TextureOptions.BILINEAR);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSceneTextureAtlas, Game.getInstance().getApplicationContext(), "Background.png", 0, 0);
		this.mSettingsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSceneTextureAtlas, Game.getInstance().getApplicationContext(), "Settings.png", 800, 0);
		this.mInventoryTabTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSceneTextureAtlas, Game.getInstance().getApplicationContext(), "Inventory.png", 800, 64);
		this.mEquipmentTabTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSceneTextureAtlas, Game.getInstance().getApplicationContext(), "Equipment.png", 800, 114);
		this.mSkillsTabTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSceneTextureAtlas, Game.getInstance().getApplicationContext(), "Skills.png", 800, 164);
		this.mAttributesTabTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSceneTextureAtlas, Game.getInstance().getApplicationContext(), "Attributes.png", 800, 214);
		this.mInfoTabTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSceneTextureAtlas, Game.getInstance().getApplicationContext(), "Info.png", 800, 264);
		this.mSceneTextureAtlas.load();
		//Cargar los otros atlas ahora?
	
		//Backgroud
		this.mBackgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, Game.getInstance().getVertexBufferObjectManager()) {};
		this.mGameMenuEntity.attachChild(mBackgroundSprite);		
		
		//Settings
		this.mSettingsSprite = new Sprite(Game.getSceneManager().getDisplay().getDisplayWidth() - 64, 20,this.mSettingsTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {					
			boolean mGrabbed = false;
				@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					switch(pSceneTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:
						this.mGrabbed = true;
						break;
					case TouchEvent.ACTION_UP:	
						if(this.mGrabbed) {
							this.mGrabbed = false;	
						GameMenuScene.this.mInventoryTabSprite.setAlpha(0.5f);
						GameMenuScene.this.clearTouchAreas();
						UnloadEntity(mCurrentEntity);
						mCurrentEntity = LoadSettingsEntity();
						GameMenuScene.this.attachChild(mCurrentEntity);
						GameMenuScene.this.loadTabTouchAreas();
						}
						break;
					}
					return true;
					}					
				};
				this.mSettingsSprite.setScale(0.8f);
				this.mGameMenuEntity.attachChild(mSettingsSprite);
				
		
		//Inventory Tab
		this.mInventoryTabSprite = new Sprite(0, 0,this.mInventoryTabTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_OUTSIDE:
			case TouchEvent.ACTION_CANCEL:
				//cargar datos?
				break;
			case TouchEvent.ACTION_DOWN:
			case TouchEvent.ACTION_UP:				
				GameMenuScene.this.mInventoryTabSprite.setAlpha(0.5f);
				GameMenuScene.this.clearTouchAreas();
				UnloadEntity(mCurrentEntity);
				mCurrentEntity = LoadInventoryEntity();
				GameMenuScene.this.attachChild(mCurrentEntity);
				GameMenuScene.this.loadTabTouchAreas();
				break;
			}
			return true;
			}					
		};
		this.mGameMenuEntity.attachChild(mInventoryTabSprite);
		
		
		//Equipment Tab
		this.mEquipmentTabSprite = new Sprite(147, 0,this.mEquipmentTabTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {
			boolean mGrabbed = false;
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				this.mGrabbed = true;
				break;
			case TouchEvent.ACTION_UP:
				if(this.mGrabbed){
				GameMenuScene.this.mEquipmentTabSprite.setAlpha(0.5f);
				GameMenuScene.this.clearTouchAreas();
				UnloadEntity(mCurrentEntity);
				mCurrentEntity = LoadEquipmentEntity();
				GameMenuScene.this.attachChild(mCurrentEntity);
				GameMenuScene.this.loadTabTouchAreas();
				this.mGrabbed=false;
				break;
				}
			}
			return true;
			}					
		};
		this.mGameMenuEntity.attachChild(mEquipmentTabSprite);
		
		
		//Skills Tab
		this.mSkillsTabSprite = new Sprite(311, 0,this.mSkillsTabTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_OUTSIDE:
			case TouchEvent.ACTION_CANCEL:
				break;
			case TouchEvent.ACTION_DOWN:
			case TouchEvent.ACTION_UP:
			GameMenuScene.this.mSkillsTabSprite.setAlpha(0.5f);
			GameMenuScene.this.clearTouchAreas();
			UnloadEntity(mCurrentEntity);
			mCurrentEntity = LoadSkillsEntity();
			GameMenuScene.this.attachChild(mCurrentEntity);
			GameMenuScene.this.loadTabTouchAreas();
				break;
			}
			return true;
			}					
		};
		this.mGameMenuEntity.attachChild(mSkillsTabSprite);
		
		//Attributes Tab
		this.mAttributesTabSprite = new Sprite(482, 0,this.mAttributesTabTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_OUTSIDE:
			case TouchEvent.ACTION_CANCEL:
				break;
			case TouchEvent.ACTION_DOWN:
				GameMenuScene.this.mAttributesTabSprite.setAlpha(0.5f);
				UnloadEntity(mCurrentEntity);
				mCurrentEntity = LoadAttributesEntity();
				GameMenuScene.this.attachChild(mCurrentEntity);
				GameMenuScene.this.loadTabTouchAreas();
				break;
			}
			return true;
			}					
		};
		this.mGameMenuEntity.attachChild(mAttributesTabSprite);
		

		//Info Tab
		this.mInfoTabSprite = new Sprite(623, 0,this.mInfoTabTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_OUTSIDE:
			case TouchEvent.ACTION_CANCEL:
				break;
			case TouchEvent.ACTION_DOWN:
				GameMenuScene.this.mInfoTabSprite.setAlpha(0.5f);
				UnloadEntity(mCurrentEntity);
				mCurrentEntity = LoadInfoEntity();
				GameMenuScene.this.attachChild(mCurrentEntity);
				GameMenuScene.this.loadTabTouchAreas();
				break;
			}
			return true;
			}					
		};
		this.mGameMenuEntity.attachChild(mInfoTabSprite);
		
			
		
		
					
		this.mInventoryTabSprite.setAlpha(0.5f);
		mCurrentEntity = LoadInventoryEntity();
		this.loadTabTouchAreas();
		GameMenuScene.this.attachChild(mCurrentEntity);
		
		//##############FIN DE LA ENTIDAD PRINCIPAL########################
		
		this.setTouchAreaBindingOnActionDownEnabled(true);
		
	}
			
	public void loadTabTouchAreas(){
		this.registerTouchArea(this.mSettingsSprite);
		this.registerTouchArea(this.mInventoryTabSprite);
		this.registerTouchArea(this.mEquipmentTabSprite);
		this.registerTouchArea(this.mSkillsTabSprite);
		this.registerTouchArea(this.mAttributesTabSprite);
		this.registerTouchArea(this.mInfoTabSprite);
	}
	
	
	
	//#################INVENTORY ENTITY######################
	public Entity LoadInventoryEntity(){
				this.mInventoryEntity.detachChildren();//La limpio, necesario?
		
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Interfaces/InGameMenu/Inventory/");
				this.mInventoryTextureAtlas = new BitmapTextureAtlas(Game.getInstance().getTextureManager(), 1024,1024, TextureOptions.BILINEAR);
				this.mInventoryUseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mInventoryTextureAtlas, Game.getInstance().getApplicationContext(), "UseEquip.png", 0, 0);
				this.mInventoryTossTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mInventoryTextureAtlas, Game.getInstance().getApplicationContext(), "Toss.png", 192, 0);
				this.mInventoryMoneyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mInventoryTextureAtlas, Game.getInstance().getApplicationContext(), "Money.png", 0, 290);
				this.mInventoryTextureAtlas.load();
				
				//Use Sprite
				this.mInventoryUseSprite = new Sprite(Game.getSceneManager().getDisplay().getDisplayWidth() - 92, 150,this.mInventoryUseTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {};
				this.mInventoryEntity.attachChild(mInventoryUseSprite);
				
				
				//Toss Sprite
				this.mInventoryTossSprite = new Sprite(Game.getSceneManager().getDisplay().getDisplayWidth() - 92, 295,this.mInventoryTossTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {};
				this.mInventoryEntity.attachChild(mInventoryTossSprite);
				
				
				//Money Sprite
				this.mInventoryMoneySprite = new Sprite(Game.getSceneManager().getDisplay().getDisplayWidth() - 110, 125,this.mInventoryMoneyTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {};
				this.mInventoryEntity.attachChild(mInventoryMoneySprite);

					
				//ITEM
				//load a los objetos(funcion?)####### Texture atlas distinto para items??###############################################################
					this.mInventoryTextureAtlas.unload();
					this.mItemTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mInventoryTextureAtlas, Game.getInstance().getApplicationContext(), "Item.png", 190, 290);
					this.mInventoryTextureAtlas.load();
					
					this.mItemSprite = new Sprite(50, 100,this.mItemTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {
						boolean mGrabbed = false;
						@Override
						public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						switch(pSceneTouchEvent.getAction()) {
						case TouchEvent.ACTION_DOWN:
							GameMenuScene.this.mItemSprite.setScale(3.0f);
							this.mGrabbed = true;
							break;
						case TouchEvent.ACTION_MOVE:
							if(this.mGrabbed) {
								GameMenuScene.this.mItemSprite.setPosition(pSceneTouchEvent.getX() - GameMenuScene.this.mItemSprite.getWidth() / 2, pSceneTouchEvent.getY() - GameMenuScene.this.mItemSprite.getHeight() / 2);
							}
							break;
						case TouchEvent.ACTION_UP:
							if(this.mGrabbed) {
								this.mGrabbed = false;
								if(GameMenuScene.this.mItemSprite.collidesWith(mInventoryUseSprite)){
								//	if(GameMenuScene.this.mEquipmentManager.EquipmentFunction(mEquipmentSwordItemSprite)== true){
									//borrarlo, se usa
								//	}else {
									GameMenuScene.this.mItemSprite.setPosition(GameMenuScene.this.mItemSprite.getInitialX(),GameMenuScene.this.mItemSprite.getInitialY());
								//	}
								}else{
								//	if(GameMenuScene.this.mEquipmentManager.IsEquipped(this,GameMenuScene.this.mEquipmentManager.SortEquip(this)) == true){
								//		GameMenuScene.this.mEquipmentManager.UnequipItem(this,GameMenuScene.this.mEquipmentManager.SortEquip(this));
								//	}
								//	GameMenuScene.this.mItemSprite.setPosition(GameMenuScene.this.mItemSprite.getInitialX(),GameMenuScene.this.mItemSprite.getInitialY());
									}
								GameMenuScene.this.mItemSprite.setScale(2.0f);
							}
							break;
						}
						return true;
						}					
					};
					GameMenuScene.this.mItemSprite.setScale(2.0f);
					this.mInventoryEntity.attachChild(mItemSprite);
					this.registerTouchArea(mItemSprite);
					
					return this.mInventoryEntity;
	}	
	
		
	
	
	
	
	
	//#################EQUIPMENT ENTITY######################
	public Entity LoadEquipmentEntity(){
		//HACER FUNCION PARA CARGAR EL EQUIPMENT VIEJO, NECESITO EL SQL
		
		this.mEquipmentEntity.detachChildren();//La limpio, necesario?
		this.mEquipmentUnEquippedItemsEntity = new Entity(0,0);
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Interfaces/");
		this.mEquipmentTextureAtlas = new BitmapTextureAtlas(Game.getInstance().getTextureManager(), 1024,1024, TextureOptions.BILINEAR);
		this.mEquipmentBoxTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mEquipmentTextureAtlas, Game.getInstance().getApplicationContext(), "InGameMenu/Equipment/Box.png", 0, 0);
		this.mEquipmentAttributesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mEquipmentTextureAtlas, Game.getInstance().getApplicationContext(), "InGameMenu/Equipment/Attributes.png", 390, 0);
		this.mEquipmentItemsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mEquipmentTextureAtlas, Game.getInstance().getApplicationContext(), "InGameMenu/Equipment/Items.png", 390, 35);
		this.mEquipmentHeadTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mEquipmentTextureAtlas, Game.getInstance().getApplicationContext(), "InGameMenu/Equipment/Head.png", 390, 69);
		this.mEquipmentBodyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mEquipmentTextureAtlas, Game.getInstance().getApplicationContext(), "InGameMenu/Equipment/Body.png", 390, 150);
		this.mEquipmentLegsTextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mEquipmentTextureAtlas, Game.getInstance().getApplicationContext(), "InGameMenu/Equipment/Legs.png", 390, 250);
		this.mEquipmentWeaponTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mEquipmentTextureAtlas, Game.getInstance().getApplicationContext(), "InGameMenu/Equipment/Weapon.png", 390, 350);
		this.mEquipmentOffhandTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mEquipmentTextureAtlas, Game.getInstance().getApplicationContext(), "InGameMenu/Equipment/Offhand.png", 0, 406);
		this.mEquipmentExtraTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mEquipmentTextureAtlas, Game.getInstance().getApplicationContext(), "InGameMenu/Equipment/Extra.png", 80, 406);
		this.mEquipmentTextureAtlas.load();
		
		
		//Cajas de contenidos
		this.mEquipmentBoxSprite = new Sprite(20, 70,this.mEquipmentBoxTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {	};
		this.mEquipmentEntity.attachChild(mEquipmentBoxSprite);
		//############HACER QUE LAS POSICIONES SEAN RELATIVAS###################
		this.mEquipmentBox2Sprite = new Sprite(415, 70,this.mEquipmentBoxTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {	};
		this.mEquipmentEntity.attachChild(mEquipmentBox2Sprite);
			
		this.mEquipmentUnEquippedItemsEntity.setPosition(this.mEquipmentBox2Sprite.getX()+10,this.mEquipmentBox2Sprite.getY()+45);
		
				
		//Items Tab
		this.mEquipmentItemsSprite = new Sprite(420, 75,this.mEquipmentItemsTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {
			boolean mGrabbed = false;
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				this.mGrabbed = true;
				break;
			case TouchEvent.ACTION_UP:
				if(this.mGrabbed) {
					this.mGrabbed = false;					
					GameMenuScene.this.mEquipmentItemsSprite.setAlpha(0.5f);
				//	GameMenuScene.this.loadEquippedItems();
				}
				break;
			}
			return true;
			}							
		};
		this.mEquipmentEntity.attachChild(mEquipmentItemsSprite);
		this.registerTouchArea(mEquipmentItemsSprite);
		
		//Attributes Tab
		this.mEquipmentAttributesSprite = new Sprite(612, 75,this.mEquipmentAttributesTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_OUTSIDE:
			case TouchEvent.ACTION_CANCEL:
				break;
			case TouchEvent.ACTION_DOWN:
			case TouchEvent.ACTION_UP:
			/*	GameMenuScene.this.mEquipmentAttributesSprite.setAlpha(0.5f);
				for(int i=0;i<GameMenuScene.this.mDataHandler.getInventoryCount();i++){					
					final Item pItem = new Item(mDataHandler,mEquipmentTextureAtlas,0+64*i,500,500+64*i,250,mEquipmentEntity,GameMenuScene.this,GameMenuScene.this.mDataHandler.getInventoryItemID(i),0);
				}
				*/
				
				break;
			}
			return true;
			}					
		};
		this.mEquipmentEntity.attachChild(mEquipmentAttributesSprite);
		//##########################BODY###################################
		
		//Helm Sprite
		this.mEquipmentHeadSprite = new Sprite(165, 85,this.mEquipmentHeadTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {};
		this.mEquipmentEntity.attachChild(mEquipmentHeadSprite);
		
		//Plate Sprite
		this.mEquipmentBodySprite = new Sprite(144, 186,this.mEquipmentBodyTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {};
		this.mEquipmentEntity.attachChild(mEquipmentBodySprite);
		
		//Legs Sprite
		this.mEquipmentLegsSprite = new Sprite(169, 306,this.mEquipmentLegsTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {};
		this.mEquipmentEntity.attachChild(mEquipmentLegsSprite);
		
		
		//Weapon Sprite
		this.mEquipmentWeaponSprite = new Sprite(46, 180,this.mEquipmentWeaponTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {
			boolean mGrabbed = false;
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:	
				this.mGrabbed = true;
				//checkear si ya tiene algo atacheado, si no tiene:
				//Leer que items en equipment son weapons y ponerlos en algun color
				//si tiene:
				//dragear el item afuera del sprite y desatachearlo y registrar el touch area
				break;
			case TouchEvent.ACTION_MOVE:
				if(this.mGrabbed) {
					//GameMenuScene.this.ITEM ATACHEADO.setPosition(pSceneTouchEvent.getX() - GameMenuScene.this.mITEM ATACHEADO.getWidth() / 2, pSceneTouchEvent.getY() - GameMenuScene.this.mITEM ATACHEADO.getHeight() / 2);
				}
				break;
			case TouchEvent.ACTION_UP:
				if(this.mGrabbed) {
					this.mGrabbed = false;
					//checkear si ya tiene algo atacheado, si no tiene:
					//Leer que items en equipment son weapons y ponerlos en algun color
					//si tiene:
					//dragear el item afuera del sprite y desatachearlo y registrar el touch area
				}
				break;
			}
			return true;
			}					
		};
		this.mEquipmentEntity.attachChild(mEquipmentWeaponSprite);
		
		
		//Offhand Sprite
		this.mEquipmentOffhandSprite = new Sprite(284, 180,this.mEquipmentOffhandTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {};
		this.mEquipmentEntity.attachChild(mEquipmentOffhandSprite);
		
		
		//Extra Sprite
		this.mEquipmentExtraSprite = new Sprite(272, 311,this.mEquipmentExtraTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {};
		this.mEquipmentEntity.attachChild(mEquipmentExtraSprite);
		
				//Funcion para registrar touch areas??
		this.registerTouchArea(this.mEquipmentAttributesSprite);
		this.registerTouchArea(this.mEquipmentItemsSprite);

		//########################FIN DEL BODY#############################
		
		this.loadEquippedItems();
		this.mUnEquippedCount = 0;
		this.loadUnEquippedItems();
		this.mEquipmentEntity.attachChild(mEquipmentUnEquippedItemsEntity);
		return this.mEquipmentEntity;
	}
	
	
	
	//################################################################################################
	//								FUNCIONES DE EQUIPMENT ENTITY
	//################################################################################################
	public void EquipItem(Item pItem, Boolean pColorChange,Boolean pCollides){
		Sprite tempSprite = null;//le doy null porque sino rompe las bolas
		switch(pItem.getType()){//iguala pSprite al container que corresponde
		case 0:
			tempSprite = this.mEquipmentHeadSprite;		
			break;
		case 1:
			tempSprite = this.mEquipmentBodySprite;
			break;
		case 2:
			tempSprite = this.mEquipmentLegsSprite;
			break;
		case 3:
			tempSprite = this.mEquipmentExtraSprite;
			break;
		case 4:
			tempSprite = this.mEquipmentOffhandSprite;
			break;
		case 5:
			tempSprite = this.mEquipmentWeaponSprite;
			break;
		}
		this.ColorSprite(tempSprite, pColorChange);//cambia el color
		
		if(pColorChange == false){//si ColorChange = false (osea que es en el Action_UP)
			this.MoveItem(tempSprite, pItem,pCollides);//Ejecuta el equipamiento
		}
		
	}
	
	public void ColorSprite(Sprite pSprite, Boolean pChangeColor){//funcion para cambiar colores
			if(pChangeColor==true){//si es en el Action_DOWN cambia el color a verde
				pSprite.setGreen(1f);
				pSprite.setRed(0.2f);
				pSprite.setBlue(0.2f);
			}else {//Si es en el Action_UP resetea el color ***ESTA ANDANDO MAL***
				pSprite.setGreen(0f);
				pSprite.setRed(0f);
				pSprite.setBlue(0f);			
			}
			
		}
	
	
	public void MoveItem(Sprite pSprite, Item pItem,Boolean pCollides){//Funcion para iniciar equipamiento grafico + helper
		if(pCollides == true){//Si colisiona
			if(this.mEquipmentManager.EquipmentFunction(pItem) == true){//Si se equipo			
				pItem.getIcon().setPosition(pSprite.getX() + pSprite.getWidth() / 2 - pItem.getIcon().getWidth() / 2,pSprite.getY() + pSprite.getHeight() / 2 - pItem.getIcon().getHeight() / 2);//lo pone donde corresponde			
				pSprite.setAlpha(0.5f);
			} else {//Si se desequipo(porque ya estaba equipado) lo devuelve el Unequip				
				pSprite.setAlpha(1.0f);
				
			}
		} else{//Si no colisiona
			if(this.mEquipmentManager.IsEquipped(pItem,pItem.getType()) == true){//se fija si estaba equipado
				this.mEquipmentManager.UnequipItem(pItem.getType());//si lo estaba lo desequipa				
			} else{//Si no estaba equipado
				this.mUnEquippedCount-=1;
				this.PlaceEquipmentItem(pItem);
			}
		}
	}
	
	
	private void loadUnEquippedItems(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Items/");
		this.mEquipmentUnEquippedItemsTextureAtlas= new BitmapTextureAtlas(Game.getInstance().getTextureManager(), 512,24, TextureOptions.BILINEAR);
		int tempArray[] = mDataHandler.getEquippedIDs(0);
		for(int i=0;i<tempArray.length;i++){	
			if(this.mDataHandler.getItemClass(tempArray[i]) == this.mDataHandler.getPlayerClass(0)){//checkeo que los items sean de la clase del player
			//	this.mUnEquippedCount+=1;//le sumo uno al unEquipped count para saber cuantos items hay sin equipar  \\ no le sumo, ya lo hace la funcion
			final Item pItem = new Item(mDataHandler,mEquipmentUnEquippedItemsTextureAtlas,0+24*i,0,450 + 60 * i,200,mEquipmentUnEquippedItemsEntity,GameMenuScene.this,tempArray[i],0);
			this.PlaceEquipmentItem(pItem);
			}//si no son no los cargo wepa
		}
		this.mEquipmentUnEquippedItemsTextureAtlas.load();
	}
	
	
	private void loadEquippedItems(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Items/");
		this.mEquipmentEquippedItemsTextureAtlas = new BitmapTextureAtlas(Game.getInstance().getTextureManager(), 144,24, TextureOptions.BILINEAR);
		
		int tempArray[] = mDataHandler.getEquippedIDs(1);//consegui las ID de todo lo que esta equipado
		for(int i=0;i<tempArray.length;i++){					
			final Item pItem = new Item(mDataHandler,mEquipmentEquippedItemsTextureAtlas,0+24*i,0,0,0,mEquipmentEntity,GameMenuScene.this,tempArray[i],0);
			
			mEquipmentManager.LoadEquippedItem(pItem);
			
			Sprite tempSprite = null;
			switch(pItem.getType()){
			case 0:
				tempSprite = this.mEquipmentHeadSprite;		
				break;
			case 1:
				tempSprite = this.mEquipmentBodySprite;
				break;
			case 2:
				tempSprite = this.mEquipmentLegsSprite;
				break;
			case 3:
				tempSprite = this.mEquipmentExtraSprite;
				break;
			case 4:
				tempSprite = this.mEquipmentOffhandSprite;
				break;
			case 5:
				tempSprite = this.mEquipmentWeaponSprite;
				break;
			}
			pItem.getIcon().setPosition(tempSprite.getX() + tempSprite.getWidth() / 2 - pItem.getIcon().getWidth() / 2,tempSprite.getY() + tempSprite.getHeight() / 2 - pItem.getIcon().getHeight() / 2);//lo pone donde corresponde
			tempSprite.setAlpha(0.5f);
		}
		this.mEquipmentEquippedItemsTextureAtlas.load();
	}
	
	
	public void PlaceEquipmentItem(Item pItem){
		//this.mItemsArray[this.mUnEquippedCount] = pItem;
		//this.mCountArray[this.mUnEquippedCount] = this.mUnEquippedCount;
		int row = (int)(this.mEquipmentBox2Sprite.getWidth())/ 36;//cuantos items hay por fila
		//int tY = (this.mCountArray[this.mUnEquippedCount] / row) * 36;//divido los items por la cantidad de items por fila, me dice cuantas filas hay.multiplico por 36 (item =24, scale = 1.5 asi que tama�o = 36) y consigo el Y 
		int tY = (this.mUnEquippedCount / row) * 36;//divido los items por la cantidad de items por fila, me dice cuantas filas hay.multiplico por 36 (item =24, scale = 1.5 asi que tama�o = 36) y consigo el Y
		//int tX = (this.mCountArray[this.mUnEquippedCount] % row) * 36;//me devuelve el resto, se cuantos sobran en una fila
		int tX = (this.mUnEquippedCount % row) * 36;//me devuelve el resto, se cuantos sobran en una fila
		pItem.getIcon().setPosition(tX, tY);
		this.mUnEquippedCount += 1;
	}
	
	
	public int getUnEquippedCount(){
		return this.mUnEquippedCount;
	}
	
	public void setUnEquippedCount(int pUnEquippedCount){
		this.mUnEquippedCount = pUnEquippedCount;
	}
	
	public Sprite getEquipmentBoxSprite(){//para los collides
		return this.mEquipmentBoxSprite;
	}
	
	public Entity getEquipmentEntity(){
		return this.mEquipmentEntity;
	}
	
	public Entity getEquipmentUnEquippedItemsEntity(){
		return this.mEquipmentUnEquippedItemsEntity;
	}
	//################################################################################################
	//################################################################################################	
	
	
	
	
	//#################SKILLS ENTITY######################	
	public Entity LoadSkillsEntity(){
		this.mSkillsEntity.detachChildren();//La limpio, necesario?

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Interfaces/InGameMenu/Skills/");
		this.mSkillsTextureAtlas = new BitmapTextureAtlas(Game.getInstance().getTextureManager(), 1024,1024, TextureOptions.BILINEAR);
		this.mSkillTreeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSkillsTextureAtlas, Game.getInstance().getApplicationContext(), "fondo.png", 0, 0);
		this.mSkillsTextureAtlas.load();
		
		this.mSkillTreeBackgroundSprite = new Sprite(165, 85,this.mSkillTreeTextureRegion,Game.getInstance().getVertexBufferObjectManager()) {
			boolean mGrabbed = false;
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:	
				this.mGrabbed = true;
				
				break;
			case TouchEvent.ACTION_MOVE:
				if(this.mGrabbed) {				
					GameMenuScene.this.mSkillTreeBackgroundSprite.setX(pSceneTouchEvent.getX() - GameMenuScene.this.mSkillTreeBackgroundSprite.getWidth() / 2);
				}
				break;
			case TouchEvent.ACTION_UP:
				if(this.mGrabbed) {
					this.mGrabbed = false;
					
				}
				break;
			}
			return true;
			}					
		};
		this.mSkillTreeBackgroundSprite.setAlpha(0.5f);
		this.mSkillsEntity.attachChild(mSkillTreeBackgroundSprite);
		this.registerTouchArea(mSkillTreeBackgroundSprite);
		
		return this.mSkillsEntity;
	}
	
	//################################################################################################
	//								FUNCIONES DE SKILLS ENTITY
	//################################################################################################
	
	public void LoadSkillsTree(){
		
	}
	
	//################################################################################################
	//################################################################################################	
	
	
	
	//#################ATTRIBUTES ENTITY######################
	public Entity LoadAttributesEntity(){
		this.mAttributesEntity.detachChildren();//La limpio, necesario?
		
		
		return this.mAttributesEntity;
	}
	
	
	
	//#################INFO ENTITY######################
	public Entity LoadInfoEntity(){
		this.mInfoEntity.detachChildren();//La limpio, necesario?


		return this.mInfoEntity;
	}
	
	
	//##################SETTINGS ENTITY###################
	public Entity LoadSettingsEntity(){
		this.mSettingsEntity.detachChildren();//La limpio, necesario?


		return this.mSettingsEntity;
	}
	
	
	//#################UNLOAD ENTITY######################
	public void UnloadEntity(Entity pEntity){
		this.detachChild(pEntity);
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















/*
 * 
 * -FUNCION PARA CARGAR Y DESCARGAR TOUCH AREAS
 * -CENTRAR LA POSICION A LOS ITEMS (- THIS.WIDTH / 2, FIJARME SI HAY SET CENTER O HACER UNA FUNCION)
 * 
 * 
 * 
 * */







