package com.mario.game.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mario.game.MarioGame;
import com.mario.game.Scenes.Hud;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Mario;

public class Brick extends InteractiveTileObject{
	public Brick(PlayScreen screen, MapObject object){
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(MarioGame.BRICK_BIT);
	}

	@Override
	public void onHeadHit(Mario mario) {
		if(mario.isBig() || mario.isFire()){
			Gdx.app.log("Brick", "Collision");
			setCategoryFilter(MarioGame.DESTROYED_BIT);
			getCell().setTile(null);
			Hud.addScore(200);
			MarioGame.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
		}
		MarioGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
	}
}
