package com.mario.game.Sprites.Other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Mario;
import com.mario.game.Sprites.TileObjects.InteractiveTileObject;

public class Flag extends InteractiveTileObject {

	public Flag(PlayScreen screen, MapObject object) {
		
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(MarioGame.FLAG_BIT);
		Gdx.app.log("Flag", "Created");
	}

	@Override
	public void onHeadHit(Mario mario) {
		Gdx.app.log("Flag", "onHeadHit");
		mario.hitFlag();
		
	}

	
}
