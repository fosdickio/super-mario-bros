package com.mario.game.Sprites.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Mario;
import com.mario.game.Sprites.Other.Fireball;

public class Turtle extends Enemy{
	
	public enum State {WALKING, STANDING_SHELL, MOVING_SHELL};
	public State currentState;
	public State previousState;
	
	public static final int KICK_LEFT_SPEED = -2;
	public static final int KICK_RIGHT_SPEED = 2;
	
	private float stateTime;
	private Animation walkAnimation;
	private Array<TextureRegion> frames;
	private TextureRegion shell;
	
	//private boolean setToDestory;
	//private boolean destoryed;
	
	public Turtle(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		frames = new Array<TextureRegion>();
		frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
		shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
		
		walkAnimation = new Animation(0.2f, frames);
		currentState = previousState = State.WALKING;
		
		setBounds(getX(), getY(), 16 / MarioGame.PPM, 24/ MarioGame.PPM);
	}
	@Override
	protected void defineEnemy() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
			
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(7 / MarioGame.PPM);
		
		fdef.filter.categoryBits = MarioGame.ENEMY_BIT;
		fdef.filter.maskBits = MarioGame.GROUND_BIT 
				| MarioGame.BRICK_BIT 
				| MarioGame.COIN_BIT 
				| MarioGame.ENEMY_BIT 
				| MarioGame.OBJECT_BIT
				| MarioGame.MARIO_BIT
				| MarioGame.FIREBALL_BIT;
			
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		
		//Create Turtle head
		PolygonShape head = new PolygonShape();
		Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-6, 8).scl(1/MarioGame.PPM);
		vertice[1] = new Vector2(6, 8).scl(1/MarioGame.PPM);
		vertice[2] = new Vector2(-4, 3).scl(1/MarioGame.PPM);
		vertice[3] = new Vector2(4, 3).scl(1/MarioGame.PPM);
		head.set(vertice);
		
		fdef.shape = head;
		//how mario bounces off head
		fdef.restitution = 0.9f;
		fdef.filter.categoryBits = MarioGame.ENEMY_HEAD_BIT;
		b2body.createFixture(fdef).setUserData(this);
		
	}
	
	@Override
	public void update(float dt) {
		setRegion(getFrame(dt));
		if(currentState == State.STANDING_SHELL && stateTime > 5){
			currentState = State.WALKING;
			velocity.x = 1;
		}
		
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / MarioGame.PPM);
		b2body.setLinearVelocity(velocity);
	}
	
	@Override
	public void hitOnHead(Mario mario) {
		if(currentState != State.STANDING_SHELL){
			currentState = State.STANDING_SHELL;
			velocity.x = 0;
		}
		else {
			kick(mario.getX() <= this.getX() ? KICK_RIGHT_SPEED : KICK_LEFT_SPEED);
		}
		
	}
	
	public void kick(int speed){
		velocity.x = speed;
		currentState = State.MOVING_SHELL;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public TextureRegion getFrame(float dt) {
		
		TextureRegion region;
		
		switch (currentState) {
		case STANDING_SHELL:
		case MOVING_SHELL:
			region = shell;
			break;
		case WALKING:
		default:
			region = walkAnimation.getKeyFrame(stateTime, true);
			break;
		}
		
		if(velocity.x > 0 && region.isFlipX() == false) {
			region.flip(true, false);
		}
		if(velocity.x < 0 && region.isFlipX() == true) {
			region.flip(true, false);
		}
		
		stateTime = currentState == previousState ? stateTime + dt : 0;
		previousState = currentState;
		return region;
		
	}
	@Override
	public void hitByEnemy(Enemy enemy) {
		reverseVelocity(true, false);
		
	}
	
    @Override
    public void hitByFire(Fireball fireball){
    	Gdx.app.log("Fireball and Turtle", "Collision");
    	currentState = State.STANDING_SHELL;
		velocity.x = 0;
		fireball.setToDestroy();
    }
    	
    
}
