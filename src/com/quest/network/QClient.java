package com.quest.network;

import java.io.IOException;
import java.net.Socket;

import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector.ISocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.protocol.server.IClientMessageHandler;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.MessagePool;

import android.util.Log;

import com.quest.constants.ClientMessageFlags;
import com.quest.constants.ServerMessageFlags;
import com.quest.entities.Player;
import com.quest.entities.objects.Attack;
import com.quest.game.Game;
import com.quest.network.messages.client.ClientMessageAreaAttack;
import com.quest.network.messages.client.ClientMessageAttackMessage;
import com.quest.network.messages.client.ClientMessageChangeMap;
import com.quest.network.messages.client.ClientMessageConnectionRequest;
import com.quest.network.messages.client.ClientMessageMovePlayer;
import com.quest.network.messages.client.ClientMessagePlayerCreate;
import com.quest.network.messages.client.ClientMessageSelectedPlayer;
import com.quest.network.messages.client.ConnectionPingClientMessage;
import com.quest.network.messages.server.ConnectionPongServerMessage;
import com.quest.network.messages.server.ServerMessageConnectionAcknowledge;
import com.quest.network.messages.server.ServerMessageConnectionRefuse;
import com.quest.network.messages.server.ServerMessageCreatePlayer;
import com.quest.network.messages.server.ServerMessageDisplayAreaAttack;
import com.quest.network.messages.server.ServerMessageExistingPlayer;
import com.quest.network.messages.server.ServerMessageFixedAttackData;
import com.quest.network.messages.server.ServerMessageMapChanged;
import com.quest.network.messages.server.ServerMessageMatchStarted;
import com.quest.network.messages.server.ServerMessageMobDied;
import com.quest.network.messages.server.ServerMessageMoveMob;
import com.quest.network.messages.server.ServerMessageSendPlayer;
import com.quest.network.messages.server.ServerMessageSpawnMob;
import com.quest.network.messages.server.ServerMessageUpdateEntityPosition;
import com.quest.objects.BooleanMessage;
import com.quest.util.constants.IMeasureConstants;

public class QClient extends ServerConnector<SocketConnection> implements ClientMessageFlags, ServerMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================
	static int SERVER_PORT = 4444;
	// ===========================================================
	// Fields
	// ===========================================================
	private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();
	
	// ===========================================================
	// Constructors
	// ===========================================================
		private void initMessagePool() {
			this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_PING, ConnectionPingClientMessage.class);
			this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_REQUEST, ClientMessageConnectionRequest.class);
			this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_PLAYER_CREATE, ClientMessagePlayerCreate.class);
			this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_SELECTED_PLAYER, ClientMessageSelectedPlayer.class);
			this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_MOVE_PLAYER, ClientMessageMovePlayer.class);
			this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_PLAYER_CHANGED_MAP, ClientMessageChangeMap.class);
			this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_ATTACK_MESSAGE, ClientMessageAttackMessage.class);
			this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_AREA_ATTACK_MESSAGE, ClientMessageAreaAttack.class);
			}
	
		public QClient(final String pServerIP, final ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
			super(new SocketConnection(new Socket(pServerIP, SERVER_PORT)), pSocketConnectionServerConnectorListener);
			
			
			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ACKNOWLEDGE, ServerMessageConnectionAcknowledge.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerMessageConnectionAcknowledge serverMessageConnectionAcknowledge= (ServerMessageConnectionAcknowledge) pServerMessage;
					Log.d("Quest!","Acknowledged");
					
					Game.getMatchData().setMatchID(serverMessageConnectionAcknowledge.getMatchID());
					Game.getSceneManager().getMatchScene().ClearTouchAreas();
					Game.getSceneManager().getMatchScene().SwitchEntity(Game.getSceneManager().getMatchScene().LoadMatchEntity(3));
				}
			});
	
			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_REFUSE, ServerMessageConnectionRefuse.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerMessageConnectionRefuse serverMessageConnectionRefuse = (ServerMessageConnectionRefuse) pServerMessage;
					if(serverMessageConnectionRefuse.getReason()){
						new BooleanMessage(serverMessageConnectionRefuse.getMatchName(), "Wrong password provided.","Ok", Game.getInstance()){};
					}else{
						new BooleanMessage(serverMessageConnectionRefuse.getMatchName(), "You have been kicked.","Ok", Game.getInstance()){
							@Override
							public void onOK() {
								//hacer que no pueda entrar
								super.onOK();
							}						
						};
					}
				}
			});
			
			//mando que cree un chara
			this.registerServerMessage(FLAG_MESSAGE_SERVER_CREATE_PLAYER, ServerMessageCreatePlayer.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					//No tiene player en la partida, le pido que se haga uno
					Game.getSceneManager().getMatchScene().msgCreateRemoteCharacter();
				}
			});
			//Llego un chara que tengo
			this.registerServerMessage(FLAG_MESSAGE_SERVER_EXISTING_PLAYER, ServerMessageExistingPlayer.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerMessageExistingPlayer serverMessageExistingPlayer = (ServerMessageExistingPlayer) pServerMessage;
					//Tiene player en la partida, lo agrego a la lista
					Game.getSceneManager().getMatchScene().LoadOwnRemoteCharacters(serverMessageExistingPlayer.getCharacterID(), serverMessageExistingPlayer.getLevel(), serverMessageExistingPlayer.getPlayerClass());
				}
			});
			
			//Recibi un new player!
			this.registerServerMessage(FLAG_MESSAGE_SERVER_SEND_PLAYER, ServerMessageSendPlayer.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					Log.d("Quest!","Llego player");
					final ServerMessageSendPlayer serverMessageSendPlayer = (ServerMessageSendPlayer) pServerMessage;
					Log.d("Quest!","Llego player, userid: "+serverMessageSendPlayer.getUserID());
					Game.getPlayerHelper().addPlayer(new Player(serverMessageSendPlayer.getUserID(), serverMessageSendPlayer.getPlayerID(), serverMessageSendPlayer.getPlayerClass(), serverMessageSendPlayer.getLevel(), serverMessageSendPlayer.getExperience(), serverMessageSendPlayer.getMoney(), serverMessageSendPlayer.getAttributes(), serverMessageSendPlayer.getCurrHPMP(), serverMessageSendPlayer.getHeadID(), serverMessageSendPlayer.getItemID(), serverMessageSendPlayer.getAmounts(), serverMessageSendPlayer.getIsEquipped()));
				}
			});

			this.registerServerMessage(FLAG_MESSAGE_SERVER_MATCH_STARTED, ServerMessageMatchStarted.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					//Game.getTextHelper().FlushText("MatchScene");
					Game.getSceneManager().setGameScene();
				}
			});
			
			
			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PONG, ConnectionPongServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ConnectionPongServerMessage connectionPongServerMessage = (ConnectionPongServerMessage) pServerMessage;
					final long roundtripMilliseconds = (System.currentTimeMillis() - connectionPongServerMessage.getTimestamp())/2;
					Log.d("Quest!","CLIENT Ping: " + roundtripMilliseconds + "ms");					
				}
			});

			this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_ENTITY_POSITION, ServerMessageUpdateEntityPosition.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerMessageUpdateEntityPosition serverMessageUpdateEntityPosition = (ServerMessageUpdateEntityPosition) pServerMessage;
					if(!Game.getUserID().equals(serverMessageUpdateEntityPosition.getPlayerKey()) || Game.getPlayerHelper().getPlayer(serverMessageUpdateEntityPosition.getPlayerKey()).getCurrentMap()==Game.getPlayerHelper().getOwnPlayer().getCurrentMap()){
						Game.getPlayerHelper().getPlayer(serverMessageUpdateEntityPosition.getPlayerKey()).moveToTile(Game.getMapManager().getTMXTileAt(serverMessageUpdateEntityPosition.getX() * IMeasureConstants.TILE_SIZE, serverMessageUpdateEntityPosition.getY() * IMeasureConstants.TILE_SIZE));
					}
				}
			});
			
			this.registerServerMessage(FLAG_MESSAGE_SERVER_PLAYER_CHANGED_MAP, ServerMessageMapChanged.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerMessageMapChanged serverMessageMapChanged = (ServerMessageMapChanged) pServerMessage;
					if(!Game.getUserID().equals(serverMessageMapChanged.getPlayerKey()) && Game.getPlayerHelper().getPlayer(serverMessageMapChanged.getPlayerKey()).getCurrentMap()==Game.getPlayerHelper().getOwnPlayer().getCurrentMap()){
						Game.getSceneManager().getGameScene().detachChild(Game.getPlayerHelper().getPlayer(serverMessageMapChanged.getPlayerKey()));
						Game.getSceneManager().getGameScene().unregisterTouchArea(Game.getPlayerHelper().getPlayer(serverMessageMapChanged.getPlayerKey()));//checkear si funciona ***
						Game.getPlayerHelper().getPlayer(serverMessageMapChanged.getPlayerKey()).setCurrentMap(serverMessageMapChanged.getMapID());
					}
				}
			});
				
			this.registerServerMessage(FLAG_MESSAGE_SERVER_MOVE_MOB, ServerMessageMoveMob.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerMessageMoveMob serverMessageMoveMob = (ServerMessageMoveMob) pServerMessage;
					//mover los mobs con esos datos
					Game.getMobHelper().getMob(serverMessageMoveMob.getMobKey()).moveToTile(Game.getMapManager().getTMXTileAt(serverMessageMoveMob.getX() * IMeasureConstants.TILE_SIZE, serverMessageMoveMob.getY() * IMeasureConstants.TILE_SIZE));
				}
			});
		
			this.registerServerMessage(FLAG_MESSAGE_SERVER_FIXED_ATTACK_DATA, ServerMessageFixedAttackData.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerMessageFixedAttackData serverMessageFixedAttackData = (ServerMessageFixedAttackData) pServerMessage;
					//simular el ataque con esos datos
					if(serverMessageFixedAttackData.isMonsterAttacking()){
						Game.getBattleHelper().displayAttack(Game.getMobHelper().getMob(serverMessageFixedAttackData.getMobID()), serverMessageFixedAttackData.getAttackID(), serverMessageFixedAttackData.getDamage(), Game.getPlayerHelper().getPlayer(serverMessageFixedAttackData.getPlayerKey()),serverMessageFixedAttackData.isMonsterAttacking());
					}else{
						Game.getBattleHelper().displayAttack(Game.getPlayerHelper().getPlayer(serverMessageFixedAttackData.getPlayerKey()), serverMessageFixedAttackData.getAttackID(), serverMessageFixedAttackData.getDamage(), Game.getMobHelper().getMob(serverMessageFixedAttackData.getMobID()),serverMessageFixedAttackData.isMonsterAttacking());
					}
				}
			});
			
			this.registerServerMessage(FLAG_MESSAGE_SERVER_MOB_DIED, ServerMessageMobDied.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerMessageMobDied serverMessageMobDied = (ServerMessageMobDied) pServerMessage;
					//simular la muerte
					Game.getBattleHelper().killMob(Game.getMobHelper().getMob(serverMessageMobDied.getMobEntityUserData()), serverMessageMobDied.getDroppedItem(),serverMessageMobDied.getDroppedAmount(), serverMessageMobDied.getExperience(), serverMessageMobDied.getMoney(), (Player)(Game.getPlayerHelper().getPlayer(serverMessageMobDied.getPlayerKey())));
					
				}
			});
			
			this.registerServerMessage(FLAG_MESSAGE_SERVER_SPAWN_MOB, ServerMessageSpawnMob.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerMessageSpawnMob serverMessageSpawnMob = (ServerMessageSpawnMob) pServerMessage;
					Game.getSceneManager().getGameScene().CreateMob(serverMessageSpawnMob.getMOB_FLAG(), serverMessageSpawnMob.getTileX(), serverMessageSpawnMob.getTileY(), serverMessageSpawnMob.getMap());
				}
			});
			
			this.registerServerMessage(FLAG_MESSAGE_SERVER_DISPLAY_AREA_ATTACK, ServerMessageDisplayAreaAttack.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerMessageDisplayAreaAttack serverMessageDisplayAreaAttack = (ServerMessageDisplayAreaAttack) pServerMessage;
					Attack tmpAttack = Game.getAttacksHelper().addNewAttack(serverMessageDisplayAreaAttack.getAttack_Flag());
					tmpAttack.setAnimationAtCenter(serverMessageDisplayAreaAttack.getTileX(),serverMessageDisplayAreaAttack.getTileY());
					Game.getSceneManager().getGameScene().getAttackLayer().add(tmpAttack);
				}
			});
			
		this.initMessagePool();
	}
	
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
		
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
		
	// ===========================================================
	// Methods
	// ===========================================================
		public void sendPingMessage(){			
			final ConnectionPingClientMessage connectionPingClientMessage = (ConnectionPingClientMessage) QClient.this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_CONNECTION_PING);
			connectionPingClientMessage.setTimestamp(System.currentTimeMillis());
			try {
				sendClientMessage(connectionPingClientMessage);				
			} catch (Exception e) {
				// TODO: handle exception
			}
			QClient.this.mMessagePool.recycleMessage(connectionPingClientMessage);
		}
		

		public void sendConnectionRequestMessage(final String pUserID,final String pUsername,final String pPassword,final String pMatchName){			
			final ClientMessageConnectionRequest clientMessageConnectionRequest = (ClientMessageConnectionRequest) QClient.this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_CONNECTION_REQUEST);
			clientMessageConnectionRequest.setUserID(pUserID);
			clientMessageConnectionRequest.setUsername(pUsername);
			clientMessageConnectionRequest.setPassword(pPassword);
			clientMessageConnectionRequest.setMatchName(pMatchName);
			try {
				sendClientMessage(clientMessageConnectionRequest);				
			} catch (Exception e) {
				// TODO: handle exception
			}
			QClient.this.mMessagePool.recycleMessage(clientMessageConnectionRequest);
		}
		
		public void sendPlayerCreate(int[] pChoices,String pUserID){			
			final ClientMessagePlayerCreate clientMessagePlayerCreate= (ClientMessagePlayerCreate) QClient.this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_PLAYER_CREATE);
			clientMessagePlayerCreate.setChoices(pChoices);
			clientMessagePlayerCreate.setUserID(pUserID);
			try {
				sendClientMessage(clientMessagePlayerCreate);				
			} catch (Exception e) {
				// TODO: handle exception
			}
			QClient.this.mMessagePool.recycleMessage(clientMessagePlayerCreate);
		}	
		
		public void sendSelectedPlayer(int pPlayerID){			
			final ClientMessageSelectedPlayer clientMessageSelectedPlayer= (ClientMessageSelectedPlayer) QClient.this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_SELECTED_PLAYER);
			clientMessageSelectedPlayer.setPlayerID(pPlayerID);
			try {
				sendClientMessage(clientMessageSelectedPlayer);				
			} catch (Exception e) {
				// TODO: handle exception
			}
			QClient.this.mMessagePool.recycleMessage(clientMessageSelectedPlayer);
		}	
		/*
		public void sendUnequipMessage(int pItemKey){
			final ClientMessageSelectedPlayer clientMessageSelectedPlayer= (ClientMessageSelectedPlayer) QClient.this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_SELECTED_PLAYER);
			clientMessageSelectedPlayer.setPlayerID(pPlayerID);
			try {
				sendClientMessage(clientMessageSelectedPlayer);				
			} catch (Exception e) {
				// TODO: handle exception
			}
			QClient.this.mMessagePool.recycleMessage(clientMessageSelectedPlayer);
		}*/
		
		public void sendMovePlayerMessage(String pPlayerKey, int pX,int pY){			
			final ClientMessageMovePlayer clientMessageMovePlayer = (ClientMessageMovePlayer) QClient.this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_MOVE_PLAYER);
			clientMessageMovePlayer.setPlayerKey(pPlayerKey);
			clientMessageMovePlayer.setX(pX);
			clientMessageMovePlayer.setY(pY);
			try {
				sendClientMessage(clientMessageMovePlayer);				
			} catch (Exception e) {
				// TODO: handle exception
			}
			QClient.this.mMessagePool.recycleMessage(clientMessageMovePlayer);
		}
		
		public void sendAttackMessage(int pAttackedMobID, int pAttackID){			
			final ClientMessageAttackMessage clientMessageAttackMessage = (ClientMessageAttackMessage) QClient.this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_ATTACK_MESSAGE);
			clientMessageAttackMessage.setAttackedMobID(pAttackedMobID);
			clientMessageAttackMessage.setAttackID(pAttackID);
			try {
				sendClientMessage(clientMessageAttackMessage);				
			} catch (Exception e) {
				// TODO: handle exception
			}
			QClient.this.mMessagePool.recycleMessage(clientMessageAttackMessage);
		}
		
		
		public void sendAreaAttackMessage(int pAttack_Flag, int pX,int pY){
				final ClientMessageAreaAttack clientMessageAreaAttack = (ClientMessageAreaAttack) QClient.this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_AREA_ATTACK_MESSAGE);
				clientMessageAreaAttack.setAttack_Flag(pAttack_Flag);
				clientMessageAreaAttack.setTileX(pX);
				clientMessageAreaAttack.setTileY(pY);
				try {
					sendClientMessage(clientMessageAreaAttack);				
				} catch (Exception e) {
					// TODO: handle exception
				}
				QClient.this.mMessagePool.recycleMessage(clientMessageAreaAttack);	
		}
		
		public void sendPlayerChangedMap(String pPlayerKey, int pMapID){
			final ClientMessageChangeMap clientMessageChangeMap = (ClientMessageChangeMap) QClient.this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_PLAYER_CHANGED_MAP);
			clientMessageChangeMap.setPlayerKey(pPlayerKey);
			clientMessageChangeMap.setMapID(pMapID);
			try {
				sendClientMessage(clientMessageChangeMap);				
			} catch (Exception e) {
				// TODO: handle exception
			}
			QClient.this.mMessagePool.recycleMessage(clientMessageChangeMap);	
	}
		
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}