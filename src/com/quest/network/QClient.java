package com.quest.network;

import java.io.IOException;
import java.net.Socket;

import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector.ISocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;

import android.util.Log;

import com.quest.game.Game;
import com.quest.network.messages.client.ClientMessageFlags;
import com.quest.network.messages.server.ConnectionCloseServerMessage;
import com.quest.network.messages.server.ConnectionEstablishedServerMessage;
import com.quest.network.messages.server.ConnectionPongServerMessage;
import com.quest.network.messages.server.ConnectionRejectedProtocolMissmatchServerMessage;
import com.quest.network.messages.server.ServerMessageConnectionEstablished;
import com.quest.network.messages.server.ServerMessageFlags;
import com.quest.network.messages.server.UpdateEntityPositionServerMessage;
import com.quest.util.constants.MessageConstants;

public class QClient extends ServerConnector<SocketConnection> implements ClientMessageFlags, ServerMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================
	static int SERVER_PORT = 4444;
	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

		public QClient(final String pServerIP, final ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
			super(new SocketConnection(new Socket(pServerIP, SERVER_PORT)), pSocketConnectionServerConnectorListener);
	
			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					Log.d("Quest!","(QClient)CLIENT: Connection terminated.");
					Game.getInstance().finish();				
				}
			});
	
			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED1, ServerMessageConnectionEstablished.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					Log.d("Quest!","CLIENT LLEGO EL MENSAJE DEL SERVER");
					final ServerMessageConnectionEstablished serverMessageConnectionEstablished = (ServerMessageConnectionEstablished) pServerMessage;
					final boolean characterexists = serverMessageConnectionEstablished.getCharacterExists();
					Log.d("Quest!","Client - "+String.valueOf(characterexists));
				}
			});
			
			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED, ConnectionEstablishedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					Log.d("Quest!","(QClient)CLIENT: Connection established.");
				}
			});
	
			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH, ConnectionRejectedProtocolMissmatchServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ConnectionRejectedProtocolMissmatchServerMessage connectionRejectedProtocolMissmatchServerMessage = (ConnectionRejectedProtocolMissmatchServerMessage)pServerMessage;
					if(connectionRejectedProtocolMissmatchServerMessage.getProtocolVersion() > MessageConstants.PROTOCOL_VERSION) {
						//						Toast.makeText(context, text, duration).show();
					} else if(connectionRejectedProtocolMissmatchServerMessage.getProtocolVersion() < MessageConstants.PROTOCOL_VERSION) {
						//						Toast.makeText(context, text, duration).show();
					}
					Log.d("Quest!","(QClient)CLIENT: Connection rejected.");
					Game.getInstance().finish();				
				}
			});
	
			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PONG, ConnectionPongServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ConnectionPongServerMessage connectionPongServerMessage = (ConnectionPongServerMessage) pServerMessage;
					final long roundtripMilliseconds = System.currentTimeMillis() - connectionPongServerMessage.getTimestamp();
					Log.d("Quest!","Ping: " + roundtripMilliseconds / 2 + "ms");
				}
			});
			
			this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_ENTITY_POSITION, UpdateEntityPositionServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final UpdateEntityPositionServerMessage updateEntityPosition = (UpdateEntityPositionServerMessage) pServerMessage;
					// mandarle nueva posicion a todos
					/* final long roundtripMilliseconds = System.currentTimeMillis() - connectionPongServerMessage.getTimestamp();
					Log.d("Quest!","Ping: " + roundtripMilliseconds / 2 + "ms"); */
				}
			});
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}