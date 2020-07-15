package com.mario.game.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Mario;

public class Mushroom extends Item{

	public Mushroom(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
		velocity = new Vector2(0.7f,0);
	}

	@Override
	public void defineItem() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bdef);
			
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / MarioGame.PPM);
		fdef.filter.categoryBits = MarioGame.ITEM_BIT;
		fdef.filter.maskBits = MarioGame.MARIO_BIT 
				| MarioGame.OBJECT_BIT
				| MarioGame.GROUND_BIT
				| MarioGame.BRICK_BIT
				| MarioGame.COIN_BIT;
			
		fdef.shape = shape;
		body.createFixture(fdef).setUserData(this);
		
	}

	@Override
	public void use(Mario mario) {
		destroy();
		mario.grow();
		
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() /2);
		velocity.y = body.getLinearVelocity().y;
		body.setLinearVelocity(velocity);
	}

}
