package com.mario.game.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mario.game.MarioGame;
import com.mario.game.Scenes.Hud;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Mario;
import com.mario.game.Sprites.Items.Flower;
import com.mario.game.Sprites.Items.ItemDef;
import com.mario.game.Sprites.Items.Mushroom;

public class Coin extends InteractiveTileObject{
	
	private static TiledMapTileSet tileSet;
	private final int BLANK_COIN = 28;
	
	public Coin(PlayScreen screen, MapObject object){
		super(screen, object);
		tileSet = map.getTileSets().getTileSet("tileset-mario");
		fixture.setUserData(this);
		setCategoryFilter(MarioGame.COIN_BIT);
	}

	@Override
	public void onHeadHit(Mario mario) {
		//Gdx.app.log("Coin", "Collision");
		if(getCell().getTile().getId() == BLANK_COIN) {
			MarioGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
			Gdx.app.log("blank", "Collision");
		}
		else {
			if(object.getProperties().containsKey("mushroom")){
				Gdx.app.log("Mushroom", "Collision");
				screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioGame.PPM), Mushroom.class));
				MarioGame.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
			}
			if (object.getProperties().containsKey("flower")){
				Gdx.app.log("Flower", "Collision");
				screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioGame.PPM), Flower.class));
				MarioGame.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
			}
			else
			MarioGame.manager.get("audio/sounds/coin.wav", Sound.class).play();
			Gdx.app.log("GotCoin", "Collision");
			
		}
		getCell().setTile(tileSet.getTile(BLANK_COIN));
		Hud.addScore(300);
		
	}
	
	
}
