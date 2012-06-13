/**
 * 
 */
package com.quest.entities;

import java.util.Random;

import com.quest.game.Game;

/**
 * @author raccoon
 *
 */
//public class Enemy extends Entity implements IOnScreenControlListener{
public class Mob extends BaseEntity{
	
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private Random rand;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public Mob() {
		// TODO Auto-generated constructor stub
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void randomPath() 
	{		
		if(!this.isWalking) 
		{	boolean move = false;
			float moveToXTile = this.getX();
			float moveToYTile = this.getY();
			switch(this.getRandom(1, 10))
			{
			case 1://Arriba
				 moveToXTile = this.getX();
				 moveToYTile = this.getY() - 32;
				 move = true;
				break;
			case 2://Abajo
				 moveToXTile = this.getX();
				 moveToYTile = this.getY() + 32;
				 move = true;
				break;
			case 3://Derecha
				 moveToXTile = this.getX() + 32;
				 moveToYTile = this.getY();
				 move = true;
				break;
			case 4://Izquierda
				 moveToXTile = this.getX() - 32;
				 moveToYTile = this.getY();
				 move = true;
				break;			
			case 5://Arriba 2
				 moveToXTile = this.getX();
				 moveToYTile = this.getY() - 64;
				 move = true;
				break;
			case 6://Abajo 2
				 moveToXTile = this.getX();
				 moveToYTile = this.getY() + 64;
				 move = true;
				 break;
			case 7://Derecha 2
				 moveToXTile = this.getX() + 64;
				 moveToYTile = this.getY();
				 move = true;
				 break;
			case 8://Izquierda 2
				 moveToXTile = this.getX() - 32;
				 moveToYTile = this.getY();
				 move = true;
				 break;
			case 9:
				//nada, se queda en el lugar
				 moveToXTile = this.getX();
				 moveToYTile = this.getY();
				 move = false;
				 break;
			case 10:
				//nada, se queda en el lugar
				 moveToXTile = this.getX();
				 moveToYTile = this.getY();
				 move = false;
				break;
			}		
			if(move == true)
			{
				this.moveToTile(moveToXTile, moveToYTile, 1.0f);
			}
		}		
	}
	
	private int getRandom(int min, int max)
	{
		rand = new Random();	
		int RandomNum = rand.nextInt(max - min + 1) + min;
		return RandomNum;
	}
	

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
}
