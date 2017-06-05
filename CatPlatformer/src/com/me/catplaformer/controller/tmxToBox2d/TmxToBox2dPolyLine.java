package com.me.catplaformer.controller.tmxToBox2d;

import com.badlogic.gdx.math.Vector2;

public class TmxToBox2dPolyLine {
	
	private Vector2 mPosition;
	private Vector2[] mPolyPoints;

	public Vector2 getPosition() {
		return mPosition;
	}

	public void setPosition(Vector2 position) {
		mPosition = position;
	}
	
	public Vector2[] getPolyPoints() {
		return mPolyPoints;
	}

	public void setPolyPoints(Vector2[] polyPoints) {
		mPolyPoints = polyPoints;
	}
	
}
