package com.mario.game.Screens;

import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mario.game.MarioGame;
import com.mario.game.Scenes.Hud;
import com.mario.game.Sprites.Mario;
import com.mario.game.Sprites.Enemies.Enemy;
import com.mario.game.Sprites.Items.Flower;
import com.mario.game.Sprites.Items.Item;
import com.mario.game.Sprites.Items.ItemDef;
import com.mario.game.Sprites.Items.Mushroom;
import com.mario.game.Tools.B2WorldCreator;
import com.mario.game.Tools.ScreenManager;
import com.mario.game.Tools.WorldContactListener;
import com.mario.game.Tools.ScreenManager.ScreenEnum;

public class PlayScreen extends AbstractScreen {	
	//private MarioGame game;
	private OrthographicCamera gamecam;
	//private Viewport gameport;
	private Hud hud;
	
	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	//private World world;
	private Box2DDebugRenderer b2dr;
	private B2WorldCreator creator;
	
	private Mario player;
	
	private TextureAtlas atlas;
	
	private Music music; 
	
	//private Array<Item> items;
	//private LinkedBlockingQueue<ItemDef> itemsToSpawn;
	
	public PlayScreen(MarioGame game, String level) {
		super(game);
		//this.game = game;
		
		atlas = new TextureAtlas("mario_all.pack");
		gamecam = new OrthographicCamera();
		gameport = new FitViewport(MarioGame.V_WIDTH / MarioGame.PPM, MarioGame.V_HEIGHT / MarioGame.PPM, gamecam);
		hud = new Hud(game.batch);
		//String level = "level_1-2.tmx";
		maploader = new TmxMapLoader();
		map = maploader.load(level);
		renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioGame.PPM);
		gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);
		
		
		world = new World(new Vector2(0, -10), true);
		
		player = new Mario(this);
		
		creator = new B2WorldCreator(this);
		
		world.setContactListener(new WorldContactListener());
		
		music = MarioGame.manager.get("audio/music/mario_music.ogg", Music.class);
		music.setLooping(true);
		music.play();
		
		//items = new Array<Item>();
		//itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
		
	}
	
/*	public void spawnItem(ItemDef idef){
		itemsToSpawn.add(idef);
	}*/
	
	public void handleSpawnItems(){
		if(!itemsToSpawn.isEmpty()){
			ItemDef idef = itemsToSpawn.poll();
			if(idef.type == Mushroom.class){
				items.add(new Mushroom(this, idef.position.x, idef.position.y));
			}
			if(idef.type == Flower.class){
				items.add(new Flower(this, idef.position.x, idef.position.y));
			}
		}
	}
	
	public TextureAtlas getAtlas() {
		return atlas;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	
	public void handleInput(float dt) {
		
		if(player.currentState != Mario.State.DEAD) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && (player.getState() != Mario.State.JUMPING))
				player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
				player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
				player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
			if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
				player.shootFire();
			
		}
	}
	
	public void update(float dt) {
		handleInput(dt);
		handleSpawnItems();
		
		world.step(1/60f, 6, 2);
		player.update(dt);
		for (Enemy enemy : creator.getEnemies()){
			enemy.update(dt);
			//wake enemy up about 14 blocks away
			if(enemy.getX() < player.getX() + 224 / MarioGame.PPM)
				enemy.b2body.setActive(true);
		}
		
		for (Item item : items)
			item.update(dt);
		
		hud.update(dt);
		
		if(player.currentState != Mario.State.DEAD) {
			gamecam.position.x = MathUtils.clamp(player.b2body.getPosition().x, gameport.getWorldWidth() / 2, (227 * 16) / MarioGame.PPM);
			//gamecam.position.x = player.b2body.getPosition().x;
			
		}
		
		
		gamecam.update();
		
		renderer.setView(gamecam);
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.render();
		
		game.batch.setProjectionMatrix(gamecam.combined);
		game.batch.begin();
		player.draw(game.batch);
		for (Enemy enemy : creator.getEnemies()){
			enemy.draw(game.batch);
		}
		for(Item item : items){
			item.draw(game.batch);
		}
		game.batch.end();
		
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		
		if(gameOver()) {
			ScreenManager.getInstance().showScreen(ScreenEnum.GAME_OVER);
			dispose();
		}
		if(gameWin()){
			ScreenManager.getInstance().showScreen(ScreenEnum.LEVEL_1_2);
			dispose();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		gameport.update(width, height);
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	public World getWorld() {
		return world;
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
		map.dispose();
		world.dispose();
		renderer.dispose();
		hud.dispose();
	}

	public boolean gameOver(){
		if(player.currentState == Mario.State.DEAD && player.getStateTimer() > 3)
			return true;
		else
			return false;
	}
	
	public boolean gameWin(){
		if(player.currentState == Mario.State.WINNING && player.getStateTimer() > 6){
			Gdx.app.log("PlayScreen", "gameWin");
			return true;
		}
		else
			return false;
	}

}
