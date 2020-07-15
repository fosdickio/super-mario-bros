package com.mario.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mario.game.MarioGame;
import com.mario.game.Sprites.Mario;
import com.mario.game.Sprites.Enemies.Enemy;
import com.mario.game.Sprites.Items.Item;
import com.mario.game.Sprites.Other.Fireball;
import com.mario.game.Sprites.Other.Flag;
import com.mario.game.Sprites.TileObjects.InteractiveTileObject;



public class WorldContactListener implements ContactListener{
	//what gets called when two objects collide with each other
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		// Or the category bits of fixA and fixB together
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		
		switch (cDef) {
		
		case MarioGame.MARIO_HEAD_BIT | MarioGame.BRICK_BIT:
        case MarioGame.MARIO_HEAD_BIT | MarioGame.COIN_BIT:
        //case MarioGame.MARIO_HEAD_BIT | MarioGame.FLAG_BIT:
            if(fixA.getFilterData().categoryBits == MarioGame.MARIO_HEAD_BIT)
                ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
            else
                ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
            break;
		
		case MarioGame.ENEMY_HEAD_BIT | MarioGame.MARIO_BIT:
			if(fixA.getFilterData().categoryBits == MarioGame.ENEMY_HEAD_BIT)
				((Enemy)fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
			else 
				((Enemy)fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
			break;
			
		case MarioGame.ENEMY_BIT | MarioGame.OBJECT_BIT:
			if(fixA.getFilterData().categoryBits == MarioGame.ENEMY_BIT)
				((Enemy)fixA.getUserData()).reverseVelocity(true, false);
			else 
				((Enemy)fixB.getUserData()).reverseVelocity(true, false);
			break;
			
		case MarioGame.MARIO_BIT | MarioGame.ENEMY_BIT:
			//Gdx.app.log("MARIO", "DIED");
			if(fixA.getFilterData().categoryBits == MarioGame.MARIO_BIT)
                ((Mario) fixA.getUserData()).hit((Enemy)fixB.getUserData());
            else
            	((Mario) fixB.getUserData()).hit((Enemy)fixA.getUserData());
            break;
			
		case MarioGame.ENEMY_HEAD_BIT | MarioGame.ENEMY_HEAD_BIT:
		case MarioGame.ENEMY_BIT | MarioGame.ENEMY_BIT:
			((Enemy)fixA.getUserData()).reverseVelocity(true, false);
			((Enemy)fixB.getUserData()).reverseVelocity(true, false);
			break;
			
		case MarioGame.ITEM_BIT | MarioGame.OBJECT_BIT:
			if(fixA.getFilterData().categoryBits == MarioGame.ITEM_BIT)
				((Item)fixA.getUserData()).reverseVelocity(true, false);
			else 
				((Item)fixB.getUserData()).reverseVelocity(true, false);
			break;
			
		case MarioGame.ITEM_BIT | MarioGame.MARIO_BIT:
			if(fixA.getFilterData().categoryBits == MarioGame.ITEM_BIT)
				((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
			else 
				((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
			break;
			
		case MarioGame.FIREBALL_BIT | MarioGame.ENEMY_BIT:
		case MarioGame.FIREBALL_BIT | MarioGame.ENEMY_HEAD_BIT:
			Gdx.app.log("Fireball and enemy", "Listener");
			if(fixA.getFilterData().categoryBits == MarioGame.FIREBALL_BIT){
				((Enemy)fixB.getUserData()).hitByFire((Fireball) fixA.getUserData());
			}
			else {
				((Enemy)fixA.getUserData()).hitByFire((Fireball) fixB.getUserData());
			}
			break;
			
		case MarioGame.FIREBALL_BIT | MarioGame.OBJECT_BIT:
			Gdx.app.log("Fireball and object", "Listener");
			if(fixA.getFilterData().categoryBits == MarioGame.FIREBALL_BIT)
				((Fireball)fixA.getUserData()).setToDestroy();
			else
				((Fireball)fixB.getUserData()).setToDestroy();
			break;
		

		case MarioGame.FLAG_BIT | MarioGame.MARIO_BIT:
			Gdx.app.log("Flag and Mario", "Listener");
			if(fixA.getFilterData().categoryBits == MarioGame.MARIO_BIT || fixA.getFilterData().categoryBits == MarioGame.MARIO_HEAD_BIT)
				((Mario)fixA.getUserData()).hitFlag();
			else
				((Mario)fixB.getUserData()).hitFlag();
			
			break;
		
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
}
