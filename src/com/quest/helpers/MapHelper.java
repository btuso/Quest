package com.quest.helpers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.debug.Debug;

import android.util.Log;

import com.quest.entities.Mob;
import com.quest.game.Game;
import com.quest.helpers.interfaces.IAsyncCallback;
import com.quest.timers.Timer;
import com.quest.triggers.MapChangeTrigger;
import com.quest.triggers.Trigger;
import com.quest.util.constants.IMeasureConstants;

public class MapHelper implements IMeasureConstants {

	// ===========================================================
	// Constants
	// ===========================================================
	private int MAP_SIZE = 10;
	// ===========================================================
	// Fields
	// ===========================================================
	private TMXLoader mTmxLoader;
	private TMXTiledMap mCurrentMap;
	private List<TMXTile> mCollideTiles;
	private List<MapChangeTrigger> mTriggerTiles;
	private boolean isChangingMap;
	private HashMap<Integer, ArrayList<int[]>> MapClientCollideTiles;
	private HashMap<Integer, List<TMXTile>> MapCollideTiles;
	private boolean firstime = true;
	// ===========================================================
	// Constructors
	// ===========================================================
	public MapHelper() {

		this.mCollideTiles = new ArrayList<TMXTile>();
		this.mTriggerTiles = new ArrayList<MapChangeTrigger>();
		this.MapClientCollideTiles = new HashMap<Integer, ArrayList<int[]>>();
		this.MapCollideTiles = new HashMap<Integer, List<TMXTile>>();
		this.mTmxLoader = new TMXLoader(Game.getInstance().getAssets(), Game.getInstance().getEngine().getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, Game.getInstance().getVertexBufferObjectManager());
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public TMXTiledMap getCurrentMap() {
		return mCurrentMap;
	}

	public void setCurrentMap(TMXTiledMap pCurrentMap) {
		this.mCurrentMap = pCurrentMap;
	}

	/**
	 * @return the mCollideTiles
	 */
	public List<TMXTile> getCollideTiles() {
		return mCollideTiles;
	}

	/**
	 * @param mCollideTiles the mCollideTiles to set
	 */
	public void setCollideTiles(List<TMXTile> mCollideTiles) {
		this.mCollideTiles = mCollideTiles;
	}

	/**
	 * @return the mTriggerTiles
	 */
	public List<MapChangeTrigger> getTriggerTiles() {
		return mTriggerTiles;
	}

	/**
	 * @param mTriggerTiles the mTriggerTiles to set
	 */
	public void setTriggerTiles(List<MapChangeTrigger> mTriggerTiles) {
		this.mTriggerTiles = mTriggerTiles;
	}

	/**
	 * @return the isChangingMap
	 */
	public boolean isChangingMap() {
		return isChangingMap;
	}

	/**
	 * @param isChangingMap the isChangingMap to set
	 */
	public void setChangingMap(boolean isChangingMap) {
		this.isChangingMap = isChangingMap;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void loadMap(final String pName) {
		
		Game.getSceneManager().getLoadingScene().changeCurrentTaskText("Loading map");
		
		// Update it's map
		Game.getPlayerHelper().getOwnPlayer().setCurrentMap(Integer.parseInt(pName));
		
		// Detach old Map Layers
		if(this.mCurrentMap != null) {
			for(final TMXLayer tLayer : this.mCurrentMap.getTMXLayers()) {
				Game.getSceneManager().getGameScene().getMapLayer().detachChild(tLayer);
			}
			this.mCollideTiles.clear();
			this.mTriggerTiles.clear();
		}
		
		// Load the map
		try {
			this.mCurrentMap = this.mTmxLoader.loadFromAsset("tmx/" + pName + ".tmx");
		} catch (final TMXLoadException tmxle) {
			Debug.e(tmxle);
		}

		// Attach new Map Layers
		for(final TMXLayer tLayer : this.mCurrentMap.getTMXLayers()) {
			Game.getSceneManager().getGameScene().getMapLayer().attachChild(tLayer);
		}
		
		// Set Map Bounds 
		Game.getSceneManager().getDisplay().setMapBounds(this.mCurrentMap.getTMXLayers().get(0).getWidth(), this.mCurrentMap.getTMXLayers().get(0).getHeight());
		
		// The for loop cycles through each object on the map
		for (final TMXObjectGroup group : this.mCurrentMap.getTMXObjectGroups()) {

			// Gets the object layer with these properties. Use if
			// you have many object layers, Otherwise this can be
			// removed
			if(group.getTMXObjectGroupProperties().size()!=0)Log.e("Quest!",group.getTMXObjectGroupProperties().get(0).getName());
			if (group.getTMXObjectGroupProperties().containsTMXProperty("Collide", "true")) {

				for (final TMXObject object : group.getTMXObjects()) {

					int objectX = object.getX() + TILE_SIZE / 2;
					int objectY = object.getY() + TILE_SIZE / 2;
					// Gets the number of rows and columns in the
					// object
					int objectHeight = object.getHeight() / TILE_SIZE;
					int objectWidth = object.getWidth() / TILE_SIZE;

					// Gets the tiles the object covers and puts it
					// into the Arraylist CollideTiles
					for (int TileRow = 0; TileRow < objectHeight; TileRow++) {
						for (int TileColumn = 0; TileColumn < objectWidth; TileColumn++) {
							TMXTile tempTile = this.getTMXTileAt(objectX + TileColumn * TILE_SIZE, objectY
									+ TileRow * TILE_SIZE);
							this.mCollideTiles.add(tempTile);
						}
					}
				} 
				if(this.MapCollideTiles.get(Integer.parseInt(pName))==null){
					MapCollideTiles.put(Integer.parseInt(pName), mCollideTiles);
				}
				if(!Game.isServer()){
					if(MapClientCollideTiles.get(Integer.parseInt(pName))==null){
						ArrayList<int[]> tmpList = new ArrayList<int[]>();
						for(int i = mCollideTiles.size() - 1;i>=0;i--){
							TMXTile tile = mCollideTiles.get(i);
							tmpList.add(new int[]{tile.getTileColumn(),tile.getTileRow()});
							Log.d("Logd", "X: "+tile.getTileColumn()+" Y: "+tile.getTileRow());
						}
						MapClientCollideTiles.put(Integer.parseInt(pName), tmpList);
					}
					
					if(Game.getPlayerHelper().isAloneInMap(Game.getPlayerHelper().getOwnPlayer())){
						Game.getClient().sendCollideTiles(Integer.parseInt(pName), MapClientCollideTiles.get(Integer.parseInt(pName)));
					}
				}
			}
			
			if (group.getTMXObjectGroupProperties().containsTMXProperty("MobSpawn", "true")) {

				ArrayList<Object[]> mMobsToAllocate;
				mMobsToAllocate = new ArrayList<Object[]>();
				
				for (final TMXObject object : group.getTMXObjects()) {
					int corner1X = object.getX() / TILE_SIZE;
					int corner1Y = object.getY() / TILE_SIZE;
					int corner2X = corner1X + object.getWidth() / TILE_SIZE;
					int corner2Y = corner1Y + object.getHeight() / TILE_SIZE;

					if(Game.getPlayerHelper().isAloneInMap(Game.getPlayerHelper().getOwnPlayer()) || firstime){
						
						if(Game.isServer()){//Genero los mobs
							//Loop AmountToBeSpawned times
							Game.getSceneManager().getLoadingScene().changeCurrentTaskText("Loading Mobs");
							for(int i = 0;i<Integer.parseInt(object.getTMXObjectProperties().get(0).getValue());i++ ){
								Game.getSceneManager().getGameScene().CreateMob_Server(Integer.parseInt(object.getTMXObjectProperties().get(1).getValue()), Game.getRandomInt(corner1X, corner2X), Game.getRandomInt(corner1Y, corner2Y), Integer.parseInt(pName));	
							}
						}else{//Pido que se generen los mobs
							if(!firstime)
							Game.getClient().sendMobRequest(Integer.parseInt(object.getTMXObjectProperties().get(1).getValue()), Integer.parseInt(pName), corner1X, corner1Y, corner2X, corner2Y, Integer.parseInt(object.getTMXObjectProperties().get(0).getValue()));
							firstime = false;
						}	
					}else{
						if(Game.isServer()){
							ArrayList<Mob> mobsinmap = Game.getMobHelper().getMobsInMap(Game.getPlayerHelper().getOwnPlayer().getCurrentMap());
							for(int i = mobsinmap.size()-1;i>=0;i--){
								Game.getSceneManager().getGameScene().attachChild(mobsinmap.get(i));
								Game.getSceneManager().getGameScene().registerTouchArea(mobsinmap.get(i).getBodySprite());
							}
						}
					}
					Game.getSceneManager().getLoadingScene().changeCurrentTaskText("Allocating Mobs in pool");
					boolean add = true;
					for(int i = mMobsToAllocate.size()-1;i>=0;i--)
					{
						if((Integer)mMobsToAllocate.get(i)[0] == Integer.parseInt(object.getTMXObjectProperties().get(1).getValue()))
						{//Si la lista ya contiene el mob suma los amounts
							add = false;
							mMobsToAllocate.get(i)[1] = (Integer)mMobsToAllocate.get(i)[1] + Integer.parseInt(object.getTMXObjectProperties().get(0).getValue());;
						}
					}
					if(add)mMobsToAllocate.add(new Object[]{Integer.parseInt(object.getTMXObjectProperties().get(1).getValue()),Integer.parseInt(object.getTMXObjectProperties().get(0).getValue())});
				}
				
				Game.getMobHelper().allocateDefaultMobs(mMobsToAllocate);
				
			}
			if(Game.isServer())
			{//Habria que hacerlo cuando hayamos movido el mapa al servidor para que pueda verificar el movimiento de los mobs cliente
				if (group.getTMXObjectGroupProperties().containsTMXProperty("MobWalk", "true")) {
					
					for (final TMXObject object : group.getTMXObjects()) {
						int corner1X = object.getX() / TILE_SIZE;
						int corner1Y = object.getY() / TILE_SIZE;
						int corner2X = corner1X + object.getWidth() / TILE_SIZE;
						int corner2Y = corner1Y + object.getHeight() / TILE_SIZE;
						
						/*
						 * Separar en mobwalk true y false, o fijarse como hacer que los mobwalkfalse objects funcionen bien (se restarian de la lista mobwalk, pero a veces cargan antes...) 
						 * 
						 */
						
					}
				}
			}
			if(group.getTMXObjectGroupProperties().containsTMXProperty("MapChangeTrigger", "true")) {

				for (final TMXObject object : group.getTMXObjects()) {

					int objectX = object.getX() + TILE_SIZE / 2;
					int objectY = object.getY() + TILE_SIZE / 2;
					// Gets the number of rows and columns in the
					// object
					int objectHeight = object.getHeight() / TILE_SIZE;
					int objectWidth = object.getWidth() / TILE_SIZE;

					// Gets the tiles the object covers and puts it
					// into the Arraylist CollideTiles
					for (int TileRow = 0; TileRow < objectHeight; TileRow++) {
						for (int TileColumn = 0; TileColumn < objectWidth; TileColumn++) {
							
							TMXTile tempTile = this.getTMXTileAt(objectX + TileColumn * TILE_SIZE, objectY
									+ TileRow * TILE_SIZE);
							
							MapChangeTrigger tmpTrigger = new MapChangeTrigger(tempTile) {
								
								@Override
								public void onHandleTriggerAction() {
									// TODO Auto-generated method stub
									super.onHandleTriggerAction();
									
									// Check if it's already in a map change
									if(MapHelper.this.isChangingMap) return;
									MapHelper.this.isChangingMap = true;
									
									// Set our constants
									final int nextMapNumber = this.mNextMapNumber;
									final int nextMapX = this.mNextMapX;
									final int nextMapY = this.mNextMapY;
									
									// Load the map in the background
									Game.getSceneManager().saveCurrentSceneState(true);
									Game.getSceneManager().setLoadingScene();
									Game.getInstance().runOnUiThread(new Runnable() {
								        @Override
								        public void run() {
											 
									        new AsyncTaskLoader().execute(new IAsyncCallback() {
									        	
												@Override
												public void workToDo() {
													// TODO Auto-generated method stub
													
													if(Game.isServer()){
														Game.getSceneManager().getLoadingScene().changeCurrentTaskText("Saving data.");
														Game.getQueryQueuer().executeQueries();//*** Cambiar esto de lugar a donde corresponderia
													}
													
													// Load it and set new Player's position
													int pX = (nextMapX == 0) ? Game.getPlayerHelper().getOwnPlayer().getTMXTileAt().getTileColumn() : nextMapX;
													int pY = (nextMapY == 0) ? Game.getPlayerHelper().getOwnPlayer().getTMXTileAt().getTileRow() : nextMapY;
													
													Game.getPlayerHelper().getOwnPlayer().setCoords(pX,pY);
													
													if(Game.isServer()){
														Game.getServer().sendMessagePlayerChangedMap(Game.getUserID(),nextMapNumber,pX,pY);
														
														if(Game.getPlayerHelper().isAloneInMap(Game.getPlayerHelper().getOwnPlayer())){ //If server is alone in the map recycle mobs
															Game.getMobHelper().deleteMobs(Game.getMobHelper().getMobsInMap(Integer.parseInt(pName)));
														}else{ //If no alone dettach them
															ArrayList<Mob> mobsinmap = Game.getMobHelper().getMobsInMap(Game.getPlayerHelper().getOwnPlayer().getCurrentMap());
															for(int i = mobsinmap.size()-1;i>=0;i--){
																Game.getSceneManager().getGameScene().detachChild(mobsinmap.get(i));
																Game.getSceneManager().getGameScene().unregisterTouchArea(mobsinmap.get(i).getBodySprite());
															}
														}
														
													}else{//If client recycle mobs from past map
														Game.getClient().sendPlayerChangedMap(Game.getUserID(), nextMapNumber,pX,pY);
														Game.getMobHelper().deleteMobs(Game.getMobHelper().getMobsInMap(Integer.parseInt(pName)));
													}
													
													MapHelper.this.loadMap(String.valueOf(nextMapNumber));
													Game.getPlayerHelper().getOwnPlayer().setTileAt(pX,pY);

												}

												@Override
												public void onComplete() {
													// TODO Auto-generated method stub
													Game.getSceneManager().getLoadingScene().loadingAnimation(false);
											        // Map loaded!
													MapHelper.this.isChangingMap = false;
													Game.getSceneManager().restoreSavedScene();
													Game.getSceneManager().getDisplay().setZoom(1.6f);
													
												}
									        });
								        }
									});
								}
							};
							
							// Set our Trigger info
							tmpTrigger.setNextMapNumber(Integer.parseInt(object.getTMXObjectProperties().get(0).getValue()));
							tmpTrigger.setNextMapX(Integer.parseInt(object.getTMXObjectProperties().get(1).getValue()));
							tmpTrigger.setNextMapY(Integer.parseInt(object.getTMXObjectProperties().get(2).getValue()));
							
							// Add it to the list
							this.mTriggerTiles.add(tmpTrigger);
						}
					}
				}
			}
		}
	}
	
	public void registerCollisionTile(TMXTile tmxTileAt) {
		this.mCollideTiles.add(tmxTileAt);
	}
	
	public void unregisterCollisionTile(TMXTile tmxTileAt) {
		this.mCollideTiles.remove(tmxTileAt);
	}
	
	public TMXTile getTMXTileAt(float pScreenX, float pScreenY) {
		return this.mCurrentMap.getTMXLayers().get(0).getTMXTileAt(pScreenX, pScreenY);
	}

	public Trigger checkTrigger(TMXTile tmxTileAt) {
		for(Trigger tmpTrigger : this.mTriggerTiles) {
			if(tmpTrigger.getTile().equals(tmxTileAt)) {
				return tmpTrigger;
			}
		}
		return null;
	}
	
	public boolean checkCollision(int pMapID,TMXTile tmxTileAt) {
		if(this.MapCollideTiles.get(pMapID).contains(tmxTileAt))
		//if(this.mCollideTiles.contains(tmxTileAt))
			return true;
		else
			return false;
	}
	
	public void addCollides(int mapID, List<TMXTile> collides){
		this.MapCollideTiles.put(mapID,collides);
	}
	
	public List<TMXTile> getMapCollideTiles(int mapID){
		Iterator<Entry<Integer, List<TMXTile>>> it = this.MapCollideTiles.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = it.next();
			if(e.getKey().equals(mapID)) return (List<TMXTile>) e.getValue();
		}
		return null;
	}
	
	public boolean isLegalPosition(int pMapID,int pPositionX, int pPositionY) {
		
		// Check for Map Bounds
		if(pPositionX <= 0 || pPositionY <= 0 || pPositionX >= (MAP_WIDTH * TILE_SIZE) ||  pPositionY >= (MAP_HEIGHT * TILE_SIZE)) return false;

		// Get the Tile
		final TMXTile tmxTileTo = Game.getMapManager().getTMXTileAt(pPositionX, pPositionY);
		if(this.checkCollision(pMapID,tmxTileTo)) return false;
		
		return true; // Everything ok!
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
}
