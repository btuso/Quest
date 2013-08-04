package com.quest.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import com.quest.data.ProfileData;
import com.quest.network.messages.server.externalserver.DefaultClientMessage;
import com.quest.utils.JSONMaker;

public class Client implements messageCalls, JSONClientMessageFlags {
	
	private static String MESSAGE_HEADER = "header";
	
	Socket connection;
	
	
	public Client(final String ip, final Integer port) {
		Thread connectionThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					connection = new Socket(ip, port);
					startListening(connection);
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		connectionThread.start();
	}
	
	private void startListening(final Socket socket) {
		Runnable listener = new Runnable() {
			
			@Override
			public void run() {
				while(true){
				    BufferedReader in;
					try {
						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String result = "";
				        String inputLine;
						while ((inputLine = in.readLine()) != null) {
							result = result.concat(inputLine);  
							processMessage(new JSONArray(result));
				        }
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(listener).start();
	}

	private void processMessage(JSONArray message) throws JSONException, InstantiationException, IllegalAccessException {
		String header = message.getJSONObject(0).getString(MESSAGE_HEADER);
		DefaultClientMessage messageInstance = Messages.valueOf(header).getRunnableClass().newInstance();
		messageInstance.readData(message);
		new Thread(messageInstance).start();
	}

	private void sendMessage(final Socket socket, final HashMap<String, String> message) throws IOException {
		final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		Runnable sender = new Runnable() {
			
			@Override
			public void run() {
				JSONMaker.hashMapToJSONArrayString(message);
				out.println(message);
				out.close();
			}
		};
		new Thread(sender).start();
	}
	
	
	//MESSAGES TO SEND
	@Override
	public void sendServerConnectionRequestMessage(ProfileData profileData) {
		HashMap<String, String> message = new HashMap<String, String>();
		message.put(HEADER,HEADER_CONNECTION_REQUEST);
		message.put(USER_ID,profileData.getUserID());
		message.put(USERNAME, profileData.getUsername());
		try {
			sendMessage(connection, message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
