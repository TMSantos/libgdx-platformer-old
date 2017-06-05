package com.me.catplaformer.controller.tmxToBox2d;

import com.badlogic.gdx.math.Vector2;

public class TmxToBox2dRectangle {
	
	private Vector2 mPosition;
	private int mWidth;
	private int mHeight;

	public Vector2 getPosition() {
		return mPosition;
	}
	
	public void setPosition(Vector2 position) {
		mPosition = position;
	}
	
	public int getWidth() {
		return mWidth;
	}

	public void setWidth(int width) {
		mWidth = width;
	}

	public int getHeight() {
		return mHeight;
	}

	public void setHeight(int height) {
		mHeight = height;
	}
}
