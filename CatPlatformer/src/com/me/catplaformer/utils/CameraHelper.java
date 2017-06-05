package com.me.catplaformer.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.me.catplaformer.objects.CatPlatformerGameObject;

/**
 * Created by Tiago Santos on 05/06/2013.
 */
public class CameraHelper {

	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;
	private final float FOLLOW_SPEED = 30.0f;

	private Vector2 position;
	private float zoom;
	private CatPlatformerGameObject target;
	
	public boolean shouldUpdate;

	public CameraHelper () {
		position = new Vector2();
		zoom = 1.0f;
		shouldUpdate = true;
	}

	public void update (float deltaTime) {
		if(!shouldUpdate) return;
		if (!hasTarget()) return;
		
		position.lerp(target.body.getPosition(), FOLLOW_SPEED * deltaTime);
		
		// Prevent camera from moving down too far
		position.y = 5f;
		if(position.x < 9f) position.x = 9f;
//		if(position.y < 4f) position.y = 4f;
		if(position.x > 345f) position.x = 345f;
		//position.y = Math.max(5f, position.y);
		//position.x = Math.max(9f, position.x);
	
//TODO		//	camera.frustum.pointInFrustum(coordendas);   ---> ANALISAR
		
		Constants.CAMERA_POSITION = position.x;
	}

	public void setPosition (float x, float y) {
		this.position.set(x, y);
	}

	public Vector2 getPosition () {
		return position;
	}

	public void addZoom (float amount) {
		setZoom(zoom + amount);
	}

	public void setZoom (float zoom) {
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}

	public float getZoom () {
		return zoom;
	}

	public void setTarget (CatPlatformerGameObject target) {
		this.target = target;
	}

	public CatPlatformerGameObject getTarget () {
		return target;
	}

	public boolean hasTarget () {
		return target != null;
	}

	public boolean hasTarget (CatPlatformerGameObject target) {
		return hasTarget() && this.target.equals(target);
	}

	public void applyTo (OrthographicCamera camera) {
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
		
	}

}
