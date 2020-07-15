package com.mario.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Enemies.Enemy;
import com.mario.game.Sprites.Enemies.Goomba;
import com.mario.game.Sprites.Enemies.Turtle;
import com.mario.game.Sprites.Other.Flag;
import com.mario.game.Sprites.TileObjects.Brick;
import com.mario.game.Sprites.TileObjects.Coin;


public class B2WorldCreator {
	
	private Array<Goomba> goombas;
	private Array<Turtle> turtles;
	
	public B2WorldCreator(PlayScreen screen){
		World world = screen.getWorld();
		TiledMap map = screen.getMap();
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;
		
		// Create ground bodies/fixtures
		for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioGame.PPM, (rect.getY() + rect.getHeight() / 2) / MarioGame.PPM);
			
			body = world.createBody(bdef);
			
			shape.setAsBox(rect.getWidth() / 2 / MarioGame.PPM, rect.getHeight() / 2 / MarioGame.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}
		
		// Create pipe bodies/fixtures
		for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioGame.PPM, (rect.getY() + rect.getHeight() / 2) / MarioGame.PPM);
			
			body = world.createBody(bdef);
			
			shape.setAsBox(rect.getWidth() / 2 / MarioGame.PPM, rect.getHeight() / 2 / MarioGame.PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = MarioGame.OBJECT_BIT;
			body.createFixture(fdef);
		}
		
		// Create flag body/fixture
		for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
			new Flag(screen, object);
			
		}
		
		// Create bricks bodies/fixtures
		for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
			new Brick(screen, object);
		}
		
		// Create coin bodies/fixtures
		for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			new Coin(screen, object);
		}
		
		//Create Goombas
		goombas = new Array<Goomba>();
		for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			goombas.add(new Goomba(screen, rect.getX() / MarioGame.PPM, rect.getY() / MarioGame.PPM));
		}
		
		//Create Turtle
		turtles = new Array<Turtle>();
		for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			turtles.add(new Turtle(screen, rect.getX() / MarioGame.PPM, rect.getY() / MarioGame.PPM));
		}
		
		
	}

	public Array<Enemy> getEnemies(){
		Array<Enemy> enemies = new Array<Enemy>();
		enemies.addAll(goombas);
		enemies.addAll(turtles);
		
		return enemies;
	}
	
	public Array<Goomba> getGoombas() {
		return goombas;
	}
	
	public Array<Turtle> getTurtles() {
		return turtles;
	}
}
