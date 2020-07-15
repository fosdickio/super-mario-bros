package com.mario.game.Sprites.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
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

public class Goomba extends Enemy {

	private float stateTime;
	private Animation walkAnimation;
	private Array<TextureRegion> frames;
	
	private boolean setToDestory;
	private boolean destoryed;
	
	public Goomba(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		frames = new Array<TextureRegion>();
		for(int i = 0; i < 2; i++) {
			frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
		}
		walkAnimation = new Animation(0.4f, frames);
		stateTime = 0;
		setBounds(getX(), getY(), 16 / MarioGame.PPM, 16 / MarioGame.PPM);
		setToDestory = false;
		destoryed = false;
	}
	
	public void update(float dt) {
		stateTime += dt;
		
		if(setToDestory && !destoryed) {
			world.destroyBody(b2body);
			destoryed = true;
			setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16,16));
			stateTime = 0;
		}
		else if (!destoryed) {
			b2body.setLinearVelocity(velocity);
			setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() /2);
			setRegion(walkAnimation.getKeyFrame(stateTime, true));
		}
		
	}
	
	@Override
	protected void defineEnemy() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
			
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / MarioGame.PPM);
		
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
		
		//Create Goomba head
		PolygonShape head = new PolygonShape();
		Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-5, 8).scl(1/MarioGame.PPM);
		vertice[1] = new Vector2(5, 8).scl(1/MarioGame.PPM);
		vertice[2] = new Vector2(-3, 3).scl(1/MarioGame.PPM);
		vertice[3] = new Vector2(3, 3).scl(1/MarioGame.PPM);
		head.set(vertice);
		
		fdef.shape = head;
		//how mario bounces off head
		fdef.restitution = 0.5f;
		fdef.filter.categoryBits = MarioGame.ENEMY_HEAD_BIT;
		b2body.createFixture(fdef).setUserData(this);
	}
	
	@Override 
	public void draw(Batch batch){
		if(!destoryed || stateTime < 1)
			super.draw(batch);
	}

	@Override
	public void hitOnHead(Mario mario) {
		setToDestory = true;
		MarioGame.manager.get("audio/sounds/stomp.wav", Sound.class).play();
		
	}
	
    @Override
    public void hitByEnemy(Enemy enemy) {
        if(enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL)
            setToDestory = true;
        else
            reverseVelocity(true, false);
    }

    @Override
    public void hitByFire(Fireball fireball){
    	Gdx.app.log("Fireball and Goomba", "Collision");
    	setToDestory = true;
    	fireball.setToDestroy();
    	
    }

}
