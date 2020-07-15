package com.mario.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Enemies.Enemy;
import com.mario.game.Sprites.Enemies.Turtle;
import com.mario.game.Sprites.Other.Fireball;
import com.mario.game.Sprites.Other.Flag;

public class Mario extends Sprite {

	public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD, FLOWERING, WINNING };
	
	public State currentState;
	public State previousState;
	
	public World world;
	public Body b2body;
	private PlayScreen screen;
	
	private TextureRegion marioStand;
	private TextureRegion marioJump;
	private TextureRegion bigMarioStand;
	private TextureRegion bigMarioJump;
	private TextureRegion fireMarioJump;
	private TextureRegion fireMarioStand;
	private TextureRegion marioDead;
	
	private Animation marioRun;
	private Animation bigMarioRun;
	private Animation growMario;
	private Animation fireMarioRun;
	private Animation changeFireMario;
	private float stateTimer;
	private boolean runningRight;
	private boolean marioIsBig;
	private boolean marioIsFire;
	private boolean runGrowAnimation;
	private boolean timeToDefineBigMario;
	private boolean timeToRedefineMario;
	private boolean marioIsDead;
	
	private boolean timeToDefineFireMario;
	private boolean runFireAnimation;
	
	private Array<Fireball> fireballs;
	private boolean marioWins;
	
		
	public Mario(PlayScreen screen) {
	
		this.world = screen.getWorld();
		this.screen = screen;
		
		currentState = State.STANDING;
		previousState = State.STANDING;
		stateTimer = 0;
		runningRight = true;
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		
		//Run Animation
		for (int i = 1; i < 4; i++){
			frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
		}
		marioRun = new Animation(0.1f, frames);
		frames.clear();
		
		for (int i = 1; i < 4; i++){
			frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
		}
		bigMarioRun = new Animation(0.1f, frames);
		frames.clear();
		
		for(int i = 1; i < 4; i++){
			frames.add(new TextureRegion(screen.getAtlas().findRegion("fire_mario"), i * 16, 0,  16, 32));
		}
		fireMarioRun = new Animation(0.1f, frames);
		frames.clear();
		
		//Grow Mario
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
		growMario = new Animation(0.2f, frames);
		frames.clear();
		
		//Become fire Mario
		frames.add(new TextureRegion(screen.getAtlas().findRegion("fire_mario"), 240, 0, 16, 32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("fire_mario"), 0, 0, 16, 32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("fire_mario"), 240, 0, 16, 32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("fire_mario"), 0, 0, 16, 32));
		changeFireMario = new Animation(0.2f, frames);
		frames.clear();
		
		//Mario Jump
		marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
		bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);
		fireMarioJump = new TextureRegion(screen.getAtlas().findRegion("fire_mario"), 80, 0, 16, 32);
		
		//Mario Stand
		marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
		bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);
		fireMarioStand = new TextureRegion(screen.getAtlas().findRegion("fire_mario"), 0, 0, 16, 32);
		
		//Mario Dead
		marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);
		
		defineMario();
		setBounds(0,0, 16 / MarioGame.PPM, 16 / MarioGame.PPM);
		setRegion(marioStand);
		
		fireballs = new Array<Fireball>();
	}
	
	public void update(float dt) {
		if(marioIsBig || marioIsFire){
			setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight() / 2 - 6  / MarioGame.PPM);
		}
		else
			setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight() / 2);
		
		setRegion(getFrame(dt));
		if(timeToDefineBigMario){
			defineBigMario();
		}
		if(timeToRedefineMario) {
			redefineMario();
		}
		if(timeToDefineFireMario){
			defineFireMario();
		}
		if(b2body.getPosition().y < 0 && !marioIsDead){
			MarioGame.manager.get("audio/music/mario_music.ogg", Music.class).stop();
			MarioGame.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
			marioIsDead = true;
			Filter filter = new Filter();
			filter.maskBits = MarioGame.NOTHING_BIT;
			//set all b2bodys in game to zero bit so Mario can fall off screen
			for(Fixture fixture : b2body.getFixtureList())
				fixture.setFilterData(filter);
			b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
		}
		/*if(b2body.getPosition().x > (200 * 16) / MarioGame.PPM && !marioIsDead && !marioWins){
			MarioGame.manager.get("audio/music/mario_music.ogg", Music.class).stop();
			MarioGame.manager.get("audio/sounds/smb_world_clear.wav", Sound.class).play();
			hitFlag();
		}*/
		
		for(Fireball ball : fireballs) {	
			if(ball.isDestroyed())
				fireballs.removeValue(ball, true);
			else
				ball.update(dt);
		}
	}
		
	public void defineMario() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(32 / MarioGame.PPM, 32 / MarioGame.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
			
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / MarioGame.PPM);
		
		fdef.filter.categoryBits = MarioGame.MARIO_BIT;
		fdef.filter.maskBits = MarioGame.GROUND_BIT 
				| MarioGame.BRICK_BIT 
				| MarioGame.COIN_BIT
				| MarioGame.ENEMY_BIT
				| MarioGame.OBJECT_BIT
				| MarioGame.ITEM_BIT
				| MarioGame.ENEMY_HEAD_BIT
				| MarioGame.FLAG_BIT;
			
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		
		//edgeshape is a line between two points
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / MarioGame.PPM, 6 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, 6 / MarioGame.PPM));
		fdef.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
		fdef.shape = head;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData(this);
	}
	
	public void redefineMario(){
		Vector2 position = b2body.getPosition();
		world.destroyBody(b2body);
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(position);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
			
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / MarioGame.PPM);
		
		fdef.filter.categoryBits = MarioGame.MARIO_BIT;
		fdef.filter.maskBits = MarioGame.GROUND_BIT 
				| MarioGame.BRICK_BIT 
				| MarioGame.COIN_BIT
				| MarioGame.ENEMY_BIT
				| MarioGame.OBJECT_BIT
				| MarioGame.ITEM_BIT
				| MarioGame.ENEMY_HEAD_BIT
				| MarioGame.FLAG_BIT;
			
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		
		//edgeshape is a line between two points
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / MarioGame.PPM, 6 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, 6 / MarioGame.PPM));
		fdef.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
		fdef.shape = head;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData(this);
		
		timeToRedefineMario = false;
	}
	
	public void defineBigMario(){
		Vector2 currentPosition = b2body.getPosition();
		world.destroyBody(b2body);
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(currentPosition.add(0, 10 / MarioGame.PPM));
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
			
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / MarioGame.PPM);
		
		fdef.filter.categoryBits = MarioGame.MARIO_BIT;
		fdef.filter.maskBits = MarioGame.GROUND_BIT 
				| MarioGame.BRICK_BIT 
				| MarioGame.COIN_BIT
				| MarioGame.ENEMY_BIT
				| MarioGame.OBJECT_BIT
				| MarioGame.ITEM_BIT
				| MarioGame.ENEMY_HEAD_BIT
				| MarioGame.FLAG_BIT;
			
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		shape.setPosition(new Vector2(0, -14 / MarioGame.PPM));
		b2body.createFixture(fdef).setUserData(this);
		
		//edgeshape is a line between two points
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / MarioGame.PPM, 6 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, 6 / MarioGame.PPM));
		fdef.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
		fdef.shape = head;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData(this);
		timeToDefineBigMario = false;
	}
	
	public void defineFireMario(){
		Vector2 currentPosition = b2body.getPosition();
		world.destroyBody(b2body);
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(currentPosition.add(0, 10 / MarioGame.PPM));
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
			
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / MarioGame.PPM);
		
		fdef.filter.categoryBits = MarioGame.MARIO_BIT;
		fdef.filter.maskBits = MarioGame.GROUND_BIT 
				| MarioGame.BRICK_BIT 
				| MarioGame.COIN_BIT
				| MarioGame.ENEMY_BIT
				| MarioGame.OBJECT_BIT
				| MarioGame.ITEM_BIT
				| MarioGame.ENEMY_HEAD_BIT
				| MarioGame.FLAG_BIT;
			
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		shape.setPosition(new Vector2(0, -14 / MarioGame.PPM));
		b2body.createFixture(fdef).setUserData(this);
		
		//edgeshape is a line between two points
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / MarioGame.PPM, 6 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, 6 / MarioGame.PPM));
		fdef.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
		fdef.shape = head;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData(this);
		timeToDefineFireMario = false;
	}
	
	public TextureRegion getFrame(float dt){
		currentState = getState();
		
		TextureRegion region;
		switch(currentState){
		
		case DEAD:
			region = marioDead;
			break;
		case GROWING:
			region = growMario.getKeyFrame(stateTimer);
			if(growMario.isAnimationFinished(stateTimer))
				runGrowAnimation = false;
			break;
		case FLOWERING:
			region = changeFireMario.getKeyFrame(stateTimer);
			if(changeFireMario.isAnimationFinished(stateTimer))
				runFireAnimation = false;
		case JUMPING:
			if(marioIsBig && !marioIsFire)
				region = bigMarioJump;
			else if(marioIsFire)
				region = fireMarioJump;
			else
				region = marioJump;
			break;
		case RUNNING:
			if(marioIsBig  && !marioIsFire)
				region = bigMarioRun.getKeyFrame(stateTimer, true);
			else if(marioIsFire)
				region = fireMarioRun.getKeyFrame(stateTimer, true);
			else
				region = marioRun.getKeyFrame(stateTimer, true);
			break;
		case WINNING:
		case FALLING:
		case STANDING:
		default:
			if(marioIsBig && !marioIsFire)
				region = bigMarioStand;
			else if(marioIsFire)
				region = fireMarioStand;
			else
				region = marioStand;
			break;
		}
		
		if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
			region.flip(true, false);
			runningRight = false;
		}
		else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}
		
		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}
	
	public State getState(){
		if(marioIsDead)
			return State.DEAD;
		else if(marioWins){
			//Gdx.app.log("Mario State", "Winning");
			return State.WINNING;
		}
		else if(runGrowAnimation)
			return State.GROWING;
		else if(runFireAnimation){
			//Gdx.app.log("Mario", "Fire Animation");
			return State.FLOWERING;
		}
		else if(b2body.getLinearVelocity().y > 0 || b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)
			return State.JUMPING;
		else if(b2body.getLinearVelocity().y < 0)
			return State.FALLING;
		else if(b2body.getLinearVelocity().x != 0)
			return State.RUNNING;
		else
			return State.STANDING;
	}
	
	public void grow(){
		if(!marioIsBig && !marioIsFire){
			//Gdx.app.log("Mario", "Grow");
			runGrowAnimation = true;
			marioIsBig = true;
			timeToDefineBigMario = true;
			setBounds(getX(), getY(), getWidth(), getHeight() * 2);
			MarioGame.manager.get("audio/sounds/powerup.wav", Sound.class).play();
		}
	}
	
	public void fire() {
		if(!marioIsFire){
			runFireAnimation = true;
			marioIsFire = true;
			if(!marioIsBig){
				setBounds(getX(), getY(), getWidth(), getHeight() * 2);
			}
			//marioIsBig = true;
			timeToDefineFireMario = true;
			MarioGame.manager.get("audio/sounds/powerup.wav", Sound.class).play();
		}
	}
	
	public boolean isBig(){
        return marioIsBig;
    }
	
	public boolean isFire(){
		return marioIsFire;
	}
	
	public void hit(Enemy enemy) {
		if(enemy instanceof Turtle && ((Turtle)enemy).getCurrentState() == Turtle.State.STANDING_SHELL){
			((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
		}
		else {
			if(marioIsBig || marioIsFire){
				marioIsBig = false;
				marioIsFire = false;
				timeToRedefineMario = true;
				setBounds(getX(), getY(), getWidth(), getHeight() / 2);
				MarioGame.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
			}
			else {
				MarioGame.manager.get("audio/music/mario_music.ogg", Music.class).stop();
				MarioGame.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
				marioIsDead = true;
				Filter filter = new Filter();
				filter.maskBits = MarioGame.NOTHING_BIT;
				//set all b2bodys in game to zero bit so Mario can fall off screen
				for(Fixture fixture : b2body.getFixtureList())
					fixture.setFilterData(filter);
				b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
			}
		}
	}
	
	public boolean isDead(){
		return marioIsDead;
	}
	
	public float getStateTimer() {
		return stateTimer;
	}
	
	public void shootFire() {
		//if(marioIsFire){
			fireballs.add(new Fireball(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));                 
		//}
	}
	
	public void draw(Batch batch) {
		super.draw(batch);
		for(Fireball ball : fireballs){
			ball.draw(batch);
		}
	}
	
	public void hitFlag() {
		//Gdx.app.log("Mario", "hitFlag");
		if(!marioWins){
			MarioGame.manager.get("audio/music/mario_music.ogg", Music.class).stop();
			MarioGame.manager.get("audio/sounds/smb_world_clear.wav", Sound.class).play();
			marioWins = true;
		}
	}

}
