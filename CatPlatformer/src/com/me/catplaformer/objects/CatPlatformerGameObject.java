package com.me.catplaformer.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.me.catplaformer.utils.Constants;

public abstract class CatPlatformerGameObject {
	
	public World objectWorld;
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	public Vector2 velocity;
	public Vector2 terminalVelocity;
	public Vector2 friction;
	public Vector2 acceleration;
	public Body body;
	public float stateTime;
	public Animation animation;
	public boolean shouldRender;
	public boolean update;
	
	public CatPlatformerGameObject() {
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(0.5f, 0.5f);
		rotation = 0;
		velocity = new Vector2();
		terminalVelocity = new Vector2(1, 1);
		friction = new Vector2();
		acceleration = new Vector2();
		shouldRender = false;
		update = true;
	}
	
	public void update (float deltaTime) {
		stateTime += deltaTime;
		if((body != null && body.getPosition().x > Constants.CAMERA_POSITION - Constants.VIEWPORT_WIDTH-2) && (body.getPosition().x < Constants.CAMERA_POSITION + Constants.VIEWPORT_WIDTH+2)){
			if(update) shouldRender = true;
		}else{
			shouldRender = false;
		}

	}
	
	public void setAnimation (Animation animation) {
		this.animation = animation;
		stateTime = 0;
	}
	
	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public abstract void render (SpriteBatch batch);

}
