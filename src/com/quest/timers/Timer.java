package com.quest.timers;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import com.quest.entities.Mob;
import com.quest.game.Game;
import com.quest.timers.interfaces.ITimerAction;

// TODO Mover a una clase principal que los dem�s timers hereden de esto
// 		usando un updatehandler

public class Timer {
		// ===========================================================
		// Constants
		// ===========================================================
		
		
		// ===========================================================
		// Fields
		// ===========================================================
		private ITimerCallback mTimerCallback;
	
		// ===========================================================
		// Constructors
		// ===========================================================
	
		public Timer(float pTimePerTick, ITimerCallback iTimerCallback) {
			
			Game.getInstance().getEngine().registerUpdateHandler(new TimerHandler(pTimePerTick, true, iTimerCallback));
		}
	
	
		// ===========================================================
		// Getter & Setter
		// ===========================================================
		/**
		 * @return the mTimerCallback
		 */
		public ITimerCallback getTimerCallback() {
			return mTimerCallback;
		}


		/**
		 * @param mTimerCallback the mTimerCallback to set
		 */
		public void setTimerCallback(ITimerCallback mTimerCallback) {
			this.mTimerCallback = mTimerCallback;
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
