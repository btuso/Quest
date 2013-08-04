package com.quest.utils;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONMaker {
	public static String hashMapToJSONArrayString(HashMap<String,String> map){
		StringBuilder builder = new StringBuilder();
		Iterator<String> it = map.keySet().iterator();
		
		builder.append("[{");
		while(it.hasNext()){
			String key = it.next();
			builder.append("\"");
			builder.append(key);
			builder.append("\":\"");
			builder.append(map.get(key));
			builder.append("\",");
		}
		builder.deleteCharAt(builder.length());
		builder.append("}]");
		
		return builder.toString();
	}
	
	public static JSONArray hashMapToJSONArray(HashMap<String, String> map) throws JSONException{
		return new JSONArray(hashMapToJSONArrayString(map));
	}
}
