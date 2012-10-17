package com.quest.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import com.quest.timers.Timer;

public class TimerHelper {

	// ===========================================================
	// Constants
	// ===========================================================
	
	
	// ===========================================================
	// Fields
	// ===========================================================
	private HashMap<Timer, String> mTimersList;

	
	// ===========================================================
	// Constructors
	// ===========================================================
	public TimerHelper() {
		
		this.mTimersList = new HashMap<Timer, String>();
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	/**
	 * @return the mTimersList
	 */
	public HashMap<Timer, String> getTimersList() {
		return mTimersList;
	}

	/**
	 * @param mTimersList the mTimersList to set
	 */
	public void setTimersList(HashMap<Timer, String> mTimersList) {
		this.mTimersList = mTimersList;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	
	public void addTimer(Timer pTimer, String pKey) {
		this.mTimersList.put(pTimer, pKey);
	}
	
	public void deleteTimer(String pKey) {
		if(!this.getTimer(pKey).equals(null)) {
			this.mTimersList.remove(this.getTimer(pKey));
		} else {
			Log.d("Quest!", "TimerHelper: Erase - No Timer matches key");
		}
	}
	
	public Timer getTimer(String pKey) {
		/*for(Timer tmpTimer : this.mTimersList.) {
			if(tmpTimer.getUserData().equals(pKey))
				return tmpTimer;
		}*/ // falta hacer el get de cada timer, sino funciona todo
		Log.e("Quest!", "PlayerHelper: Search - No Timer matches key");
		return null;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}