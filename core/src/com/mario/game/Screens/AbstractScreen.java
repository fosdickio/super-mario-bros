package com.mario.game.Screens;

import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mario.game.MarioGame;
import com.mario.game.Sprites.Mario;
import com.mario.game.Sprites.Items.Item;
import com.mario.game.Sprites.Items.ItemDef;
import com.mario.game.Tools.B2WorldCreator;

public abstract class AbstractScreen implements Screen{
	MarioGame game;
	protected Viewport gameport;
	protected World world;
	protected Array<Item> items;
	protected LinkedBlockingQueue<ItemDef> itemsToSpawn;
	
	public AbstractScreen(MarioGame game){
		this.game = game;
		gameport = new FitViewport(MarioGame.V_WIDTH, MarioGame.V_HEIGHT, new OrthographicCamera());
		
		
		items = new Array<Item>();
		itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	public void spawnItem(ItemDef idef){
		itemsToSpawn.add(idef);
	}
	
	
	
	
}
