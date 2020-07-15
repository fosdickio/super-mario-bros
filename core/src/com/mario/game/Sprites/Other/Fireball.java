package com.mario.game.Sprites.Other;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;

public class Fireball extends Sprite {
	
	PlayScreen screen;
    World world;
    Array<TextureRegion> frames;
    Animation fireAnimation;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;

    Body b2body;
    
    public Fireball(PlayScreen screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("fireball"), i * 8, 0, 8, 8));
        }
        fireAnimation = new Animation(0.2f, frames);
        setRegion(fireAnimation.getKeyFrame(0));
        setBounds(x, y, 6 / MarioGame.PPM, 6 / MarioGame.PPM);
        defineFireBall();
        setToDestroy = false;
        destroyed = false;
    }

    public void defineFireBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 20 /MarioGame.PPM : getX() - 20 /MarioGame.PPM, getY() + 12 /MarioGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
        	b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / MarioGame.PPM);
        fdef.filter.categoryBits = MarioGame.FIREBALL_BIT;
        fdef.filter.maskBits = MarioGame.GROUND_BIT |
        		MarioGame.COIN_BIT |
        		MarioGame.BRICK_BIT |
        		MarioGame.ENEMY_BIT |
        		MarioGame.ENEMY_HEAD_BIT |
        		MarioGame.OBJECT_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 2 : -2, 1.5f));
    }

    public void update(float dt){
        stateTime += dt;
        setRegion(fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 2f || setToDestroy) && !destroyed) {
        	if(!world.isLocked()){
        		b2body.setUserData(null);
        		world.destroyBody(b2body);
        		destroyed = true;
            	//b2body = null;
            	return;
        	}
        }else{
        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }


}
